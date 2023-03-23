package com.dmaziarek_tus.plant_monitor_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.util.Log;

import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.databinding.ActivityHistoricalDataBinding;
import com.dmaziarek_tus.plant_monitor_app.util.PlantNamesSingleton;
import com.dmaziarek_tus.plant_monitor_app.model.User;
import com.dmaziarek_tus.plant_monitor_app.util.PlantUtils;
import com.dmaziarek_tus.plant_monitor_app.util.UserUtils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoricalDataActivity extends DrawerBaseActivity {
    ActivityHistoricalDataBinding binding;
    String plantName, userName;
    double soilMoistureVal, sunlightVal, humidityVal, temperatureVal;
    ArrayList<String> plantNameList = new ArrayList<>();
    ArrayList<Double> humidityList = new ArrayList<>();
    ArrayList<Integer> soilMoistureList = new ArrayList<>();
    ArrayList<Double> temperatureList = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("#.##");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoricalDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Historical Data");

        lineChart = findViewById(R.id.lineChart);   // Get the line chart from the layout

        userName = UserUtils.getDisplayNameFromFirebase();

        Intent intent = getIntent();
        plantName = intent.getStringExtra("plantName");
        plantNameList = PlantNamesSingleton.getInstance().getPlantNames();
        Log.d("HistoricalDataActivity", "plantName: " + plantName);

        if (plantNameList == null) {  // If the plant name list is empty, then the user has not added any plants and will be prompted to add plants
            PlantUtils.noPlantsAdded(HistoricalDataActivity.this);
        } else if (plantNameList.isEmpty()) {
            PlantUtils.noPlantsAdded(HistoricalDataActivity.this);
        }
        // If user goes straight to view plant health and not through select plant (and has plants), then plantName will be null.
        // This will cause the app to crash, so we need to check if plantName is null and if it is, then we need to get the plant name from the singleton class
        else if (plantName == null || plantName.isEmpty() || plantName.equals("")) {
            Log.d("PlantHealthActivity", "Plant names: " + plantNameList);
            plantName = plantNameList.get(0);  // Get the first plant name from the array list
            retrieveDataAndPopulateCharts();
        }
        else {
             retrieveDataAndPopulateCharts();
        }
    }

    private void populateLineChart(ArrayList<Integer> soilMoistureList) {
        ArrayList<Entry> soilMoistureEntries = new ArrayList<>();

        for (int i = 0; i < soilMoistureList.size(); i++) {
            soilMoistureEntries.add(new Entry(i, soilMoistureList.get(i)));
        }

        LineDataSet lineDataSet = new LineDataSet(soilMoistureEntries, "");
        lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        LineData data = new LineData(lineDataSet);

        lineChart.getAxisLeft().setDrawGridLines(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        lineChart.getLegend().setEnabled(true);
        lineChart.getDescription().setEnabled(false);
        lineChart.animateX(1500, Easing.EasingOption.EaseInSine);
        lineChart.setData(data); // Set the data to the line chart
        lineChart.invalidate(); // Refresh the line chart
    }

    private void retrieveDataAndPopulateCharts() {
        myRef = database.getReference("Users/" + userName + "/Plants/" + plantName + "/history");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Loop through each record
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get the values for humidity, soil moisture, and temperature
                    double humidity = snapshot.child("humidity").getValue(Double.class);
                    int soilMoisture = snapshot.child("soil_Moisture").getValue(Integer.class);
                    double temperature = snapshot.child("temperature").getValue(Double.class);

                    // Add the values to the corresponding array list
                    soilMoistureList.add(soilMoisture);

                    populateLineChart(soilMoistureList);
                }
                myRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("HistoricalDataActivity", "onCancelled: " + databaseError.getMessage());
            }
        });
    }
}