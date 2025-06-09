package com.example.amitappfit.screens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.amitappfit.R;
import com.example.amitappfit.model.Look;
import com.example.amitappfit.services.DatabaseService;
import com.example.amitappfit.util.ImageUtil;

public class LookDetailsActivity extends AppCompatActivity {

    // הגדרת רכיבי UI להצגת מידע על הלוק
    private TextView tvLookName, tvTop, tvBottom, tvShoes;
    private ImageView imgTop, imgBottom, imgShoes;
    private Button btnDeleteLook, btnChangeLook;

    DatabaseService databaseService; // שירות לגישה למסד נתונים
    String lookId;                   // מזהה הלוק שמוצג
    String userId;                   // מזהה המשתמש
    Look currentLook;                // אובייקט הלוק הנוכחי

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_details);

        // אתחול שירות מסד הנתונים
        databaseService = DatabaseService.getInstance();

        // קישור רכיבי הממשק לפי ה-IDs שלהם בקובץ ה-layout
        tvLookName = findViewById(R.id.tvLookName);
        tvTop = findViewById(R.id.tvTop);
        tvBottom = findViewById(R.id.tvBottom);
        tvShoes = findViewById(R.id.tvShoes);
        imgTop = findViewById(R.id.imgTop);
        imgBottom = findViewById(R.id.imgBottom);
        imgShoes = findViewById(R.id.imgShoes);

        btnDeleteLook = findViewById(R.id.btnDeleteLook);
        btnChangeLook = findViewById(R.id.btnChangeLook);

        // קבלת הנתונים שנשלחו ל-Activity דרך ה-Intent
        Intent intent = getIntent();
        lookId = intent.getStringExtra("look_id");         // מזהה הלוק להצגה
        userId = intent.getStringExtra("look_user_id");    // מזהה המשתמש

        // אם חסר מידע קריטי, סוגרים את המסך
        if (lookId == null || userId == null){
            finish();
            return;
        }

        // הגדרת פעולה לכפתור מחיקה - כאשר לוחצים, קוראים לפונקציה שמוחקת את הלוק
        btnDeleteLook.setOnClickListener(v -> deleteLook());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // טוען את פרטי הלוק מהמסד ומציג אותם במסך
        databaseService.getLook(userId, lookId, new DatabaseService.DatabaseCallback<Look>() {
            @Override
            public void onCompleted(Look look) {
                currentLook = look;

                // מציג את שם הלוק
                tvLookName.setText(look.getName());

                // בדיקה האם פריט העליונה קיים
                if (look.getTop() == null) {
                    tvTop.setVisibility(View.GONE);      // מסתיר טקסט ו-ImageView אם הפריט ריק
                    imgTop.setVisibility(View.GONE);
                } else {
                    tvTop.setText("Top: " + look.getTop().getTitle()); // מציג את שם הפריט
                    imgTop.setImageBitmap(ImageUtil.convertFrom64base(look.getTop().getPicBase64())); // ממיר מ-Base64 לתמונה ומציג
                    tvTop.setVisibility(View.VISIBLE);
                    imgTop.setVisibility(View.VISIBLE);
                }

                // בדיקה האם פריט התחתונה קיים
                if (look.getBottom() == null) {
                    tvBottom.setVisibility(View.GONE);
                    imgBottom.setVisibility(View.GONE);
                } else {
                    tvBottom.setText("Bottom: " + look.getBottom().getTitle());
                    imgBottom.setImageBitmap(ImageUtil.convertFrom64base(look.getBottom().getPicBase64()));
                    tvBottom.setVisibility(View.VISIBLE);
                    imgBottom.setVisibility(View.VISIBLE);
                }

                // בדיקה האם פריט הנעליים קיים
                if (look.getShoes() == null) {
                    tvShoes.setVisibility(View.GONE);
                    imgShoes.setVisibility(View.GONE);
                } else {
                    tvShoes.setText("Shoes: " + look.getShoes().getTitle());
                    imgShoes.setImageBitmap(ImageUtil.convertFrom64base(look.getShoes().getPicBase64()));
                    tvShoes.setVisibility(View.VISIBLE);
                    imgShoes.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailed(Exception e) {
                // במידה ויש שגיאה בטעינת הפרטים - מציג הודעת Toast למשתמש
                Toast.makeText(LookDetailsActivity.this, "Failed to load look details.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // פונקציה שמוחקת את הלוק ממסד הנתונים
    private void deleteLook() {
        databaseService.deleteLook(userId, lookId, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                // מציג הודעה שהלוק נמחק בהצלחה
                Toast.makeText(LookDetailsActivity.this, "Look deleted successfully", Toast.LENGTH_SHORT).show();

                // מפנה את המשתמש חזרה למסך שמציג את כל הלוקים השמורים
                Intent intent = new Intent(LookDetailsActivity.this, YourSavedLooks.class);
                startActivity(intent);
                finish(); // סוגר את המסך הנוכחי
            }

            @Override
            public void onFailed(Exception e) {
                // טיפול בשגיאות (אפשר להוסיף כאן הודעות שגיאה)
            }
        });
    }

    // פונקציה שמטפלת בלחיצה על כפתור "שנה לוק"
    public void onClick(View view) {
        Intent intent = new Intent(LookDetailsActivity.this, EditLook.class);
        intent.putExtra("LOOK_ID", lookId);           // מעבירה את מזהה הלוק לעריכה
        intent.putExtra("LOOK_USER_ID", userId);      // ומזהה המשתמש
        startActivity(intent);                         // פותחת את מסך עריכת הלוק
    }
}
