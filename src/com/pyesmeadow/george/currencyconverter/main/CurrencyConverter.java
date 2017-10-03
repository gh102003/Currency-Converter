package com.pyesmeadow.george.currencyconverter.main;

import com.pyesmeadow.george.currencyconverter.currency.Currency;
import com.pyesmeadow.george.currencyconverter.util.FontUtil;
import com.pyesmeadow.george.currencyconverter.util.FontUtil.FontProfile;

/**
 * @author georg_000
 * @version 1.0.0
 */
public class CurrencyConverter {

	public static CurrencyConverterFrame frame;

	public static final String VERSION = "1.0.0";

	private static FontProfile fontProfile = FontProfile.MEDIUM;

	public static void main(String[] args)
	{
		boolean shouldUpdateOnStart = true;

		for (String arg : args)
		{
			if (arg.contains("--disableInitialUpdate"))
			{
				shouldUpdateOnStart = false;
			}
		}

		frame = new CurrencyConverterFrame(shouldUpdateOnStart);
	}

	public static double convertCurrency(double amount, Currency currencyFrom, Currency currencyTo)
	{
		double currencyInUSD = amount * currencyFrom.getValueInUSD();
		return currencyInUSD / currencyTo.getValueInUSD();
	}

	public static FontProfile getFontProfile()
	{
		return fontProfile;
	}

	/**
	 * Sets the font profile. Also updates components.
	 *
	 * @param fontProfile
	 *            the profile to update to
	 */
	public static void setFontProfile(FontProfile fontProfile)
	{
		CurrencyConverter.fontProfile = fontProfile;
		FontUtil.updateComponentFontVariations(fontProfile, false);
	}
}
