package com.dmaziarek_tus.plant_monitor_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText editText_UserName;
    EditText editText_Email;
    EditText editText_Password;
    EditText editText_ConfirmPassword;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editText_UserName = (EditText) findViewById(R.id.editText_UserName);
        editText_Email = (EditText) findViewById(R.id.editText_Email);
        editText_Password = (EditText) findViewById(R.id.editText_Password);
        editText_ConfirmPassword = (EditText) findViewById(R.id.editText_ConfirmPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
    }

    public void onButtonSignUpClicked(View view) {
        String userName = editText_UserName.getText().toString().trim();
        String email = editText_Email.getText().toString().trim();
        String password = editText_Password.getText().toString().trim();
        String confirmPassword = editText_ConfirmPassword.getText().toString().trim();

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

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(userName, email, password);
                    FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
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
}