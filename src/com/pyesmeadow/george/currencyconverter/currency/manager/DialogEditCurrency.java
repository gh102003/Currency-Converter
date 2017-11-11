package com.pyesmeadow.george.currencyconverter.currency.manager;

import com.pyesmeadow.george.currencyconverter.currency.Currency;
import com.pyesmeadow.george.currencyconverter.main.CurrencyConverter;
import com.pyesmeadow.george.currencyconverter.util.PNGFileFilter;
import com.pyesmeadow.george.currencyconverter.util.ResourceUtil;

import javax.swing.*;
import java.awt.*;

public class DialogEditCurrency extends JDialog {

	private static final long serialVersionUID = 3281730857908243553L;

	private JPanel panel = new JPanel(new GridBagLayout());

	private JLabel labelName = new JLabel("Name:");
	private JTextField fieldName = new JTextField(10);

	private JLabel labelIdentifier = new JLabel("ID:");
	private JTextField fieldIdentifier = new JTextField(10);

	private JLabel labelLocale = new JLabel("Locale:");
	private JTextField fieldLocale = new JTextField(10);

	private JLabel labelValueInUSD = new JLabel("Value in USD:");
	private JTextField fieldValueInUSD = new JTextField(10);

	private JLabel labelIconPath = new JLabel("Icon:");
	private JTextField fieldIconPath = new JTextField(20);
	private JButton btnBrowse = new JButton("Browse...");

	private DialogEditCurrency(Currency defaultValues)
	{
		fieldName.setText(defaultValues.getName());
		fieldIdentifier.setText(defaultValues.getIdentifier());
		fieldLocale.setText(defaultValues.getLocale().toString());
		fieldValueInUSD.setText(String.valueOf(defaultValues.getValueInUSD()));
		fieldIconPath.setText(defaultValues.getIconPath());

		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 5, 5, 5);

		panel.add(labelName, c);
		panel.add(labelIdentifier, c);
		panel.add(labelLocale, c);
		panel.add(labelValueInUSD, c);
		panel.add(labelIconPath, c);

		c.gridx = 1;
		c.gridy = GridBagConstraints.RELATIVE;
		c.gridwidth = 2;
		c.gridheight = 1;

		panel.add(fieldName, c);
		panel.add(fieldIdentifier, c);
		panel.add(fieldLocale, c);
		panel.add(fieldValueInUSD, c);

		c.gridwidth = 1;

		panel.add(fieldIconPath, c);

		c.gridx = 2;
		panel.add(btnBrowse, c);

		this.addListeners();
	}

	public static Currency showDialog(Currency defaultValues)
	{
		DialogEditCurrency dialog = new DialogEditCurrency(defaultValues);

		int result = JOptionPane.showOptionDialog(null, dialog.panel, "Edit Currency", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				new ImageIcon(CurrencyConverter.EDIT_ICON),
				new String[] { "Edit Currency", "Cancel" }, "Cancel");

		if(result == JOptionPane.OK_OPTION)
		{
			try
			{
				return new Currency(dialog.fieldIdentifier.getText(), dialog.fieldName.getText(),
						Currency.getLocaleFromLocaleString(dialog.fieldLocale.getText()),
						Double.parseDouble(dialog.fieldValueInUSD.getText()), dialog.fieldIconPath.getText());
			}
			catch (NullPointerException | NumberFormatException e)
			{
				e.printStackTrace();
			}
		}

		return null;
	}

	protected void addListeners()
	{
		btnBrowse.addActionListener(e -> {

			JFileChooser fileChooser = new JFileChooser(ResourceUtil.getAppdataDirectory());

			fileChooser.setFileFilter(new PNGFileFilter());

			int returnVal = fileChooser.showDialog(this, "Select Icon");

			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				String absolutePath = fileChooser.getSelectedFile().getPath();
				String relativePath = absolutePath.substring(absolutePath.lastIndexOf("currency_icon"));

				this.fieldIconPath.setText(relativePath);
			}

		});
	}
}
