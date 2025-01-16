package com.example.amitappfit.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.amitappfit.R;
import com.example.amitappfit.model.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class addItem extends AppCompatActivity {

    private EditText etItemName; // שדה להזנת שם הפריט
    private Spinner spinnerCategory; // Spinner לקטגוריות
    private Button btnSaveItem; // כפתור לשמירת הפריט
    private Button btnUploadImage; // כפתור להעלאת תמונה
    private ImageView ivPreview; // תצוגת מקדימה של התמונה
    private SharedPreferencesManager sharedPreferencesManager; // מנהל SharedPreferences
    private static final int PICK_IMAGE_REQUEST = 1; // מזהה לבחירת תמונה

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item); // מחבר את ה-XML למחלקה

        // אתחול רכיבים מה-XML
        etItemName = findViewById(R.id.etItemName);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSaveItem = findViewById(R.id.btnSaveItem);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        ivPreview = findViewById(R.id.ivPreview);

        // אתחול SharedPreferencesManager
        sharedPreferencesManager = new SharedPreferencesManager(this);

        // הגדרת קטגוריות ל-Spinner
        setupCategorySpinner();

        // לחיצה על כפתור "Save Item"
        btnSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });

        // לחיצה על כפתור "Upload Image"
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });
    }

    // אתחול Spinner עם קטגוריות
    private void setupCategorySpinner() {
        List<String> categories = new ArrayList<>();
        categories.add("Tops");
        categories.add("Bottoms");
        categories.add("Shoes");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    // פתיחת בורר תמונות
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            ivPreview.setImageURI(imageUri);
            Toast.makeText(this, "Image selected successfully", Toast.LENGTH_SHORT).show();
        }
    }

    // שמירת הפריט
    private void saveItem() {
        String itemName = etItemName.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        // בדיקות תקינות
        if (itemName.isEmpty()) {
            Toast.makeText(this, "Please enter item name", Toast.LENGTH_SHORT).show();
            return;
        }

        // שמירת פריט ב-SharedPreferences
        String itemData = itemName + " (" + category + ")";
        sharedPreferencesManager.saveItem(itemData);

        // הודעה למשתמש
        Toast.makeText(this, "Item saved: " + itemData, Toast.LENGTH_SHORT).show();

        // חזרה ל-MyClosetActivity
        Intent intent = new Intent(addItem.this, MyClosetActivity.class);
        startActivity(intent);
        finish(); // לסגור את המסך הנוכחי
    }
}