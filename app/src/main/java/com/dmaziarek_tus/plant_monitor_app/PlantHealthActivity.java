package com.dmaziarek_tus.plant_monitor_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dmaziarek_tus.plant_monitor_app.databinding.ActivityPlantHealthBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlantHealthActivity extends DrawerBaseActivity {

    ActivityPlantHealthBinding binding;
    TextView textView_SoilMoisture, textView_Sunlight, textView_Humidity, textView_Temperature;
    FirebaseDatabase database;
    double soilMoistureVal, sunlightVal, humidityVal, temperatureVal;
    String readableMoistureVal, readableSunlightVal, readableHumidityVal, readableTemperatureVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlantHealthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Plant Health Values");

        textView_SoilMoisture = (TextView) findViewById(R.id.textView_SoilMoistureValue);
        textView_Sunlight = (TextView) findViewById(R.id.textView_SunlightValue);
        textView_Humidity = (TextView) findViewById(R.id.textView_HumidityValue);
        textView_Temperature = (TextView) findViewById(R.id.textView_TemperatureValue);

        readPlantHealthValues();
    }

    public void readPlantHealthValues() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Plant_Name");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                soilMoistureVal = Double.valueOf(snapshot.child("Soil_Moisture").getValue().toString());
                sunlightVal = Double.valueOf(snapshot.child("Sunlight/Visible").getValue().toString());
                humidityVal = Double.valueOf(snapshot.child("Temp_Humid/Humidity").getValue().toString());
                temperatureVal = Double.valueOf(snapshot.child("Temp_Humid/Temperature").getValue().toString());

                if (soilMoistureVal >= 300 && soilMoistureVal <= 450) {
                    readableMoistureVal = "In water";
                } else if (soilMoistureVal >= 450 && soilMoistureVal <= 600) {
                    readableMoistureVal = "Moist";
                } else if (soilMoistureVal >= 600 && soilMoistureVal <= 800) {
                    readableMoistureVal = "Dry";
                } else if (soilMoistureVal >= 800 && soilMoistureVal <= 1023) {
                    readableMoistureVal = "Very dry";
                } else {
                    readableMoistureVal = "Error";
                }

                if (sunlightVal >= 0 && sunlightVal <= 100) {
                    readableSunlightVal = "Dark";
                } else if (sunlightVal >= 100 && sunlightVal <= 300) {
                    readableSunlightVal = "Dim";
                } else if (sunlightVal >= 300 && sunlightVal <= 500) {
                    readableSunlightVal = "Bright";
                } else if (sunlightVal >= 500 && sunlightVal <= 1023) {
                    readableSunlightVal = "Very bright";
                } else {
                    readableSunlightVal = "Error";
                }

                if (humidityVal >= 0 && humidityVal <= 25) {
                    readableHumidityVal = "Dry air";
                } else if (humidityVal >= 26 && humidityVal <= 50) {
                    readableHumidityVal  = "Ideal humidity";
                } else if (humidityVal > 50) {
                    readableHumidityVal = "Too humid. Mold and mildew prone.";
                }

                if (temperatureVal <= -4) {
                    readableTemperatureVal = "Severe freeze. Heavy damage to most plants.";
                } else if (temperatureVal <= -2) {
                    readableTemperatureVal = "Moderate freeze. Destructive to most plants.";
                } else if (temperatureVal <= -1.66) {
                    readableTemperatureVal = "Light freeze. Will kill tender plants.";
                } else if (temperatureVal >= 15 && temperatureVal < 24) {
                    readableTemperatureVal = "Ideal temperature for growth.";
                } else if (temperatureVal > 24) {
                    readableTemperatureVal = "Too hot for indoor plants. Spray with mist.";
                }

                // Set the text of the text views to the values from the database
                textView_SoilMoisture.setText(soilMoistureVal + " " + readableMoistureVal);
                textView_Sunlight.setText(sunlightVal + " " + readableSunlightVal);
                textView_Humidity.setText(humidityVal + " " + readableHumidityVal);
                textView_Temperature.setText(temperatureVal + " " + readableTemperatureVal);

                // For debugging purposes
                Log.d("Soil Moisture: ", snapshot.child("Soil_Moisture").getValue().toString());
                Log.d("Sunlight: ", snapshot.child("Sunlight/Visible").getValue().toString());
                Log.d("Humidity: ", snapshot.child("Temp_Humid/Humidity").getValue().toString());
                Log.d("Temperature: ", snapshot.child("Temp_Humid/Temperature").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PlantHealthActivity.this, "Failed to read values", Toast.LENGTH_SHORT).show();
            }
        });
    }
}