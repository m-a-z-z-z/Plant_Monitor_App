package com.dmaziarek_tus.plant_monitor_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class SelectPlantActivity extends AppCompatActivity {
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_plant);

        User user = new User();
        userName = user.getUserName().trim();

        ArrayList<String> plants = new ArrayList<>();
        plants.add("Plant 1");
        plants.add("Plant 2");
    }

}