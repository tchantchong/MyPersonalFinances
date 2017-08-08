package com.mpf.mypersonalfinances.models;

import java.util.Date;

/**
 * Created by Lucas on 26/07/2017.
 */

public class Expense {
    public String category;
    public String description;
    public String id;
    public String date;
    public double value;
    public double oldValue;

    public Expense() {
        //Default constructor for DataSnapshot.getValue(Expense.class)
    }

    public Expense(String category, String description, String id, String date, double value, double oldValue) {
        this.category = category;
        this.description = description;
        this.id = id;
        this.date = date;
        this.value = value;
        this.oldValue = oldValue;
    }
}
