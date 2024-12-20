package com.example.amitappfit.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amitappfit.R;
import com.example.amitappfit.adapters.ClosetAdapter;
import com.example.amitappfit.model.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class MyClosetActivity extends AppCompatActivity {

    private Button btnAddItem, btnCreateLook;
    private Spinner spinnerCategories; // Spinner לבחירת קטגוריה
    private RecyclerView rvClosetItems; // RecyclerView להצגת הפריטים
    private ClosetAdapter adapter; // מחלקת האדפטר
    private SharedPreferencesManager sharedPreferencesManager; // מנהל SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_closet); // מחבר את ה-XML למחלקה

        // אתחול רכיבים מה-XML
        btnAddItem = findViewById(R.id.btnAddItem);
        spinnerCategories = findViewById(R.id.spinnerCategories);
        rvClosetItems = findViewById(R.id.rvClosetItems);
        btnCreateLook = findViewById(R.id.btnCreateLook);

        // אתחול SharedPreferencesManager
        sharedPreferencesManager = new SharedPreferencesManager(this);

        // הגדרת RecyclerView
        rvClosetItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ClosetAdapter(new ArrayList<>());
        rvClosetItems.setAdapter(adapter);

        // הגדרת Spinner עם קטגוריות
        setupCategorySpinner();

        // טעינת הפריטים מה-SharedPreferences
        loadItems();

        // לחיצה על כפתור הוספת פריט
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ניווט לעמוד הוספת פריט
                Intent intent = new Intent(MyClosetActivity.this, addItem.class);
                startActivity(intent);
            }
        });

        // לחיצה על כפתור יצירת לוק
        btnCreateLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ניווט לעמוד יצירת לוק
                Intent intent = new Intent(MyClosetActivity.this, CreateLook.class);
                startActivity(intent);
            }
        });

        // פעולה בעת שינוי ב-Spinner של הקטגוריה
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filterItemsByCategory();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // לא נדרש לבצע שום פעולה אם לא נבחרה שום קטגוריה
            }
        });
    }

    // אתחול Spinner עם קטגוריות
    private void setupCategorySpinner() {
        List<String> categories = new ArrayList<>();
        categories.add("All Items");
        categories.add("Tops");
        categories.add("Bottoms");
        categories.add("Shoes");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(adapter);
    }

    // טעינת הפריטים מתוך SharedPreferences
    private void loadItems() {
        List<String> items = sharedPreferencesManager.getItems();
        adapter.updateItems(items); // מעדכן את האדפטר עם הפריטים
    }

    // סינון פריטים לפי קטגוריה
    private void filterItemsByCategory() {
        String selectedCategory = spinnerCategories.getSelectedItem().toString();
        List<String> allItems = sharedPreferencesManager.getItems();
        List<String> filteredItems = new ArrayList<>();

        // אם נבחרה קטגוריית "All Items", הצג את כל הפריטים
        if (selectedCategory.equals("All Items")) {
            filteredItems.addAll(allItems);
        } else {
            // סינון פריטים לפי קטגוריה
            for (String item : allItems) {
                if (item.contains(selectedCategory)) {
                    filteredItems.add(item);
                }
            }
        }

        // עדכון RecyclerView עם הפריטים המסוננים
        adapter.updateItems(filteredItems);
    }
}
