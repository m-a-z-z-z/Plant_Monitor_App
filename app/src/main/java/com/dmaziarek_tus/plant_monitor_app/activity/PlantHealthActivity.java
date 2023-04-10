package com.dmaziarek_tus.plant_monitor_app.activity;

import androidx.annotation.NonNull;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.databinding.ActivityPlantHealthBinding;
import com.dmaziarek_tus.plant_monitor_app.model.Plant;
import com.dmaziarek_tus.plant_monitor_app.util.PlantNamesSingleton;
import com.dmaziarek_tus.plant_monitor_app.model.User;
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
    TextView textView_SoilMoisture, textView_Sunlight, textView_Humidity, textView_Temperature, textView_readableHumid, textView_readableTemp, textView_plantName;
    FirebaseDatabase database;
    double soilMoistureVal, sunlightVal, humidityVal, temperatureVal;
    String readableMoistureVal, readableSunlightVal, readableHumidityVal, readableTemperatureVal, plantID, plantName, plantType, userName;
    DecimalFormat df = new DecimalFormat("#.##");
    ArrayList<Plant> plantList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlantHealthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Plant Health Values");

        // Get UI elements
        textView_SoilMoisture = (TextView) findViewById(R.id.textView_SoilMoistureValue);
        textView_Sunlight = (TextView) findViewById(R.id.textView_SunlightValue);
        textView_Humidity = (TextView) findViewById(R.id.textView_HumidityValue);
        textView_Temperature = (TextView) findViewById(R.id.textView_TemperatureValue);
        textView_readableHumid = (TextView) findViewById(R.id.textView_readableHumid);
        textView_readableTemp = (TextView) findViewById(R.id.textView_readableTemp);
        textView_plantName = (TextView) findViewById(R.id.textView_plantName);

        userName = UserUtils.getDisplayNameFromFirebase();

        // Get plant ID from previous activity
        Intent intent = getIntent();
        plantID = intent.getStringExtra("plantID");
        plantList = PlantNamesSingleton.getInstance().getPlantList();
        Log.d("SelectPlantActivity", "onCreate - Plant names: " + plantList);

        // I know this looks dumb but the app would crash every time if plantNameList == Null or plantNameList.isEmpty() was in one if statement
        // If the plant name list is empty, user will be redirected to add plants activity
        if (plantList == null) {
            PlantUtils.noPlantsAdded(this);
        } else if (plantList.isEmpty()) {
            PlantUtils.noPlantsAdded(this);
        }
        // If user goes straight to view plant health and not through select plant (and has plants), then plantName will be null.
        // This will cause the app to crash, so we need to check if plantName is null and if it is, then we need to get the plant name from the singleton class
        else if (plantID == null || plantID.isEmpty() || plantID == "") {
            Log.d("PlantHealthActivity", "Plant names: " + plantList);
            plantID = plantList.get(0).getPlantID();  // Get the first plant from the array list
            readPlantHealthValues();
        }
        else {
            readPlantHealthValues();
        }

    }

    public void onHistoricalDataButtonClick(View view) {
        Intent intent = new Intent(this, HistoricalDataActivity.class);
        intent.putExtra("plantID", plantID);
        startActivity(intent);
    }

    public void promptChangePlantName(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Plant Name");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPlantName = input.getText().toString();
                PlantUtils.updatePlantNameInDB(plantName, newPlantName);
                PlantUtils.updatePlantPhotoNames(plantName, newPlantName);
                textView_plantName.setText(newPlantName);
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
                soilMoistureVal = Double.valueOf(snapshot.child("soil_Moisture").getValue().toString());
                sunlightVal = Double.valueOf(snapshot.child("visLight").getValue().toString());
                humidityVal = Double.valueOf(snapshot.child("humidity").getValue().toString());
                temperatureVal = Double.valueOf(snapshot.child("temperature").getValue().toString());

                // Checks for soil moisture
                if (soilMoistureVal <= 550) {
                    readableMoistureVal = " - Soil is too wet";
                } else if (soilMoistureVal >= 551 && soilMoistureVal <= 650) {
                    readableMoistureVal = " - Moist";
                } else if (soilMoistureVal >= 651 && soilMoistureVal <= 700) {
                    readableMoistureVal = " - Dry";
                } else if (soilMoistureVal >= 701 && soilMoistureVal <= 1000) {
                    readableMoistureVal = " - Very dry";
                } else if (soilMoistureVal == 0) {
                    readableMoistureVal = " - Error or no sensor active";
                }
                else {
                    readableMoistureVal = "Error";
                }

                // Checks for sunlight
                if (sunlightVal >= 0 && sunlightVal <= 100) {
                    readableSunlightVal = " - Dark";
                } else if (sunlightVal >= 100 && sunlightVal <= 300) {
                    readableSunlightVal = " - Dim";
                } else if (sunlightVal >= 300 && sunlightVal <= 500) {
                    readableSunlightVal = " - Bright";
                } else if (sunlightVal >= 500 && sunlightVal <= 1023) {
                    readableSunlightVal = " - Very bright";
                } else {
                    readableSunlightVal = "Error";
                }

                // Checks for humidity
                if (humidityVal >= 0 && humidityVal <= 25) {
                    readableHumidityVal = "Dry air";
                } else if (humidityVal >= 26 && humidityVal <= 50) {
                    readableHumidityVal  = "Ideal humidity";
                } else if (humidityVal > 50) {
                    readableHumidityVal = "Too humid. Mold and mildew prone.";
                }

                // Checks for temperature
                switch (plantType) {
                    case "Cactus":
                        if (temperatureVal >= 10 && temperatureVal < 27) {
                            readableTemperatureVal = "Ideal temperature for cacti/desert plants.";
                        } else if (temperatureVal < 10) {
                            readableTemperatureVal = "May be too cold for indoor cacti/desert plants.";
                        }
                        break;
                    case "Succulent":
                        if (temperatureVal >= 35) {
                            readableTemperatureVal = "Too hot for succulents. Extended exposure may cause damage or death.";
                        } else if (temperatureVal >= 15 && temperatureVal < 27) {
                            readableTemperatureVal = "Ideal temperature for succulents.";
                        } else if (temperatureVal > 10 && temperatureVal < 15) {
                            readableTemperatureVal = ""; // Nothing special to state about temperature.
                        } else if (temperatureVal > 5 && temperatureVal <= 10) {
                            readableTemperatureVal = "Too cold for indoor succulents.";
                        } else if (temperatureVal >= 0 && temperatureVal <= 5) {
                            readableTemperatureVal = "Succulents may be damaged by cold.";
                        } else if (temperatureVal < 0) {
                            readableTemperatureVal = "Succulents may be damaged or killed by frost.";
                        }
                        break;
                    case "Flower":
                        if (temperatureVal >= 32) {
                            readableTemperatureVal = "Too hot for flowers. Extended exposure may cause damage or death.";
                        } else if (temperatureVal >= 15 && temperatureVal <= 24) {
                            readableTemperatureVal = "Ideal temperature for flowers.";
                        } else if (temperatureVal > 10 && temperatureVal < 15) {
                            readableTemperatureVal = "Cooler temperature for flowers. Generally expected at night.";
                        } else if (temperatureVal > 1 && temperatureVal <= 10) {
                            readableTemperatureVal = "Too cold for flowers.";
                        } else if (temperatureVal >= 0) {
                            readableTemperatureVal = "Flowers may be damaged or killed by frost.";
                        }
                        break;
                    case "Tender Perennial":
                        if (temperatureVal <= 15) {
                            readableTemperatureVal = "Too cold for tender perennials. May damage or kill.";
                        }
                        break;
                    case "Tropical/Subtropical":
                        if (temperatureVal >= 32) {
                            readableTemperatureVal = "Too hot for subtropical and tropical plants. Extended exposure may cause damage or death.";
                        } else if (temperatureVal >= 15 && temperatureVal <= 32) {
                            readableTemperatureVal = "Ideal for subtropical and tropical plants.";
                        } else if (temperatureVal > 10 && temperatureVal < 15) {
                            readableTemperatureVal = ""; // Nothing special to state about temperature.
                        } else if (temperatureVal >= 4 && temperatureVal < 10) {
                            readableTemperatureVal = "Prolonged exposure will damage or kill.";
                        } else if (temperatureVal <= 0 ) {
                            readableTemperatureVal = "Plant will be killed by frost.";
                        }
                }

                // Set the text of the text views to the values from the database
                textView_plantName.setText(plantName);
                textView_SoilMoisture.setText(soilMoistureVal + readableMoistureVal);
                textView_Sunlight.setText(sunlightVal + readableSunlightVal);
                textView_Humidity.setText(df.format(humidityVal) + "%");
                textView_readableHumid.setText(readableHumidityVal);
                textView_Temperature.setText(df.format(temperatureVal) + "°C");
                textView_readableTemp.setText(readableTemperatureVal);

                // For debugging purposes
                Log.d("PlantHealthActivity", "readPlantHealthValues - plantName: " + plantName);
                Log.d("PlantHealthActivity", "readPlantHealthValues - plantType: " + plantType);
                Log.d("PlantHealthActivity", "readPlantHealthValues - soilMoisture: " + snapshot.child("soil_Moisture").getValue().toString());
                Log.d("PlantHealthActivity", "readPlantHealthValues - visLight: " + snapshot.child("visLight").getValue().toString());
                Log.d("PlantHealthActivity", "readPlantHealthValues - humidity: " + snapshot.child("humidity").getValue().toString());
                Log.d("PlantHealthActivity", "readPlantHealthValues - temperature: " + snapshot.child("temperature").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PlantHealthActivity.this, "Failed to read values", Toast.LENGTH_SHORT).show();
            }
        });
    }
}