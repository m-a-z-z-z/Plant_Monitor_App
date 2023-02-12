package com.dmaziarek_tus.plant_monitor_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        FirebaseApp.initializeApp(this);
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
    }

    public void onButtonSignUpClicked(View view) {
//        Intent intent = new Intent(this, SignUpActivity.class);
//        startActivity(intent);
    }

    public void onButtonSignInClicked(View view) {
//        Intent intent = new Intent(this, SignInActivity.class);
//        startActivity(intent);
    }
}