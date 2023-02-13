package com.dmaziarek_tus.plant_monitor_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        LottieAnimationView lottieAnimationView = findViewById(R.id.lottieAnimationView);
        TextView textView = findViewById(R.id.textViewSplash);

//        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
                    Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();   // finish() is used to destroy the activity, will stop the user navigating back to the splash screen
                }
            }
        };
        thread.start();
    }
}