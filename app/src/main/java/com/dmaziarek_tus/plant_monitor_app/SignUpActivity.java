package com.dmaziarek_tus.plant_monitor_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dmaziarek_tus.plant_monitor_app.model.User;
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

public class SignUpActivity extends AppCompatActivity {
    EditText editText_UserName, editText_Email, editText_Password, editText_ConfirmPassword;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    DataSnapshot snapshot;
    String userName, email, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editText_UserName = (EditText) findViewById(R.id.editText_UserName);
        editText_Email = (EditText) findViewById(R.id.editText_EmailSignIn);
        editText_Password = (EditText) findViewById(R.id.editText_PasswordSignIn);
        editText_ConfirmPassword = (EditText) findViewById(R.id.editText_ConfirmPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
    }

    public void onButtonSignUpClicked(View view) {
        userName = editText_UserName.getText().toString().trim();
        email = editText_Email.getText().toString().trim();
        password = editText_Password.getText().toString().trim();
        confirmPassword = editText_ConfirmPassword.getText().toString().trim();

        if (userName.isEmpty()) {
            editText_UserName.setError("Username is required");
            editText_UserName.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            editText_Email.setError("Email is required");
            editText_Email.requestFocus();
            return;
        }
        if (password.isEmpty() || password.length() < 6) {
            editText_Password.setError("Password is required");
            editText_Password.requestFocus();
            return;
        }
        if (confirmPassword.isEmpty() || confirmPassword.length() < 6) {
            editText_ConfirmPassword.setError("Confirm password is required");
            editText_ConfirmPassword.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            editText_ConfirmPassword.setError("Passwords do not match");
            editText_ConfirmPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        // If checks pass, register user
        registerUser();

        // Redirect to sign in activity
        loadSignInActivity(view);
    }

    private void registerUser() {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(userName, email, password);
                    FirebaseUser fbUser = mAuth.getCurrentUser();
                    setDisplayName(fbUser, userName);

                    // Add user to Firebase Realtime Database
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(userName).setValue(user)// User will be saved under path "Users": {"**Username**"} in realtime database
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Failed to register. Try again!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(SignUpActivity.this, "Failed to register. Try again!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setDisplayName(FirebaseUser firebaseUser, String userName) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
            .setDisplayName(userName)
            .build();

        firebaseUser.updateProfile(profileUpdates)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "User profile updated.");
                    }
                }
            });
    }

    private boolean checkUserNameExistsInFirebase(String userName) {
        Log.d("TAG", "checkUserNameExistsInFirebase: Checking if " + userName + " exists in Firebase Realtime Database...");
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users").child("userName");
        Query query = databaseRef.orderByChild("userName").equalTo(userName);

        final boolean[] exists = new boolean[1];
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                exists[0] = snapshot.exists();
                Log.d("TAG", "onDataChange: User exists: " + exists[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                exists[0] = false;
                Log.d("TAG", "onCancelled: User exists: " + exists[0]);
            }
        });

        return exists[0];
    }

    public void loadSignInActivity(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}