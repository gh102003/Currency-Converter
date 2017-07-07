package com.pyesmeadow.george.currencyconverter.currency;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.pyesmeadow.george.currencyconverter.currency.manager.CurrencyManager;
import com.pyesmeadow.george.currencyconverter.main.CurrencyConverter;
import com.pyesmeadow.george.currencyconverter.util.FontUtil;
import com.pyesmeadow.george.currencyconverter.util.FontUtil.FontVariation;
import com.pyesmeadow.george.currencyconverter.util.HTTPUtil;

public class CurrencyUpdater extends JDialog implements Runnable {

	private static final long serialVersionUID = 4510480362783863522L;

	public JLabel labelMainStatus = new JLabel("Updating currencies...");
	public JPanel panelStatus = new JPanel();

	private CurrencyManager currencyManager;

	private boolean showStatus;

	public CurrencyUpdater(CurrencyManager currencyManager, boolean showStatus)
	{
		this.currencyManager = currencyManager;
		this.showStatus = showStatus;

		if (this.showStatus)
		{
			initDialog();
		}
	}

	private void initDialog()
	{
		panelStatus.setLayout(new BoxLayout(panelStatus, BoxLayout.PAGE_AXIS));

		add(panelStatus, BorderLayout.CENTER);

		/* Add components */
		panelStatus.add(labelMainStatus);

		labelMainStatus.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

		/* Setup dialog */
		registerComponentFontVariations();
		FontUtil.updateComponentFontVariations(CurrencyConverter.getFontProfile(), false);
		setSize(new Dimension(400, 300));
		setResizable(false);
		setTitle("Currency Updater");
		setIconImages(CurrencyConverter.currencyConverterFrame.getIconImages());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}

	private void registerComponentFontVariations()
	{
		FontUtil.registerComponentFontVariation(labelMainStatus, FontVariation.SMALL_BOLD);
	}

	/**
	 * Updates all currencies and creates a dialog. Updates conversion and
	 * display
	 */
	public void updateCurrencies()
	{

		int failureCount = 0;

		for (Currency c : currencyManager.getCurrencyList().getCurrencies())
		{
			if (!updateCurrency(c))
			{
				failureCount++;
			}
		}

		if (failureCount > 0)
		{
			JOptionPane.showMessageDialog(this, "Could not update " + failureCount + " currencies", "Currency Updater",
					JOptionPane.WARNING_MESSAGE);
		} else if (showStatus)
		{
			JOptionPane.showMessageDialog(this, "Updated all currencies successfully", "Currency Updater",
					JOptionPane.INFORMATION_MESSAGE);
		}

		dispose();
	}

	/**
	 * Updates a single currency from the internet. Handles all exceptions.
	 * 
	 * @param currency
	 *            currency to update
	 * 
	 * @return true if update succesful, false if unsuccesful
	 */
	private boolean updateCurrency(Currency currency)
	{
		try
		{
			String conversionHTML = HTTPUtil
					.sendPostRequest("http://www.xe.com/currencyconverter/convert/?Amount=1&From=EUR&To=USD",
							"Amount=1", "From=" + currency.getIdentifier(), "To=USD")
					.toString();

			// Trim to value
			String a = conversionHTML.split("uccResultAmount\'>")[1];
			String b = a.split("</span>")[0];

			// Check for an invalid currency identifier
			if (b.equals("0.00"))
			{
				throw new IllegalArgumentException(currency.getIdentifier() + " is not a valid currency identifier.");
			}

			double currencyValue = Double.parseDouble(b.replaceAll(",", ""));

			// Print and set value
			System.out.println(currency.getIdentifier() + " value: " + currencyValue);
			currency.setValueInUSD(currencyValue);

			// Add status label
			JLabel label = new JLabel("Successfully updated value for " + currency.getName());
			label.setBorder(BorderFactory.createEmptyBorder(1, 5, 1, 5));
			FontUtil.registerComponentFontVariation(label, FontVariation.SMALL_PLAIN);
			FontUtil.updateComponentFontVariations(CurrencyConverter.getFontProfile(), false);
			panelStatus.add(label);
			validate();

			return true;

		} catch (Exception e)
		{
			e.printStackTrace();

			System.err.println("Error connecting to server. Could not update value of " + currency.getName());

			// Add status label
			JLabel label = new JLabel("Could not update value for the " + currency.getName());
			label.setBorder(BorderFactory.createEmptyBorder(1, 5, 1, 5));
			FontUtil.registerComponentFontVariation(label, FontVariation.SMALL_BOLD);
			FontUtil.updateComponentFontVariations(CurrencyConverter.getFontProfile(), false);
			panelStatus.add(label);
			validate();

			return false;
		}
	}

	/**
	 * What to do when thread is started
	 */
	@Override
	public void run()
	{
		updateCurrencies();
	}
}
