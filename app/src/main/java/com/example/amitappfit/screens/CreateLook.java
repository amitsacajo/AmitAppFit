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

    // הגדרת שדות - רכיבי UI ורשימות פריטים
    private EditText etLookName;
    private Spinner spinnerTops, spinnerBottoms, spinnerShoes;
    private Button btnSaveLook;
    DatabaseService databaseService;

    List<Item> allItems = new ArrayList<>(); // כל הפריטים של המשתמש

    // רשימות פריטים מסוננות לפי קטגוריה
    private List<Item> tops = new ArrayList<>();
    private List<Item> bottoms = new ArrayList<>();
    private List<Item> shoes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_look);

        // הגדרת התאמה לקצוות המסך (Edge-to-Edge UI)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // אתחול השירות למסד הנתונים
        databaseService = DatabaseService.getInstance();

        // קישור רכיבי המסך מה-XML
        etLookName = findViewById(R.id.etLookName);
        spinnerTops = findViewById(R.id.spinnerTops);
        spinnerBottoms = findViewById(R.id.spinnerBottoms);
        spinnerShoes = findViewById(R.id.spinnerShoes);
        btnSaveLook = findViewById(R.id.btnSaveLook);

        // טעינת כל הפריטים של המשתמש מהמסד
        databaseService.getItemList(AuthenticationService.getInstance().getCurrentUserId(), new DatabaseService.DatabaseCallback<List<Item>>() {
            @Override
            public void onCompleted(List<Item> items) {
                allItems = items;      // שומר את כל הפריטים שהוחזרו
                setupSpinners();       // הגדרת הספינרים עם הפריטים המסוננים
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(CreateLook.this, "Failed to load items.", Toast.LENGTH_SHORT).show();
            }
        });

        // כאשר לוחצים על כפתור השמירה – מפעיל את saveLook()
        btnSaveLook.setOnClickListener(v -> saveLook());
    }

    // פונקציה שמכינה את הספינרים לפי קטגוריות
    private void setupSpinners() {
        tops = filterItemsByCategory("Tops");
        bottoms = filterItemsByCategory("Bottoms");
        shoes = filterItemsByCategory("Shoes");

        // התאמה של מתאמים (Adapters) כדי להציג את הפריטים בספינרים
        SpinnerItemAdapter topsAdapter = new SpinnerItemAdapter(this, tops);
        SpinnerItemAdapter bottomsAdapter = new SpinnerItemAdapter(this, bottoms);
        SpinnerItemAdapter shoesAdapter = new SpinnerItemAdapter(this, shoes);

        // הגדרת המתאמים לכל ספינר
        spinnerTops.setAdapter(topsAdapter);
        spinnerBottoms.setAdapter(bottomsAdapter);
        spinnerShoes.setAdapter(shoesAdapter);
    }

    // סינון רשימת הפריטים לפי קטגוריה (Tops, Bottoms, Shoes)
    private List<Item> filterItemsByCategory(String category) {
        List<Item> filteredItems = new ArrayList<>();
        for (Item item : allItems) {
            if (item.getCategory().equals(category)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    // שמירת הלוק למסד הנתונים
    private void saveLook() {
        String lookName = etLookName.getText().toString().trim(); // קבלת שם הלוק מהשדה

        // בדיקה אם השם ריק
        if (lookName.isEmpty()) {
            Toast.makeText(this, "Please enter a name for your look", Toast.LENGTH_SHORT).show();
            return;
        }

        // בדיקה אם יש לפחות פריט אחד מכל קטגוריה
        if (tops.isEmpty() || bottoms.isEmpty() || shoes.isEmpty()) {
            Toast.makeText(this, "You must have at least one item in each category (Top, Bottom, Shoes) to create a look.", Toast.LENGTH_LONG).show();
            return;
        }

        // קבלת הפריטים שנבחרו מהספינרים
        Item top = (Item) spinnerTops.getSelectedItem();
        Item bottom = (Item) spinnerBottoms.getSelectedItem();
        Item shoesItem = (Item) spinnerShoes.getSelectedItem();

        // יצירת מזהה חדש ללוק
        String id = databaseService.generateNewLookId();

        // יצירת אובייקט Look חדש
        Look newLook = new Look(
                id,
                lookName,
                top,
                bottom,
                shoesItem,
                AuthenticationService.getInstance().getCurrentUserId() // שמירה על שייכות למשתמש הנוכחי
        );

        // שמירת הלוק למסד
        databaseService.createNewLook(newLook, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Toast.makeText(getApplicationContext(), "Look \"" + lookName + "\" saved successfully!", Toast.LENGTH_SHORT).show();

                // מעבר למסך שמציג את הלוקים שנשמרו
                Intent intent = new Intent(CreateLook.this, YourSavedLooks.class);
                startActivity(intent);
                finish(); // סגירת המסך הנוכחי
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to save look. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
