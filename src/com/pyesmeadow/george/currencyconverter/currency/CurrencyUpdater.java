package com.pyesmeadow.george.currencyconverter.currency;

import com.pyesmeadow.george.currencyconverter.currency.manager.CurrencyManager;
import com.pyesmeadow.george.currencyconverter.main.CurrencyConverter;
import com.pyesmeadow.george.currencyconverter.util.FontUtil;
import com.pyesmeadow.george.currencyconverter.util.FontUtil.FontVariation;
import com.pyesmeadow.george.currencyconverter.util.HTTPUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CurrencyUpdater extends JDialog implements Runnable {

	private static final long serialVersionUID = 4510480362783863522L;

	private JLabel labelMainStatus = new JLabel("Updating currencies...");
	private JPanel panelStatus = new JPanel();

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
		setIconImages(CurrencyConverter.APP_ICONS);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);
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
			if (c.getIdentifier().equals("USD")) continue; // Skip updating USD because its value will always be 1
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
	 * @return true if update successful, false if unsuccessful
	 */
	private boolean updateCurrency(Currency currency)
	{
		try
		{
			// Get currency data from API
			String url = String.format("https://free.currencyconverterapi.com/api/v6/convert?q=%s_USD&compact=ultra",
					currency.getIdentifier());
			String updateData = HTTPUtil.sendGetRequest(url).toString(); // JSON file

			// Parse currency data
			JSONParser parser = new JSONParser();
			JSONObject updateJSON = (JSONObject) parser.parse(updateData);
			Object valueFromData = updateJSON.get(currency.getIdentifier() + "_USD");

			if (valueFromData == null)
			{
				throw new IllegalArgumentException("Currency " + currency.getIdentifier() + " is not valid");
			}

			double currencyValue = (double) valueFromData;

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
		}
		catch (ParseException | IllegalArgumentException | IOException e)
		{
			e.printStackTrace();

			System.err.println("Could not update value of " + currency.getName());

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
