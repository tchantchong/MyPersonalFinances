package com.mpf.mypersonalfinances.models;

/**
 * Created by Lucas on 09/08/2017.
 */

public class Period {
    public boolean initialized;

    public Period() {
        //Default constructor for DataSnapshot.getValue(Period.class)
    }
    public Period(boolean initialized) {
        this.initialized = initialized;
    }
}
