package com.dmaziarek_tus.plant_monitor_app.model;

import android.util.Log;

import com.dmaziarek_tus.plant_monitor_app.util.UserUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User {
    private String userName;
    private String email;
    private String password;

    public User() { } // empty constructor

    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public String getUserName() {
        return this.userName = UserUtils.getDisplayNameFromFirebase();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
