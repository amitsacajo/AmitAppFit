package com.example.amitappfit.screens;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.amitappfit.R;
import com.example.amitappfit.adapters.ImageSourceAdapter;
import com.example.amitappfit.model.SharedPreferencesManager;
import com.example.amitappfit.util.ImageUtil;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class addItem extends AppCompatActivity implements View.OnClickListener {

    private EditText etItemName; // שדה להזנת שם הפריט
    private Spinner spinnerCategory; // Spinner לקטגוריות
    private Button btnSaveItem; // כפתור לשמירת הפריט
    private ImageView ivPreview; // תצוגת מקדימה של התמונה
    private SharedPreferencesManager sharedPreferencesManager; // מנהל SharedPreferences

    /// Activity result launcher for selecting image from gallery
    private ActivityResultLauncher<Intent> selectImageLauncher;
    /// Activity result launcher for capturing image from camera
    private ActivityResultLauncher<Intent> captureImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item); // מחבר את ה-XML למחלקה

        ImageUtil.requestPermission(this);

        // אתחול רכיבים מה-XML
        etItemName = findViewById(R.id.etItemName);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSaveItem = findViewById(R.id.btnSaveItem);
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
        ivPreview.setOnClickListener(this);



        /// register the activity result launcher for selecting image from gallery
        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        ivPreview.setImageURI(selectedImage);
                        /// set the tag for the image view to null
                        ivPreview.setTag(null);
                    }
                });

        /// register the activity result launcher for capturing image from camera
        captureImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        ivPreview.setImageBitmap(bitmap);
                        /// set the tag for the image view to null
                        ivPreview.setTag(null);
                    }
                });
    }

    /// select image from gallery
    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectImageLauncher.launch(intent);
    }

    /// capture image from camera
    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImageLauncher.launch(takePictureIntent);
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

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image Source");

        final ArrayList<Map.Entry<String, Integer>> options = new ArrayList<>();
        options.add(new AbstractMap.SimpleEntry<>("Gallery", R.drawable.gallery_thumbnail));
        options.add(new AbstractMap.SimpleEntry<>("Camera", R.drawable.photo_camera));

        ImageSourceAdapter adapter = new ImageSourceAdapter(getApplicationContext(), options);

        builder.setAdapter(adapter, (DialogInterface dialog, int index) -> {
            if (index == 0) {
                selectImageFromGallery();
            } else if (index == 1) {
                captureImageFromCamera();
            }
        });

        builder.show();
    }
}