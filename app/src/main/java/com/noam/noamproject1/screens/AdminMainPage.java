package com.noam.noamproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.noam.noamproject1.R;
import com.noam.noamproject1.screens.Admin.AdminShowAttraction;
import com.noam.noamproject1.screens.Admin.AdminShowUser;
import com.noam.noamproject1.services.AuthenticationService;
import com.noam.noamproject1.utils.SharedPreferencesUtil;

import java.text.Normalizer;

public class AdminMainPage extends AppCompatActivity implements View.OnClickListener {

    Button btnShowUser, btnShowAdmin,btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_main_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
    }

    private void initViews() {
        btnShowUser = findViewById(R.id.btnShowUser);
        btnShowAdmin = findViewById(R.id.btnShowAdmin);
        btnLogout = findViewById(R.id.btnLogout);
        btnShowUser.setOnClickListener(this);
        btnShowAdmin.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == btnShowUser) {
            Intent go = new Intent(this, AdminShowUser.class);
            startActivity(go);
        } else if (view == btnShowAdmin) {
            Intent go = new Intent(this, AdminShowAttraction.class);
            startActivity(go);
        }
        else if (view==btnLogout)
        {
            AuthenticationService.getInstance().signOut();
            SharedPreferencesUtil.clear(this);
            Intent intent= new Intent(this, TheFirstViewOfTheApp.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }

    }
}
