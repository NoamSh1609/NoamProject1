package com.noam.noamproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.noam.noamproject1.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000; // משך זמן המסך – 2 שניות

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // קישור לתמונה מה-layout
        ImageView logoImage = findViewById(R.id.logoImage);

        // הפעלת אנימציה (fade-in)
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logoImage.startAnimation(fadeIn);

        // מעבר למסך הבית לאחר ההשהיה
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, TheFirstViewOfTheApp.class);
            startActivity(intent);
            finish(); // סוגר את מסך הפתיחה כדי שלא יופיע שוב בלחיצה אחורה
        }, SPLASH_DURATION);
    }
}
