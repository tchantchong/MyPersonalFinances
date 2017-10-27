package com.mpf.mypersonalfinances.models.investments;

import com.mpf.mypersonalfinances.models.currencies.Currency;

/**
 * Created by Lucas on 14/09/2017.
 */

public class VariableIncome extends Investment {

    public String currency;
    public Double currencyValueInUSDolars;
    public Double totalValue;
    public Double totalValueInLocalCurrency;
    public Double todaySellPrice;
    public Double yearDividendInLocalCurrency;
    public Double yearDividendYield;

    public VariableIncome() {
        //Default constructor for DataSnapshot.getValue(VariableIncome.class)
    }

    public VariableIncome(String currency, Double totalValue) {
        this.currency = currency;
        for (Currency currency_: currencies.CurrenciesList()) {
            if (currency == currency_.name) {
                this.currencyValueInUSDolars = currency_.valueInUSDolars;
            }
        }
        this.totalValue = totalValue;
        this.totalValueInLocalCurrency = totalValue/currencyValueInUSDolars;

    }
}
