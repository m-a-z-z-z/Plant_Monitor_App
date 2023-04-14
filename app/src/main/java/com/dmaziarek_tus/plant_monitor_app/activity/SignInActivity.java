package com.dmaziarek_tus.plant_monitor_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.util.PlantUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    EditText editText_emailSignIn, editText_passwordSignIn;
    ProgressBar progressBar;
    Button button_SignIn;
    FirebaseAuth mAuth;
    String email, password, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editText_passwordSignIn = (EditText) findViewById(R.id.editText_PasswordSignIn);
        editText_emailSignIn = (EditText) findViewById(R.id.editText_EmailSignIn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        button_SignIn = (Button) findViewById(R.id.button_signIn);
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

        button_SignIn.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        // Sign in user if checks are passed
        signInUser();
    }

    private void signInUser() {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignInActivity.this, "Login successful", Toast.LENGTH_LONG).show();

                        PlantUtils.retrieveUserPlants();
                        // Load dashboard activity
                        Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();   // Close sign in activity
                    }
                    else {
                        Toast.makeText(SignInActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        button_SignIn.setVisibility(View.VISIBLE);
                    }
                }
            }
        );
    }

}