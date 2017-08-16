package com.mpf.mypersonalfinances.models;

/**
 * Created by Lucas on 14/08/2017.
 */

public class Investment {

    public String name;
    public String buyDate;
    public Double buyPrice;

    public Investment() {
        //Default constructor for DataSnapshot.getValue(Investment.class)
    }

    public Investment (String name) {
        this.name = name;
    }
}
