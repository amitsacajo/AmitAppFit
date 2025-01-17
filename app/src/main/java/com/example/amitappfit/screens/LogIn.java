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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail, etPassword;
    Button btnLog, btnSignUp;
    String email, pass;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;

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

        initViews();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLog = findViewById(R.id.btnLog);
        btnSignUp = findViewById(R.id.btnSignUp); // כפתור להרשמה
        btnLog.setOnClickListener(this);
        btnSignUp.setOnClickListener(this); // מאזין ללחיצה על כפתור ההרשמה
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLog) {
            email = etEmail.getText().toString();
            pass = etPassword.getText().toString();

            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // התחברות מוצלחת
                                Log.d("TAG", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                if (user != null) {
                                    String userId = user.getUid();  // זיהוי המשתמש באמצעות ה-UID

                                    // כאן אפשר לשלוף או להוסיף נתונים אישיים של המשתמש
                                    myRef.child(userId).get().addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            // נתוני המשתמש נמצאים כאן
                                            Log.d("TAG", "User data: " + task1.getResult().getValue());
                                        } else {
                                            // במקרה של כשלון בשליפת הנתונים
                                            Log.w("TAG", "Failed to read user data", task1.getException());
                                        }
                                    });

                                    // שמירת נתונים של המשתמש לדוגמה, למשל שם:
                                    DatabaseReference userRef = myRef.child(userId);
                                    userRef.child("username").setValue("Amit");  // שמירת שם המשתמש או נתון אחר

                                    // מעבר לעמוד הראשי (MyClosetActivity)
                                    Intent go = new Intent(getApplicationContext(), MyClosetActivity.class);
                                    startActivity(go);
                                    finish(); // סוגר את מסך ההתחברות
                                }
                            } else {
                                // במקרה של כשלון
                                Log.w("TAG", "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else if (v.getId() == R.id.btnSignUp) {
            // מעבר לעמוד ההרשמה
            Intent registerIntent = new Intent(getApplicationContext(), Register.class);
            startActivity(registerIntent);
        }
    }
}
