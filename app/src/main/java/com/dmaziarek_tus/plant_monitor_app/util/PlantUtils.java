package com.dmaziarek_tus.plant_monitor_app.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.dmaziarek_tus.plant_monitor_app.activity.AddPlantActivity;
import com.dmaziarek_tus.plant_monitor_app.activity.PlantHealthActivity;

public class PlantUtils {
    public static void noPlantsAdded(Context activityContext) {
        Toast.makeText(activityContext, "No plants added", Toast.LENGTH_SHORT).show();
        Log.d("PlantHealthActivity", "onCreate - No plants added");
        Intent intent = new Intent(activityContext, AddPlantActivity.class);
        activityContext.startActivity(intent);
    }
}
