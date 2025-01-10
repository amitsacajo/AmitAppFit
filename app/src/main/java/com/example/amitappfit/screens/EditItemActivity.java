package com.example.amitappfit.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.amitappfit.R;
import com.example.amitappfit.model.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class EditItemActivity extends AppCompatActivity {

    private EditText etItemName;
    private Spinner spinnerCategory;
    private Button btnSaveChanges;
    private SharedPreferencesManager sharedPreferencesManager;

    private String originalItemData; // הפריט המקורי שנבחר

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etItemName = findViewById(R.id.etItemName);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        sharedPreferencesManager = new SharedPreferencesManager(this);

        // קבלת נתוני הפריט שנבחר
        Intent intent = getIntent();
        originalItemData = intent.getStringExtra("itemData");
        if (originalItemData != null) {
            String[] itemParts = originalItemData.split(" \\(");
            etItemName.setText(itemParts[0]); // שם הפריט
            String category = itemParts[1].replace(")", ""); // קטגוריה
            setupCategorySpinner(category); // הגדרת הקטגוריה הנוכחית ב-Spinner
        }

        // שמירת השינויים
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
    }

    private void setupCategorySpinner(String selectedCategory) {
        List<String> categories = new ArrayList<>();
        categories.add("Tops");
        categories.add("Bottoms");
        categories.add("Shoes");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // הגדרת הקטגוריה הנבחרת
        int position = categories.indexOf(selectedCategory);
        if (position != -1) {
            spinnerCategory.setSelection(position);
        }
    }

    private void saveChanges() {
        String updatedItemName = etItemName.getText().toString().trim();
        String updatedCategory = spinnerCategory.getSelectedItem().toString();

        if (updatedItemName.isEmpty()) {
            Toast.makeText(this, "Please enter an item name", Toast.LENGTH_SHORT).show();
            return;
        }

        String updatedItemData = updatedItemName + " (" + updatedCategory + ")";
        sharedPreferencesManager.updateItem(originalItemData, updatedItemData);

        Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();

        // חזרה למסך הארון
        Intent intent = new Intent(EditItemActivity.this, MyClosetActivity.class);
        startActivity(intent);
        finish();
    }
}
