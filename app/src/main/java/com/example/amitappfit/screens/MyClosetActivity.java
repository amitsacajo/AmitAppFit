package com.example.amitappfit.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amitappfit.R;
import com.example.amitappfit.adapters.ClosetAdapter;
import com.example.amitappfit.model.Item;
import com.example.amitappfit.services.AuthenticationService;
import com.example.amitappfit.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

public class MyClosetActivity extends AppCompatActivity {

    private Button btnAddItem, btnCreateLook, btnYourSavedLooks;
    private Spinner spinnerCategories;
    private RecyclerView rvClosetItems;
    private ClosetAdapter adapter;

    List<Item> allItems = new ArrayList<>();
    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_closet);

        // אתחול ה-Toolbar
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);  // הוספתי את השורה הזאת

        databaseService = DatabaseService.getInstance();

        // אתחול רכיבים מה-XML
        btnAddItem = findViewById(R.id.btnAddItem);
        spinnerCategories = findViewById(R.id.spinnerCategories);
        rvClosetItems = findViewById(R.id.rvClosetItems);
        btnCreateLook = findViewById(R.id.btnCreateLook);
        btnYourSavedLooks = findViewById(R.id.btnYourSavedLooks);

        // הגדרת RecyclerView
        rvClosetItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ClosetAdapter(new ArrayList<>(), this);
        rvClosetItems.setAdapter(adapter);

        // הגדרת Spinner עם קטגוריות
        setupCategorySpinner();

        // לחיצה על כפתור הוספת פריט
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyClosetActivity.this, addItem.class);
                startActivity(intent);
            }
        });

        // לחיצה על כפתור יצירת לוק
        btnCreateLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyClosetActivity.this, CreateLook.class);
                startActivity(intent);
            }
        });

        // לחיצה על כפתור הלוקים השמורים
        btnYourSavedLooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyClosetActivity.this, YourSavedLooks.class);
                startActivity(intent);
            }
        });

        // סינון לפי קטגוריה
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filterItemsByCategory();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // כלום
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }

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

    private void loadItems() {
        databaseService.getItemList(AuthenticationService.getInstance().getCurrentUserId(), new DatabaseService.DatabaseCallback<List<Item>>() {
            @Override
            public void onCompleted(List<Item> items) {
                allItems.clear();
                allItems.addAll(items);
                adapter.updateItems(allItems);
            }

            @Override
            public void onFailed(Exception e) {
                // טיפול בשגיאות
                Toast.makeText(MyClosetActivity.this, "Failed to load items: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void filterItemsByCategory() {
        String selectedCategory = spinnerCategories.getSelectedItem().toString();

        List<Item> filteredItems = new ArrayList<>();
        if (selectedCategory.equals("All Items")) {
            filteredItems.addAll(allItems);
        } else {
            for (Item item : allItems) {
                if (item.getCategory().equals(selectedCategory)) {
                    filteredItems.add(item);
                }
            }
        }

        adapter.updateItems(filteredItems);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_more) {
            // פעולה עבור פריט "More"
            Toast.makeText(this, "More options clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.showP) {
            // ניווט לעמוד פרופיל המשתמש
            Intent intent1 = new Intent(this, UserProfileActivity.class);
            startActivity(intent1);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_closet_menu, menu);  // תוודא שהקובץ my_closet_menu.xml קיים
        return true;
    }
}
