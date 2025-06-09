package com.example.amitappfit.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amitappfit.R;
import com.example.amitappfit.adapters.ClosetAdapter;
import com.example.amitappfit.model.Item;
import com.example.amitappfit.services.AuthenticationService;
import com.example.amitappfit.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

public class MyClosetActivity extends AppCompatActivity {
    // הגדרת כפתורים, ספינר (תפריט נפתח) וריקלאוויו להצגת פריטים
    private Button btnAddItem, btnCreateLook, btnYourSavedLooks;
    private Spinner spinnerCategories;
    private RecyclerView rvClosetItems;
    private ClosetAdapter adapter;

    List<Item> allItems = new ArrayList<>(); // רשימה של כל הפריטים בארון
    DatabaseService databaseService; // שירות לתקשורת עם מסד הנתונים

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_closet);

        // אתחול ה-Toolbar – פס עליון עם אפשרויות ותפריט
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);  // מחברים את ה-Toolbar ל-Activity

        databaseService = DatabaseService.getInstance(); // משיגים מופע של מסד הנתונים

        // מחברים משתנים לרכיבים ב-XML לפי id
        btnAddItem = findViewById(R.id.btnAddItem);
        spinnerCategories = findViewById(R.id.spinnerCategories);
        rvClosetItems = findViewById(R.id.rvClosetItems);
        btnCreateLook = findViewById(R.id.btnCreateLook);
        btnYourSavedLooks = findViewById(R.id.btnYourSavedLooks);

        // הגדרת ה-RecyclerView להצגת רשימת פריטים בקו אנכי
        rvClosetItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ClosetAdapter(new ArrayList<>(), this); // אתחול המתאם עם רשימה ריקה כרגע
        rvClosetItems.setAdapter(adapter); // מחברים את המתאם לרכיב

        // הגדרת הספינר עם קטגוריות לבחירה
        setupCategorySpinner();

        // הגדרת פעולה בלחיצה על כפתור הוספת פריט - פותח את המסך להוספת פריט חדש
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyClosetActivity.this, addItem.class);
                startActivity(intent);
            }
        });

        // הגדרת פעולה בלחיצה על כפתור יצירת לוק - פותח מסך יצירת לוק חדש
        btnCreateLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyClosetActivity.this, CreateLook.class);
                startActivity(intent);
            }
        });

        // הגדרת פעולה בלחיצה על כפתור הצגת הלוקים השמורים - פותח מסך הלוקים ששמר המשתמש
        btnYourSavedLooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyClosetActivity.this, YourSavedLooks.class);
                startActivity(intent);
            }
        });

        // מאזין לבחירת פריט בספינר - בכל שינוי בבחירה מפעיל סינון פריטים לפי הקטגוריה שנבחרה
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filterItemsByCategory(); // מפעיל סינון
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // לא עושה כלום אם לא נבחר כלום
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItems(); // בכל פעם שהמסך חוזר להיות פעיל טוען את רשימת הפריטים מהמסד
    }

    private void setupCategorySpinner() {
        // מגדיר את רשימת הקטגוריות שתופענה בספינר
        List<String> categories = new ArrayList<>();
        categories.add("All Items"); // כל הפריטים
        categories.add("Tops");      // עליוניות
        categories.add("Bottoms");   // תחתוניות
        categories.add("Shoes");     // נעליים

        // מגדיר את המתאם לספינר עם הרשימה ומגדיר עיצוב ברירת מחדל
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(adapter); // מחבר את המתאם לספינר
    }

    private void loadItems() {
        // מבקש מהמסד לקבל את כל הפריטים של המשתמש הנוכחי
        databaseService.getItemList(AuthenticationService.getInstance().getCurrentUserId(), new DatabaseService.DatabaseCallback<List<Item>>() {
            @Override
            public void onCompleted(List<Item> items) {
                allItems.clear();       // מנקה את הרשימה המקומית
                allItems.addAll(items); // מוסיף את כל הפריטים שהגיעו מהמסד
                adapter.updateItems(allItems); // מעדכן את המתאם ברשימת הפריטים להצגה
            }

            @Override
            public void onFailed(Exception e) {
                // אם טעינת הפריטים נכשלה, מציג הודעת שגיאה למשתמש
                Toast.makeText(MyClosetActivity.this, "Failed to load items: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void filterItemsByCategory() {
        String selectedCategory = spinnerCategories.getSelectedItem().toString(); // מקבל את הקטגוריה שנבחרה בספינר

        List<Item> filteredItems = new ArrayList<>();
        if (selectedCategory.equals("All Items")) {
            filteredItems.addAll(allItems); // אם נבחר "כל הפריטים", מציג את כולם
        } else {
            // סינון הפריטים לפי הקטגוריה שנבחרה
            for (Item item : allItems) {
                if (item.getCategory().equals(selectedCategory)) {
                    filteredItems.add(item);
                }
            }
        }

        adapter.updateItems(filteredItems); // מעדכן את המתאם עם הרשימה המסוננת להצגה
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // טיפול בלחיצות על פריטי תפריט ה-Toolbar
        if (item.getItemId() == R.id.menu_more) {
            // לחיצה על פריט "More" בתפריט - מציג הודעה קצרה
            Toast.makeText(this, "More options clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.showP) {
            // לחיצה על פריט תפריט "showP" - מפעיל ניווט לעמוד פרופיל המשתמש
            Intent intent1 = new Intent(this, UserProfileActivity.class);
            startActivity(intent1);
            return true;
        } else {
            // ברירת מחדל - מפעיל את ההתנהגות הרגילה
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // טוען את קובץ התפריט XML לתוך ה-Toolbar
        getMenuInflater().inflate(R.menu.my_closet_menu, menu);  // ודא שקובץ my_closet_menu.xml נמצא בתיקיית res/menu
        return true; // מציין שהתפריט נטען בהצלחה
    }
}

