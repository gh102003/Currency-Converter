package com.pyesmeadow.george.currencyconverter.main;

import com.pyesmeadow.george.currencyconverter.currency.Currency;
import com.pyesmeadow.george.currencyconverter.currency.CurrencyDetailsContainerPanel;
import com.pyesmeadow.george.currencyconverter.currency.CurrencyUpdater;
import com.pyesmeadow.george.currencyconverter.currency.manager.CurrencyList;
import com.pyesmeadow.george.currencyconverter.currency.manager.CurrencyManager;
import com.pyesmeadow.george.currencyconverter.save.Save;
import com.pyesmeadow.george.currencyconverter.save.SaveManager;
import com.pyesmeadow.george.currencyconverter.save.gui.SavePanel;
import com.pyesmeadow.george.currencyconverter.util.FontUtil;
import com.pyesmeadow.george.currencyconverter.util.FontUtil.FontVariation;
import com.pyesmeadow.george.currencyconverter.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CurrencyConverterFrame extends JFrame implements KeyListener, ItemListener {

	private static final long serialVersionUID = 3584042683319202624L;
	private static final int CORE_HEIGHT = 125;
	private static final int WIDTH = 600;

	// Currency and save managers
	public CurrencyManager currencyManager = new CurrencyManager();

	// Dialogs and panels
	private AboutDialog aboutDialog;
	private CurrencyUpdater currencyUpdater;

	// Toggleable components
	private List<IToggleableComponent> toggleableComponents = new ArrayList<>();
	private SaveManager saveManager = new SaveManager();
	public SavePanel panelSaves = new SavePanel(saveManager);
	// Create main components
	private JPanel panelConversion = new JPanel();
	private JComboBox<String> comboFromCurrency = new JComboBox<>();
	private JTextField fieldFromCurrencyAmount = new JTextField(8);
	private JLabel labelEquals = new JLabel("=");
	private JComboBox<String> comboToCurrency = new JComboBox<>();
	private JLabel labelToCurrencyAmount = new JLabel("      ");
	private JButton btnSave = new JButton("Save");
	// Create currency details components
	private CurrencyDetailsContainerPanel panelCurrencyDetailsContainer = new CurrencyDetailsContainerPanel();
	// Menu bar
	private CurrencyConverterMenuBar menuBar = new CurrencyConverterMenuBar(this);

	CurrencyConverterFrame(boolean shouldUpdateOnStart)
	{
		Thread thread = null;

		if (shouldUpdateOnStart)
		{
			// Update currencies
			this.currencyUpdater = new CurrencyUpdater(this.currencyManager, false);
			thread = new Thread(this.currencyUpdater, "Thread-CurrencyUpdater");
			thread.start();
		}

		// Setup main layout
		setLayout(new GridBagLayout());

		// Setup panels
		panelConversion.setLayout(new BoxLayout(panelConversion, BoxLayout.LINE_AXIS));
		panelConversion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panelConversion.setAlignmentX(CENTER_ALIGNMENT);
		panelConversion.setMaximumSize(new Dimension(800, 35));

		// Setup components
		fieldFromCurrencyAmount.setMinimumSize(new Dimension(80, 35));

		comboFromCurrency.setAlignmentY(Component.CENTER_ALIGNMENT);
		fieldFromCurrencyAmount.setAlignmentY(Component.CENTER_ALIGNMENT);
		labelEquals.setAlignmentY(Component.CENTER_ALIGNMENT);
		comboToCurrency.setAlignmentY(Component.CENTER_ALIGNMENT);
		labelToCurrencyAmount.setAlignmentY(Component.CENTER_ALIGNMENT);

		// Add components to panels
		panelConversion.add(comboFromCurrency);
		panelConversion.add(Box.createRigidArea(new Dimension(10, 0)));
		panelConversion.add(fieldFromCurrencyAmount);
		panelConversion.add(Box.createRigidArea(new Dimension(10, 0)));
		panelConversion.add(labelEquals);
		panelConversion.add(Box.createRigidArea(new Dimension(10, 0)));
		panelConversion.add(comboToCurrency);
		panelConversion.add(Box.createRigidArea(new Dimension(10, 0)));
		panelConversion.add(labelToCurrencyAmount);
		panelConversion.add(Box.createRigidArea(new Dimension(10, 0)));
		panelConversion.add(btnSave);

		if (shouldUpdateOnStart)
		{
			// Wait for currencies to finish updating
			try
			{
				thread.join();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		addCurrencies(currencyManager.getCurrencyList());

		// Add panels and components to frame
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.1;

		add(panelConversion, c);

		// Toggleable components
		toggleableComponents.add(panelCurrencyDetailsContainer);
		toggleableComponents.add(panelSaves);
		registerToggleableComponents();

		// Set font size
		registerComponentFontVariations();
		FontUtil.updateComponentFontVariations(CurrencyConverter.getFontProfile(), true);

		// Add tooltips for the components
		comboFromCurrency.setToolTipText("Choose a currency to convert from");
		fieldFromCurrencyAmount.setToolTipText("Enter an amount to convert");
		fieldFromCurrencyAmount.setHorizontalAlignment(SwingConstants.RIGHT);
		comboToCurrency.setToolTipText("Choose a currency to convert to");
		labelEquals.setToolTipText("Click to swap currencies");

		// Add listeners for each input device
		comboFromCurrency.addItemListener(this);
		fieldFromCurrencyAmount.addKeyListener(this);
		comboToCurrency.addItemListener(this);

		addListeners();

		// Set title and icons
		setTitle("Currency Converter");
		setIconImages(CurrencyConverter.APP_ICONS);

		// Set frame parameters
		setJMenuBar(this.menuBar);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		updateConversion();
		pack();
		updateHeight();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	List<IToggleableComponent> getToggleableComponentsWithPredicate(Predicate<IToggleableComponent> predicate)
	{
		return toggleableComponents.stream().filter(predicate).collect(Collectors.toList());
	}

	List<IToggleableComponent> getToggleableComponentsByID(String id)
	{
		return getToggleableComponentsWithPredicate(component -> component.getID().equals(id));
	}

	private List<IToggleableComponent> getToggleableComponents()
	{
		return toggleableComponents;
	}

	private void registerToggleableComponents()
	{
		for (IToggleableComponent component : toggleableComponents)
		{
			GridBagConstraints c = component.getGridBagConstraints();
			c.gridx = 0;
			c.gridy = GridBagConstraints.RELATIVE;

			component.getComponent().setBorder(BorderFactory.createTitledBorder(component.getID()));

			add(component.getComponent(), c);
		}

		menuBar.addToggleableComponentMenuItems(toggleableComponents);
	}

	public void updateConversion()
	{
		// Set the amount to be converted but default to 0
		double amountFrom;

		try
		{
			amountFrom = Double.parseDouble(fieldFromCurrencyAmount.getText());
		}
		catch (NullPointerException | NumberFormatException e)
		{
			amountFrom = 0;
		}

		CurrencyList currencyList = this.currencyManager.getCurrencyList();

		Currency currencyFrom = currencyList.getCurrencyFromID((String) comboFromCurrency.getSelectedItem());
		Currency currencyTo = currencyList.getCurrencyFromID((String) comboToCurrency.getSelectedItem());

		// Update the output label to the conversion
		if (currencyTo != null && currencyFrom != null)
		{
			labelToCurrencyAmount.setText(currencyTo.getFormatting().format(CurrencyConverter.convertCurrency(amountFrom,
					currencyFrom,
					currencyTo)));
		}
	}

	private String getFromCurrencyID()
	{
		return (String) comboFromCurrency.getSelectedItem();
	}

	private String getToCurrencyID()
	{
		return (String) comboToCurrency.getSelectedItem();
	}

	private void swapCurrencies()
	{
		String prevFrom = (String) comboFromCurrency.getSelectedItem();
		String prevTo = (String) comboToCurrency.getSelectedItem();

		double prevConversion = 0;

		try
		{
			prevConversion = currencyManager.getCurrencyList().getCurrencyFromID(prevTo).getFormatting().parse(
					labelToCurrencyAmount.getText()).doubleValue();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		// Swap amounts
		comboFromCurrency.setSelectedItem(comboToCurrency.getSelectedItem());
		comboToCurrency.setSelectedItem(prevFrom);

		// Swap combo boxes
		fieldFromCurrencyAmount.setText(String.valueOf(prevConversion));

		updateConversion();
	}

	/**
	 * Removes and re-adds the contents of the combo boxes and the currency
	 * details panels. Updates conversion.
	 */
	public void refreshCurrencies()
	{
		comboFromCurrency.removeAllItems();
		comboToCurrency.removeAllItems();

		this.addCurrencies(this.currencyManager.getCurrencyList());
	}

	void updateHeight()
	{
		int minimumHeight = CORE_HEIGHT;
		int defaultHeight = CORE_HEIGHT;

		List<IToggleableComponent> toggleableComponentsWithPredicate = this.getToggleableComponentsWithPredicate(component -> component.getComponent().isVisible());
		for (IToggleableComponent component : toggleableComponentsWithPredicate)
		{
			minimumHeight += component.getMinimumHeight();
			defaultHeight += component.getDefaultHeight();
		}

		setMinimumSize(new Dimension(WIDTH, minimumHeight));
		setSize(new Dimension(WIDTH, defaultHeight));
	}

	/**
	 * Sets up the components for the currencies. Used on startup only.
	 */
	private void addCurrencies(CurrencyList currencyList)
	{
		ArrayList<Currency> currencies = currencyList.getCurrencies();

		// Add choices to combo boxes
		for (Currency currency : currencies)
		{
			comboFromCurrency.addItem(currency.getIdentifier());
			comboToCurrency.addItem(currency.getIdentifier());
		}

		// Set default ComboBox items
		Currency localCurrency = currencyManager.getCurrencyList().getCurrencyFromLocale(Locale.getDefault());

		if (localCurrency != null)
		{
			comboFromCurrency.setSelectedItem(localCurrency.getIdentifier());
		}

		if (comboFromCurrency.getSelectedItem() != null && comboFromCurrency.getSelectedItem().equals("USD"))
		{
			comboToCurrency.setSelectedItem("EUR");
		}
		else
		{
			comboToCurrency.setSelectedItem("USD");
		}

		// Add currency details panels
		panelCurrencyDetailsContainer.refreshDetailsPanels(currencyList);
		panelCurrencyDetailsContainer.updateShownCurrencyPanels(getFromCurrencyID(), getToCurrencyID());
	}

	protected void registerComponentFontVariations()
	{
		// panelConversion
		if (!Util.isRunningOnMac())
		{
			FontUtil.registerComponentFontVariation(comboFromCurrency, FontVariation.LARGE_PLAIN);
			FontUtil.registerComponentFontVariation(fieldFromCurrencyAmount, FontVariation.LARGE_PLAIN);
			FontUtil.registerComponentFontVariation(labelEquals, FontVariation.LARGE_PLAIN);
			FontUtil.registerComponentFontVariation(comboToCurrency, FontVariation.LARGE_PLAIN);
			FontUtil.registerComponentFontVariation(labelToCurrencyAmount, FontVariation.LARGE_SYMBOL);
			FontUtil.registerComponentFontVariation(btnSave, FontVariation.LARGE_PLAIN);
		}
		else
		{
			FontUtil.registerComponentFontVariation(comboFromCurrency, FontVariation.SMALL_PLAIN);
			FontUtil.registerComponentFontVariation(fieldFromCurrencyAmount, FontVariation.SMALL_PLAIN);
			FontUtil.registerComponentFontVariation(labelEquals, FontVariation.SMALL_PLAIN);
			FontUtil.registerComponentFontVariation(comboToCurrency, FontVariation.SMALL_PLAIN);
			FontUtil.registerComponentFontVariation(labelToCurrencyAmount, FontVariation.SMALL_PLAIN);
			FontUtil.registerComponentFontVariation(btnSave, FontVariation.SMALL_PLAIN);
		}
	}

	boolean isAboutDialogOpen()
	{
		return aboutDialog != null && aboutDialog.isVisible();
	}

	public void setAboutDialog(AboutDialog aboutDialog)
	{
		this.aboutDialog = aboutDialog;
	}

	public void setCurrencyUpdater(CurrencyUpdater currencyUpdater)
	{
		this.currencyUpdater = currencyUpdater;
	}

	boolean isCurrencyUpdaterOpen()
	{
		return currencyUpdater != null && currencyUpdater.isVisible();
	}

	protected void addListeners()
	{
		labelEquals.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e)
			{
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				labelEquals.setForeground(Color.BLACK);
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				labelEquals.setForeground(Color.RED);
			}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				swapCurrencies();
			}
		});

		btnSave.addActionListener(evt ->
		{
			CurrencyList currencyList = currencyManager.getCurrencyList();

			Currency fromCurrency = currencyList.getCurrencyFromID((String) comboFromCurrency.getSelectedItem());
			Currency toCurrency = currencyList.getCurrencyFromID((String) comboToCurrency.getSelectedItem());

			double fromAmount;

			try
			{
				fromAmount = Double.parseDouble(fieldFromCurrencyAmount.getText());
			}
			catch (NullPointerException | NumberFormatException e)
			{
				fromAmount = 0;
			}

			double toAmount;

			try
			{
				toAmount = toCurrency.getFormatting().parse(labelToCurrencyAmount.getText()).doubleValue();
			}
			catch (ParseException e)
			{
				toAmount = 0;
			}


			Save save = new Save(fromCurrency, fromAmount, toCurrency, toAmount);
			saveManager.addSave(save);
		});
	}

	@Override
	public void itemStateChanged(ItemEvent e)
	{
		if (e.getStateChange() == ItemEvent.SELECTED)
		{
			updateConversion();

			panelCurrencyDetailsContainer.updateShownCurrencyPanels(getFromCurrencyID(), getToCurrencyID());
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		updateConversion();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		updateConversion();
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		updateConversion();
	}
}
