package com.example.digtai_codriver;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText fnameEditText, mnameEditText, lnameEditText, phoneEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button signupButton;
    private ImageView passwordToggle, confirmPasswordToggle;
    private TextView loginTextView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        fnameEditText = findViewById(R.id.fnameEditText);
        mnameEditText = findViewById(R.id.mnameEditText);
        lnameEditText = findViewById(R.id.lnameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        signupButton = findViewById(R.id.signup_button);
        passwordToggle = findViewById(R.id.password_toggle);
        confirmPasswordToggle = findViewById(R.id.confirm_password_toggle);
        loginTextView = findViewById(R.id.login_textView);

        // Navigate to Login Activity
        loginTextView.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        signupButton.setOnClickListener(view -> registerUser());

        // Toggle password visibility
        passwordToggle.setOnClickListener(view -> togglePasswordVisibility(passwordEditText, passwordToggle));
        confirmPasswordToggle.setOnClickListener(view -> togglePasswordVisibility(confirmPasswordEditText, confirmPasswordToggle));
    }

    private void togglePasswordVisibility(EditText editText, ImageView toggleButton) {
        if (editText.getTransformationMethod() instanceof PasswordTransformationMethod) {
            // Show password
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            toggleButton.setImageResource(R.drawable.ic_visibility_on);
        } else {
            // Hide password
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            toggleButton.setImageResource(R.drawable.ic_visibility_off);
        }
        editText.setSelection(editText.getText().length()); // Move cursor to the end
    }

    private void registerUser() {
        String fname = fnameEditText.getText().toString().trim();
        String mname = mnameEditText.getText().toString().trim();
        String lname = lnameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        Double balance = 1000.0;

        // Validate input fields
        if (fname.isEmpty() || lname.isEmpty()) {
            Toast.makeText(this, "Full name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.isEmpty() || phone.length() < 10) {
            phoneEditText.setError("Enter a valid phone number");
            return;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email address");
            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return;
        }

        // Firebase Registration
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Store user details in Firestore
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("firstName", fname);
                            userMap.put("middleName", mname);
                            userMap.put("lastName", lname);
                            userMap.put("phoneNumber", phone);
                            userMap.put("email", email);
                            userMap.put("balance", balance);

                            db.collection("users").document(user.getUid())
                                    .set(userMap)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(SignupActivity.this, "Signup successful! Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(SignupActivity.this, "Failed to store user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}