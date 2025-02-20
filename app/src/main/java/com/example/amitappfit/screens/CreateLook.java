
package com.example.amitappfit.screens;

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
import com.example.amitappfit.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class CreateLook extends AppCompatActivity {

    private EditText etLookName;
    private Spinner spinnerTops, spinnerBottoms, spinnerShoes;
    private Button btnSaveLook;
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

        // הגדרת ה-Spinners עם הפריטים

        databaseService.getItemList(new DatabaseService.DatabaseCallback<List<Item>>() {
            @Override
            public void onCompleted(List<Item> items) {
                allItems = items;
                setupSpinners();
            }

            @Override
            public void onFailed(Exception e) {

            }
        });



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

