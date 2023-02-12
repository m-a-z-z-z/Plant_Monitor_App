package com.dmaziarek_tus.plant_monitor_app;

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
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
