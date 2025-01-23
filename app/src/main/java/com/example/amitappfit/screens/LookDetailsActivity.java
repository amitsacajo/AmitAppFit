package com.example.amitappfit.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.amitappfit.R;
import com.example.amitappfit.model.Look;
import com.example.amitappfit.model.SharedPreferencesManager;
import com.example.amitappfit.services.DatabaseService;

public class LookDetailsActivity extends AppCompatActivity {

    private TextView tvLookName, tvTop, tvBottom, tvShoes;
    private Button btnDeleteLook;
    private SharedPreferencesManager sharedPreferencesManager;

    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_details);

        databaseService = DatabaseService.getInstance();

        // הגדרת TextViews
        tvLookName = findViewById(R.id.tvLookName);
        tvTop = findViewById(R.id.tvTop);
        tvBottom = findViewById(R.id.tvBottom);
        tvShoes = findViewById(R.id.tvShoes);
        btnDeleteLook = findViewById(R.id.btnDeleteLook);

        // הגדרת SharedPreferencesManager
        sharedPreferencesManager = new SharedPreferencesManager(this);

        // קבלת הנתונים מה-Intent
        Intent intent = getIntent();
        String lookId = intent.getStringExtra("look_id");

        databaseService.getLook(lookId, new DatabaseService.DatabaseCallback<Look>() {
            @Override
            public void onCompleted(Look look) {
                // הצגת הנתונים ב-TextViews
                tvLookName.setText(look.getName());
                tvTop.setText("Top: " + look.getTop());
                tvBottom.setText("Bottom: " + look.getBottom());
                tvShoes.setText("Shoes: " + look.getShoes());
            }

            @Override
            public void onFailed(Exception e) {

            }
        });




        // הגדרת לחיצה על כפתור מחיקה
        btnDeleteLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLook(lookId); // מחיקת הלוק
            }
        });
    }

    // פונקציה למחיקת הלוק
    private void deleteLook(String lookId) {

        databaseService.deleteLook(lookId, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                sharedPreferencesManager.deleteLook(lookId); // מחיקת הלוק מה-SharedPreferences

                // הצגת הודעה למשתמש
                Toast.makeText(LookDetailsActivity.this, "Look deleted successfully", Toast.LENGTH_SHORT).show();

                // חזרה למסך הראשי או כל מסך אחר
                Intent intent = new Intent(LookDetailsActivity.this, MyClosetActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(Exception e) {

            }
        });

    }
}
