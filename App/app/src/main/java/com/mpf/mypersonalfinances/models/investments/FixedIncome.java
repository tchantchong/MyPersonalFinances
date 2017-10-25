package com.mpf.mypersonalfinances.models.investments;

/**
 * Created by Lucas on 14/09/2017.
 */

public class FixedIncome extends Investment {

    public Double fixedYield;

    public FixedIncome(){
        //Default constructor for DataSnapshot.getValue(FixedIncome.class)
    }

    public FixedIncome(Double fixedYield) {
        super();
        this.fixedYield = fixedYield;
    }
}
