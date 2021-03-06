package com.pyesmeadow.george.currencyconverter.main;

import com.pyesmeadow.george.currencyconverter.currency.Currency;
import com.pyesmeadow.george.currencyconverter.util.FontUtil;
import com.pyesmeadow.george.currencyconverter.util.FontUtil.FontProfile;
import com.pyesmeadow.george.currencyconverter.util.ResourceUtil;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author georg_000
 */
public class CurrencyConverter {

	public static final String VERSION = "1.3.2";
	public static CurrencyConverterFrame frame;

	// Icons
	public static Image ADD_ICON;
	public static Image INFO_ICON;
	public static Image INFO_ICON_HOVER;
	public static Image EDIT_ICON;
	public static Image EDIT_ICON_HOVER;
	public static Image REMOVE_ICON;
	public static Image REMOVE_ICON_HOVER;
	public static Image LOGO;

	public static final int APP_ICON_VERSION = 3;
	public static List<Image> APP_ICONS = new ArrayList<>();

	private static FontProfile fontProfile = FontProfile.MEDIUM;

	public static void main(String[] args)
	{
		// Set L&F
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// Handle args
		boolean shouldUpdateOnStart = true;

		for (String arg : args)
		{
			if (arg.equals("--disableInitialUpdate"))
			{
				shouldUpdateOnStart = false;
			}
		}

		try
		{
			ADD_ICON = ResourceUtil.getImage("assets/icon/add.png");
			INFO_ICON = ResourceUtil.getImage("assets/icon/info.png");
			INFO_ICON_HOVER = ResourceUtil.getImage("assets/icon/info_hover.png");
			EDIT_ICON = ResourceUtil.getImage("assets/icon/edit.png");
			EDIT_ICON_HOVER = ResourceUtil.getImage("assets/icon/edit_hover.png");
			REMOVE_ICON = ResourceUtil.getImage("assets/icon/remove.png");
			REMOVE_ICON_HOVER = ResourceUtil.getImage("assets/icon/remove_hover.png");
			LOGO = ResourceUtil.getImage("assets/logo.png");

			Image icon16 = ResourceUtil.getImage("assets/icon/app_icon/v" + APP_ICON_VERSION + "/icon_16.png");
			Image icon32 = ResourceUtil.getImage("assets/icon/app_icon/v" + APP_ICON_VERSION + "/icon_32.png");
			Image icon64 = ResourceUtil.getImage("assets/icon/app_icon/v" + APP_ICON_VERSION + "/icon_64.png");
			Image icon128 = ResourceUtil.getImage("assets/icon/app_icon/v" + APP_ICON_VERSION + "/icon_128.png");
			Image icon256 = ResourceUtil.getImage("assets/icon/app_icon/v" + APP_ICON_VERSION + "/icon_256.png");

			APP_ICONS.add(icon16);
			APP_ICONS.add(icon32);
			APP_ICONS.add(icon64);
			APP_ICONS.add(icon128);
			APP_ICONS.add(icon256);
		}
		catch (FileNotFoundException e)
		{
			System.err.println("Error loading icons");
			e.printStackTrace();
		}

		frame = new CurrencyConverterFrame(shouldUpdateOnStart);
	}

	static double convertCurrency(double amount, Currency currencyFrom, Currency currencyTo)
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
	 * @param fontProfile the profile to update to
	 */
	static void setFontProfile(FontProfile fontProfile)
	{
		CurrencyConverter.fontProfile = fontProfile;
		FontUtil.updateComponentFontVariations(fontProfile, false);
	}
}
