package com.dmaziarek_tus.plant_monitor_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.util.PlantUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        LottieAnimationView lottieAnimationView = findViewById(R.id.lottieAnimationView);
        TextView textView = findViewById(R.id.textViewSplash);
        mAuth = FirebaseAuth.getInstance();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   // Hide status bar

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Log.i("SplashActivity", "No user signed in");
                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);
        if (mAuth.getCurrentUser() != null) {
            Log.i("SplashActivity", "User is signed in");
            PlantUtils.retrieveUserPlants();
        }

        // Animations for splash screen
        lottieAnimationView.animate().translationY(-1500).setDuration(1000).setStartDelay(2500);
        textView.animate().translationY(1000).setDuration(1000).setStartDelay(2500);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (mAuth.getCurrentUser() != null) {
                        Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();   // finish() is used to destroy the activity, will stop the user navigating back to the splash screen
                    } else {
                        Log.i("SplashActivity", "User is not signed in");
                        Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finish();   // finish() is used to destroy the activity, will stop the user navigating back to the splash screen
                    }
                }
            }
        };
        thread.start();
    }
}

