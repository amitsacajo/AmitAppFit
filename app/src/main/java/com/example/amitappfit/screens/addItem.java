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
import com.example.amitappfit.model.Item;
import com.example.amitappfit.services.AuthenticationService;
import com.example.amitappfit.services.DatabaseService;
import com.example.amitappfit.util.ImageUtil;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class addItem extends AppCompatActivity implements View.OnClickListener {

    // שדות למרכיבי הממשק
    private EditText etItemName; // שדה להזנת שם הפריט
    private Spinner spinnerCategory; // תפריט נפתח לבחירת קטגוריה
    private ImageView ivPreview; // תצוגה מקדימה של התמונה שהמשתמש בחר

    // משגרי תוצאות לפעולות של בחירת תמונה מהגלריה או צילום תמונה
    private ActivityResultLauncher<Intent> selectImageLauncher;
    private ActivityResultLauncher<Intent> captureImageLauncher;

    // שירות לגישה למסד הנתונים
    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item); // מחבר את ה-XML למסך הנוכחי

        ImageUtil.requestPermission(this); // בקשת הרשאות גישה למדיה/מצלמה
        databaseService = DatabaseService.getInstance(); // אתחול שירות הנתונים

        // קישור רכיבי ה-XML לשדות בקוד
        etItemName = findViewById(R.id.etItemName);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        ivPreview = findViewById(R.id.ivPreview);
        Button btnSaveItem = findViewById(R.id.btnSaveItem);

        // אתחול הקטגוריות לתפריט הנפתח (Tops, Bottoms, Shoes)
        setupCategorySpinner();

        // הגדרת לחיצה על כפתור "Save Item" לקריאה לפונקציית saveItem
        btnSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });

        // לחיצה על תמונת התצוגה תציג בחירה בין מצלמה לגלריה
        ivPreview.setOnClickListener(this);

        // משגר לבחירת תמונה מהגלריה
        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        ivPreview.setImageURI(selectedImage); // מציג את התמונה שנבחרה
                        ivPreview.setTag(null); // איפוס תג לצורך זיהוי עתידי
                    }
                });

        // משגר לצילום תמונה מהמצלמה
        captureImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        ivPreview.setImageBitmap(bitmap); // מציג את התמונה המצולמת
                        ivPreview.setTag(null); // איפוס תג
                    }
                });
    }

    // פונקציה לפתיחת הגלריה
    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectImageLauncher.launch(intent);
    }

    // פונקציה להפעלת מצלמה
    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImageLauncher.launch(takePictureIntent);
    }

    // אתחול הקטגוריות ב־Spinner (רשימה נפתחת)
    private void setupCategorySpinner() {
        List<String> categories = new ArrayList<>();
        categories.add("Tops");
        categories.add("Bottoms");
        categories.add("Shoes");

        // מתאם פשוט להצגת רשימת מחרוזות בתוך Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    // שמירת פריט למסד הנתונים
    private void saveItem() {
        String itemName = etItemName.getText().toString().trim(); // קבלת שם הפריט
        String category = spinnerCategory.getSelectedItem().toString(); // קבלת הקטגוריה הנבחרת

        // בדיקה אם השם ריק
        if (itemName.isEmpty()) {
            Toast.makeText(this, "Please enter item name", Toast.LENGTH_SHORT).show();
            return;
        }

        // יצירת פריט חדש עם תמונה מקודדת ל-Base64
        String id = databaseService.generateNewItemId();
        Item item = new Item(
                id,
                itemName,
                category,
                ImageUtil.convertTo64Base(ivPreview), // המרה של תמונה לבסיס 64
                AuthenticationService.getInstance().getCurrentUserId() // ID של המשתמש המחובר
        );

        // שמירת הפריט במסד הנתונים
        databaseService.createNewItem(item, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                finish(); // סגירת המסך (חזרה ל־MyClosetActivity)
            }

            @Override
            public void onFailed(Exception e) {
                // טיפול בשגיאה אם נדרש (כאן זה ריק)
            }
        });
    }

    // תיבת דיאלוג לבחירה בין צילום לבין גלריה
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image Source");

        // אפשרויות עם טקסט ואייקון: גלריה או מצלמה
        final ArrayList<Map.Entry<String, Integer>> options = new ArrayList<>();
        options.add(new AbstractMap.SimpleEntry<>("Gallery", R.drawable.gallery_thumbnail));
        options.add(new AbstractMap.SimpleEntry<>("Camera", R.drawable.photo_camera));

        // מתאם מותאם לתיבת הבחירה עם אייקונים
        ImageSourceAdapter adapter = new ImageSourceAdapter(getApplicationContext(), options);

        // הצגת הדיאלוג עם שתי האפשרויות
        builder.setAdapter(adapter, (DialogInterface dialog, int index) -> {
            if (index == 0) {
                selectImageFromGallery(); // בחירת תמונה מהגלריה
            } else if (index == 1) {
                captureImageFromCamera(); // צילום תמונה מהמצלמה
            }
        });

        builder.show(); // הצגת הדיאלוג
    }
}
