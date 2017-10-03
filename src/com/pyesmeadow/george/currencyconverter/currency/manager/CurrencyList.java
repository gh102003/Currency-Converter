package com.pyesmeadow.george.currencyconverter.currency.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Locale;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.pyesmeadow.george.currencyconverter.currency.Currency;
import com.pyesmeadow.george.currencyconverter.util.ResourceUtil;

public class CurrencyList {

	private ArrayList<Currency> currencies = new ArrayList<>();

	private CurrencyListHandler handler = new CurrencyListHandler();

	public CurrencyList()
	{
		handler.readCurrencies();
	}

	public void addCurrency(Currency currency)
	{
		currencies.add(currency);

		handler.writeCurrencies();
	}

	/**
	 * @return whether or not the list contained the {@code currency}
	 */
	public boolean removeCurrency(Currency currency)
	{
		boolean existed = this.currencies.remove(currency);

		handler.writeCurrencies();

		return existed;
	}

	/**
	 * @return whether or not the list contained the {@code currency}
	 */
	public boolean editCurrency(Currency oldCurrency, Currency newCurrency)
	{
		int index = this.currencies.indexOf(oldCurrency);

		if (index < 0 || index >= this.currencies.size())
			return false;

		this.currencies.set(index, newCurrency);

		handler.writeCurrencies();

		return true;
	}

	public void refreshFromJSON()
	{
		handler.readCurrencies();
	}

	public Currency getCurrencyFromID(String identifier)
	{
		for (Currency currency : this.currencies)
		{
			if (currency.getIdentifier().equals(identifier))
			{
				return currency;
			}
		}

		return null;
	}

	public Currency getCurrencyFromLocale(Locale locale)
	{
		for (Currency currency : currencies)
		{
			if (locale.equals(currency.getLocale()))
			{
				return currency;
			}
		}

		return null;
	}

	public ArrayList<Currency> getCurrencies()
	{
		return currencies;
	}

	/**
	 * Resets currencies to their default value.
	 */
	public void resetCurrencies()
	{
		try
		{
			ArrayList<Currency> currencyList = new ArrayList<Currency>();

			currencyList.add(new Currency("USD", "US Dollar", Currency.getLocaleFromLocaleString("en_US"), 1,
					"currency_icon/USD.png"));
			currencyList.add(new Currency("GBP", "Great British Pound", Currency.getLocaleFromLocaleString("en_GB"),
					1.23927, "currency_icon/GBP.png"));
			currencyList.add(new Currency("EUR", "Euro", Currency.getLocaleFromLocaleString("de_DE"), 1.07083,
					"currency_icon/EUR.png"));
			currencyList.add(new Currency("AUD", "Australian Dollar", Currency.getLocaleFromLocaleString("en_AU"),
					0.75469, "currency_icon/AUD.png"));
			currencyList.add(new Currency("CAD", "Canadian Dollar", Currency.getLocaleFromLocaleString("en_CA"),
					0.76617, "currency_icon/CAD.png"));
			currencyList.add(new Currency("JPY", "Japanese Yen", Currency.getLocaleFromLocaleString("ja_JP"), 0.00886,
					"currency_icon/JPY.png"));
			currencyList.add(new Currency("CNY", "Chinese Yuan", Currency.getLocaleFromLocaleString("zh_CN"), 0.14585,
					"currency_icon/CNY.png"));
			currencyList.add(new Currency("KRW", "South Korean Won", Currency.getLocaleFromLocaleString("ko_KR"),
					0.00086, "currency_icon/KRW.png"));
			currencyList.add(new Currency("XBT", "Bitcoin", Currency.getLocaleFromLocaleString("en_US"), 1001.76,
					"currency_icon/XBT.png"));

			CurrencyList.this.currencies = currencyList;

			handler.writeCurrencies();

		} catch (Exception e)
		{

		}
	}

	/**
	 * Handles reading and writing from and to the Currency List JSON
	 */
	private class CurrencyListHandler {

		/**
		 * Writes {@code currencies} to the currency list JSON
		 */
		@SuppressWarnings("unchecked")
		private void writeCurrencies()
		{
			JSONArray currencyListJSON = new JSONArray();

			for (Currency currency : CurrencyList.this.currencies)
			{
				JSONObject currencyJSON = new JSONObject();

				currencyJSON.put("id", currency.getIdentifier());
				currencyJSON.put("name", currency.getName());
				currencyJSON.put("locale", currency.getLocale().toString());
				currencyJSON.put("valueInUSD", currency.getValueInUSD());
				currencyJSON.put("iconPath", currency.getIconPath());

				currencyListJSON.add(currencyJSON);
			}

			JSONObject mainJSON = new JSONObject();

			mainJSON.put("currencies", currencyListJSON);

			try (FileWriter writer = new FileWriter(ResourceUtil.getCurrencyList()))
			{
				mainJSON.writeJSONString(writer);
				writer.flush();

			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		/**
		 * Reads the currency list JSON and sets {@code currencies} to the list
		 * of currencies
		 */
		private void readCurrencies()
		{
			ArrayList<Currency> currencyList = new ArrayList<Currency>();

			try
			{
				JSONParser parser = new JSONParser();

				// Load JSON
				File fileAppdataCurrencyList;
				try
				{
					fileAppdataCurrencyList = ResourceUtil.getCurrencyList();

					// Parse JSON
					FileReader reader = new FileReader(fileAppdataCurrencyList);
					Object obj = parser.parse(reader);

					JSONObject mainJSON = (JSONObject) obj;
					JSONArray currencyListJSON = (JSONArray) mainJSON.get("currencies");

					for (Object c : currencyListJSON)
					{

						// Cast to JSONObject
						JSONObject currencyJSON = (JSONObject) c;

						// Setup currency parameters
						String identifier = (String) currencyJSON.get("id");
						String name = (String) currencyJSON.get("name");
						Locale locale = Currency.getLocaleFromLocaleString((String) currencyJSON.get("locale"));
						double valueInUSD = (Double) currencyJSON.get("valueInUSD");
						String iconPath = (String) currencyJSON.get("iconPath");

						// Create a new Currency and add it to the list
						Currency currency = new Currency(identifier, name, locale, valueInUSD, iconPath);
						currencyList.add(currency);

						CurrencyList.this.currencies = currencyList;
					}

				} catch (FileNotFoundException e)
				{
					String path = ResourceUtil.getAppdataDirectory().getAbsolutePath() + "/currencies.json";
					new File(path).createNewFile();
					resetCurrencies();
				}

			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
