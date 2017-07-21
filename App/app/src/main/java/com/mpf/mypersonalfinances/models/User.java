package com.mpf.mypersonalfinances.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Lucas on 21/07/2017.
 */

@IgnoreExtraProperties
public class User {
    public String name;

    public User() {
        //Default constructor for DataSnapshot.getValue(User.class)
    }

    public User(String name) {
        this.name = name;
    }
}
