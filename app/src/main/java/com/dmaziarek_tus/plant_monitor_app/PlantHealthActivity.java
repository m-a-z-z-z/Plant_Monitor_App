package com.dmaziarek_tus.plant_monitor_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dmaziarek_tus.plant_monitor_app.databinding.ActivityPlantHealthBinding;

public class PlantHealthActivity extends DrawerBaseActivity {

    ActivityPlantHealthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlantHealthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Plant Health Values");
    }
}