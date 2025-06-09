package com.example.amitappfit.screens;

import android.app.AlertDialog;
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
import androidx.appcompat.app.AppCompatActivity;

import com.example.amitappfit.R;
import com.example.amitappfit.model.Item;
import com.example.amitappfit.services.DatabaseService;
import com.example.amitappfit.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class EditItemActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etItemName;
    private Spinner spinnerCategory;
    private Button btnSaveChanges;
    private Button btnDeleteItem;
    private ImageView ivPreview;
    DatabaseService databaseService;

    private String userId, itemId;
    private Item currentItem;

    // משמש לבחירת תמונה מהגלריה
    private ActivityResultLauncher<Intent> selectImageLauncher;

    // משמש לצילום תמונה מהמצלמה
    private ActivityResultLauncher<Intent> captureImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // אתחול שירות הנתונים
        databaseService = DatabaseService.getInstance();

        // בקשת הרשאות אם צריך (לגישה לגלריה וכו')
        ImageUtil.requestPermission(this);

        // חיבור בין המשתנים לרכיבים במסך
        etItemName = findViewById(R.id.etItemName);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnDeleteItem = findViewById(R.id.btnDeleteItem);
        ivPreview = findViewById(R.id.ivPreview);

        // קבלת הנתונים מה־Intent שהעביר את הפריט לעריכה
        Intent intent = getIntent();
        userId = intent.getStringExtra("item_user_id");
        itemId = intent.getStringExtra("item_id");

        // אם לא התקבלו נתונים – סיום הפעילות
        if (itemId == null || userId == null) {
            finish();
            return;
        }

        // שליפת הפריט מהמסד והצגתו במסך
        databaseService.getItem(userId, itemId, new DatabaseService.DatabaseCallback<Item>() {
            @Override
            public void onCompleted(Item item) {
                currentItem = item;
                etItemName.setText(item.getTitle()); // הצגת שם הפריט
                setupCategorySpinner(item.getCategory()); // הצגת הקטגוריה
                ivPreview.setImageBitmap(ImageUtil.convertFrom64base(item.getPicBase64())); // הצגת התמונה
            }

            @Override
            public void onFailed(Exception e) {
                // כאן אפשר להוסיף טיפול בשגיאה
            }
        });

        // לחיצה על "שמור שינויים"
        btnSaveChanges.setOnClickListener(v -> saveChanges());

        // לחיצה על "מחק פריט"
        btnDeleteItem.setOnClickListener(v -> deleteItem());

        // לחיצה על התמונה – פתיחת תיבת בחירה בין מצלמה לגלריה
        ivPreview.setOnClickListener(this);

        // אתחול משגרי התמונה (מצלמה וגלריה)
        setupImageLaunchers();
    }

    // אתחול התפריט הנפתח של הקטגוריות
    private void setupCategorySpinner(String selectedCategory) {
        List<String> categories = new ArrayList<>();
        categories.add("Tops");    // חולצות
        categories.add("Bottoms"); // מכנסיים
        categories.add("Shoes");   // נעליים

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // קביעת הקטגוריה הנוכחית
        int position = categories.indexOf(selectedCategory);
        if (position != -1) {
            spinnerCategory.setSelection(position);
        }
    }

    // אתחול של משגרי התמונה (מצלמה וגלריה)
    private void setupImageLaunchers() {
        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData(); // קבלת URI של התמונה
                        ivPreview.setImageURI(selectedImage); // הצגת התמונה במסך
                    }
                });

        captureImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data"); // קבלת תמונה כ־Bitmap
                        ivPreview.setImageBitmap(bitmap); // הצגת התמונה
                    }
                });
    }

    // שמירת השינויים של הפריט
    private void saveChanges() {
        if (currentItem == null) return;

        String updatedItemName = etItemName.getText().toString().trim();
        String updatedCategory = spinnerCategory.getSelectedItem().toString();

        if (updatedItemName.isEmpty()) {
            Toast.makeText(this, "Please enter an item name", Toast.LENGTH_SHORT).show();
            return;
        }

        // עדכון הנתונים באובייקט
        currentItem.setTitle(updatedItemName);
        currentItem.setCategory(updatedCategory);
        currentItem.setPicBase64(ImageUtil.convertTo64Base(ivPreview)); // המרה לתמונה מקודדת

        // שמירת הפריט במסד הנתונים – שים לב! אולי עדיף לקרוא לפונקציית updateItem ולא createNewItem
        databaseService.createNewItem(currentItem, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Toast.makeText(getApplicationContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();

                // מעבר חזרה לארון הבגדים
                Intent intent = new Intent(EditItemActivity.this, MyClosetActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(Exception e) {
                // כאן אפשר להוסיף טיפול בשגיאה
            }
        });
    }

    // מחיקת הפריט מהמסד
    private void deleteItem() {
        if (currentItem == null) return;

        databaseService.deleteItem(currentItem.getUserId(), currentItem.getId(), new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Toast.makeText(getApplicationContext(), "Item deleted successfully", Toast.LENGTH_SHORT).show();

                // מעבר חזרה לארון הבגדים
                Intent intent = new Intent(EditItemActivity.this, MyClosetActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(Exception e) {
                // כאן אפשר להוסיף טיפול בשגיאה
            }
        });
    }

    // טיפול בלחיצה על התמונה – פתיחת חלון לבחירה בין מצלמה לגלריה
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image Source");

        builder.setItems(new String[]{"Gallery", "Camera"}, (dialog, which) -> {
            if (which == 0) {
                selectImageFromGallery();
            } else if (which == 1) {
                captureImageFromCamera();
            }
        });

        builder.show();
    }

    // פתיחת הגלריה לבחירת תמונה
    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectImageLauncher.launch(intent);
    }

    // פתיחת מצלמה לצילום תמונה חדשה
    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImageLauncher.launch(takePictureIntent);
    }
}
