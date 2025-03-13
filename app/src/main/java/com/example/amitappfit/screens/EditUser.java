package com.example.amitappfit.screens;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.amitappfit.R;

public class EditUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        // שמירה בקובץ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // הגדרת רכיבי הממשק
        EditText firstNameEditText = findViewById(R.id.et_first_name);
        EditText lastNameEditText = findViewById(R.id.et_last_name);
        EditText emailEditText = findViewById(R.id.et_email);
        EditText phoneEditText = findViewById(R.id.et_phone);
        EditText passwordEditText = findViewById(R.id.et_password);
        Button saveChangesButton = findViewById(R.id.btn_save_changes);

        // הגדרת שדות המידע עבור הטעינה הראשונית
        loadUserData(sharedPreferences, firstNameEditText, lastNameEditText, emailEditText, phoneEditText, passwordEditText);

        // הקשבה לכפתור שמירה
        saveChangesButton.setOnClickListener(view -> {
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // שמירת המידע
            if (isValidInput(firstName, lastName, email, phone, password)) {
                // שמירת המידע ב-SharedPreferences
                editor.putString("FirstName", firstName);
                editor.putString("LastName", lastName);
                editor.putString("Email", email);
                editor.putString("Phone", phone);
                editor.putString("Password", password);
                editor.apply();

                Toast.makeText(EditUser.this, "Changes Saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditUser.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserData(SharedPreferences sharedPreferences, EditText firstNameEditText, EditText lastNameEditText,
                              EditText emailEditText, EditText phoneEditText, EditText passwordEditText) {
        // טעינת המידע המוקדם מתוך ה-SharedPreferences
        firstNameEditText.setText(sharedPreferences.getString("FirstName", ""));
        lastNameEditText.setText(sharedPreferences.getString("LastName", ""));
        emailEditText.setText(sharedPreferences.getString("Email", ""));
        phoneEditText.setText(sharedPreferences.getString("Phone", ""));
        passwordEditText.setText(sharedPreferences.getString("Password", ""));
    }

    private boolean isValidInput(String firstName, String lastName, String email, String phone, String password) {
        // וידוא שכל השדות מלאים
        return !firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !password.isEmpty();
    }
}
