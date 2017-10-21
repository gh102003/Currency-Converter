package com.pyesmeadow.george.currencyconverter.save.gui;

import com.pyesmeadow.george.currencyconverter.currency.Currency;
import com.pyesmeadow.george.currencyconverter.currency.CurrencyDetailsPanel;
import com.pyesmeadow.george.currencyconverter.main.CurrencyConverter;
import com.pyesmeadow.george.currencyconverter.save.Save;
import com.pyesmeadow.george.currencyconverter.util.FontUtil;

import javax.swing.*;
import java.awt.*;

class SaveInfoDialog extends JDialog {

	private final Save SAVE;

	private final CurrencyDetailsPanel panelFromCurrency;
	private final JLabel labelFromAmount;

	private final CurrencyDetailsPanel panelToCurrency;
	private final JLabel labelToAmount;

	SaveInfoDialog(final Save save)
	{
		this.SAVE = save;

		Currency fromCurrency = save.getFromCurrency();
		Currency toCurrency = save.getToCurrency();

		panelFromCurrency = new CurrencyDetailsPanel(fromCurrency, 64);
		panelToCurrency = new CurrencyDetailsPanel(toCurrency, 64);

		labelFromAmount = new JLabel(fromCurrency.getFormatting().format(save.getFromAmount()));
		labelToAmount = new JLabel(toCurrency.getFormatting().format(save.getToAmount()));

		panelFromCurrency.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		panelToCurrency.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = GridBagConstraints.RELATIVE;
		c.gridy = 0;
		c.insets = new Insets(4, 4, 4, 4);
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(panelFromCurrency, c);
		add(panelToCurrency, c);
		c.gridy = 1;
		add(labelFromAmount, c);
		add(labelToAmount, c);

		registerComponentFontVariations();
		FontUtil.updateComponentFontVariations(CurrencyConverter.getFontProfile(), false);

		setSize(new Dimension(450, 250));
		setMinimumSize(new Dimension(450, 250));
		setMaximumSize(new Dimension(480, 280));
		setIconImages(CurrencyConverter.frame.getIconImages());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Save info");
		pack();
		revalidate();
		setVisible(true);
	}

	protected void registerComponentFontVariations()
	{
		FontUtil.registerComponentFontVariation(labelFromAmount, FontUtil.FontVariation.SMALL_SYMBOL);
		FontUtil.registerComponentFontVariation(labelToAmount, FontUtil.FontVariation.SMALL_SYMBOL);
	}
}
