package com.mpf.mypersonalfinances.models.currencies;

/**
 * Created by Lucas on 14/09/2017.
 */

public class Currency {
    public String name;
    public Double valueInUSDolars;

    public Currency(String name, Double valueInUSDolars) {
        this.name = name;
        this.valueInUSDolars = valueInUSDolars;
    }
}
