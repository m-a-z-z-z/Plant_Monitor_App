package com.dmaziarek_tus.plant_monitor_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.cardview.widget.CardView;

import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.databinding.ActivityDashboardBinding;
import com.dmaziarek_tus.plant_monitor_app.util.PlantUtils;

public class DashboardActivity extends DrawerBaseActivity {

    ActivityDashboardBinding binding;
    CardView cv_plantHealthValues, cv_viewAddedPlants, cv_addPlant, cv_historicalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Dashboard");

        cv_plantHealthValues = (CardView) findViewById(R.id.cv_plantHealthValues);
        cv_viewAddedPlants = (CardView) findViewById(R.id.cv_viewAddedPlants);
        cv_addPlant = (CardView) findViewById(R.id.cv_addPlant);
        cv_historicalData = (CardView) findViewById(R.id.cv_historicalData);

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