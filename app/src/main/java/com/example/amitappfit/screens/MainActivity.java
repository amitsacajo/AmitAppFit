package com.example.amitappfit.screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.amitappfit.R;
import com.example.amitappfit.services.AuthenticationService;
import com.example.amitappfit.util.SharedPreferencesUtil;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnTOlog, btnTOreg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        if (AuthenticationService.getInstance().isUserSignedIn()) {
            Intent intent = new Intent(this, MyClosetActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        initViews(); // אתחול הכפתורים וה-Views
    }

    private void initViews() {
        btnTOlog = findViewById(R.id.btnTOlog);
        btnTOreg = findViewById(R.id.btnTOreg);
        btnTOlog.setOnClickListener(this);
        btnTOreg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnTOreg) {
            Intent intentToRegister = new Intent(this, Register.class);
            startActivity(intentToRegister);
        } else if (v == btnTOlog) {
            Intent intentToLogIn = new Intent(this, LogIn.class);
            startActivity(intentToLogIn);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
