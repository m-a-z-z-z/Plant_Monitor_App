package com.dmaziarek_tus.plant_monitor_app.activity;

import androidx.annotation.NonNull;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.databinding.ActivityPlantHealthBinding;
import com.dmaziarek_tus.plant_monitor_app.model.Plant;
import com.dmaziarek_tus.plant_monitor_app.util.PlantListSingleton;
import com.dmaziarek_tus.plant_monitor_app.util.PlantUtils;
import com.dmaziarek_tus.plant_monitor_app.util.UserUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlantHealthActivity extends DrawerBaseActivity {
    ActivityPlantHealthBinding binding;
    TextView textView_SoilMoistureValue, textView_SoilMoistureStatus, textView_SunlightValue, textView_SunlightStatus, textView_HumidityValue, textView_HumidityStatus, textView_TemperatureValue, textView_TemperatureStatus, textView_plantName;
    FirebaseDatabase database;
    double soilMoistureVal, sunlightVal, humidityVal, temperatureVal;
    int minTemp, maxTemp;
    int minMoisture, maxMoisture;
    String readableMoistureVal, readableSunlightVal, readableHumidityVal, readableTemperatureVal, plantID, plantName, plantType, userName, selectedSoilMoisture;
    DecimalFormat df = new DecimalFormat("#.##");
    ArrayList<Plant> plantList = new ArrayList<>();
    Plant plant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlantHealthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Plant Health Values");

        // Get UI elements
        textView_SoilMoistureValue = (TextView) findViewById(R.id.textView_SoilMoistureValue);
        textView_SoilMoistureStatus = (TextView) findViewById(R.id.textView_SoilMoistureStatus);
        textView_SunlightValue = (TextView) findViewById(R.id.textView_SunlightValue);
        textView_SunlightStatus = (TextView) findViewById(R.id.textView_SunlightStatus);
        textView_HumidityValue = (TextView) findViewById(R.id.textView_HumidityValue);
        textView_HumidityStatus = (TextView) findViewById(R.id.textView_HumidityStatus);
        textView_TemperatureValue = (TextView) findViewById(R.id.textView_TemperatureValue);
        textView_TemperatureStatus = (TextView) findViewById(R.id.textView_TemperatureStatus);
        textView_plantName = (TextView) findViewById(R.id.textView_plantName);

        userName = UserUtils.getDisplayNameFromFirebase();

        // Get plant object from previous activity
        Intent intent = getIntent();
        plant = (Plant) intent.getSerializableExtra("plant");
        // If user does not go through select plant activity, get plant from list
        plantList = PlantListSingleton.getInstance().getPlantList();
        Log.d("SelectPlantActivity", "onCreate - Plant names: " + plantList);

        if (plant != null) { // Will execute if user goes through select plant activity
            Log.d("PlantHealthActivity", "Plant retrieved from intent");
            plantID = plant.getPlantID();
            readPlantHealthValues();
        } else if (!plantList.isEmpty()) { // Will execute if user does not go through select plant activity and has plants
            Log.d("PlantHealthActivity", "Plant retrieved from list");
            plant = plantList.get(0);
            plantID = plant.getPlantID();
            readPlantHealthValues();
        } else { // Will execute if user has no plants
            PlantUtils.noPlantsAdded(this);
        }

    }

    public void onHistoricalDataButtonClick(View view) {
        Intent intent = new Intent(this, HistoricalDataActivity.class);
        intent.putExtra("plantID", plantID);
        startActivity(intent);
    }

    public void promptDeletePlant(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Plant");
        builder.setMessage("Are you sure you want to delete this plant?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PlantUtils.deletePlantFromDB(plantID);
                PlantListSingleton.getInstance().removePlant(plantID);
                Log.d("PlantHealthActivity", "Plant names: " + plantList);
                Toast.makeText(PlantHealthActivity.this, "Plant deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlantHealthActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void promptEditPlantDetails(View view) {
        // inflate the custom dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_plant2, null);

        // Get references to the UI elements in dialog
        EditText editTextPlantName = dialogView.findViewById(R.id.editText_PlantName);
        Spinner spinnerPlantType = dialogView.findViewById(R.id.spinner_PlantType);
        Spinner spinnerPreferredMoisture = dialogView.findViewById(R.id.spinner_PreferredMoisture);
        SeekBar seekbarMinTemp = dialogView.findViewById(R.id.seekBar_MinTemp);
        SeekBar seekbarMaxTemp = dialogView.findViewById(R.id.seekBar_MaxTemp);
        TextView textViewMinTemp = dialogView.findViewById(R.id.textView_MinTemp);
        TextView textViewMaxTemp = dialogView.findViewById(R.id.textView_MaxTemp);
        ImageView imageViewPlantPhoto = dialogView.findViewById(R.id.imageView_plantPhoto);
        Button cameraBtn = dialogView.findViewById(R.id.cameraBtn);

        // Set current name of plant in the dialog
        editTextPlantName.setText(plant.getPlantName());
        textViewMinTemp.setText(String.valueOf(plant.getMinTemp()));
        textViewMaxTemp.setText(String.valueOf(plant.getMaxTemp()));

        // Set plant type spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.plant_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlantType.setAdapter(adapter);

        // Set preferred moisture spinner
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.preferred_soil_moisture, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPreferredMoisture.setAdapter(adapter2);

        // Seekbar for min temp
        // Set the seekbar to the current min temp
        minTemp = (int) plant.getMinTemp();
        seekbarMinTemp.setProgress(minTemp);
        seekbarMinTemp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minTemp = progress;
                textViewMinTemp.setText(minTemp + "°C");
                Log.d("AddPlantActivity", "onProgressChanged - Min temp: " + minTemp);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Seekbar for max temp
        // Set the seekbar to the current max temp
        maxTemp = (int) plant.getMaxTemp();
        seekbarMaxTemp.setProgress(maxTemp);
        seekbarMaxTemp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maxTemp = progress;
                textViewMaxTemp.setText(maxTemp + "°C");
                Log.d("AddPlantActivity", "onProgressChanged - Max temp: " + maxTemp);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Change Plant Attributes");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedSoilMoisture = spinnerPreferredMoisture.getSelectedItem().toString();
                switch (selectedSoilMoisture) {
                    case "0%-20% (Extremely dry)":
                        minMoisture = 0;
                        maxMoisture = 20;
                        break;
                    case "21%-40% (Dryish - Well drained)":
                        minMoisture = 21;
                        maxMoisture = 40;
                        break;
                    case "41%-60% (Tolerates moist soil)":
                        minMoisture = 41;
                        maxMoisture = 60;
                        break;
                    case "61%-80% (Tolerates wet soil)":
                        minMoisture = 61;
                        maxMoisture = 80;
                        break;
                    case "81%-100% (Extremely wet)":
                        minMoisture = 81;
                        maxMoisture = 100;
                        break;
                }

                String newPlantName = editTextPlantName.getText().toString(); plant.setPlantName(newPlantName);
                String newPlantType = spinnerPlantType.getSelectedItem().toString(); plant.setPlantType(newPlantType);
                int newMinMoisture = minMoisture; plant.setMinSoilMoisture(newMinMoisture);
                int newMaxMoisture = maxMoisture; plant.setMaxSoilMoisture(newMaxMoisture);
                double newMinTemp = minTemp; plant.setMinTemp(newMinTemp);
                double newMaxTemp = maxTemp; plant.setMaxTemp(newMaxTemp);

                PlantUtils.updatePlantDetailsInDB(plantID, newPlantName, newPlantType, newMinMoisture, newMaxMoisture, newMinTemp, newMaxTemp);
                textView_plantName.setText(newPlantName);

                // Update the plant in the plant list
                for(Plant p : plantList) {
                    if(p.getPlantID().equals(plant.getPlantID())) {
                        p.setPlantName(newPlantName);
                        p.setPlantType(newPlantType);
                        p.setMinSoilMoisture(newMinMoisture);
                        p.setMaxSoilMoisture(newMaxMoisture);
                        p.setMinTemp(newMinTemp);
                        p.setMaxTemp(newMaxTemp);
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void readPlantHealthValues() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users/" + userName + "/Plants/" + plantID);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                plantName = snapshot.child("plantName").getValue().toString();
                plantType = snapshot.child("plantType").getValue().toString();
                soilMoistureVal = Double.valueOf(snapshot.child("soilMoisture").getValue().toString());
                sunlightVal = Double.valueOf(snapshot.child("visLight").getValue().toString());
                humidityVal = Double.valueOf(snapshot.child("humidity").getValue().toString());
                temperatureVal = Double.valueOf(snapshot.child("temperature").getValue().toString());

                // Checks for soil moisture
                if (soilMoistureVal > plant.getMaxSoilMoisture()) {
                    readableMoistureVal = "Too wet. Recommended moisture is " + plant.getMaxSoilMoisture() + "% - " + plant.getMinSoilMoisture() + "%";
                } else if (soilMoistureVal < plant.getMinSoilMoisture()) {
                    readableMoistureVal = "Too dry. Recommended moisture is " + plant.getMaxSoilMoisture() + "% - " + plant.getMinSoilMoisture() + "%";
                } else {
                    readableMoistureVal = "Moisture is correct";
                }

                // Checks for sunlight
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

                // Checks for humidity
                if (humidityVal >= 0 && humidityVal <= 25) {
                    readableHumidityVal = "Air is too dry. May cause damage to plant.";
                } else if (humidityVal > 25 && humidityVal < 39) {
                    readableHumidityVal = "Consider increasing humidity levels around plant.";
                } else if (humidityVal >= 40 && humidityVal <= 70) {
                    readableHumidityVal  = "Ideal humidity";
                } else if (humidityVal > 90) {
                    readableHumidityVal = "Too humid. Mold and mildew prone.";
                }

                // Checks for temperature
                if (temperatureVal > plant.getMaxTemp()) {
                    readableTemperatureVal = "Too hot. May cause damage to plant.";
                } else if (temperatureVal < plant.getMinTemp()) {
                    readableTemperatureVal = "Too cold. May cause damage to plant.";
                } else {
                    readableTemperatureVal = "Ideal temperature";
                }

                // Set the text of the text views to the values from the database
                textView_plantName.setText(plantName);
                textView_SoilMoistureValue.setText(soilMoistureVal + "%");
                textView_SoilMoistureStatus.setText(readableMoistureVal);
                textView_SunlightValue.setText(sunlightVal + " lm");
                textView_SunlightStatus.setText(readableSunlightVal);
                textView_HumidityValue.setText(df.format(humidityVal) + "%");
                textView_HumidityStatus.setText(readableHumidityVal);
                textView_TemperatureValue.setText(df.format(temperatureVal) + "°C");
                textView_TemperatureStatus.setText(readableTemperatureVal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PlantHealthActivity", "Failed to read values", error.toException());
                Toast.makeText(PlantHealthActivity.this, "Failed to read values", Toast.LENGTH_SHORT).show();
            }
        });
    }
}