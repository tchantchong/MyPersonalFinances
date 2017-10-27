package com.mpf.mypersonalfinances.models.investments;

import com.mpf.mypersonalfinances.models.currencies.Currencies;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lucas on 14/08/2017.
 */

public class Investment {

    public String name;
    public String buyDate;
    public String investmentType;
    public Double buyUnitPrice;
    public Double todaySellPrice;
    public Double quantity;
    public Currencies currencies;

    public Investment() {
        //Default constructor for DataSnapshot.getValue(Investment.class)
    }

    public Investment (String name, String buyDate, String investmentType, Double buyUnitPrice, Double quantity) {
        this.name = name;
        this.buyDate = buyDate;
        this.investmentType = investmentType;
        this.buyUnitPrice = buyUnitPrice;
        this.quantity = quantity;
        this.currencies = new Currencies();
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("name", name);
        result.put("buyDate", buyDate);
        result.put("investmentType", investmentType);
        result.put("buyUnitPrice", buyUnitPrice);
        result.put("quantity", quantity);

        return result;
    }
}
