package com.example.amitappfit.screens;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amitappfit.R;
import com.example.amitappfit.adapters.LookAdapter;
import com.example.amitappfit.model.Look;
import com.example.amitappfit.services.AuthenticationService;
import com.example.amitappfit.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class YourSavedLooks extends AppCompatActivity {

    private RecyclerView rvSavedLooks;  // רכיב להצגת רשימה (לוקים שמורים)
    private List<Look> savedLooks;       // רשימה של הלוקים השמורים למשתמש

    DatabaseService databaseService;    // שירות עבודה עם מסד הנתונים
    String userId;                      // מזהה המשתמש הנוכחי
    LookAdapter adapter;                // מתאם להצגת הלוקים ברשימה

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_saved_looks); // טוען את ממשק המשתמש מהקובץ XML

        // מקבל את מזהה המשתמש (UID) מהאינטנט שקיבל את המסך, אם לא קיים - משיג את ה-UID של המשתמש הנוכחי מהשירות
        userId = getIntent().getStringExtra("USER_UID");
        if (userId == null || userId.isEmpty()) {
            userId = AuthenticationService.getInstance().getCurrentUserId();
        }

        // אתחול רכיב ה-RecyclerView להצגת הלוקים
        rvSavedLooks = findViewById(R.id.rvSavedLooks);

        databaseService = DatabaseService.getInstance();  // משיג מופע של שירות מסד הנתונים

        savedLooks = new ArrayList<>();                    // אתחול רשימת הלוקים כעת ריקה

        rvSavedLooks.setLayoutManager(new LinearLayoutManager(this)); // הגדרת תצוגה אנכית של הרשימה

        // יצירת המתאם עם הרשימה והגדרת מאזין ללחיצה על כל פריט (כדי לפתוח את פרטי הלוק)
        adapter = new LookAdapter(savedLooks, this::openLookDetails);

        // מחברים את המתאם ל-RecyclerView
        rvSavedLooks.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // בכל פעם שהמסך חוזר להיות פעיל טוען את רשימת הלוקים מהמסד עבור המשתמש
        databaseService.getLookList(userId, new DatabaseService.DatabaseCallback<List<Look>>() {
            @Override
            public void onCompleted(List<Look> looks) {
                savedLooks.clear();       // מנקה את הרשימה המקומית
                savedLooks.addAll(looks); // מוסיף את הלוקים שהתקבלו מהמסד
                adapter.notifyDataSetChanged(); // מעדכן את המתאם להצגה מחודשת של הנתונים
            }

            @Override
            public void onFailed(Exception e) {
                // במקרה של שגיאה בטעינת הלוקים אפשר להוסיף טיפול, כרגע ריק
            }
        });
    }

    // פונקציה שנקראת כשמשתמש לוחץ על לוק מסוים ברשימה
    private void openLookDetails(Look look) {
        Intent intent = new Intent(this, LookDetailsActivity.class); // יוצר כוונה לפתיחת המסך לפרטי הלוק
        intent.putExtra("look_id", look.getId());                     // מעביר את מזהה הלוק ל-Activity החדש
        intent.putExtra("look_user_id", look.getUserId());            // מעביר את מזהה המשתמש של הלוק
        startActivity(intent);                                         // מפעיל את המסך לפרטי הלוק
    }
}

