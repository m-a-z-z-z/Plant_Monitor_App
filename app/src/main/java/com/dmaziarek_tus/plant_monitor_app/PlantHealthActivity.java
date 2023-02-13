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
                textView_SoilMoisture.setText(snapshot.child("Soil_Moisture").getValue().toString());
                textView_Sunlight.setText(snapshot.child("Sunlight/Visible").getValue().toString());
                textView_Humidity.setText(snapshot.child("Temp_Humid/Humidity").getValue().toString());
                textView_Temperature.setText(snapshot.child("Temp_Humid/Temperature").getValue().toString());

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