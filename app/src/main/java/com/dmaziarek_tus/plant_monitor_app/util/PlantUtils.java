package com.dmaziarek_tus.plant_monitor_app.util;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.activity.AddPlantActivity;
import com.dmaziarek_tus.plant_monitor_app.activity.PlantHealthActivity;
import com.dmaziarek_tus.plant_monitor_app.model.Plant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

    public static void plantSelected(Context activityContext, Plant plant) {
        Intent intent = new Intent(activityContext, PlantHealthActivity.class);
        intent.putExtra("plant", plant);
        activityContext.startActivity(intent);
    }

    public static void retrieveUserPlants() {
        // Get username to use for reference in database to retrieve users plants
        String userName = UserUtils.getDisplayNameFromFirebase();
        Log.d("SignInActivity", "retrieveUserPlants - display name: " + userName);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users/" + userName + "/Plants");
        ArrayList<Plant> plantList = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();

                for (DataSnapshot child : children) {
                    String plantID = child.child("plantID").getValue(String.class); // Get plant ID
                    String plantName = child.child("plantName").getValue(String.class); // Get plant name
                    String plantType = child.child("plantType").getValue(String.class); // Get plant type
                    String photoUrl = child.child("photoUrl").getValue(String.class); // Get plant URL
                    int minMoisture = child.child("minSoilMoisture").getValue(int.class); // Get plant preferred soil moisture
                    int maxMoisture = child.child("maxSoilMoisture").getValue(int.class); // Get plant preferred soil moisture
                    double minTemp = child.child("minTemp").getValue(double.class);
                    double maxTemp = child.child("maxTemp").getValue(double.class);
                    int minLight = child.child("minLight").getValue(int.class);
                    int maxLight = child.child("maxLight").getValue(int.class);
                    Plant plant = new Plant(plantID, plantName, plantType, photoUrl, minMoisture, maxMoisture, minTemp, maxTemp, minLight, maxLight);
                    plantList.add(plant);
                    Log.d("PlantUtils", "retrieveUserPlants, onDataChange"
                            + "\nPlant name: " + plantName
                            + "\nplant type: " + plantType
                            + "\nplant ID: " + plantID
                            + "\nphoto URL: " + photoUrl
                            + "\nmin. moisture: " + minMoisture
                            + "\nmax moisture: " + maxMoisture
                            + "\nmin temp: " + minTemp
                            + "\nmax temp: " + maxTemp
                            + "\nmin light: " + minLight
                            + "\nmax light: " + maxLight
                    );
                }
                Log.d("PlantUtils", "onDataChange - Plant names: " + plantList);

                PlantListSingleton.getInstance().setPlantList(plantList);
                myRef.removeEventListener(this);    // Remove listener to prevent multiple calls
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("PlantUtils", "onCancelled - Error: " + error.getMessage());
            }
        });
    }

    public static void updatePlantDetailsInDB(String plantID, String newPlantName, String plantType, int minMoisture, int maxMoisture, double minTemp, double maxTemp) {
        String userName = UserUtils.getDisplayNameFromFirebase();
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Users/" + userName + "/Plants/" + plantID);
        databaseReference1.child("plantName").setValue(newPlantName);
        databaseReference1.child("plantType").setValue(plantType);
        databaseReference1.child("minSoilMoisture").setValue(minMoisture);
        databaseReference1.child("maxSoilMoisture").setValue(maxMoisture);
        databaseReference1.child("minTemp").setValue(minTemp);
        databaseReference1.child("maxTemp").setValue(maxTemp);
    }

    public static void deletePlantFromDB(Plant plant) {
        // Remove from DB
        String userName = UserUtils.getDisplayNameFromFirebase();
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Users/" + userName + "/Plants/" + plant.getPlantID());
        databaseReference1.removeValue();

        // Delete photo from storage
        String filename = plant.getPhotoUrl();
        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference("images/"+filename);
        mStorageReference.delete();
    }

    // Not needed anymore but might be useful in the future
    public static void checkForPlantsWithSameName(String plantName, OnPlantExistsCallback callback) {
        String userName = UserUtils.getDisplayNameFromFirebase();
        DatabaseReference plantsRef = FirebaseDatabase.getInstance().getReference("Users/" + userName + "/Plants");
        plantsRef.child(plantName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean exists = snapshot.exists();
                if (exists) {
                    Log.d("PlantUtils", "onDataChange - Plant name already exists");
                }
                callback.onPlantExists(exists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("PlantUtils", "onCancelled - Error: " + error.getMessage());
                callback.onPlantExists(false);
            }
        });
    }
    public interface OnPlantExistsCallback {
        void onPlantExists(boolean exists);
    }

    public static void notifyWhenPlantsCritical(Context context) {
        Map<String, Integer> soilMoistureMap = new HashMap<>();
        String userName = UserUtils.getDisplayNameFromFirebase();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + userName + "/Plants");

        // notification stuff here
        // Notification channel
        String channelID = "plant_notification_channel";
        String channelName = "Plant Notification Channel";
        String channelDescription = "Channel for plant notifications";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, channelName, importance);
            channel.setDescription(channelDescription);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            String plantName;
            int soilMoisture;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iterate through all the plants
                for (DataSnapshot plantSnapshot : dataSnapshot.getChildren()) {
                    // Get the name of the plant
                    plantName = plantSnapshot.child("plantName").getValue(String.class);
                    Log.d("PlantUtils", "notifyWhenPlantsCritical, onDataChange - plant name: " + plantName);
                    // Get the soil moisture of the plant
                    soilMoisture = plantSnapshot.child("soilMoisture").getValue(Integer.class);

                    soilMoistureMap.put(plantName, soilMoisture);   // Add plant name and soil moi
                }

                for (Map.Entry<String, Integer> entry : soilMoistureMap.entrySet()) {
                    if (entry.getValue() >= 700) {
                        // Notification builder
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID)
                                .setSmallIcon(R.drawable.ic_water_drop)
                                .setContentTitle(entry.getKey() + " needs watering")
                                .setContentText("Your plant is dry and crusty dawg.")
                                .setPriority(NotificationCompat.PRIORITY_HIGH);

                        Log.d("PlantUtils", "onDataChange - " + entry.getKey() + " is critical");
                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                        }
                        notificationManagerCompat.notify(1, builder.build());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
                Log.e("PlantUtils", "onCancelled - Error: " + databaseError.getMessage());
            }
        });

    }

}
