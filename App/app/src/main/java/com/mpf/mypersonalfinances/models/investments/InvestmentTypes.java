package com.mpf.mypersonalfinances.models.investments;

/**
 * Created by Lucas on 14/09/2017.
 */

public enum InvestmentTypes {
    PREFIXED("Prefixed"),
    POSFIXED("Posfixed"),
    VARIABLE("Variable");

    private String investmentType;

    InvestmentTypes(String investmentType) {
        this.investmentType = investmentType;
    }

    @Override
    public String toString() {
        return investmentType;
    }
}
