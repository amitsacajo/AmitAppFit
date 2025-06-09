package com.example.amitappfit.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.amitappfit.R;
import com.example.amitappfit.adapters.SpinnerItemAdapter;
import com.example.amitappfit.model.Item;
import com.example.amitappfit.model.Look;
import com.example.amitappfit.services.AuthenticationService;
import com.example.amitappfit.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class CreateLook extends AppCompatActivity {

    private EditText etLookName;
    private Spinner spinnerTops, spinnerBottoms, spinnerShoes;
    private Button btnSaveLook;
    DatabaseService databaseService;

    List<Item> allItems = new ArrayList<>();

    // רשימות פריטים מסוננות לפי קטגוריה
    private List<Item> tops = new ArrayList<>();
    private List<Item> bottoms = new ArrayList<>();
    private List<Item> shoes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_look);

        // הגדרת Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseService = DatabaseService.getInstance();

        // אתחול רכיבים
        etLookName = findViewById(R.id.etLookName);
        spinnerTops = findViewById(R.id.spinnerTops);
        spinnerBottoms = findViewById(R.id.spinnerBottoms);
        spinnerShoes = findViewById(R.id.spinnerShoes);
        btnSaveLook = findViewById(R.id.btnSaveLook);

        // טעינת כל הפריטים מהמסד
        databaseService.getItemList(AuthenticationService.getInstance().getCurrentUserId(), new DatabaseService.DatabaseCallback<List<Item>>() {
            @Override
            public void onCompleted(List<Item> items) {
                allItems = items;
                setupSpinners();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(CreateLook.this, "Failed to load items.", Toast.LENGTH_SHORT).show();
            }
        });

        // לחיצה על שמירת הלוק
        btnSaveLook.setOnClickListener(v -> saveLook());
    }

    private void setupSpinners() {
        tops = filterItemsByCategory("Tops");
        bottoms = filterItemsByCategory("Bottoms");
        shoes = filterItemsByCategory("Shoes");

        SpinnerItemAdapter topsAdapter = new SpinnerItemAdapter(this, tops);
        SpinnerItemAdapter bottomsAdapter = new SpinnerItemAdapter(this, bottoms);
        SpinnerItemAdapter shoesAdapter = new SpinnerItemAdapter(this, shoes);

        spinnerTops.setAdapter(topsAdapter);
        spinnerBottoms.setAdapter(bottomsAdapter);
        spinnerShoes.setAdapter(shoesAdapter);
    }

    private List<Item> filterItemsByCategory(String category) {
        List<Item> filteredItems = new ArrayList<>();
        for (Item item : allItems) {
            if (item.getCategory().equals(category)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    private void saveLook() {
        String lookName = etLookName.getText().toString().trim();

        if (lookName.isEmpty()) {
            Toast.makeText(this, "Please enter a name for your look", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tops.isEmpty() || bottoms.isEmpty() || shoes.isEmpty()) {
            Toast.makeText(this, "You must have at least one item in each category (Top, Bottom, Shoes) to create a look.", Toast.LENGTH_LONG).show();
            return;
        }

        Item top = (Item) spinnerTops.getSelectedItem();
        Item bottom = (Item) spinnerBottoms.getSelectedItem();
        Item shoesItem = (Item) spinnerShoes.getSelectedItem();

        String id = databaseService.generateNewLookId();

        Look newLook = new Look(
                id,
                lookName,
                top,
                bottom,
                shoesItem,
                AuthenticationService.getInstance().getCurrentUserId()
        );

        databaseService.createNewLook(newLook, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Toast.makeText(getApplicationContext(), "Look \"" + lookName + "\" saved successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CreateLook.this, YourSavedLooks.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to save look. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
