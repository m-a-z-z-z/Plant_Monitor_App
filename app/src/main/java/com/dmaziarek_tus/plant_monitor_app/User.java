package com.dmaziarek_tus.plant_monitor_app;

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
        String displayName = null;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            displayName = user.getDisplayName();
            // do something with the user's information
        }
        Log.d("User Class", "getUserData - display name: " + displayName);
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
