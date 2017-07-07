package com.pyesmeadow.george.currencyconverter.currency.manager;

import java.io.File;
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

	private ArrayList<Currency> currencies;

	public CurrencyList()
	{
		readCurrencies();
	}

	public void addCurrency(Currency currency)
	{
		currencies.add(currency);
		
		this.writeCurrencies();
	}

	/**
	 * @return whether or not the list contained the {@code currency}
	 */
	public boolean removeCurrency(Currency currency)
	{
		boolean existed = this.currencies.remove(currency);
		
		this.writeCurrencies();
		
		return existed;
	}
	
	/**
	 * @return whether or not the list contained the {@code currency}
	 */
	public boolean editCurrency(Currency oldCurrency, Currency newCurrency)
	{
		int index = this.currencies.indexOf(oldCurrency);
		
		if(index < 0 || index >= this.currencies.size()) return false;
		
		this.currencies.set(index, newCurrency);
		
		this.writeCurrencies();
		
		return true;
	}

	public Currency getCurrencyFromIdentifier(String identifier)
	{
		for(Currency currency : this.currencies)
		{
			if(currency.getIdentifier().equals(identifier))
			{
				return currency;
			}
		}

		return null;
	}

	public Currency getCurrencyFromLocale(Locale locale)
	{
		for(Currency currency : currencies)
		{
			if(locale.equals(currency.getLocale()))
			{
				return currency;
			}
		}

		return null;
	}

	/**
	 * Writes {@code currencies} to the currency list JSON
	 */
	@SuppressWarnings("unchecked")
	public void writeCurrencies()
	{
		JSONArray currencyListJSON = new JSONArray();

		for(Currency currency : this.currencies)
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

		try(FileWriter writer = new FileWriter(ResourceUtil.getCurrencyList(true)))
		{
			writer.write(mainJSON.toJSONString());
			writer.flush();

		} catch(Exception e)
		{
			// TODO handle exception
			e.printStackTrace();
		}
	}

	/**
	 * Reads the currency list JSON and sets {@code currencies} to the list of
	 * currencies
	 */
	public void readCurrencies()
	{
		ArrayList<Currency> currencyList = new ArrayList<Currency>();

		try
		{
			JSONParser parser = new JSONParser();

			// Check JSON exists
			File fileAppdataCurrencyList = ResourceUtil.getCurrencyList(true);

			// Parse JSON
			FileReader reader = new FileReader(fileAppdataCurrencyList);
			Object obj = parser.parse(reader);

			JSONObject mainJSON = (JSONObject) obj;
			JSONArray currencyListJSON = (JSONArray) mainJSON.get("currencies");

			for(Object c : currencyListJSON)
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
			}

		} catch(Exception e)
		{
			e.printStackTrace();
		}

		this.currencies = currencyList;
	}

	public ArrayList<Currency> getCurrencies()
	{
		return currencies;
	}

	public void setCurrencies(ArrayList<Currency> currencies)
	{
		this.currencies = currencies;
	}
}
