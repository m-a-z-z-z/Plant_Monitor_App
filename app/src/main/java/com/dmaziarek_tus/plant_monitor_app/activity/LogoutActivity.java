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
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the activity stack (all activities below the WelcomeActivity) so that the user cannot navigate back to the logout screen
                startActivity(intent);
                finishAffinity();   // Close all activities
            }
            }
        };
        thread.start();
    }
}