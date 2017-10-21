package com.pyesmeadow.george.currencyconverter.main;

import com.pyesmeadow.george.currencyconverter.currency.Currency;
import com.pyesmeadow.george.currencyconverter.util.FontUtil;
import com.pyesmeadow.george.currencyconverter.util.FontUtil.FontProfile;
import com.pyesmeadow.george.currencyconverter.util.ResourceUtil;

import java.awt.*;
<<<<<<< HEAD
import java.util.List;
=======
import java.io.FileNotFoundException;
>>>>>>> saves

/**
 * @author georg_000
 * @version 1.0.1
 */
public class CurrencyConverter {

	public static CurrencyConverterFrame frame;

	public static final String VERSION = "1.0.0";

	private static FontProfile fontProfile = FontProfile.MEDIUM;

	// Icons
	public static Image ADD_ICON;
	public static Image INFO_ICON;
	public static Image INFO_ICON_HOVER;
	public static Image EDIT_ICON;
	public static Image EDIT_ICON_HOVER;
	public static Image REMOVE_ICON;
	public static Image REMOVE_ICON_HOVER;

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

		try
		{
			ADD_ICON = ResourceUtil.getImage("assets/add.png");
			INFO_ICON = ResourceUtil.getImage("assets/info.png");
			INFO_ICON_HOVER = ResourceUtil.getImage("assets/info_hover.png");
			EDIT_ICON = ResourceUtil.getImage("assets/edit.png");
			EDIT_ICON_HOVER = ResourceUtil.getImage("assets/edit_hover.png");
			REMOVE_ICON = ResourceUtil.getImage("assets/remove.png");
			REMOVE_ICON_HOVER = ResourceUtil.getImage("assets/remove_hover.png");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
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
