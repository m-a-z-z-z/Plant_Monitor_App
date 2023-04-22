package com.dmaziarek_tus.plant_monitor_app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.databinding.ActivityAddPlantBinding;
import com.dmaziarek_tus.plant_monitor_app.model.Plant;
import com.dmaziarek_tus.plant_monitor_app.util.PlantListSingleton;
import com.dmaziarek_tus.plant_monitor_app.util.PlantUtils;
import com.dmaziarek_tus.plant_monitor_app.util.UserUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class AddPlantActivity extends DrawerBaseActivity {
    ActivityAddPlantBinding binding;
    EditText editText_PlantName;
    TextView textView_MinTemp, textView_MaxTemp;
    Button button_AddPlant;
    Spinner spinner_PlantType, spinner_SoilMoisture, spinner_Light;
    ImageView imageView;
    ProgressBar progressBar;
    SeekBar minTempSeekbar, maxTempSeekbar;
    String userName, plantID, plantName, selectedPlantType, selectedSoilMoisture, selectedLight, filename;
    double minTemp, maxTemp;
    int minMoisture, maxMoisture, minLight, maxLight;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPlantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Add Plant");

        // Get UI elements
        editText_PlantName = (EditText) findViewById(R.id.editText_PlantName);
        textView_MinTemp = (TextView) findViewById(R.id.textView_MinTemp);
        textView_MaxTemp = (TextView) findViewById(R.id.textView_MaxTemp);
        spinner_PlantType = (Spinner) findViewById(R.id.spinner_PlantType);
        spinner_SoilMoisture = (Spinner) findViewById(R.id.spinner_PreferredMoisture);
        spinner_Light = (Spinner) findViewById(R.id.spinner_PreferredLight);
        button_AddPlant = (Button) findViewById(R.id.button_AddPlant);
        imageView = (ImageView) findViewById(R.id.imageView_plantPhoto);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        minTempSeekbar = (SeekBar) findViewById(R.id.seekBar_MinTemp);
        maxTempSeekbar = (SeekBar) findViewById(R.id.seekBar_MaxTemp);

        // Get user to add plant under
        userName = UserUtils.getDisplayNameFromFirebase();

        // Set spinner for plant type
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.plant_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_PlantType.setAdapter(adapter);
        spinner_PlantType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPlantType = parent.getItemAtPosition(position).toString();
                Log.d("AddPlantActivity", "onItemSelected: " + selectedPlantType);
                spinner_PlantType.setOnItemSelectedListener(this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Set spinner for soil moisture
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.preferred_soil_moisture, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_SoilMoisture.setAdapter(adapter2);
        spinner_SoilMoisture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSoilMoisture = parent.getItemAtPosition(position).toString();
                Log.d("AddPlantActivity", "onItemSelected: " + selectedSoilMoisture);
                spinner_SoilMoisture.setOnItemSelectedListener(this);
                // Translate selected soil moisture to min and max moisture
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
                Log.d("AddPlantActivity", "onItemSelected - minMoisture: " + minMoisture + " maxMoisture: " + maxMoisture);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }

        });

        // Set spinner for light
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.preferred_light, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Light.setAdapter(adapter3);
        spinner_Light.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLight = parent.getItemAtPosition(position).toString();
                Log.d("AddPlantActivity", "onItemSelected: " + selectedLight);
                spinner_Light.setOnItemSelectedListener(this);

                // Translate selected light to min and max light
                switch (selectedLight) {
                    case "Low–light (50–250 lumens)":
                        minLight = 50;
                        maxLight = 250;
                        break;
                    case "Medium–light (250–1000 lumens)":
                        minLight = 250;
                        maxLight = 1000;
                        break;
                    case "High–light (1000+ lumens)":
                        minLight = 1000;
                        maxLight = 10000;
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Set seekbars for min and max temperature
        minTempSeekbar.setMax(60); minTempSeekbar.setProgress(1);
        minTempSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minTemp = progress;
                textView_MinTemp.setText(minTemp + "°C");
                Log.d("AddPlantActivity", "onProgressChanged - Min temp: " + minTemp);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        maxTempSeekbar.setMax(60); maxTempSeekbar.setProgress(1);
        maxTempSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maxTemp = progress;
                textView_MaxTemp.setText(maxTemp + "°C");
                Log.d("AddPlantActivity", "onProgressChanged - Max temp: " + maxTemp);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    // Add plant to database
    public void onButtonAddPlantClicked(View view) {
        plantName = editText_PlantName.getText().toString().trim();
        if(plantName.isEmpty()) {
            editText_PlantName.setError("Plant name is required");
            editText_PlantName.requestFocus();
            return;
        }
        int randomNum = (int) (Math.random() * 1000);
        plantID = "plant_" + plantName + "_" + randomNum;
        plantID = plantID.replaceAll("\\s+",""); // Remove whitespace as filename cannot contain whitespace
        filename = plantID + ".jpg";    // Used for retrieving photo from storage

        progressBar.setVisibility(View.VISIBLE);
        button_AddPlant.setVisibility(View.GONE);

        addPlantToDB(view);
    }

    public void addPlantToDB(View view) {
        Plant plant = new Plant(plantID, plantName, selectedPlantType, filename, minMoisture, maxMoisture, minTemp, maxTemp, minLight, maxLight);
        PlantListSingleton.getInstance().addPlant(plant);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userName).child("Plants");
        usersRef.child(plantID).setValue(plant)
            .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        uploadPhoto(view, filename);
                        Toast.makeText(AddPlantActivity.this, "Plant added successfully", Toast.LENGTH_LONG).show();
                        Log.d("AddPlantActivity", "onButtonAddPlantClicked - Plant added to database");
                        PlantUtils.plantSelected(this, plant);
                    } else {
                        Toast.makeText(AddPlantActivity.this, "Error adding plant. Does plant in that name already exist?", Toast.LENGTH_LONG).show();
                        Log.d("AddPlantActivity", "onButtonAddPlantClicked - Plant not added to database");
                        progressBar.setVisibility(View.GONE);
                        button_AddPlant.setVisibility(View.VISIBLE);
                    }
                }
            );
    }

    public void onButtonTakePhotoClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            mImageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(mImageBitmap);
        }
    }

    protected void uploadPhoto(View view, String photoName) {
        if (mImageBitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            StorageReference imagesRef = FirebaseStorage.getInstance().getReference().child("images/" + photoName);

            UploadTask uploadTask = imagesRef.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Image uploaded successfully
                    Log.d("AddPlantActivity", "onSuccess: Image uploaded successfully");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Image upload failed
                    Log.d("AddPlantActivity", "onFailure: Image upload failed.\n " + e.getMessage());
                }
            });
        }
    }

}