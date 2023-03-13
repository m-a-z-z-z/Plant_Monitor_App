package com.dmaziarek_tus.plant_monitor_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.dmaziarek_tus.plant_monitor_app.databinding.ActivityLogoutBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutActivity extends DrawerBaseActivity {
    ActivityLogoutBinding binding;
    FirebaseAuth mAuth;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Logging out");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth.getInstance().signOut(); // sign out of firebase

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
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