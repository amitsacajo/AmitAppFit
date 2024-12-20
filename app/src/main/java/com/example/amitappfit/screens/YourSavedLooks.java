package com.example.amitappfit.screens;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amitappfit.R;
import com.example.amitappfit.adapters.LookAdapter;
import com.example.amitappfit.model.Look;
import com.example.amitappfit.model.SharedPreferencesManager;

import java.util.List;

public class YourSavedLooks extends AppCompatActivity {

    private RecyclerView rvSavedLooks;
    private SharedPreferencesManager sharedPreferencesManager;
    private List<Look> savedLooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_your_saved_looks);

        // הגדרת Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // אתחול רכיבי RecyclerView
        rvSavedLooks = findViewById(R.id.rvSavedLooks);
        sharedPreferencesManager = new SharedPreferencesManager(this);

        // טעינת הלוקים
        savedLooks = sharedPreferencesManager.getSavedLooks();

        // הגדרת RecyclerView
        rvSavedLooks.setLayoutManager(new LinearLayoutManager(this));
        LookAdapter adapter = new LookAdapter(savedLooks);
        rvSavedLooks.setAdapter(adapter);
    }
}
