package com.example.amitappfit.screens;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.amitappfit.R;
import com.example.amitappfit.adapters.LookAdapter;
import com.example.amitappfit.model.Look;
import com.example.amitappfit.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class YourSavedLooks extends AppCompatActivity {

    private RecyclerView rvSavedLooks;
    private List<Look> savedLooks;

    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_saved_looks);

        // אתחול רכיבי RecyclerView
        rvSavedLooks = findViewById(R.id.rvSavedLooks);
        databaseService = DatabaseService.getInstance();

        // טעינת הלוקים
        savedLooks = new ArrayList<>();

        // הגדרת RecyclerView עם כפתור ללחיצה על פריט
        rvSavedLooks.setLayoutManager(new LinearLayoutManager(this));
        LookAdapter adapter = new LookAdapter(savedLooks, this::openLookDetails);
        rvSavedLooks.setAdapter(adapter);

        // קריאה למסד הנתונים לקבלת רשימת הלוקים
        databaseService.getLookList(new DatabaseService.DatabaseCallback<List<Look>>() {
            @Override
            public void onCompleted(List<Look> looks) {
                savedLooks.clear();
                savedLooks.addAll(looks);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception e) {
                // טיפול בשגיאות במידת הצורך
            }
        });
    }

    private void openLookDetails(Look look) {
        // פתיחת פרטי הלוק
        Intent intent = new Intent(this, LookDetailsActivity.class);
        intent.putExtra("look_id", look.getId());
        startActivity(intent);
    }
}
