package com.mpf.mypersonalfinances.models;

/**
 * Created by Lucas on 26/07/2017.
 */

public class Income {
    public String name;
    public double value;
    public double oldValue;

    public Income() {
        //Default constructor for DataSnapshot.getValue(Income.class)
    }
    public Income(String name, double value, double oldValue) {
        this.name = name;
        this.value = value;
        this.oldValue = oldValue;
    }
}
