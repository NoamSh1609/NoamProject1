package com.noam.noamproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.noam.noamproject1.R;
import com.noam.noamproject1.services.AuthenticationService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TheFirstViewOfTheApp extends AppCompatActivity implements View.OnClickListener {
    private Button btnform, btnLoginMain, btnWebsiteR,btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_the_first_view_of_the_app);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (AuthenticationService.getInstance().isUserSignedIn()) {
            Intent intent = new Intent(this, After_Login.class);
            startActivity(intent);
            finish();
            return;
        }

        initViews();

    }

    private void initViews() {
        btnform = findViewById(R.id.btnForm);
        btnAbout= findViewById(R.id.btnAbout);
        btnLoginMain = findViewById(R.id.btnLoginMain);
        btnWebsiteR = findViewById(R.id.btnWebsiteReview);

        // חיבור הכפתורים למאזין קליקים
        btnform.setOnClickListener(this);
        btnLoginMain.setOnClickListener(this);
        btnWebsiteR.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnform) {
            startActivity(new Intent(this, Register.class));
        } else if (view == btnLoginMain) {
            startActivity(new Intent(this, Login.class));
        } else if (view == btnWebsiteR) {
            startActivity(new Intent(this, WebsiteReview.class));
        }
        else if (view == btnAbout) {
            startActivity(new Intent(this, wondermate.class));
        }
    }
}
