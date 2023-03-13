package com.dmaziarek_tus.plant_monitor_app.model;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User {
    private String userName;
    private String email;
    private String password;

    public User() { } // empty constructor

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getUserName() {
        String fbDisplayName = null;

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser != null) {
            fbDisplayName = fbUser.getDisplayName();
            // do something with the user's information
        }
        this.userName = fbDisplayName;
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
