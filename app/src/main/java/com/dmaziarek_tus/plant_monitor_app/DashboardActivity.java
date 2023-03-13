package com.dmaziarek_tus.plant_monitor_app;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.util.Log;

import com.dmaziarek_tus.plant_monitor_app.databinding.ActivityDashboardBinding;
import com.dmaziarek_tus.plant_monitor_app.model.MySingleton;
import com.dmaziarek_tus.plant_monitor_app.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardActivity extends DrawerBaseActivity {

    ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Dashboard");
    }
}