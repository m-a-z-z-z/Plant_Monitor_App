package com.dmaziarek_tus.plant_monitor_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.databinding.ActivityDashboardBinding;
import com.dmaziarek_tus.plant_monitor_app.model.Plant;
import com.dmaziarek_tus.plant_monitor_app.util.NotificationService;
import com.dmaziarek_tus.plant_monitor_app.util.PlantListSingleton;

import java.util.ArrayList;

public class DashboardActivity extends DrawerBaseActivity {

    ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Dashboard");

        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
    }

    public void onDashboardCVClick(View view) {
        switch (view.getId()) {
            case R.id.cv_plantHealthValues:
                Intent intent = new Intent(this, PlantHealthActivity.class);
                startActivity(intent);
                break;
            case R.id.cv_viewAddedPlants:
                Intent intent2 = new Intent(this, SelectPlantActivity.class);
                startActivity(intent2);
                break;
            case R.id.cv_addPlant:
                Intent intent3 = new Intent(this, AddPlantActivity.class);
                startActivity(intent3);
                break;
            case R.id.cv_historicalData:
                Intent intent4 = new Intent(this, HistoricalDataActivity.class);
                startActivity(intent4);
                break;
        }
        finish();
    }
}