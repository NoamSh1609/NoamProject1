package com.noam.noamproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.noam.noamproject1.R;
import com.noam.noamproject1.services.AuthenticationService;
import com.noam.noamproject1.utils.SharedPreferencesUtil;

public class After_Login extends AppCompatActivity implements View.OnClickListener {

    Button btnAddAttraction, btnAttractionActivity, My_FavAt, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_after_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (!AuthenticationService.getInstance().isUserSignedIn()) {
            Intent intent = new Intent(this, TheFirstViewOfTheApp.class);
            startActivity(intent);
            finish();
            return;
        }

        initViews();
    }

    private void initViews() {
        // Set the custom toolbar as the ActionBar
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        btnLogout = findViewById(R.id.btnLogout);
        btnAttractionActivity = findViewById(R.id.btnAttractionActivity);
        btnAddAttraction = findViewById(R.id.btnAddAttraction);
        My_FavAt = findViewById(R.id.My_FavAt);

        btnAddAttraction.setOnClickListener(this);
        btnAttractionActivity.setOnClickListener(this);
        My_FavAt.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnAttractionActivity) {
            startActivity(new Intent(this, ShowAttractionsActivity.class));
        } else if (view == btnAddAttraction) {
            startActivity(new Intent(this, AddAttraction.class));
        } else if (view == My_FavAt) {
            startActivity(new Intent(this, MyFavoritesActivity.class));
        } else if (view == btnLogout) {
            handleLogout();
        }
    }

    private void handleLogout() {
        AuthenticationService.getInstance().signOut();
        SharedPreferencesUtil.clear(this);
        Intent intent = new Intent(this, TheFirstViewOfTheApp.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // === תפריט שלוש נקודות ===

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // אם הלקוח לחץ על "התנתקות"
        if (id == R.id.action_logout) {
            handleLogout();
            return true;
        }
        // אם הלקוח לחץ על "אודות"
        else if (id == R.id.action_about) {
            startActivity(new Intent(this, wondermate.class));  // אם יש צורך, החלף בשם המתאים

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}