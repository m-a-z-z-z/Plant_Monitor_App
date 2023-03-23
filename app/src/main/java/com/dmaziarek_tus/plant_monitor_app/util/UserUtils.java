package com.dmaziarek_tus.plant_monitor_app.util;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.activity.DashboardActivity;
import com.dmaziarek_tus.plant_monitor_app.activity.DrawerBaseActivity;
import com.dmaziarek_tus.plant_monitor_app.activity.SignInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class UserUtils {

    public static void setDisplayNameForFirebase(FirebaseUser firebaseUser, String userName) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build();

        firebaseUser.updateProfile(profileUpdates)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("SignUpActivity", "User profile updated.");
                    }
                }
            });
    }

    public static String getDisplayNameFromFirebase() {
        String fbDisplayName = null;

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser != null) {
            fbDisplayName = fbUser.getDisplayName();
            // do something with the user's information
        }
        return fbDisplayName;
    }

    public static void checkForUsersWithSameUsername(String username, OnUsernameTakenCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        Query query = ref.orderByChild("userName").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    callback.onUsernameTaken(true);
                } else {
                    callback.onUsernameTaken(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onUsernameTaken(false);
            }
        });
    }

    public interface OnUsernameTakenCallback {
        void onUsernameTaken(boolean isTaken);
    }

    public static void checkForUsersWithSameEmail(String email, OnEmailTakenCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        Query query = ref.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    callback.onEmailTaken(true);
                } else {
                    callback.onEmailTaken(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onEmailTaken(false);
            }
        });
    }

    public interface OnEmailTakenCallback {
        void onEmailTaken(boolean isTaken);
    }
}
