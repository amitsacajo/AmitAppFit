/*
package com.example.amitappfit.screens;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.amitappfit.R;
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
    private List<String> allItems;
    DatabaseService databaseService;

    int selectedButton;

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

        selectedButton = -1;

        databaseService = DatabaseService.getInstance();

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

        /// register the activity result launcher for selecting image from gallery
        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        ImageView iv;
                        if (selectedButton == 1) {
                            iv = ivTop;
                        } else if (selectedButton == 2) {
                            iv = ivBottom;
                        } else if (selectedButton == 3) {
                            iv = ivShoes;
                        } else {
                            return;
                        }

                        Uri selectedImage = result.getData().getData();
                        iv.setImageURI(selectedImage);
                        /// set the tag for the image view to null
                        iv.setTag(null);
                    }
                });

        /// register the activity result launcher for capturing image from camera
        captureImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        ImageView iv;
                        if (selectedButton == 1) {
                            iv = ivTop;
                        } else if (selectedButton == 2) {
                            iv = ivBottom;
                        } else if (selectedButton == 3) {
                            iv = ivShoes;
                        } else {
                            return;
                        }
                        iv.setImageBitmap(bitmap);
                        /// set the tag for the image view to null
                        iv.setTag(null);
                    }
                });

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

        String id = databaseService.generateNewLookId();

        // יצירת לוק חדש
        Look newLook = new Look(id, lookName, new Item(top, ImageUtil.convertTo64Base(ivTop)), new Item(bottom, null), new Item(shoes, null));

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
*/
