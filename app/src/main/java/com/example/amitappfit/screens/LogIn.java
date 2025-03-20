package com.example.amitappfit.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.amitappfit.R;
import com.example.amitappfit.model.User;
import com.example.amitappfit.services.AuthenticationService;
import com.example.amitappfit.services.DatabaseService;
import com.example.amitappfit.util.SharedPreferencesUtil;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail, etPassword;
    Button btnLog, btnSignUp;
    String email, pass;
    AuthenticationService authenticationService;
    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();
        initViews();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLog = findViewById(R.id.btnLog);
        btnSignUp = findViewById(R.id.btnSignUp); // כפתור להרשמה
        btnLog.setOnClickListener(this);
        btnSignUp.setOnClickListener(this); // מאזין ללחיצה על כפתור ההרשמה
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLog) {
            email = etEmail.getText().toString();
            pass = etPassword.getText().toString();

            authenticationService.signIn(email, pass, new AuthenticationService.AuthCallback() {
                @Override
                public void onCompleted(String uid) {
                    databaseService.getUser(uid, new DatabaseService.DatabaseCallback<User>() {
                        @Override
                        public void onCompleted(User user) {
                            SharedPreferencesUtil.saveUser(getApplicationContext(), user);
                            Intent go = new Intent(getApplicationContext(), MyClosetActivity.class);
                            startActivity(go);
                            finish();
                        }

                        @Override
                        public void onFailed(Exception e) {
                            Log.w("TAG", "getUser:failure", e);
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailed(Exception e) {
                    Log.w("TAG", "signInWithEmail:failure", e);
                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            });


        } else if (v.getId() == R.id.btnSignUp) {
            // מעבר לעמוד ההרשמה
            Intent registerIntent = new Intent(getApplicationContext(), Register.class);
            startActivity(registerIntent);
        }
    }
}
