package com.pyesmeadow.george.currencyconverter.currency;

import com.pyesmeadow.george.currencyconverter.util.FontUtil;
import com.pyesmeadow.george.currencyconverter.util.ResourceUtil;
import com.pyesmeadow.george.currencyconverter.util.Util;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;

public class CurrencyDetailsPanel extends JPanel {

	private static final long serialVersionUID = 6454486646470504024L;
	private final JLabel labelCurrencyName;
	private final JLabel labelCurrencyValue;
	private final JLabel labelCurrencyValueInverse;
	private final JLabel labelCurrencyIcon;
	private final JButton btnWikipedia;
	public Currency currency;

	public CurrencyDetailsPanel(Currency currency, int iconSize)
	{
		/* Set layout of the panel */
		setLayout(new GridBagLayout());

		/* Save currency */
		this.currency = currency;

		/* Create components */
		labelCurrencyName = new JLabel(currency.getName() + " (" + currency.getIdentifier() + ")");

		labelCurrencyValue = new JLabel("Worth US" + Util.getUSDFormatting().format(currency.getValueInUSD()));
		labelCurrencyValueInverse = new JLabel("US$1 is worth " + currency.getFormatting().format(1 / currency.getValueInUSD()));

		labelCurrencyIcon = new JLabel(new ImageIcon(ResourceUtil.resizeImage(currency.getIcon().getImage(),
				iconSize,
				iconSize)));

		btnWikipedia = new JButton("View on Wikipedia");
		btnWikipedia.addActionListener(l ->
		{
			try
			{
				ResourceUtil.openAbsoluteFile(new URI("https://www.wikipedia.org/wiki/" + currency.getName().replace(" ", "_")));
			}
			catch (URISyntaxException e)
			{
				e.printStackTrace();
			}
		});

		int borderSize = Math.floorDiv(iconSize, 7);

		labelCurrencyIcon.setBorder(BorderFactory.createEmptyBorder(borderSize, borderSize, borderSize, borderSize));

		/* Setup GridBagConstraints */
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.insets = new Insets(8, 8, 0, 8);
		add(labelCurrencyName, c);
		add(labelCurrencyValue, c);
		c.insets = new Insets(0, 8, 8, 8);
		add(labelCurrencyValueInverse, c);
		c.insets = new Insets(8, 8, 8, 8);
		add(labelCurrencyIcon, c);
		c.insets = new Insets(0, 8, 8, 8);
		add(btnWikipedia, c);

		/* Setup panel */
		registerComponentFontVariations();
		setPreferredSize(new Dimension(250, 160 + iconSize));
		setMaximumSize(new Dimension(250, 160 + iconSize));
	}

	protected void registerComponentFontVariations()
	{
		FontUtil.registerComponentFontVariation(labelCurrencyName, FontUtil.FontVariation.SMALL_BOLD);
		FontUtil.registerComponentFontVariation(labelCurrencyValue, FontUtil.FontVariation.SMALL_PLAIN);
		FontUtil.registerComponentFontVariation(labelCurrencyValueInverse, FontUtil.FontVariation.SMALL_SYMBOL);
		FontUtil.registerComponentFontVariation(btnWikipedia, FontUtil.FontVariation.SMALL_PLAIN);
	}
}
