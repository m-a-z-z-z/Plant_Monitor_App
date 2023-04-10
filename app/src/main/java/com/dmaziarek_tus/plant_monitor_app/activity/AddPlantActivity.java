package com.dmaziarek_tus.plant_monitor_app.activity;

import static com.dmaziarek_tus.plant_monitor_app.util.PlantUtils.checkForPlantsWithSameName;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.databinding.ActivityAddPlantBinding;
import com.dmaziarek_tus.plant_monitor_app.model.Plant;
import com.dmaziarek_tus.plant_monitor_app.util.PlantNamesSingleton;
import com.dmaziarek_tus.plant_monitor_app.model.User;
import com.dmaziarek_tus.plant_monitor_app.util.PlantUtils;
import com.dmaziarek_tus.plant_monitor_app.util.UserUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AddPlantActivity extends DrawerBaseActivity {
    ActivityAddPlantBinding binding;
    EditText editText_PlantName;
    Button button_AddPlant;
    Spinner spinner;
    ImageView imageView;
    ProgressBar progressBar;
    String userName, plantID, plantName, selectedPlantType, filename;
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
        spinner = (Spinner) findViewById(R.id.spinner_PlantType);
        button_AddPlant = (Button) findViewById(R.id.button_AddPlant);
        imageView = (ImageView) findViewById(R.id.imageView_plantPhoto);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Set spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.plant_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        // Respond to spinner selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPlantType = parent.getItemAtPosition(position).toString();
                Log.d("AddPlantActivity", "onItemSelected: " + selectedPlantType);
                spinner.setOnItemSelectedListener(this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Get user to add plant under
        userName = UserUtils.getDisplayNameFromFirebase();
    }

    // Add plant to database
    public void onButtonAddPlantClicked(View view) {
        plantName = editText_PlantName.getText().toString().trim();
        if(plantName.isEmpty()) {
            editText_PlantName.setError("Plant name is required");
            editText_PlantName.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        button_AddPlant.setVisibility(View.GONE);

        checkForPlantsWithSameName(plantName, new PlantUtils.OnPlantExistsCallback() {
            @Override
            public void onPlantExists(boolean exists) {
                if(exists) {
                    Toast.makeText(AddPlantActivity.this, "Plant with that name already exists", Toast.LENGTH_LONG).show();
                    int randomNum = (int) (Math.random() * 1000);
                    plantName += " " + randomNum;
                    plantID = userName + "_plant_" + randomNum;
                    Log.d("AddPlantActivity", "onButtonAddPlantClicked, onPlantExists - Plant name: " + plantName);
                    addPlantToDB(view);
                }
                else {
                    Toast.makeText(AddPlantActivity.this, "Plant name is unique", Toast.LENGTH_LONG).show();
                    int randomNum = (int) (Math.random() * 1000);
                    plantID = userName + "_plant_" + randomNum;
                    addPlantToDB(view);
                }
            }
        });
    }

    public void addPlantToDB(View view) {
        filename = plantID + ".jpg";    // Used for retrieving photo from storage
        Plant plant = new Plant(plantID, plantName, selectedPlantType, filename);
        PlantNamesSingleton.getInstance().addPlant(plant);

        FirebaseDatabase.getInstance().getReference("Users")
            .child(userName).child("Plants").child(plantID).setValue(plant)
            .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        uploadPhoto(view, filename);
                        Toast.makeText(AddPlantActivity.this, "Plant added successfully", Toast.LENGTH_LONG).show();
                        Log.d("AddPlantActivity", "onButtonAddPlantClicked - Plant added to database");
                        Intent intent = new Intent(AddPlantActivity.this, PlantHealthActivity.class);
                        intent.putExtra("plantID", plantID);
                        startActivity(intent);
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