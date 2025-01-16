package com.example.amitappfit.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.amitappfit.R;
import com.example.amitappfit.model.SharedPreferencesManager;

public class LookDetailsActivity extends AppCompatActivity {

    private TextView tvLookName, tvTop, tvBottom, tvShoes;
    private Button btnDeleteLook;
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_details);

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
        String lookName = intent.getStringExtra("look_name");
        String top = intent.getStringExtra("look_top");
        String bottom = intent.getStringExtra("look_bottom");
        String shoes = intent.getStringExtra("look_shoes");

        // הצגת הנתונים ב-TextViews
        tvLookName.setText(lookName);
        tvTop.setText("Top: " + top);
        tvBottom.setText("Bottom: " + bottom);
        tvShoes.setText("Shoes: " + shoes);

        // הגדרת לחיצה על כפתור מחיקה
        btnDeleteLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLook(lookName); // מחיקת הלוק
            }
        });
    }

    // פונקציה למחיקת הלוק
    private void deleteLook(String lookName) {
        sharedPreferencesManager.deleteLook(lookName); // מחיקת הלוק מה-SharedPreferences

        // הצגת הודעה למשתמש
        Toast.makeText(LookDetailsActivity.this, "Look deleted successfully", Toast.LENGTH_SHORT).show();

        // חזרה למסך הראשי או כל מסך אחר
        Intent intent = new Intent(LookDetailsActivity.this, MyClosetActivity.class);
        startActivity(intent);
        finish();
    }
}
