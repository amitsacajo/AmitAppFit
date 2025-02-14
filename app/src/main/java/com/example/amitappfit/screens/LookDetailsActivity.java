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

        // קריאה למסד הנתונים לקבלת פרטי הלוק
        databaseService.getLook(lookId, new DatabaseService.DatabaseCallback<Look>() {
            @Override
            public void onCompleted(Look look) {
                // הצגת הנתונים ב-TextViews
                tvLookName.setText(look.getName());
                tvTop.setText("Top: " + look.getTop().getTitle());
                tvBottom.setText("Bottom: " + look.getBottom().getTitle());
                tvShoes.setText("Shoes: " + look.getShoes().getTitle());

                // הצגת התמונות ב-ImageViews
                imgTop.setImageBitmap(ImageUtil.convertFrom64base(look.getTop().getPicBase64()));
                imgBottom.setImageBitmap(ImageUtil.convertFrom64base(look.getBottom().getPicBase64()));
                imgShoes.setImageBitmap(ImageUtil.convertFrom64base(look.getShoes().getPicBase64()));
            }

            @Override
            public void onFailed(Exception e) {
                // טיפול בשגיאות
                Toast.makeText(LookDetailsActivity.this, "Failed to load look details.", Toast.LENGTH_SHORT).show();
            }
        });

        // הגדרת לחיצה על כפתור מחיקה
        btnDeleteLook.setOnClickListener(v -> deleteLook(lookId)); // מחיקת הלוק




    }

    // פונקציה למחיקת הלוק
    private void deleteLook(String lookId) {
        databaseService.deleteLook(lookId, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                // הצגת הודעה למשתמש
                Toast.makeText(LookDetailsActivity.this, "Look deleted successfully", Toast.LENGTH_SHORT).show();

                // חזרה למסך הראשי או כל מסך אחר
                Intent intent = new Intent(LookDetailsActivity.this, MyClosetActivity.class);
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
        startActivity(intent);


    }
}
