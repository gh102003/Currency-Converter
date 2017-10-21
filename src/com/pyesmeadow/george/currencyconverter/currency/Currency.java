package com.pyesmeadow.george.currencyconverter.currency;

import com.pyesmeadow.george.currencyconverter.util.ResourceUtil;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;

public class Currency {

	private String identifier;
	private String name;
	private NumberFormat formatting;
	private Locale locale;
	private double valueInUSD;
	private String iconPath;

	public Currency(String identifier, String name, Locale locale, double valueInUSD, String iconPath)
	{
		this.identifier = identifier;
		this.name = name;
		this.formatting = NumberFormat.getCurrencyInstance(locale);
		this.locale = locale;
		this.valueInUSD = valueInUSD;
		this.iconPath = iconPath;
	}

	/**
	 * Converts a stored JSON object into a Currency
	 */
	public static Currency deserializeFromJSON(JSONObject currencyJSON) throws IllegalArgumentException
	{
		try
		{
			// Setup currency parameters
			String identifier = (String) currencyJSON.get("id");
			String name = (String) currencyJSON.get("name");
			Locale locale = Currency.getLocaleFromLocaleString((String) currencyJSON.get("locale"));
			double valueInUSD = (Double) currencyJSON.get("valueInUSD");
			String iconPath = (String) currencyJSON.get("iconPath");

			// Create a new Currency
			return new Currency(identifier, name, locale, valueInUSD, iconPath);
		}
		catch (ClassCastException | NullPointerException e)
		{
			throw new IllegalArgumentException("The formatting of currencies.json has missing or incorrect fields");
		}
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
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			return null;
		}
	}

	public static NumberFormat getCurrencyFormattingFromLocaleString(String localeString)
	{
		return NumberFormat.getCurrencyInstance(getLocaleFromLocaleString(localeString));
	}

	/**
	 * Creates a JSON object from this Currency
	 */
	public JSONObject serializeToJSON()
	{
		JSONObject currencyJSON = new JSONObject();

		currencyJSON.put("id", this.getIdentifier());
		currencyJSON.put("name", this.getName());
		currencyJSON.put("locale", this.getLocale().toString());
		currencyJSON.put("valueInUSD", this.getValueInUSD());
		currencyJSON.put("iconPath", this.getIconPath());

		return currencyJSON;
	}

	public String getIdentifier()
	{
		return identifier;
	}

	public NumberFormat getFormatting()
	{
		return formatting;
	}

	public void setFormatting(NumberFormat formatting)
	{
		this.formatting = formatting;
	}

	public double getValueInUSD()
	{
		return valueInUSD;
	}

	public void setValueInUSD(double valueInUSD)
	{
		this.valueInUSD = valueInUSD;
	}

	public String getName()
	{
		return name;
	}

	/**
	 * @return the icon to use for this currency
	 */
	public ImageIcon getIcon()
	{
		String iconPathAppdata = ResourceUtil.getAppdataDirectory() + "/" + iconPath;
		URL iconPathResources = ResourceUtil.class.getClassLoader().getResource("assets/" + iconPath);
		URL iconPathUnknown = ResourceUtil.class.getClassLoader().getResource("assets/currency_icon/unknown.png");

		if (new File(iconPathAppdata).exists() && new File(iconPathAppdata).isFile())
		{

			return new ImageIcon(iconPathAppdata);
		}

		try
		{
			if (new File(iconPathResources.toURI().getPath()).canRead() && new File(iconPathResources.toURI().getPath()).isFile())
			{

				return new ImageIcon(Toolkit.getDefaultToolkit().getImage(iconPathResources));

			}
		}
		catch (Exception e)
		{
		}

		System.err.println(this.name + " has no image.");
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(iconPathUnknown));
	}

	public Locale getLocale()
	{
		return locale;
	}

	/**
	 * Used for writing to JSON
	 *
	 * @return a String that can be used as an {@code iconPath} to construct a
	 * {@code Currency}
	 */
	public String getIconPath()
	{
		return iconPath;
	}
}
