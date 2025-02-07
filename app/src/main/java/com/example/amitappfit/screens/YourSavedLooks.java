package com.example.amitappfit.screens;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.amitappfit.R;
import com.example.amitappfit.adapters.LookAdapter;
import com.example.amitappfit.model.Look;
import com.example.amitappfit.model.SharedPreferencesManager;
import com.example.amitappfit.services.DatabaseService;

import java.util.List;

public class YourSavedLooks extends AppCompatActivity {

    private RecyclerView rvSavedLooks;
    private SharedPreferencesManager sharedPreferencesManager;
    private List<Look> savedLooks;

    DatabaseService databaseService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_saved_looks);

        // אתחול רכיבי RecyclerView
        rvSavedLooks = findViewById(R.id.rvSavedLooks);
        sharedPreferencesManager = new SharedPreferencesManager(this);

        // טעינת הלוקים
        savedLooks = sharedPreferencesManager.getSavedLooks();

        // הגדרת RecyclerView עם כפתור ללחיצה על פריט
        rvSavedLooks.setLayoutManager(new LinearLayoutManager(this));
        LookAdapter adapter = new LookAdapter(savedLooks, new LookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Look look) {
                // פתיחת עמוד עם פרטי הלוק
                openLookDetails(look);
            }
        });
        rvSavedLooks.setAdapter(adapter);
    }

    private void openLookDetails(Look look) {
        Intent intent = new Intent(this, LookDetailsActivity.class);
        intent.putExtra("look_id", look.getId());
        startActivity(intent);
    }
}
