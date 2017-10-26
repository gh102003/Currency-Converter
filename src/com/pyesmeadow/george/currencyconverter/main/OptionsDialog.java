package com.pyesmeadow.george.currencyconverter.main;

import com.pyesmeadow.george.currencyconverter.currency.Currency;
import com.pyesmeadow.george.currencyconverter.util.FontUtil;
import com.pyesmeadow.george.currencyconverter.util.FontUtil.FontProfile;
import com.pyesmeadow.george.currencyconverter.util.FontUtil.FontVariation;
import com.pyesmeadow.george.currencyconverter.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class OptionsDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -1768396463834050040L;

	public static JPanel panelFormatting = new JPanel();
	public static JCheckBox checkboxAlternativeEURFormatting = new JCheckBox("Use alternative formatting for Euros");
	public static JCheckBox checkboxAlternativeCADFormatting = new JCheckBox(
			"Use alternative formatting for Canadian Dollars");

	public static JPanel panelAppearance = new JPanel();
	public static JLabel labelFontSize = new JLabel("Font size");

	public static JComboBox<FontProfile> comboFontSize = new JComboBox<FontProfile>(FontProfile.values());

	public OptionsDialog()
	{
		/* Setup main layout */
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		setLayout(fl);

		/* Setup panels */
		panelFormatting.setLayout(new BoxLayout(panelFormatting, BoxLayout.PAGE_AXIS));
		panelFormatting.setBorder(BorderFactory.createTitledBorder("Formatting"));

		panelAppearance.setLayout(new GridBagLayout());
		panelAppearance.setBorder(BorderFactory.createTitledBorder("Appearance"));

		/* Setup components */
		labelFontSize.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
		labelFontSize.setLabelFor(comboFontSize);

		comboFontSize.setSelectedItem(CurrencyConverter.getFontProfile());

		/* Setup component tooltips */
		checkboxAlternativeEURFormatting.setToolTipText("Use alternative formatting when displaying Euros, "
				+ "like the conventions used in Ireland, the Netherlands and Italy");
		checkboxAlternativeCADFormatting.setToolTipText("Use alternative formatting when displaying Canadian Dollars, "
				+ "like the conventions used in French-speaking parts of Canada");

		/* Add components to panels */
		panelFormatting.add(checkboxAlternativeEURFormatting);
		panelFormatting.add(checkboxAlternativeCADFormatting);

		GridBagConstraints c = new GridBagConstraints();

		// Don't show font size controls on MacOS
		if(!Util.isRunningOnMac()) {
			c.gridx = 0;
			c.gridy = GridBagConstraints.RELATIVE;
			c.anchor = GridBagConstraints.LINE_START;
			c.weightx = 0;
			c.fill = GridBagConstraints.NONE;
			panelAppearance.add(labelFontSize, c);

			c.gridx = GridBagConstraints.RELATIVE;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.gridy = 0;
			c.weightx = 1;
			c.fill = GridBagConstraints.NONE;
			c.anchor = GridBagConstraints.LINE_START;
			panelAppearance.add(comboFontSize, c);
		}

		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;

		/* Add panels to frame */
		add(panelFormatting);
		add(panelAppearance);

		/* Add misc components to frame */

		/* Setup listeners */
		checkboxAlternativeEURFormatting.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e)
			{
				Currency currencyEUR = CurrencyConverter.frame.currencyManager.getCurrencyList().getCurrencyFromID("EUR");

				if(currencyEUR != null) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						currencyEUR.setFormatting(Currency.getCurrencyFormattingFromLocaleString("it_IT"));
					} else {
						currencyEUR.setFormatting(Currency.getCurrencyFormattingFromLocaleString("de_DE"));
					}
				}

				CurrencyConverter.frame.updateConversion();
			}
		});

		checkboxAlternativeCADFormatting.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e)
			{
				Currency currencyCAD = CurrencyConverter.frame.currencyManager.getCurrencyList().getCurrencyFromID("CAD");

				if(currencyCAD != null) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						currencyCAD.setFormatting(Currency.getCurrencyFormattingFromLocaleString("fr_CA"));
					} else {
						currencyCAD.setFormatting(Currency.getCurrencyFormattingFromLocaleString("en_CA"));
					}
				}

				CurrencyConverter.frame.updateConversion();
			}
		});

		comboFontSize.addActionListener(this);

		/* Setup frame */
		registerComponentFontVariations();
		FontUtil.updateComponentFontVariations(CurrencyConverter.getFontProfile(), false);
		setTitle("Options");
		setIconImages(CurrencyConverter.APP_ICONS);
		setSize(563, 250);
		setMinimumSize(new Dimension(377, 219));
		setVisible(true);
	}

	protected void registerComponentFontVariations()
	{
		FontUtil.registerComponentFontVariation(checkboxAlternativeEURFormatting, FontVariation.SMALL_PLAIN);
		FontUtil.registerComponentFontVariation(checkboxAlternativeCADFormatting, FontVariation.SMALL_PLAIN);

		FontUtil.registerComponentFontVariation(labelFontSize, FontVariation.SMALL_PLAIN);
		FontUtil.registerComponentFontVariation(comboFontSize, FontVariation.SMALL_PLAIN);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == comboFontSize) {
			CurrencyConverter.setFontProfile((FontProfile) comboFontSize.getSelectedItem());
		}
	}
}
