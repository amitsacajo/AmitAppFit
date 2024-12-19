package com.example.amitappfit.screens;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amitappfit.R;
import com.example.amitappfit.adapters.AddItemAdapter;
import com.example.amitappfit.model.SharedPreferencesManager; // הוספת מחלקת SharedPreferences

import java.util.ArrayList;
import java.util.List;

public class addItem extends AppCompatActivity {

    private List<String> items; // רשימה לפריטים
    private AddItemAdapter itemsAdapter; // אדפטר ל-RecyclerView
    private SharedPreferencesManager sharedPreferencesManager; // יצירת מופע של SharedPreferencesManager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // יצירת מופע של SharedPreferencesManager
        sharedPreferencesManager = new SharedPreferencesManager(this);

        // Spinner לקטגוריות
        Spinner spinnerCategory = findViewById(R.id.spinnerCategories);
        Button btnAddItem = findViewById(R.id.btnAddItem);
        RecyclerView rvClosetItems = findViewById(R.id.rvClosetItems);

        // יצירת רשימה של קטגוריות
        List<String> categories = new ArrayList<>();
        categories.add("Tops");
        categories.add("Bottoms");
        categories.add("Shoes");

        // יצירת אדפטר עבור ה-Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        // שליפת הפריטים מ-SharedPreferences
        items = sharedPreferencesManager.getItems();

        // הגדרת RecyclerView
        itemsAdapter = new AddItemAdapter(items); // שימוש באדפטר החדש
        rvClosetItems.setLayoutManager(new LinearLayoutManager(this));
        rvClosetItems.setAdapter(itemsAdapter);

        // לחיצה על כפתור הוספת פריט
        btnAddItem.setOnClickListener(view -> {
            String selectedCategory = spinnerCategory.getSelectedItem().toString();

            if (!selectedCategory.isEmpty()) {
                // שמירת הפריט ב-SharedPreferences
                sharedPreferencesManager.saveItem(selectedCategory);

                // עדכון רשימת הפריטים ב-RecyclerView
                items = sharedPreferencesManager.getItems();
                itemsAdapter.updateItems(items); // עדכון הפריטים באדפטר
                Toast.makeText(this, "Item added: " + selectedCategory, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
