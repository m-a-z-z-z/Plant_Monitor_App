package com.dmaziarek_tus.plant_monitor_app.activity;

import static com.dmaziarek_tus.plant_monitor_app.util.UserUtils.checkForUsersWithSameEmail;
import static com.dmaziarek_tus.plant_monitor_app.util.UserUtils.checkForUsersWithSameUsername;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.model.User;
import com.dmaziarek_tus.plant_monitor_app.util.UserUtils;
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
    Button button_SignUp;
    private FirebaseAuth mAuth;
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
        button_SignUp = (Button) findViewById(R.id.button_signUp);
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
        if (!email.isEmpty()){
            checkForUsersWithSameEmail(email, new UserUtils.OnEmailTakenCallback() {
                @Override
                public void onEmailTaken(boolean isTaken) {
                    if (isTaken) {
                        editText_Email.setError("Email is already in use");
                        editText_Email.requestFocus();
                        return;
                    }
                }
            });
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
        if(!userName.isEmpty()) {
            checkForUsersWithSameUsername(userName, new UserUtils.OnUsernameTakenCallback() {
                @Override
                public void onUsernameTaken(boolean isTaken) {
                    if (isTaken) {
                        editText_UserName.setError("Username is already taken");
                        editText_UserName.requestFocus();
                        return;
                    }
                    else {
                        progressBar.setVisibility(View.VISIBLE);
                        button_SignUp.setVisibility(View.GONE);

                        // If checks pass, register user
                        registerUser();
                    }
                }
            });
        }


    }

    private void registerUser() {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(email, userName);
                    FirebaseUser fbUser = mAuth.getCurrentUser();
                    UserUtils.setDisplayNameForFirebase(fbUser, userName);

                    // Add user to Firebase Realtime Database
                    FirebaseDatabase.getInstance().getReference("Users")
                        .child(userName).setValue(user)// User will be saved under path "Users": {"**Username**"} in realtime database
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                    // Redirect to sign in activity
                                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                    startActivity(intent);
                                    finish();   // Close sign up activity
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Failed to register. Try again!", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                    button_SignUp.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    );
                }
                else {
                    Toast.makeText(SignUpActivity.this, "Failed to register. Try again!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    button_SignUp.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void loadSignInActivity(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}