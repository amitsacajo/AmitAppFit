package com.example.amitappfit.screens;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.amitappfit.R;
import com.example.amitappfit.model.SharedPreferencesManager;
import com.example.amitappfit.model.Look;

import java.util.ArrayList;
import java.util.List;

public class CreateLook extends AppCompatActivity {

    private EditText etLookName;
    private Spinner spinnerTops, spinnerBottoms, spinnerShoes;
    private Button btnSaveLook;
    private SharedPreferencesManager sharedPreferencesManager;
    private List<String> allItems;

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

        // אתחול רכיבים
        etLookName = findViewById(R.id.etLookName);
        spinnerTops = findViewById(R.id.spinnerTops);
        spinnerBottoms = findViewById(R.id.spinnerBottoms);
        spinnerShoes = findViewById(R.id.spinnerShoes);
        btnSaveLook = findViewById(R.id.btnSaveLook);

        // אתחול SharedPreferencesManager
        sharedPreferencesManager = new SharedPreferencesManager(this);

        // טעינת פריטים מתוך SharedPreferences
        allItems = sharedPreferencesManager.getItems();

        // הגדרת ה-Spinners עם הפריטים
        setupSpinners();

        // שמירת הלוק
        btnSaveLook.setOnClickListener(v -> saveLook());
    }

    private void setupSpinners() {
        List<String> tops = filterItemsByCategory("Tops");
        List<String> bottoms = filterItemsByCategory("Bottoms");
        List<String> shoes = filterItemsByCategory("Shoes");

        ArrayAdapter<String> topsAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, tops);
        ArrayAdapter<String> bottomsAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, bottoms);
        ArrayAdapter<String> shoesAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, shoes);

        topsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bottomsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shoesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerTops.setAdapter(topsAdapter);
        spinnerBottoms.setAdapter(bottomsAdapter);
        spinnerShoes.setAdapter(shoesAdapter);
    }

    private List<String> filterItemsByCategory(String category) {
        List<String> filteredItems = new ArrayList<>();
        for (String item : allItems) {
            if (item.contains(category)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    private void saveLook() {
        String lookName = etLookName.getText().toString();
        if (lookName.isEmpty()) {
            Toast.makeText(this, "Please enter a name for your look", Toast.LENGTH_SHORT).show();
            return;
        }

        // אם לא נוספו פריטים לקטגוריה של חולצות, נציג הודעה למשתמש
        if (spinnerTops.getAdapter() == null || spinnerTops.getAdapter().getCount() == 0) {
            Toast.makeText(this, "Please add items to Tops category first", Toast.LENGTH_SHORT).show();
            return;
        }

        String top = spinnerTops.getSelectedItem().toString();
        String bottom = spinnerBottoms.getSelectedItem().toString();
        String shoes = spinnerShoes.getSelectedItem().toString();

        // יצירת לוק חדש
        Look newLook = new Look(lookName, top, bottom, shoes);

        // שמירת הלוק ב-SharedPreferences
        sharedPreferencesManager.saveLook(lookName, newLook);

        Toast.makeText(this, "Look \"" + lookName + "\" saved successfully!", Toast.LENGTH_SHORT).show();

        // חזרה לעמוד הקודם
        finish();
    }
}
