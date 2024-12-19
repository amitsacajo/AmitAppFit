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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText etFname, etLname, etPhone, etEmail, etPassword;
    Button btnReg;
    TextView tvReg;

    String fName,lName, phone, email, pass;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        initviews();

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        mAuth = FirebaseAuth.getInstance();


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
        Boolean isValid = true;
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


            if (isValid == true) {

                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TAG", "createUserWithEmail:success");
                                    FirebaseUser fireuser = mAuth.getCurrentUser();
                                    User newUser = new User(fireuser.getUid(), fName, lName, phone, email, pass);
                                    myRef.child(fireuser.getUid()).setValue(newUser);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();

                                    editor.putString("email", email);
                                    editor.putString("password", pass);

                                    editor.commit();
                                    Intent goLog = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(goLog);


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }


                            }
                        });
            }




        }


    }




