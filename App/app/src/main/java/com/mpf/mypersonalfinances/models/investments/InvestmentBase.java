package com.mpf.mypersonalfinances.models.investments;

import com.mpf.mypersonalfinances.models.currencies.Currencies;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lucas on 24/10/2017.
 */

public class InvestmentBase {

    public String id;
    public String name;
    public String buyDate;
    public String investmentType;
    public Double buyUnitPrice;
    public Double todaySellPrice;
    public Double quantity;
    public Currencies currencies;
    public Double fixedYield;
    public Double endValue;
    public Double variableYield;

    public InvestmentBase() {
        //Default constructor for DataSnapshot.getValue(InvestmentBase.class)
    }

    public InvestmentBase (String id, String name, String buyDate, String investmentType,
                           Double buyUnitPrice,Double quantity, Double fixedYield, Double endValue) {
        this.id = id;
        this.name = name;
        this.buyDate = buyDate;
        this.investmentType = investmentType;
        this.buyUnitPrice = buyUnitPrice;
        this.quantity = quantity;
        this.currencies = new Currencies();
        this.fixedYield = fixedYield;
        this.endValue = endValue;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("id", id);
        result.put("name", name);
        result.put("buyDate", buyDate);
        result.put("investmentType", investmentType);
        result.put("buyUnitPrice", buyUnitPrice);
        result.put("quantity", quantity);
        result.put("fixedYield", fixedYield);
        result.put("endValue", endValue);

        return result;
    }
}
