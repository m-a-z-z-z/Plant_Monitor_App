package com.dmaziarek_tus.plant_monitor_app.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.databinding.ActivityAddPlantBinding;
import com.dmaziarek_tus.plant_monitor_app.model.Plant;
import com.dmaziarek_tus.plant_monitor_app.util.PlantNamesSingleton;
import com.dmaziarek_tus.plant_monitor_app.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddPlantActivity extends DrawerBaseActivity {
    ActivityAddPlantBinding binding;
    EditText editText_PlantName;
    Button button_AddPlant;
    Spinner spinner;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference dbRef;
    String userName, plantName, selectedPlantType;

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

        // Get Firebase instances for sending data
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("Users");

        // Get user to add plant under
        User user = new User();
        userName = user.getUserName().trim();
    }

    // Add plant to database
    public void onButtonAddPlantClicked(View view) {
        plantName = editText_PlantName.getText().toString().trim();
        PlantNamesSingleton.getInstance().addPlantName(plantName);
        Plant plant = new Plant(plantName, selectedPlantType);
        Log.d("AddPlantActivity", "onButtonAddPlantClicked: " + plantName + " " + selectedPlantType);

        dbRef.child(userName).child("Plants").child(plantName).setValue(plant)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(AddPlantActivity.this, "Plant added successfully", Toast.LENGTH_LONG).show();
                    Log.d("AddPlantActivity", "onButtonAddPlantClicked - Plant added to database");
                    editText_PlantName.setEnabled(false);
                } else {
                    Toast.makeText(AddPlantActivity.this, "Error adding plant. Does plant in that name already exist?", Toast.LENGTH_LONG).show();
                    Log.d("AddPlantActivity", "onButtonAddPlantClicked - Plant not added to database");
                }
            }
        );
    }
}