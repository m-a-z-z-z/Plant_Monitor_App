package com.dmaziarek_tus.plant_monitor_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddPlant extends AppCompatActivity {
    EditText editText_PlantName;
    Button button_AddPlant;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference dbRef;
    String userName, plantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        editText_PlantName = (EditText) findViewById(R.id.editText_PlantName);
        button_AddPlant = (Button) findViewById(R.id.button_AddPlant);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("Users");

        userName = getUserName().trim();
    }

    // Add plant to database
    public void onButtonAddPlantClicked(View view) {
        plantName = editText_PlantName.getText().toString().trim();
        Plant plant = new Plant();
        dbRef.child(userName).child("Plants").child(plantName).setValue(plant)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(AddPlant.this, "Plant added successfully", Toast.LENGTH_LONG).show();
                    Log.d("TAG", "onButtonAddPlantClicked - Plant added to database");
                    editText_PlantName.setEnabled(false);
                } else {
                    Toast.makeText(AddPlant.this, "Error adding plant. Does plant in that name already exist?", Toast.LENGTH_LONG).show();
                    Log.d("TAG", "onButtonAddPlantClicked - Plant not added to database");
                }
            }
        );


    }

    private String getUserName() {
        String displayName = null, email = null, uid = null;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
            email = user.getEmail();
            displayName = user.getDisplayName();
            // do something with the user's information
        }
        Log.d("TAG", "getUserData - display name: " + displayName);
        Log.d("TAG", "getUserData - email: " + email);
        Log.d("TAG", "getUserData - uid: " + uid);
        return displayName;
    }
}