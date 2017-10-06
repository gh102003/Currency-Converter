package com.pyesmeadow.george.currencyconverter.saves;

import com.pyesmeadow.george.currencyconverter.currency.Currency;
import com.pyesmeadow.george.currencyconverter.main.CurrencyConverter;
import org.json.simple.JSONObject;
import sun.util.resources.cldr.uk.CurrencyNames_uk;

import java.util.Locale;

@SuppressWarnings("unchecked")
public class Save {

	private final Currency fromCurrency;
	private final double fromAmount;
	private final Currency toCurrency;
	private final double toAmount;

	Save(Currency fromCurrency, double fromAmount, Currency toCurrency, double toAmount)
	{
		this.fromCurrency = fromCurrency;
		this.fromAmount = fromAmount;
		this.toCurrency = toCurrency;
		this.toAmount = toAmount;
	}

	/**
	 * Converts a stored JSON object into a Save
	 */
	static Save deserializeFromJSON(JSONObject input) throws IllegalArgumentException
	{
		try
		{
			Currency fromCurrency = Currency.deserializeFromJSON((JSONObject) input.get("fromCurrency"));
			double fromAmount = (double) input.get("fromAmount");
			Currency toCurrency = Currency.deserializeFromJSON((JSONObject) input.get("toCurrency"));
			double toAmount = (double) input.get("toAmount");

			return new Save(fromCurrency, fromAmount, toCurrency, toAmount);
		}
		catch (IllegalArgumentException | ClassCastException | NullPointerException e)
		{
			throw new IllegalArgumentException("The formatting of currencies.json has missing or incorrect fields");
		}
	}

	/**
	 * Creates a JSON object from this Save
	 */
	JSONObject serializeToJSON()
	{
		JSONObject output = new JSONObject();

		output.put("fromCurrency", fromCurrency.serializeToJSON());
		output.put("fromAmount", fromAmount);
		output.put("toCurrency", toCurrency.serializeToJSON());
		output.put("toAmount", toAmount);

		return output;
	}

	Currency getFromCurrency()
	{
		return fromCurrency;
	}

	Currency getToCurrency()
	{
		return toCurrency;
	}

	double getFromAmount()
	{
		return fromAmount;
	}

	double getToAmount()
	{
		return toAmount;
	}
}
