package com.pyesmeadow.george.currencyconverter.main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.pyesmeadow.george.currencyconverter.currency.Currency;
import com.pyesmeadow.george.currencyconverter.currency.CurrencyDetailsPanel;
import com.pyesmeadow.george.currencyconverter.currency.CurrencyUpdater;
import com.pyesmeadow.george.currencyconverter.currency.manager.CurrencyList;
import com.pyesmeadow.george.currencyconverter.currency.manager.CurrencyManager;
import com.pyesmeadow.george.currencyconverter.util.FontUtil;
import com.pyesmeadow.george.currencyconverter.util.FontUtil.FontVariation;
import com.pyesmeadow.george.currencyconverter.util.Util;

public class CurrencyConverterFrame extends JFrame implements KeyListener, ItemListener {

	private static final long serialVersionUID = 3584042683319202624L;

	// Currency Manager
	public CurrencyManager currencyManager = new CurrencyManager();

	// Menu bar
	public CurrencyConverterMenuBar menuBar = new CurrencyConverterMenuBar(this);

	private static final Dimension DEFAULT_SIZE = new Dimension(600, 410);
	private static final Dimension MINIMUM_SIZE = new Dimension(600, 410);
	private static final Dimension COLLAPSED_MINIMUM_SIZE = new Dimension(600, 150);

	// Create main components
	public JPanel panelMain = new JPanel();
	public JComboBox<String> comboFromCurrency = new JComboBox<String>();
	public JTextField fieldFromCurrencyAmount = new JTextField(8);
	public JLabel labelEquals = new JLabel("=");
	public JComboBox<String> comboToCurrency = new JComboBox<String>();
	public JLabel labelToCurrencyAmount = new JLabel("      ");

	// Create currency details components
	public JPanel panelCurrencyDetails = new JPanel();
	public JPanel panelFromCurrency = new JPanel();
	public JPanel panelToCurrency = new JPanel();

	// Dialogs
	public AboutDialog aboutDialog;
	public OptionsDialog optionsDialog;
	public CurrencyUpdater currencyUpdater;

	public CurrencyConverterFrame(boolean shouldUpdateOnStart)
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
		panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.LINE_AXIS));
		panelMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panelMain.setAlignmentX(CENTER_ALIGNMENT);
		panelMain.setMaximumSize(new Dimension(800, 35));

		panelFromCurrency.setLayout(new CardLayout());
		panelFromCurrency.setBorder(BorderFactory.createTitledBorder("From Currency"));

		panelToCurrency.setLayout(new CardLayout());
		panelToCurrency.setBorder(BorderFactory.createTitledBorder("To Currency"));

		panelCurrencyDetails.setBorder(BorderFactory.createTitledBorder("Currency Details"));

		// Setup components
		fieldFromCurrencyAmount.setMinimumSize(new Dimension(80, 35));

		comboFromCurrency.setAlignmentY(Component.CENTER_ALIGNMENT);
		fieldFromCurrencyAmount.setAlignmentY(Component.CENTER_ALIGNMENT);
		labelEquals.setAlignmentY(Component.CENTER_ALIGNMENT);
		comboToCurrency.setAlignmentY(Component.CENTER_ALIGNMENT);
		labelToCurrencyAmount.setAlignmentY(Component.CENTER_ALIGNMENT);

		// Add components to panels
		panelMain.add(comboFromCurrency);
		panelMain.add(Box.createRigidArea(new Dimension(10, 0)));
		panelMain.add(fieldFromCurrencyAmount);
		panelMain.add(Box.createRigidArea(new Dimension(10, 0)));
		panelMain.add(labelEquals);
		panelMain.add(Box.createRigidArea(new Dimension(10, 0)));
		panelMain.add(comboToCurrency);
		panelMain.add(Box.createRigidArea(new Dimension(10, 0)));
		panelMain.add(labelToCurrencyAmount);

		if (shouldUpdateOnStart)
		{
			// Wait for currencies to finish updating
			try
			{
				thread.join();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		addCurrencies(currencyManager.getCurrencyList());

		panelCurrencyDetails.add(panelFromCurrency);
		panelCurrencyDetails.add(panelToCurrency);

		// Add panels and components to frame
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;

		add(panelMain, c);
		add(panelCurrencyDetails, c);

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

		Image icon16 = Toolkit.getDefaultToolkit()
				.getImage(CurrencyConverterFrame.class.getResource("/assets/icon16.png"));
		Image icon32 = Toolkit.getDefaultToolkit()
				.getImage(CurrencyConverterFrame.class.getResource("/assets/icon32.png"));
		Image icon64 = Toolkit.getDefaultToolkit()
				.getImage(CurrencyConverterFrame.class.getResource("/assets/icon64.png"));
		Image icon128 = Toolkit.getDefaultToolkit()
				.getImage(CurrencyConverterFrame.class.getResource("/assets/icon128.png"));

		iconList.add(icon16);
		iconList.add(icon32);
		iconList.add(icon64);
		iconList.add(icon128);

		setIconImages(iconList);

		// Set frame parameters
		this.setJMenuBar(this.menuBar);

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(CurrencyConverterFrame.DEFAULT_SIZE);
		setMinimumSize(CurrencyConverterFrame.MINIMUM_SIZE);
		setVisible(true);
		updateConversion();
	}

	public void updateConversion()
	{
		// Set the amount to be converted but default to 0
		float amountFrom;

		try
		{
			amountFrom = Float.parseFloat(fieldFromCurrencyAmount.getText());
		} catch (NullPointerException | NumberFormatException e)
		{
			amountFrom = 0;
		}

		CurrencyList currencyList = this.currencyManager.getCurrencyList();

		Currency currencyFrom = currencyList.getCurrencyFromID((String) comboFromCurrency.getSelectedItem());
		Currency currencyTo = currencyList.getCurrencyFromID((String) comboToCurrency.getSelectedItem());

		// Update the output label to the conversion
		if (currencyTo != null && currencyFrom != null)
		{
			labelToCurrencyAmount.setText(currencyTo.getCurrencyFormatting()
					.format(CurrencyConverter.convertCurrency(amountFrom, currencyFrom, currencyTo)));
		}
	}

	public void swapCurrencies()
	{
		String prevFrom = (String) comboFromCurrency.getSelectedItem();
		String prevTo = (String) comboToCurrency.getSelectedItem();

		double prevConversion = 0;

		try
		{
			prevConversion = currencyManager.getCurrencyList().getCurrencyFromID(prevTo).getCurrencyFormatting()
											.parse(labelToCurrencyAmount.getText()).doubleValue();
		} catch (ParseException e)
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

	public void registerComponentFontVariations()
	{
		// panelMain
		if (!Util.isRunningOnMac())
		{
			FontUtil.registerComponentFontVariation(comboFromCurrency, FontVariation.LARGE_PLAIN);
			FontUtil.registerComponentFontVariation(fieldFromCurrencyAmount, FontVariation.LARGE_PLAIN);
			FontUtil.registerComponentFontVariation(labelEquals, FontVariation.LARGE_PLAIN);
			FontUtil.registerComponentFontVariation(comboToCurrency, FontVariation.LARGE_PLAIN);
			FontUtil.registerComponentFontVariation(labelToCurrencyAmount, FontVariation.LARGE_SYMBOL);
		} else
		{
			FontUtil.registerComponentFontVariation(comboFromCurrency, FontVariation.SMALL_PLAIN);
			FontUtil.registerComponentFontVariation(fieldFromCurrencyAmount, FontVariation.SMALL_PLAIN);
			FontUtil.registerComponentFontVariation(labelEquals, FontVariation.SMALL_PLAIN);
			FontUtil.registerComponentFontVariation(comboToCurrency, FontVariation.SMALL_PLAIN);
			FontUtil.registerComponentFontVariation(labelToCurrencyAmount, FontVariation.SMALL_PLAIN);
		}
	}

	public void setCurrencyDetailsVisibility(boolean visible)
	{
		if (!visible)
		{
			setMinimumSize(COLLAPSED_MINIMUM_SIZE);
		} else
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
		} else
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
			JPanel panel = new CurrencyDetailsPanel(currency);
			panelFromCurrency.add(panel, currency.getIdentifier());

			panel = new CurrencyDetailsPanel(currency);
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
		labelEquals.addMouseListener(new MouseListener()
		{
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
