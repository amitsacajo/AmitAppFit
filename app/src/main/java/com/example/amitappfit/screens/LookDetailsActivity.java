package com.example.amitappfit.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.amitappfit.R;

public class LookDetailsActivity extends AppCompatActivity {

    private TextView tvLookName, tvTop, tvBottom, tvShoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_details);

        // הגדרת TextViews
        tvLookName = findViewById(R.id.tvLookName);
        tvTop = findViewById(R.id.tvTop);
        tvBottom = findViewById(R.id.tvBottom);
        tvShoes = findViewById(R.id.tvShoes);

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
    }
}
