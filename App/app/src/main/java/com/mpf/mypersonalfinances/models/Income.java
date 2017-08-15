package com.mpf.mypersonalfinances.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lucas on 26/07/2017.
 */

public class Income {
    public String description;
    public String id;
    public String date;
    public double value;
    public double oldValue;

    public Income() {
        //Default constructor for DataSnapshot.getValue(Income.class)
    }
    public Income(String description, String id, String date, double value, double oldValue) {
        this.description = description;
        this.id = id;
        this.date = date;
        this.value = value;
        this.oldValue = oldValue;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("description", description);
        result.put("id", id);
        result.put("date", date);
        result.put("value", value);
        result.put("oldValue", oldValue);

        return result;
    }
}
