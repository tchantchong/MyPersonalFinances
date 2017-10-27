package com.mpf.mypersonalfinances.models.investments;

/**
 * Created by Lucas on 14/09/2017.
 */

public class Prefixed extends FixedIncome {
    public Double endValue;

    public Prefixed() {
        //Default constructor for DataSnapshot.getValue(Prefixed.class)
    }

    public Prefixed(Double endValue) {
        this.endValue = endValue;
    }
}
