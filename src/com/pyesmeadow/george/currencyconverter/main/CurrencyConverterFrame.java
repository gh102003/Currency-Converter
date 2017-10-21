package com.pyesmeadow.george.currencyconverter.main;

import com.pyesmeadow.george.currencyconverter.currency.Currency;
import com.pyesmeadow.george.currencyconverter.currency.CurrencyDetailsPanel;
import com.pyesmeadow.george.currencyconverter.currency.CurrencyUpdater;
import com.pyesmeadow.george.currencyconverter.currency.manager.CurrencyList;
import com.pyesmeadow.george.currencyconverter.currency.manager.CurrencyManager;
import com.pyesmeadow.george.currencyconverter.save.Save;
import com.pyesmeadow.george.currencyconverter.save.SaveManager;
import com.pyesmeadow.george.currencyconverter.save.gui.SavePanel;
import com.pyesmeadow.george.currencyconverter.util.FontUtil;
import com.pyesmeadow.george.currencyconverter.util.FontUtil.FontVariation;
import com.pyesmeadow.george.currencyconverter.util.ResourceUtil;
import com.pyesmeadow.george.currencyconverter.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

public class CurrencyConverterFrame extends JFrame implements KeyListener, ItemListener {

	private static final long serialVersionUID = 3584042683319202624L;
	private static final Dimension DEFAULT_SIZE = new Dimension(600, 520);
	private static final Dimension MINIMUM_SIZE = new Dimension(600, 500);
	private static final Dimension COLLAPSED_MINIMUM_SIZE = new Dimension(600, 250);

	// Currency and save managers
	public CurrencyManager currencyManager = new CurrencyManager();
	// Menu bar
	public CurrencyConverterMenuBar menuBar = new CurrencyConverterMenuBar(this);
	// Dialogs and panels
	public AboutDialog aboutDialog;
	public OptionsDialog optionsDialog;
	public CurrencyUpdater currencyUpdater;
	private SaveManager saveManager = new SaveManager();
	public SavePanel panelSaves = new SavePanel(saveManager);
	// Create main components
	private JPanel panelConversion = new JPanel();
	private JComboBox<String> comboFromCurrency = new JComboBox<String>();
	private JTextField fieldFromCurrencyAmount = new JTextField(8);
	private JLabel labelEquals = new JLabel("=");
	private JComboBox<String> comboToCurrency = new JComboBox<String>();
	private JLabel labelToCurrencyAmount = new JLabel("      ");
	private JButton btnSave = new JButton("Save");
	// Create currency details components
	private JPanel panelCurrencyDetails = new JPanel();
	private JPanel panelFromCurrency = new JPanel();
	private JPanel panelToCurrency = new JPanel();

	public CurrencyConverterFrame(boolean shouldUpdateOnStart)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

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

		panelFromCurrency.setLayout(new CardLayout());
		panelFromCurrency.setBorder(BorderFactory.createTitledBorder("From Currency"));

		panelToCurrency.setLayout(new CardLayout());
		panelToCurrency.setBorder(BorderFactory.createTitledBorder("To Currency"));

		panelCurrencyDetails.setBorder(BorderFactory.createTitledBorder("Currency Details"));
		panelSaves.setBorder(BorderFactory.createTitledBorder("Saves"));

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

		panelCurrencyDetails.setLayout(new BoxLayout(panelCurrencyDetails, BoxLayout.LINE_AXIS));

		panelCurrencyDetails.add(panelFromCurrency);
		panelCurrencyDetails.add(panelToCurrency);

		// Add panels and components to frame
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.1;

		add(panelConversion, c);
		add(panelCurrencyDetails, c);
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		add(panelSaves, c);

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

		this.addListeners();

		// Set frame title
		setTitle("Currency Converter");

		// Load icon images
		ArrayList<Image> iconList = new ArrayList<Image>();

<<<<<<< HEAD
		Image icon16 = Toolkit.getDefaultToolkit()
				.getImage(CurrencyConverterFrame.class.getResource("/assets/icon_16.png"));
		Image icon32 = Toolkit.getDefaultToolkit()
				.getImage(CurrencyConverterFrame.class.getResource("/assets/icon_32.png"));
		Image icon64 = Toolkit.getDefaultToolkit()
				.getImage(CurrencyConverterFrame.class.getResource("/assets/icon_64.png"));
		Image icon128 = Toolkit.getDefaultToolkit()
				.getImage(CurrencyConverterFrame.class.getResource("/assets/icon_128.png"));
=======
		Image icon16 = null;
		Image icon32 = null;
		Image icon64 = null;
		Image icon128 = null;
		try
		{
			icon16 = ResourceUtil.getImage("assets/icon16.png");
			icon32 = ResourceUtil.getImage("assets/icon32.png");
			icon64 = ResourceUtil.getImage("assets/icon64.png");
			icon128 = ResourceUtil.getImage("assets/icon128.png");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
>>>>>>> saves

		iconList.add(icon16);
		iconList.add(icon32);
		iconList.add(icon64);
		iconList.add(icon128);

		setIconImages(iconList);

		// Set frame parameters
		this.setJMenuBar(this.menuBar);

		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(CurrencyConverterFrame.DEFAULT_SIZE);
		setMinimumSize(CurrencyConverterFrame.MINIMUM_SIZE);
		setVisible(true);
		updateConversion();
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

	public void setCurrencyDetailsVisibility(boolean visible)
	{
		if (!visible)
		{
			setMinimumSize(COLLAPSED_MINIMUM_SIZE);
		}
		else
		{
			setMinimumSize(MINIMUM_SIZE);
		}

		panelCurrencyDetails.setVisible(visible);
	}

	/**
	 * Removes and re-adds the contents of the combo boxes and the currency
	 * details panels. Updates conversion.
	 */
	public void refreshCurrencies()
	{
		comboFromCurrency.removeAllItems();
		comboToCurrency.removeAllItems();

		panelFromCurrency.removeAll();
		panelToCurrency.removeAll();

		this.addCurrencies(this.currencyManager.getCurrencyList());
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
		this.addDetailsPanels();

		this.updateShownCurrencyPanels();
	}

	private void addDetailsPanels()
	{
		// Remove panels
		panelFromCurrency.removeAll();
		panelToCurrency.removeAll();

		// Add panels
		for (Currency currency : currencyManager.getCurrencyList().getCurrencies())
		{
			JPanel panel = new CurrencyDetailsPanel(currency, 128);
			panelFromCurrency.add(panel, currency.getIdentifier());

			panel = new CurrencyDetailsPanel(currency, 128);
			panelToCurrency.add(panel, currency.getIdentifier());
		}

		FontUtil.updateComponentFontVariations(CurrencyConverter.getFontProfile(), false);

		this.updateShownCurrencyPanels();

		this.updateConversion();
	}

	private void updateShownCurrencyPanels()
	{
		CardLayout cl = (CardLayout) panelFromCurrency.getLayout();
		cl.show(panelFromCurrency, (String) comboFromCurrency.getSelectedItem());
		cl = (CardLayout) panelToCurrency.getLayout();
		cl.show(panelToCurrency, (String) comboToCurrency.getSelectedItem());
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

			updateShownCurrencyPanels();
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
