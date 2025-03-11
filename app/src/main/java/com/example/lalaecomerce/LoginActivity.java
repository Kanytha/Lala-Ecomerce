package com.example.lalaecomerce;

import static androidx.core.content.ContextCompat.startActivity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignUp;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Set click listeners
        btnLogin.setOnClickListener(v -> login());
        tvSignUp.setOnClickListener(v -> startActivity(new Intent(this, SignUpActivity.class)));
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // --- Validation ---
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Email is required");
            return;
        } else {
            tilEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Password is required");
            return;
        } else {
            tilPassword.setError(null);
        }

        // Check if user exists
        if (!databaseHelper.checkUser(email)) {
            Toast.makeText(this, "Email does not exist", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate credentials
        if (databaseHelper.validateLogin(email, password)) {
            // Get user role
            String role = databaseHelper.getUserRole(email);

            // Redirect based on role
            Intent intent;
            if (role.equals("admin")) {
                intent = new Intent(this, AdminActivity.class);
            } else {
                intent = new Intent(this, MainActivity.class);
            }

            intent.putExtra("USER_EMAIL", email);
            intent.putExtra("USER_ROLE", role);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
        }
    }
}