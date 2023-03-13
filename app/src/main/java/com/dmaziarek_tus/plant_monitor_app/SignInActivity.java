package com.dmaziarek_tus.plant_monitor_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dmaziarek_tus.plant_monitor_app.model.PlantNamesSingleton;
import com.dmaziarek_tus.plant_monitor_app.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SignInActivity extends AppCompatActivity {

    EditText editText_emailSignIn, editText_passwordSignIn;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String email, password, userName;
    ArrayList<String> plantNameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editText_passwordSignIn = (EditText) findViewById(R.id.editText_PasswordSignIn);
        editText_emailSignIn = (EditText) findViewById(R.id.editText_EmailSignIn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
    }

    public void onButtonSignInClicked(View view) {
        email = editText_emailSignIn.getText().toString().trim();
        password = editText_passwordSignIn.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editText_emailSignIn.setError("Please enter a valid email");
            editText_emailSignIn.requestFocus();
            return;
        }
        if (password.isEmpty() || password.length() < 6) {
            editText_passwordSignIn.setError("Password is required");
            editText_passwordSignIn.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Sign in user if checks are passed
        signInUser();

        // Wait for 2.5 seconds to allow sign in to complete, otherwise retrieveUserPlants() will be called before user is signed in returning null data
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // Load dashboard activity
                    retrieveUserPlants();
                    // Load dashboard activity
                    loadDashboardActivity(view);
                }
            }
        };
        thread.start();
    }

    private void signInUser() {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignInActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(SignInActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        );
    }

    public void loadDashboardActivity(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    public void retrieveUserPlants() {
        // Get username to use for reference in database to retrieve users plants
        User user = new User();
        userName = user.getUserName();
        Log.d("SignInActivity", "retrieveUserPlants - display name: " + userName);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users/" + userName + "/Plants");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();

                for (DataSnapshot child : children) {
                    String plantName = child.getKey();
                    plantNameList.add(plantName);
                    Log.d("SignInActivity", "onDataChange - Plant name: " + plantName);
                }
                Log.d("SignInActivity", "onDataChange - Plant names: " + plantNameList);

                PlantNamesSingleton.getInstance().setPlantNames(plantNameList);
                myRef.removeEventListener(this);    // Remove listener to prevent multiple calls
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("SignInActivity", "onCancelled - Error: " + error.getMessage());
            }
        });

    }
}