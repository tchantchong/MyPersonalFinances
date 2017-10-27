package com.mpf.mypersonalfinances.models.investments;

/**
 * Created by Lucas on 14/09/2017.
 */

public class Posfixed {

    public Double variableYield;

    public Posfixed() {
        //Default constructor for DataSnapshot.getValue(Posfixed.class)
    }

    public Posfixed(Double variableYield) {
        this.variableYield = variableYield;
    }
}
