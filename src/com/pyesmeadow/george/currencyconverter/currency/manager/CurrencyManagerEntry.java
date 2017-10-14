package com.pyesmeadow.george.currencyconverter.currency.manager;

import com.pyesmeadow.george.currencyconverter.currency.Currency;
import com.pyesmeadow.george.currencyconverter.main.CurrencyConverter;
import com.pyesmeadow.george.currencyconverter.util.FontUtil;
import com.pyesmeadow.george.currencyconverter.util.FontUtil.FontVariation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CurrencyManagerEntry extends JPanel {

	private static final long serialVersionUID = -5540690937827470111L;

	private Currency currency;

	private JLabel labelName;
	private JLabel labelEdit;
	private JLabel labelRemove;

	public CurrencyManagerEntry(Currency currency)
	{
		this.currency = currency;

		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		labelName = new JLabel(this.currency.getName() + " (" + this.currency.getIdentifier() + ")");
		labelName.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		FontUtil.registerComponentFontVariation(labelName, FontVariation.SMALL_PLAIN);

		labelEdit = new JLabel(new ImageIcon(CurrencyManagerDialog.editIcon));
		labelRemove = new JLabel(new ImageIcon(CurrencyManagerDialog.removeIcon));

		labelEdit.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		labelRemove.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		labelEdit.setVerticalAlignment(SwingConstants.CENTER);
		labelRemove.setVerticalAlignment(SwingConstants.CENTER);

		this.add(labelName);
		this.add(Box.createHorizontalGlue());
		this.add(labelEdit);
		this.add(labelRemove);

		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		this.createListeners();
	}

	protected void createListeners()
	{
		labelEdit.addMouseListener(new MouseListener() {
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
				labelEdit.setIcon(new ImageIcon(CurrencyManagerDialog.editIcon));
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				labelEdit.setIcon(new ImageIcon(CurrencyManagerDialog.editHoverIcon));
			}

			@Override
			public void mouseClicked(MouseEvent e)
			{

				Currency c = DialogEditCurrency.showDialog(CurrencyManagerEntry.this.currency);

				if (c != null)
				{
					CurrencyConverter.frame.currencyManager.editCurrency(CurrencyManagerEntry.this.currency, c);
				}

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
				labelRemove.setIcon(new ImageIcon(CurrencyManagerDialog.removeIcon));
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				labelRemove.setIcon(new ImageIcon(CurrencyManagerDialog.removeHoverIcon));
			}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				CurrencyConverter.frame.currencyManager.removeCurrency(CurrencyManagerEntry.this.currency);
			}
		});
	}
}
