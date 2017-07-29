package com.mpf.mypersonalfinances.models;

/**
 * Created by Lucas on 26/07/2017.
 */

public class Expense {
    public String category;
    public double value;
    public double oldValue;

    public Expense() {
        //Default constructor for DataSnapshot.getValue(Expense.class)
    }

    public Expense(String category, double value, double oldValue) {
        this.category = category;
        this.value = value;
        this.oldValue = oldValue;
    }
}
