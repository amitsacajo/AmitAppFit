
package com.example.amitappfit.screens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.amitappfit.R;
import com.example.amitappfit.adapters.ItemAdapter;
import com.example.amitappfit.adapters.SpinnerItemAdapter;
import com.example.amitappfit.model.Item;
import com.example.amitappfit.model.SharedPreferencesManager;
import com.example.amitappfit.model.Look;
import com.example.amitappfit.services.DatabaseService;
import com.example.amitappfit.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class CreateLook extends AppCompatActivity {

    private EditText etLookName;
    private Spinner spinnerTops, spinnerBottoms, spinnerShoes;
    private Button btnSaveLook;
    private SharedPreferencesManager sharedPreferencesManager;
    DatabaseService databaseService;

    List<Item> allItems = new ArrayList<>();

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

        // אתחול SharedPreferencesManager
        sharedPreferencesManager = new SharedPreferencesManager(this);

        // הגדרת ה-Spinners עם הפריטים
        setupSpinners();

        // שמירת הלוק
        btnSaveLook.setOnClickListener(v -> saveLook());

    }

    private void setupSpinners() {
        List<Item> tops = filterItemsByCategory("Tops");
        List<Item> bottoms = filterItemsByCategory("Bottoms");
        List<Item> shoes = filterItemsByCategory("Shoes");

        SpinnerItemAdapter topsAdapter = new SpinnerItemAdapter(this, android.R.layout.simple_spinner_item, tops);
        SpinnerItemAdapter bottomsAdapter = new SpinnerItemAdapter(this, android.R.layout.simple_spinner_item, bottoms);
        SpinnerItemAdapter shoesAdapter = new SpinnerItemAdapter(this, android.R.layout.simple_spinner_item, shoes);

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

        Item top = (Item) spinnerTops.getSelectedItem();
        Item bottom = (Item) spinnerBottoms.getSelectedItem();
        Item shoes = (Item) spinnerShoes.getSelectedItem();

        String id = databaseService.generateNewLookId();

        // יצירת לוק חדש
        Look newLook = new Look(id, lookName, top, bottom, shoes);

        databaseService.createNewLook(newLook, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                // שמירת הלוק ב-SharedPreferences
                sharedPreferencesManager.saveLook(lookName, newLook);

                Toast.makeText(getApplicationContext(), "Look \"" + lookName + "\" saved successfully!", Toast.LENGTH_SHORT).show();

                // חזרה לעמוד הקודם
                finish();
            }

            @Override
            public void onFailed(Exception e) {

            }
        });


    }
}

