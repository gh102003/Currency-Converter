package com.pyesmeadow.george.currencyconverter.main;

import com.pyesmeadow.george.currencyconverter.util.FontUtil;
import com.pyesmeadow.george.currencyconverter.util.FontUtil.FontVariation;
import com.pyesmeadow.george.currencyconverter.util.ResourceUtil;

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = 193595101584401663L;

	private static JLabel labelInfo = new JLabel("Currency Converter " + CurrencyConverter.VERSION);
	private static JLabel labelAuthor = new JLabel("by George Howarth");

	private static JLabel labelCopyright = new JLabel("\u00A9 George Howarth 2017");
	private static JLabel labelCopyright2 = new JLabel("All rights reserved");

	private static JLabel labelDataPath = new JLabel("Data path: " + ResourceUtil.getAppdataDirectory());

	private static JPanel panelNavigation = new JPanel();
	private static JButton btnClose = new JButton("Close");

	AboutDialog()
	{

		// Panel setup
		panelNavigation.setLayout(new BoxLayout(panelNavigation, BoxLayout.LINE_AXIS));
		panelNavigation.add(btnClose);
		panelNavigation.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// Add components
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));

		add(Box.createVerticalGlue());
		add(labelInfo);
		add(labelAuthor);
		add(Box.createRigidArea(new Dimension(0, 20)));
		add(labelCopyright);
		add(labelCopyright2);
		add(Box.createRigidArea(new Dimension(0, 20)));
		add(labelDataPath);
		add(Box.createVerticalGlue());
		add(panelNavigation);

		// Component Setup
		labelInfo.setAlignmentX(CENTER_ALIGNMENT);
		labelAuthor.setAlignmentX(CENTER_ALIGNMENT);

		labelCopyright.setAlignmentX(CENTER_ALIGNMENT);
		labelCopyright2.setAlignmentX(CENTER_ALIGNMENT);

		labelDataPath.setAlignmentX(CENTER_ALIGNMENT);

		btnClose.setAlignmentX(CENTER_ALIGNMENT);

		btnClose.addActionListener(evt ->
		{
			dispose();
		});

		/* Setup frame */
		registerComponentFontVariations();
		FontUtil.updateComponentFontVariations(CurrencyConverter.getFontProfile(), false);
		setTitle("About");
		setIconImages(CurrencyConverter.APP_ICONS);
		setSize(580, 300);
		// setResizable(false);
		setVisible(true);
	}

	private void registerComponentFontVariations()
	{
		FontUtil.registerComponentFontVariation(labelInfo, FontVariation.MEDIUM_BOLD);
		FontUtil.registerComponentFontVariation(labelAuthor, FontVariation.MEDIUM_PLAIN);
		FontUtil.registerComponentFontVariation(labelCopyright, FontVariation.MEDIUM_PLAIN);
		FontUtil.registerComponentFontVariation(labelCopyright2, FontVariation.MEDIUM_PLAIN);
		FontUtil.registerComponentFontVariation(labelDataPath, FontVariation.SMALL_PLAIN);

		FontUtil.registerComponentFontVariation(btnClose, FontVariation.MEDIUM_PLAIN);
	}
}
