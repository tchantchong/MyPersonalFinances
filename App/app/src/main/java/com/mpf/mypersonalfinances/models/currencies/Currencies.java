package com.mpf.mypersonalfinances.models.currencies;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 14/09/2017.
 */

public class Currencies {

    public Currency USD;
    public Currency BRL;

    public Currencies() {
        USD = new Currency("USD", 1.00);
        BRL = new Currency("BRL", 3.13);
    }

    public List<Currency> CurrenciesList() {
        ArrayList<Currency> ret = new ArrayList<Currency>();
        ret.add(USD);
        ret.add(BRL);
        return ret;
    }
}
