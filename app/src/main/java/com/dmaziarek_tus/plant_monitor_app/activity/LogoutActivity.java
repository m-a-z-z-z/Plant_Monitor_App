package com.dmaziarek_tus.plant_monitor_app.activity;

import android.content.Intent;
import android.os.Bundle;

import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.util.PlantListSingleton;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutActivity extends DrawerBaseActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        mAuth.getInstance().signOut(); // sign out of firebase
        PlantListSingleton.getInstance().emptyList();   // Ensures that the list is empty and will not contain plants if another user signs in on the same device

        // No real need for this thread. Just looks nice to have the signing out screen visible for a second
        Thread thread = new Thread() {
            @Override
            public void run() {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Intent intent = new Intent(LogoutActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();   // finish() is used to destroy the activity, will stop the user navigating back to the splash screen
            }
            }
        };
        thread.start();
    }
}