package com.pyesmeadow.george.currencyconverter.currency;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.pyesmeadow.george.currencyconverter.util.FontUtil;
import com.pyesmeadow.george.currencyconverter.util.FontUtil.FontVariation;
import com.pyesmeadow.george.currencyconverter.util.ResourceUtil;

public class CurrencyDetailsPanel extends JPanel {

	private static final long serialVersionUID = 6454486646470504024L;

	public Currency currency;

	public JLabel labelCurrencyName;
	public JLabel labelCurrencyValue;
	public JLabel labelCurrencyIcon;

	public CurrencyDetailsPanel(Currency currency)
	{
		/* Set layout of the panel */
		setLayout(new GridBagLayout());

		/* Save currency */
		this.currency = currency;

		/* Create components */
		labelCurrencyName = new JLabel(currency.getName() + " (" + currency.getIdentifier() + ")");

		NumberFormat valueFormatting = Currency.getCurrencyFormattingFromLocaleString("en_US");
		//valueFormatting.setRoundingMode(RoundingMode.HALF_UP);
		
		labelCurrencyValue = new JLabel("Value in US Dollars: " + valueFormatting.format(currency.getValueInUSD()));

		labelCurrencyIcon = new JLabel(
				new ImageIcon(ResourceUtil.resizeImage(currency.getIcon().getImage(), 128, 128)));

		/* Setup components */

		labelCurrencyIcon.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		/* Setup GridBagConstraints */
		GridBagConstraints constraintsText = new GridBagConstraints();
		constraintsText.gridx = 0;
		constraintsText.gridy = GridBagConstraints.RELATIVE;
		constraintsText.anchor = GridBagConstraints.LINE_START;
		constraintsText.fill = GridBagConstraints.HORIZONTAL;
		constraintsText.weightx = 1.0;

		GridBagConstraints constraintsIcon = new GridBagConstraints();
		constraintsIcon.gridx = 0;
		constraintsIcon.gridy = GridBagConstraints.RELATIVE;
		constraintsIcon.anchor = GridBagConstraints.PAGE_START;
		constraintsText.fill = GridBagConstraints.HORIZONTAL;
		constraintsIcon.weighty = 1.0;

		/* Add components */
		add(labelCurrencyName, constraintsText);
		add(labelCurrencyValue, constraintsText);
		add(labelCurrencyIcon, constraintsIcon);

		/* Setup panel */
		registerComponentFontVariations();
		setPreferredSize(new Dimension(250, 210));
	}

	public void registerComponentFontVariations()
	{
		FontUtil.registerComponentFontVariation(labelCurrencyName, FontVariation.SMALL_BOLD);
		FontUtil.registerComponentFontVariation(labelCurrencyValue, FontVariation.SMALL_PLAIN);
	}
}
