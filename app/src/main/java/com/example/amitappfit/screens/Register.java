package com.example.amitappfit.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.amitappfit.R;
import com.example.amitappfit.model.User;
import com.example.amitappfit.services.AuthenticationService;
import com.example.amitappfit.services.DatabaseService;
import com.example.amitappfit.util.SharedPreferencesUtil;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText etFname, etLname, etPhone, etEmail, etPassword;
    Button btnReg;
    String fName,lName, phone, email, pass;

    DatabaseService databaseService;
    AuthenticationService authenticationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();

        initviews();


    }

    private void initviews() {
        etFname=findViewById(R.id.etFname);
        etLname=findViewById(R.id.etLname);
        etPhone=findViewById(R.id.etPhone);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        btnReg=findViewById(R.id.btnReg);
        btnReg.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        fName = etFname.getText().toString();
        lName = etLname.getText().toString();
        phone = etPhone.getText().toString();
        email = etEmail.getText().toString();
        pass = etPassword.getText().toString();


        //check if registration is valid
        boolean isValid = true;
        if (fName.length() < 2) {

            etFname.setError("שם פרטי קצר מדי");
            isValid = false;
        }

        if (lName.length() < 2) {

            etLname.setError("שם משפחה קצר מדי");
            isValid = false;
        }
        if (phone.length() < 9 || phone.length() > 10) {
            etPhone.setError("מספר הטלפון לא תקין");
            isValid = false;
        }

        if (!email.contains("@")) {
            etEmail.setError("כתובת האימייל לא תקינה");
            isValid = false;
        }
        if (pass.length() < 6) {

            etPassword.setError("הסיסמה קצרה מדי");
            isValid = false;
        }
        if (pass.length() > 20) {

            etPassword.setError("הסיסמה ארוכה מדי");
            isValid = false;
        }

        if (!isValid) {
            return;
        }


        authenticationService.signUp(email, pass, new AuthenticationService.AuthCallback() {
            @Override
            public void onCompleted(String uid) {
                User newUser = new User(uid, fName, lName, phone, email, pass, false);
                databaseService.createNewUser(newUser, new DatabaseService.DatabaseCallback<Void>() {
                    @Override
                    public void onCompleted(Void object) {
                        SharedPreferencesUtil.saveUser(getApplicationContext(), newUser);
                        Intent goLog = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(goLog);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.w("TAG", "createUserWithEmail:failure", e);
                        Toast.makeText(Register.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailed(Exception e) {
                Log.w("TAG", "createUserWithEmail:failure", e);
                Toast.makeText(Register.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}