package com.pyesmeadow.george.currencyconverter.save.gui;

import com.pyesmeadow.george.currencyconverter.main.CurrencyConverter;
import com.pyesmeadow.george.currencyconverter.main.IToggleableComponent;
import com.pyesmeadow.george.currencyconverter.save.Save;
import com.pyesmeadow.george.currencyconverter.save.SaveManager;
import com.pyesmeadow.george.currencyconverter.util.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SavePanel extends JPanel implements IToggleableComponent {

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
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.8;
		c.weighty = 0.8;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;

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
		c.insets = new Insets(4, 4, 4, 4);

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

		FontUtil.updateComponentFontVariations(CurrencyConverter.getFontProfile(), false);
		revalidate();
	}

	@Override
	public int getMinimumHeight()
	{
		return 100;
	}

	@Override
	public int getDefaultHeight()
	{
		return 180;
	}

	@Override
	public String getID()
	{
		return "Saves";
	}

	@Override
	public GridBagConstraints getGridBagConstraints()
	{
		GridBagConstraints c = IToggleableComponent.super.getGridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		return c;
	}

	private class SavePanelEntry extends JPanel {

		private final JLabel labelFromCurrency;
		private final JLabel labelFromAmount;
		private final JLabel labelArrow;
		private final JLabel labelToCurrency;
		private final JLabel labelToAmount;

		private final JLabel labelInfo;
		private final JLabel labelRemove;

		// For reference only
		private final Save save;

		SavePanelEntry(Save save)
		{
			this.save = save;

			setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

			labelFromCurrency = new JLabel(save.getFromCurrency().getName() + " (" + save.getFromCurrency().getIdentifier() + ")");
			labelFromAmount = new JLabel(save.getFromCurrency().getFormatting().format(save.getFromAmount()));
			labelArrow = new JLabel("=");
			labelToCurrency = new JLabel(save.getToCurrency().getName() + " (" + save.getToCurrency().getIdentifier() + ")");
			labelToAmount = new JLabel(save.getToCurrency().getFormatting().format(save.getToAmount()));
			labelInfo = new JLabel(new ImageIcon(CurrencyConverter.INFO_ICON));
			labelRemove = new JLabel(new ImageIcon(CurrencyConverter.REMOVE_ICON));

			labelFromCurrency.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
			labelFromAmount.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
			labelArrow.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
			labelToCurrency.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
			labelToAmount.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
			labelInfo.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
			labelRemove.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

			labelFromCurrency.setVerticalAlignment(SwingConstants.CENTER);
			labelFromAmount.setVerticalAlignment(SwingConstants.CENTER);
			labelArrow.setVerticalAlignment(SwingConstants.CENTER);
			labelToCurrency.setVerticalAlignment(SwingConstants.CENTER);
			labelToAmount.setVerticalAlignment(SwingConstants.CENTER);
			labelInfo.setVerticalAlignment(SwingConstants.CENTER);
			labelRemove.setVerticalAlignment(SwingConstants.CENTER);

			add(labelFromCurrency);
			add(labelFromAmount);
			add(labelArrow);
			add(labelToCurrency);
			add(labelToAmount);
			add(Box.createHorizontalGlue());
			add(labelInfo);
			add(labelRemove);

			createListeners();
			registerComponentFontVariations();

			setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}

		private void createListeners()
		{
			labelInfo.addMouseListener(new MouseListener() {
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
					labelInfo.setIcon(new ImageIcon(CurrencyConverter.INFO_ICON));
				}

				@Override
				public void mouseEntered(MouseEvent e)
				{
					labelInfo.setIcon(new ImageIcon(CurrencyConverter.INFO_ICON_HOVER));
				}

				@Override
				public void mouseClicked(MouseEvent e)
				{
					new SaveInfoDialog(save);
				}
			});

			// TODO Fix rendering issues
			labelRemove.addMouseListener(new MouseListener() {

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
					labelRemove.setIcon(new ImageIcon(CurrencyConverter.REMOVE_ICON));
				}

				@Override
				public void mouseEntered(MouseEvent e)
				{
					labelRemove.setIcon(new ImageIcon(CurrencyConverter.REMOVE_ICON_HOVER));
				}

				@Override
				public void mouseClicked(MouseEvent e)
				{
					saveManager.removeSave(save);
				}
			});
		}

		private void registerComponentFontVariations()
		{
			FontUtil.registerComponentFontVariation(labelFromCurrency, FontUtil.FontVariation.SMALL_PLAIN);
			FontUtil.registerComponentFontVariation(labelFromAmount, FontUtil.FontVariation.SMALL_SYMBOL);
			FontUtil.registerComponentFontVariation(labelArrow, FontUtil.FontVariation.SMALL_PLAIN);
			FontUtil.registerComponentFontVariation(labelToCurrency, FontUtil.FontVariation.SMALL_PLAIN);
			FontUtil.registerComponentFontVariation(labelToAmount, FontUtil.FontVariation.SMALL_SYMBOL);
		}
	}
}
