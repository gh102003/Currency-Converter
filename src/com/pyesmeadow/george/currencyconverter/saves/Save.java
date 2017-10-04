package com.pyesmeadow.george.currencyconverter.saves;

import com.pyesmeadow.george.currencyconverter.currency.Currency;
import com.pyesmeadow.george.currencyconverter.main.CurrencyConverter;
import org.json.simple.JSONObject;

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
	static Save deserializeFromJSON(JSONObject input)
	{
		Currency fromCurrency = CurrencyConverter.frame.currencyManager.getCurrencyList().getCurrencyFromID((String) input.get(
				"fromCurrency"));
		double fromAmount = (double) input.get("fromAmount");
		Currency toCurrency = CurrencyConverter.frame.currencyManager.getCurrencyList().getCurrencyFromID((String) input.get(
				"toCurrency"));
		double toAmount = (double) input.get("toAmount");

		if (fromCurrency == null)
		{
			fromCurrency = new Currency((String) input.get("fromCurrency"), "Unknown", Locale.getDefault(), 1, "");
		}

		return new Save(fromCurrency, fromAmount, toCurrency, toAmount);
	}

	/**
	 * Creates a JSON object from this save
	 */
	JSONObject serializeToJSON()
	{
		JSONObject output = new JSONObject();

		output.put("fromCurrency", fromCurrency.getIdentifier());
		output.put("fromAmount", fromAmount);
		output.put("toCurrency", toCurrency.getIdentifier());
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
