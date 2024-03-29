package com.dmaziarek_tus.plant_monitor_app.activity;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.util.Log;

import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.databinding.ActivityHistoricalDataBinding;
import com.dmaziarek_tus.plant_monitor_app.model.Plant;
import com.dmaziarek_tus.plant_monitor_app.util.PlantListSingleton;
import com.dmaziarek_tus.plant_monitor_app.util.PlantUtils;
import com.dmaziarek_tus.plant_monitor_app.util.UserUtils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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
    String plantID, userName;
    ArrayList<Plant> plantList = new ArrayList<>();
    ArrayList<Integer> soilMoistureList = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("#.##");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    LineChart lineChart;
    Plant plant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoricalDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Historical Data");

        lineChart = findViewById(R.id.lineChart);   // Get the line chart from the layout
        userName = UserUtils.getDisplayNameFromFirebase();

        // Get plant object from previous activity
        Intent intent = getIntent();
        plant = (Plant) intent.getSerializableExtra("plant");
        // If user does not go through select plant activity, get plant from list
        plantList = PlantListSingleton.getInstance().getPlantList();
        Log.d("SelectPlantActivity", "onCreate \nPlant names: " + plantList);

        if (plant != null) {
            Log.d("PlantHealthActivity", "Plant retrieved from intent");
            plantID = plant.getPlantID();
            retrieveDataAndPopulateCharts();
        } else if (!plantList.isEmpty()) {
            Log.d("PlantHealthActivity", "Plant retrieved from list");
            plant = plantList.get(0);
            plantID = plant.getPlantID();
            retrieveDataAndPopulateCharts();
        } else {
            PlantUtils.noPlantsAdded(this);
        }
    }

    private void populateLineChart(ArrayList<Integer> soilMoistureList) {
        ArrayList<Entry> soilMoistureEntries = new ArrayList<>();

        for (int i = 0; i < soilMoistureList.size(); i++) {
            soilMoistureEntries.add(new Entry(i, soilMoistureList.get(i)));
        }

        LineDataSet lineDataSet = new LineDataSet(soilMoistureEntries, "Soil Moisture");
        LineData data = new LineData(lineDataSet);
        lineDataSet.setDrawValues(false);   // hides values of each reading above line on chart
        lineDataSet.setDrawCircles(false);  // hides dots representing reading on line
        lineDataSet.setLineWidth(2f);

        // Y Axis config
        YAxis yAxisLeft = lineChart.getAxisLeft();
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisLeft.setAxisMaximum(100);
        yAxisLeft.setAxisMinimum(0);
        yAxisRight.setAxisMaximum(100);
        yAxisRight.setAxisMinimum(0);
        yAxisLeft.setAxisLineWidth(1.5f);
        yAxisRight.setAxisLineWidth(1.5f);
        yAxisRight.setAxisLineColor(ColorTemplate.rgb("#000000"));
        yAxisLeft.setAxisLineColor(ColorTemplate.rgb("#000000"));


        // X Axis config
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineWidth(1.5f);
        xAxis.setAxisLineColor(ColorTemplate.rgb("#000000"));
        lineChart.getLegend().setEnabled(true);
        lineChart.getDescription().setText("Y: Soil moisture (%)\tX: Reading number");
        lineChart.animateX(1500, Easing.EasingOption.EaseInSine);
        lineChart.setData(data); // Set the data to the line chart
        lineChart.invalidate(); // Refresh the line chart
    }

    private void retrieveDataAndPopulateCharts() {
        myRef = database.getReference("Users/" + userName + "/Plants/" + plantID + "/history");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Loop through each record
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get the values for humidity, soil moisture, and temperature
                    double humidity = snapshot.child("humidity").getValue(Double.class);
                    int soilMoisture = snapshot.child("soilMoisture").getValue(Integer.class);
                    double temperature = snapshot.child("temperature").getValue(Double.class);

                    // Add the values to the corresponding array list
                    soilMoistureList.add(soilMoisture);

                }
                Log.d("HistoricalDataActivity", "onDataChange: " + soilMoistureList.size());
                populateLineChart(soilMoistureList);
                myRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("HistoricalDataActivity", "onCancelled: " + databaseError.getMessage());
            }
        });
    }
}