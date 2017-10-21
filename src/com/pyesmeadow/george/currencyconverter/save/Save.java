package com.pyesmeadow.george.currencyconverter.save;

import com.pyesmeadow.george.currencyconverter.currency.Currency;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class Save {

	private final Currency fromCurrency;
	private final double fromAmount;
	private final Currency toCurrency;
	private final double toAmount;

	public Save(Currency fromCurrency, double fromAmount, Currency toCurrency, double toAmount)
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

	public Currency getFromCurrency()
	{
		return fromCurrency;
	}

	public Currency getToCurrency()
	{
		return toCurrency;
	}

	public double getFromAmount()
	{
		return fromAmount;
	}

	public double getToAmount()
	{
		return toAmount;
	}
}
