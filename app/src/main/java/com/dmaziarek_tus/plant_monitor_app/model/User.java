package com.dmaziarek_tus.plant_monitor_app.model;

import android.util.Log;

import com.dmaziarek_tus.plant_monitor_app.util.UserUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User {
    private String userName;
    private String displayName;
    private String email;
    private String password;

    public User() { } // empty constructor

    public User(String _email, String _userName) {
        this.email = _email;
        this.userName = _userName;
    }

    public String getUserName() { return userName; }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
