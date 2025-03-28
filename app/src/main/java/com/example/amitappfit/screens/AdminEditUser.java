package com.example.amitappfit.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.amitappfit.R;
import com.example.amitappfit.model.User;
import com.example.amitappfit.services.DatabaseService;

public class AdminEditUser extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmail, etPhone, etPassword;
    private Button btnEditUserProfile, btnViewUserOutfits, btnDeleteUser;
    private DatabaseService databaseService;
    private String userId, lookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_user);

        // Initialize the views
        etFirstName = findViewById(R.id.et_user_first_name);
        etLastName = findViewById(R.id.et_user_last_name);
        etEmail = findViewById(R.id.et_user_email);
        etPhone = findViewById(R.id.et_user_phone);
        etPassword = findViewById(R.id.et_user_password);
        btnEditUserProfile = findViewById(R.id.btn_edit_user_profile);
        btnViewUserOutfits = findViewById(R.id.btn_view_user_outfits);
        btnDeleteUser = findViewById(R.id.btn_delete_user);

        // Initialize database service
        databaseService = DatabaseService.getInstance();

        // Get user ID from the intent
        userId = getIntent().getStringExtra("USER_UID");

        // Load user data
        loadUserData(userId);

        // Set click listeners
        btnEditUserProfile.setOnClickListener(v -> saveUserData());
        btnViewUserOutfits.setOnClickListener(v -> viewUserLooks());
        btnDeleteUser.setOnClickListener(v -> deleteUser());
    }

    private void loadUserData(String userId) {
        databaseService.getUser(userId, new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User user) {
                etFirstName.setText(user.getfName());
                etLastName.setText(user.getlName());
                etEmail.setText(user.getEmail());
                etPhone.setText(user.getPhone());
                etPassword.setText(user.getPassword());
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(AdminEditUser.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserData() {
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String email = etEmail.getText().toString();
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();


        boolean isAdmin = true;
        User updatedUser = new User(userId, firstName, lastName, email, phone, password, isAdmin);


        databaseService.updateUser(updatedUser, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void result) {
                Toast.makeText(AdminEditUser.this, "User updated successfully", Toast.LENGTH_SHORT).show();


                finish();

            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(AdminEditUser.this, "Failed to update user", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void deleteUser() {
        databaseService.deleteUser(userId, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void result) {
                Toast.makeText(AdminEditUser.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity after deletion
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(AdminEditUser.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void viewUserLooks() {
        // Start a new activity to view the user's looks
        Intent intent = new Intent(this, YourSavedLooks.class);
        intent.putExtra("USER_UID", userId);
        startActivity(intent);
    }
}
