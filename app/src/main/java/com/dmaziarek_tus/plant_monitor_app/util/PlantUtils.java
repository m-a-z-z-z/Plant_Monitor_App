package com.dmaziarek_tus.plant_monitor_app.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dmaziarek_tus.plant_monitor_app.activity.AddPlantActivity;
import com.dmaziarek_tus.plant_monitor_app.activity.PlantHealthActivity;
import com.dmaziarek_tus.plant_monitor_app.databinding.ActivityHistoricalDataBinding;
import com.dmaziarek_tus.plant_monitor_app.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlantUtils {

    public static void noPlantsAdded(Context activityContext) {
        Toast.makeText(activityContext, "No plants added", Toast.LENGTH_SHORT).show();
        Log.d("PlantHealthActivity", "onCreate - No plants added");
        Intent intent = new Intent(activityContext, AddPlantActivity.class);
        activityContext.startActivity(intent);
    }

    public static void plantSelected(Context activityContext, String plantName) {
        Intent intent = new Intent(activityContext, PlantHealthActivity.class);
        intent.putExtra("plantName", plantName);
        activityContext.startActivity(intent);
    }

    public static void retrieveUserPlants() {
        // Get username to use for reference in database to retrieve users plants
        User user = new User();
        String userName = user.getUserName();
        Log.d("SignInActivity", "retrieveUserPlants - display name: " + userName);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users/" + userName + "/Plants");
        ArrayList<String> plantNameList = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();

                for (DataSnapshot child : children) {
                    String plantName = child.getKey();
                    plantNameList.add(plantName);
                    Log.d("PlantUtils", "onDataChange - Plant name: " + plantName);
                }
                Log.d("PlantUtils", "onDataChange - Plant names: " + plantNameList);

                PlantNamesSingleton.getInstance().setPlantNames(plantNameList);
                myRef.removeEventListener(this);    // Remove listener to prevent multiple calls
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("SignInActivity", "onCancelled - Error: " + error.getMessage());
            }
        });
    }

//    public static void notifyWhenPlantsCritical() {
//        Map<String, Integer> soilMoistureMap = new HashMap<>();
//
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Plants");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // Iterate through all the plants
//                for (DataSnapshot plantSnapshot : dataSnapshot.getChildren()) {
//                    // Get the name of the plant
//                    String plantName = plantSnapshot.child("plantName").getValue(String.class);
//
//                    // Get the soil moisture of the plant
//                    int soilMoisture = plantSnapshot.child("soil_Moisture").getValue(Integer.class);
//
//                    // Map the soil moisture to the name of the plant
//                    // You can use a Map<String, Integer> to store the data
//                    soilMoistureMap.put(plantName, soilMoisture);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle errors here
//            }
//        });
//
//    }

}
