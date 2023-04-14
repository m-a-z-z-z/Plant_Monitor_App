package com.dmaziarek_tus.plant_monitor_app.util;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.model.Plant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NotificationService extends Service {
    private DatabaseReference dbRef;
    String userName = UserUtils.getDisplayNameFromFirebase();
    private Map<String, Long> lastNotificationTimes = new HashMap<>();
    private static final long NOTIFICATION_THRESHOLD  = 60 * 60 * 1000; // 1 hour

    @Override
    public void onCreate() {
        super.onCreate();

        dbRef = FirebaseDatabase.getInstance().getReference("Users/" + userName + "/Plants");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot plantSnapshot : dataSnapshot.getChildren()) {
                    String plantID = plantSnapshot.getKey();
                    String plantName = plantSnapshot.child("plantName").getValue(String.class);
                    int soilMoisture = plantSnapshot.child("soilMoisture").getValue(Integer.class);
                    int minSoilMoisture = plantSnapshot.child("minSoilMoisture").getValue(Integer.class);
                    int maxSoilMoisture = plantSnapshot.child("maxSoilMoisture").getValue(Integer.class);
                    long lastNotificationTime = lastNotificationTimes.containsKey(plantID) ? lastNotificationTimes.get(plantID) : 0;
                    long currentTime = System.currentTimeMillis();
                    int notificationID = new Random().nextInt(1000);

                    if (soilMoisture < minSoilMoisture) {
                        if(currentTime - lastNotificationTime > NOTIFICATION_THRESHOLD) {
                            String title = plantName + " needs water!";
                            String message = plantName + " soil is at " + soilMoisture + "% moisture content.";
                            Log.i("NotificationService", "onDataChange\n title: " + title + "\nmessage: " + message + "\n");
                            createNotification(notificationID, title, message);
                            lastNotificationTimes.put(plantID, currentTime);
                        }
                    } else if (soilMoisture > maxSoilMoisture) {
                        if(currentTime - lastNotificationTime > NOTIFICATION_THRESHOLD) {
                            String title = plantName + " is over watered!";
                            String message = plantName + " soil is at " + soilMoisture + "% moisture content.\nUse less water next time.";
                            Log.i("NotificationService", "onDataChange\n title: " + title + "\nmessage: " + message + "\n");
                            createNotification(notificationID, title, message);
                            lastNotificationTimes.put(plantID, currentTime);
                        }
                    } else {
                        lastNotificationTimes.remove(plantID);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("NotificationService", "onCancelled - Error: " + databaseError.getMessage());
            }
        });
    }

    private void createNotification(int notificationID, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelID = "Soil Moisture Notification Channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, "Soil Moisture Notification Channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel for soil moisture notifications");
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.ic_water_drop)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);

        notificationManager.notify(notificationID, builder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
