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

    private TextView tvLookName, tvTop, tvBottom, tvShoes;
    private ImageView imgTop, imgBottom, imgShoes;
    private Button btnDeleteLook, btnChangeLook;
    DatabaseService databaseService;
    String lookId;
    String userId;
    Look currentLook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_details);

        databaseService = DatabaseService.getInstance();

        // הגדרת TextViews ו-ImageViews
        tvLookName = findViewById(R.id.tvLookName);
        tvTop = findViewById(R.id.tvTop);
        tvBottom = findViewById(R.id.tvBottom);
        tvShoes = findViewById(R.id.tvShoes);
        imgTop = findViewById(R.id.imgTop);
        imgBottom = findViewById(R.id.imgBottom);
        imgShoes = findViewById(R.id.imgShoes);

        btnDeleteLook = findViewById(R.id.btnDeleteLook);
        btnChangeLook = findViewById(R.id.btnChangeLook);

        // קבלת הנתונים מה-Intent
        Intent intent = getIntent();
        lookId = intent.getStringExtra("look_id");
        userId = intent.getStringExtra("look_user_id");

        if (lookId == null || userId == null){
            finish();
            return;
        }

        // הגדרת לחיצה על כפתור מחיקה
        btnDeleteLook.setOnClickListener(v -> deleteLook()); // מחיקת הלוק
    }

    @Override
    protected void onResume() {
        super.onResume();

        // קריאה למסד הנתונים לקבלת פרטי הלוק
        databaseService.getLook(userId, lookId, new DatabaseService.DatabaseCallback<Look>() {
            @Override
            public void onCompleted(Look look) {
                currentLook = look;
                // הצגת הנתונים ב-TextViews
                tvLookName.setText(look.getName());
                if (look.getTop() == null) {
                    tvTop.setVisibility(View.GONE);
                    imgTop.setVisibility(View.GONE);
                } else {
                    tvTop.setText("Top: " + look.getTop().getTitle());
                    imgTop.setImageBitmap(ImageUtil.convertFrom64base(look.getTop().getPicBase64()));
                    tvTop.setVisibility(View.VISIBLE);
                    imgTop.setVisibility(View.VISIBLE);
                }
                if (look.getBottom() == null) {
                    tvBottom.setVisibility(View.GONE);
                    imgBottom.setVisibility(View.GONE);
                } else {
                    tvBottom.setText("Bottom: " + look.getBottom().getTitle());
                    imgBottom.setImageBitmap(ImageUtil.convertFrom64base(look.getBottom().getPicBase64()));
                    tvBottom.setVisibility(View.VISIBLE);
                    imgBottom.setVisibility(View.VISIBLE);
                }
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
                // טיפול בשגיאות
                Toast.makeText(LookDetailsActivity.this, "Failed to load look details.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // פונקציה למחיקת הלוק
    private void deleteLook() {
        databaseService.deleteLook(userId, lookId, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                // הצגת הודעה למשתמש
                Toast.makeText(LookDetailsActivity.this, "Look deleted successfully", Toast.LENGTH_SHORT).show();

                // חזרה למסך הראשי או כל מסך אחר
                Intent intent = new Intent(LookDetailsActivity.this, YourSavedLooks.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(Exception e) {
                // טיפול בשגיאות
            }
        });
    }

    public void onClick(View view) {
        Intent intent = new Intent(LookDetailsActivity.this, EditLook.class);
        intent.putExtra("LOOK_ID", lookId);
        intent.putExtra("LOOK_USER_ID", userId);
        startActivity(intent);


    }
}
