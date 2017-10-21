package com.pyesmeadow.george.currencyconverter.util;

import com.pyesmeadow.george.currencyconverter.currency.Currency;

import java.io.File;
import java.text.NumberFormat;

public class Util {

	public static String toTitleCase(String string)
	{
		string = string.toLowerCase();
		char c = string.charAt(0);
		String s = new String("" + c);
		String f = s.toUpperCase();
		return f + string.substring(1);
	}

	public static boolean isRunningOnMac()
	{
		return System.getProperty("os.name").toUpperCase().contains("MAC");
	}

	public static String getFileExtension(File file)
	{
		String ext = null;
		String s = file.getName();
		int i = s.lastIndexOf('.');

		if(i > 0 && i < s.length() - 1)
		{
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	public static NumberFormat getUSDFormatting()
	{
		return Currency.getCurrencyFormattingFromLocaleString("en_US");
	}
}
