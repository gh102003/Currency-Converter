package com.pyesmeadow.george.currencyconverter.save;

import com.pyesmeadow.george.currencyconverter.currency.Currency;
import com.pyesmeadow.george.currencyconverter.main.CurrencyConverter;
import com.pyesmeadow.george.currencyconverter.util.FontUtil;

import javax.swing.*;
import java.awt.*;

public class SavePanel extends JPanel {

	private final JPanel panelSaves = new JPanel(new GridBagLayout());
	private final JScrollPane scrollPaneSaves = new JScrollPane(panelSaves,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	private final SaveManager saveManager;

	private final JLabel labelEmpty = new JLabel("No conversions have been saved yet");

	public SavePanel(SaveManager saveManager)
	{
		this.saveManager = saveManager;

		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.weightx = 1.0;

		add(scrollPaneSaves, c);

		FontUtil.registerComponentFontVariation(labelEmpty, FontUtil.FontVariation.SMALL_PLAIN);

		repopulateSaves();
	}

	public void repopulateSaves()
	{
		// Remove every SavePanelEntry
		panelSaves.removeAll();

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.weightx = 1.0;
		c.insets = new Insets(3, 3, 3, 3);

		// For each save, add a SavePanelEntry
		for (Save save : saveManager)
		{
			panelSaves.add(new SavePanelEntry(save), c);
		}

		// If there are no saves, add a label
		if (panelSaves.getComponentCount() < 1)
		{
			panelSaves.add(labelEmpty);
		}

		revalidate();
	}

	private class SavePanelEntry extends JPanel {

		private final Currency FROM_CURRENCY;
		private final double FROM_AMOUNT;
		private final Currency TO_CURRENCY;
		private final double TO_AMOUNT;

		private final JLabel labelFromCurrency;
		private final JLabel labelFromAmount;
		private final JLabel labelArrow;
		private final JLabel labelToCurrency;
		private final JLabel labelToAmount;

		public SavePanelEntry(Save save)
		{
			setLayout(new GridBagLayout());

			this.FROM_CURRENCY = save.getFromCurrency();
			this.FROM_AMOUNT = save.getFromAmount();
			this.TO_CURRENCY = save.getToCurrency();
			this.TO_AMOUNT = save.getToAmount();

			labelFromCurrency = new JLabel(FROM_CURRENCY.getName() + " (" + FROM_CURRENCY.getIdentifier() + ")");
			labelFromAmount = new JLabel(FROM_CURRENCY.getCurrencyFormatting().format(FROM_AMOUNT));
			labelArrow = new JLabel("=");
			labelToCurrency = new JLabel(TO_CURRENCY.getName() + " (" + TO_CURRENCY.getIdentifier() + ")");
			labelToAmount = new JLabel(TO_CURRENCY.getCurrencyFormatting().format(TO_AMOUNT));

			GridBagConstraints c = new GridBagConstraints();
			c.gridx = GridBagConstraints.RELATIVE;
			c.gridy = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.CENTER;
			c.insets = new Insets(5, 5, 5, 5);

			add(labelFromCurrency, c);
			add(labelFromAmount, c);
			add(labelArrow, c);
			add(labelToCurrency, c);
			add(labelToAmount, c);

			registerComponentFontVariations();
			FontUtil.updateComponentFontVariations(CurrencyConverter.getFontProfile(), false);

			setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}

		private void registerComponentFontVariations()
		{
			FontUtil.registerComponentFontVariation(labelFromCurrency, FontUtil.FontVariation.SMALL_PLAIN);
			FontUtil.registerComponentFontVariation(labelFromAmount, FontUtil.FontVariation.SMALL_PLAIN);
			FontUtil.registerComponentFontVariation(labelArrow, FontUtil.FontVariation.SMALL_PLAIN);
			FontUtil.registerComponentFontVariation(labelToCurrency, FontUtil.FontVariation.SMALL_PLAIN);
			FontUtil.registerComponentFontVariation(labelToAmount, FontUtil.FontVariation.SMALL_PLAIN);
		}
	}
}
