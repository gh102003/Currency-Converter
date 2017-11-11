package com.pyesmeadow.george.currencyconverter.currency;

import com.pyesmeadow.george.currencyconverter.currency.manager.CurrencyList;
import com.pyesmeadow.george.currencyconverter.main.CurrencyConverter;
import com.pyesmeadow.george.currencyconverter.main.IToggleableComponent;
import com.pyesmeadow.george.currencyconverter.util.FontUtil;

import javax.swing.*;
import java.awt.*;

public class CurrencyDetailsContainerPanel extends JPanel implements IToggleableComponent {

	private JPanel panelFromCurrency = new JPanel();
	private JPanel panelToCurrency = new JPanel();

	public CurrencyDetailsContainerPanel()
	{
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		panelFromCurrency.setLayout(new CardLayout());
		panelFromCurrency.setBorder(BorderFactory.createTitledBorder("From Currency"));
		add(panelFromCurrency);

		panelToCurrency.setLayout(new CardLayout());
		panelToCurrency.setBorder(BorderFactory.createTitledBorder("To Currency"));
		add(panelToCurrency);

	}

	public void refreshDetailsPanels(CurrencyList currencyList)
	{
		// Remove panels
		panelFromCurrency.removeAll();
		panelToCurrency.removeAll();

		// Add panels
		for (Currency currency : currencyList.getCurrencies())
		{
			com.pyesmeadow.george.currencyconverter.currency.CurrencyDetailsPanel panel = new CurrencyDetailsPanel(currency, 128);
			panelFromCurrency.add(panel, currency.getIdentifier());

			panel = new CurrencyDetailsPanel(currency, 128);
			panelToCurrency.add(panel, currency.getIdentifier());
		}

		FontUtil.updateComponentFontVariations(CurrencyConverter.getFontProfile(), false);
	}

	public void updateShownCurrencyPanels(String fromCurrencyID, String toCurrencyID)
	{
		CardLayout cl = (CardLayout) panelFromCurrency.getLayout();
		cl.show(panelFromCurrency, fromCurrencyID);

		cl = (CardLayout) panelToCurrency.getLayout();
		cl.show(panelToCurrency, toCurrencyID);
	}

	@Override
	public int getMinimumHeight()
	{
		return 285;
	}

	@Override
	public int getDefaultHeight()
	{
		return 285;
	}

	@Override
	public String getID()
	{
		return "Currency Details";
	}
}
