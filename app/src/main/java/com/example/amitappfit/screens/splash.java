package com.example.amitappfit.screens;

import android.content.Intent;
import android.os.Bundle;
import android.window.SplashScreen;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.amitappfit.R;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        Thread mSplashThread = new Thread(){
            @Override
            public void run(){
                try {
                    synchronized (this) {

                        wait(3000);
                    }

                }
                catch (InterruptedException ex){

                }
                finish();

                Intent intent = new Intent(splash.this, MainActivity.class);
                startActivity(intent);
            }
        };

        mSplashThread.start();






        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}