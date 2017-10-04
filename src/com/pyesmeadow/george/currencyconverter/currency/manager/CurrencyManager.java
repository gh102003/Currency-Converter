package com.pyesmeadow.george.currencyconverter.currency.manager;

import com.pyesmeadow.george.currencyconverter.currency.Currency;
import com.pyesmeadow.george.currencyconverter.main.CurrencyConverter;
import com.pyesmeadow.george.currencyconverter.main.CurrencyConverterFrame;

public class CurrencyManager {

	private CurrencyList currencyList;
	private CurrencyManagerDialog currencyManagerDialog;

	public CurrencyManager()
	{
		this.currencyList = new CurrencyList();

		loadCurrencyDetailsPanels();
	}

	/**
	 * Adds a currency
	 */
	public void addCurrency(Currency currency)
	{
		this.currencyList.addCurrency(currency);

		updateReferencesToCurrencies();

		this.loadCurrencyDetailsPanels();
	}

	/**
	 * @return true if the currency existed previously
	 */
	public boolean removeCurrency(Currency currency)
	{
		boolean toReturn = this.currencyList.removeCurrency(currency);

		updateReferencesToCurrencies();

		return toReturn;
	}

	/**
	 * @return if the currency existed previously
	 */
	public boolean editCurrency(Currency oldCurrency, Currency newCurrency)
	{
		boolean toReturn = this.currencyList.editCurrency(oldCurrency, newCurrency);

		updateReferencesToCurrencies();

		return toReturn;
	}

	public void refreshFromJSON()
	{
		this.currencyList.refreshFromJSON();

		updateReferencesToCurrencies();
	}

	public void resetCurrencies()
	{
		this.currencyList.resetCurrencies();

		updateReferencesToCurrencies();
	}

	/**
	 *
	 */
	private void updateReferencesToCurrencies()
	{
		if (this.currencyManagerDialog != null)
		{
			this.currencyManagerDialog.updateDisplayedCurrencies();
		}

		this.loadCurrencyDetailsPanels();
	}

	private void loadCurrencyDetailsPanels()
	{
		// Setup the frame to use the currencies
		CurrencyConverterFrame frame = CurrencyConverter.frame;

		if (frame != null)
		{
			frame.refreshCurrencies();
		}
	}

	public CurrencyList getCurrencyList()
	{
		return currencyList;
	}

	public boolean openManagementDialog()
	{
		if (this.currencyManagerDialog == null || !this.currencyManagerDialog.isVisible())
		{
			this.currencyManagerDialog = new CurrencyManagerDialog(this);
			return true;
		} else
		{
			return false;
		}
	}

	public CurrencyManagerDialog getCurrencyManagerDialog()
	{
		return currencyManagerDialog;
	}

	public void setCurrencyManagerDialog(CurrencyManagerDialog currencyManagerDialog)
	{
		this.currencyManagerDialog = currencyManagerDialog;
	}
}
