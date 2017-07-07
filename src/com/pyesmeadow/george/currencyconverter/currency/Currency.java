package com.pyesmeadow.george.currencyconverter.currency;

import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.ImageIcon;

import com.pyesmeadow.george.currencyconverter.util.ResourceUtil;

public class Currency {

	private String identifier;
	private String name;
	private NumberFormat currencyFormatting;
	private Locale locale;
	private double valueInUSD;
	private String iconPath;

	public Currency(String identifier, String name, Locale locale, double valueInUSD, String iconPath)
	{
		this.identifier = identifier;
		this.name = name;
		this.currencyFormatting = NumberFormat.getCurrencyInstance(locale);
		this.locale = locale;
		this.valueInUSD = valueInUSD;
		this.iconPath = iconPath;
	}

	public String getIdentifier()
	{
		return identifier;
	}

	public NumberFormat getCurrencyFormatting()
	{
		return currencyFormatting;
	}

	public double getValueInUSD()
	{
		return valueInUSD;
	}

	public String getName()
	{
		return name;
	}

	public void setCurrencyFormatting(NumberFormat currencyFormatting)
	{
		this.currencyFormatting = currencyFormatting;
	}

	/**
	 * @return the icon to use for this currency
	 */
	public ImageIcon getIcon()
	{
		String iconPathAppdata = ResourceUtil.getAppdataDirectory() + "/" + iconPath;
		URL iconPathResources = ResourceUtil.class.getClassLoader().getResource("assets/" + iconPath);
		URL iconPathUnknown = ResourceUtil.class.getClassLoader().getResource("assets/currency_icon/unknown.png");

		if(new File(iconPathAppdata).exists() && new File(iconPathAppdata).isFile())
		{

			return new ImageIcon(iconPathAppdata);
		}

		try
		{
			if(new File(iconPathResources.toURI().getPath()).canRead()
					&& new File(iconPathResources.toURI().getPath()).isFile())
			{

				return new ImageIcon(Toolkit.getDefaultToolkit().getImage(iconPathResources));

			}
		} catch(Exception e)
		{
		}

		System.err.println(this.name + " has no image.");
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(iconPathUnknown));
	}

	public Locale getLocale()
	{
		return locale;
	}

	public void setValueInUSD(double valueInUSD)
	{
		this.valueInUSD = valueInUSD;
	}

	/**
	 * Used for writing to JSON
	 * 
	 * @return a String that can be used as an {@code iconPath} to construct a
	 *         {@code Currency}
	 */
	public String getIconPath()
	{
		return iconPath;
	}

	/**
	 * Locale strings should be in the format 'la_CO', with 'la' as the language
	 * and 'CO' as the country.
	 * 
	 * @param localeString
	 * @return
	 */
	public static Locale getLocaleFromLocaleString(String localeString)
	{
		try
		{
			return new Locale(localeString.split("_")[0], localeString.split("_")[1]);
		} catch(ArrayIndexOutOfBoundsException e)
		{
			return null;
		}
	}

	public static NumberFormat getCurrencyFormattingFromLocaleString(String localeString)
	{
		return NumberFormat.getCurrencyInstance(getLocaleFromLocaleString(localeString));
	}
}
