package com.example.amitappfit.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.amitappfit.R;
import com.example.amitappfit.adapters.SpinnerItemAdapter;
import com.example.amitappfit.model.Item;
import com.example.amitappfit.model.Look;
import com.example.amitappfit.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class EditLook extends AppCompatActivity {

    // רכיבי UI
    private EditText etLookName;
    private Spinner spinnerTops, spinnerBottoms, spinnerShoes;
    private Button btnSaveLook;

    // שירותים ונתונים
    private DatabaseService databaseService;
    private List<Item> allItems = new ArrayList<>();
    private Look currentLook;
    String lookId, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_look);

        // אתחול השירות למסד נתונים
        databaseService = DatabaseService.getInstance();

        // קישור רכיבי ממשק המשתמש לפי מזהה
        etLookName = findViewById(R.id.etLookName);
        spinnerTops = findViewById(R.id.spinnerTops);
        spinnerBottoms = findViewById(R.id.spinnerBottoms);
        spinnerShoes = findViewById(R.id.spinnerShoes);
        btnSaveLook = findViewById(R.id.btnSaveLook);

        // קבלת נתוני הלוק מתוך Intent
        Intent intent = getIntent();
        lookId = intent.getStringExtra("LOOK_ID");           // מזהה הלוק לעריכה
        userId = intent.getStringExtra("LOOK_USER_ID");      // מזהה המשתמש

        // אם חסר מידע – סגור את המסך
        if (lookId == null || userId == null) {
            finish();
            return;
        }

        // טען את נתוני הלוק מהמסד
        loadLookData();

        // הגדרת פעולה ללחיצה על כפתור שמירה
        btnSaveLook.setOnClickListener(v -> saveLookChanges());
    }

    // טוען את פרטי הלוק ממסד הנתונים
    private void loadLookData() {
        databaseService.getLook(userId, lookId, new DatabaseService.DatabaseCallback<Look>() {
            @Override
            public void onCompleted(Look look) {
                currentLook = look;
                etLookName.setText(look.getName());  // מציג את שם הלוק בשדה

                // טען את כל הפריטים של המשתמש
                databaseService.getItemList(look.getUserId(), new DatabaseService.DatabaseCallback<List<Item>>() {
                    @Override
                    public void onCompleted(List<Item> items) {
                        allItems = items;
                        setupSpinners(); // הגדרת התפריטים
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Toast.makeText(EditLook.this, "שגיאה בטעינת פריטים", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(EditLook.this, "שגיאה בטעינת הלוק", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ממיין את הפריטים לפי קטגוריה ומכניס אותם לתוך התפריטים
    private void setupSpinners() {
        List<Item> tops = filterItemsByCategory("Tops");         // פריטי עליונה
        List<Item> bottoms = filterItemsByCategory("Bottoms");   // פריטי תחתונה
        List<Item> shoes = filterItemsByCategory("Shoes");       // נעליים

        // מתאמים לתצוגה בתוך ה-Spinners
        SpinnerItemAdapter topsAdapter = new SpinnerItemAdapter(this, tops);
        SpinnerItemAdapter bottomsAdapter = new SpinnerItemAdapter(this, bottoms);
        SpinnerItemAdapter shoesAdapter = new SpinnerItemAdapter(this, shoes);

        // קישור המתאמים לתפריטים
        spinnerTops.setAdapter(topsAdapter);
        spinnerBottoms.setAdapter(bottomsAdapter);
        spinnerShoes.setAdapter(shoesAdapter);

        // בחירת הפריט הנוכחי שכבר קיים בלוק
        setSpinnerSelection(spinnerTops, tops, currentLook.getTop());
        setSpinnerSelection(spinnerBottoms, bottoms, currentLook.getBottom());
        setSpinnerSelection(spinnerShoes, shoes, currentLook.getShoes());
    }

    // מגדיר את הפריט הנבחר בתפריט כך שיתאים לפריט שכבר קיים בלוק
    private void setSpinnerSelection(Spinner spinner, List<Item> items, Item selectedItem) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(selectedItem.getId())) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    // מחזיר רשימת פריטים לפי קטגוריה מסוימת
    private List<Item> filterItemsByCategory(String category) {
        List<Item> filteredItems = new ArrayList<>();
        for (Item item : allItems) {
            if (item.getCategory().equals(category)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    // שמירת השינויים בלוק
    private void saveLookChanges() {
        if (currentLook == null) return;

        String lookName = etLookName.getText().toString();
        if (lookName.isEmpty()) {
            Toast.makeText(this, "יש להזין שם ללוק", Toast.LENGTH_SHORT).show();
            return;
        }

        // קבלת הפריטים שנבחרו מהתפריטים
        Item top = (Item) spinnerTops.getSelectedItem();
        Item bottom = (Item) spinnerBottoms.getSelectedItem();
        Item shoes = (Item) spinnerShoes.getSelectedItem();

        // עדכון הלוק בפריטים החדשים
        currentLook.setName(lookName);
        currentLook.setTop(top);
        currentLook.setBottom(bottom);
        currentLook.setShoes(shoes);

        // שליחת העדכון למסד הנתונים
        databaseService.updateLook(currentLook, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Toast.makeText(getApplicationContext(), "הלוק עודכן בהצלחה!", Toast.LENGTH_SHORT).show();

                // מעבר חזרה למסך שמציג את כל הלוקים
                Intent intent = new Intent(EditLook.this, YourSavedLooks.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("USER_UID", userId);
                startActivity(intent);
                finish(); // סגור את מסך העריכה
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(EditLook.this, "שגיאה בעדכון הלוק", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // מופעל כשחוזרים לפעילות הזו (לא בשימוש כרגע)
    @Override
    protected void onResume() {
        super.onResume();
    }
}
