package com.dmaziarek_tus.plant_monitor_app;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.util.Log;

import com.dmaziarek_tus.plant_monitor_app.databinding.ActivityDashboardBinding;
import com.dmaziarek_tus.plant_monitor_app.model.MySingleton;
import com.dmaziarek_tus.plant_monitor_app.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardActivity extends DrawerBaseActivity {

    ActivityDashboardBinding binding;
//    FirebaseDatabase database;
//    DatabaseReference myRef;
//    String userName;
//    ArrayList<String> plantNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Dashboard");

//        User user = new User();
//        userName = user.getUserName();
//        Log.d("DashboardActivity", "onCreate - display name: " + userName);
//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference("Users/" + userName + "/Plants");
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Iterable<DataSnapshot> children = snapshot.getChildren();
//
//                for (DataSnapshot child : children) {
//                    String plantName = child.getKey();
//                    plantNames.add(plantName);
//                    Log.d("DashboardActivity", "onDataChange - Plant name: " + plantName);
//                }
//                Log.d("DashboardActivity", "onDataChange - Plant names: " + plantNames);
//
//                MySingleton.getInstance().setPlantNames(plantNames);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("DashboardActivity", "onCancelled - Error: " + error.getMessage());
//            }
//        });
    }
}