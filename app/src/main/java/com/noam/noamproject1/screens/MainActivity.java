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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnform, btnLoginMain,btnWebsiteR,btnAttractionActivity,btnAddAttraction,btnMangerLoginPage,btnShowUser,btnGame,btnShowAdmin;

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

        if (!AuthenticationService.getInstance().isUserSignedIn()) {
            Intent intent = new Intent(this, TheFirstViewOfTheApp.class);
            startActivity(intent);
            finish();
            return;
        }

        initViews();

    }

    private void initViews() {
        btnMangerLoginPage = findViewById(R.id.btnMangerLoginPage);
        btnform = findViewById(R.id.btnForm);
        btnLoginMain = findViewById(R.id.btnLoginMain);
        btnWebsiteR= findViewById(R.id.btnWebsiteReview);
        btnAttractionActivity=findViewById(R.id.btnAttractionActivity);
        btnAddAttraction=findViewById(R.id.btnAddAttraction);
        btnShowUser=findViewById(R.id.btnShowUser);
        btnGame=findViewById(R.id.btnGame);
        btnShowAdmin=findViewById(R.id.btnShowAdmin);
        btnAddAttraction.setOnClickListener(this);
        btnform.setOnClickListener(this);
        btnLoginMain.setOnClickListener(this);
        btnWebsiteR.setOnClickListener(this);
        btnAttractionActivity.setOnClickListener(this);
        btnMangerLoginPage.setOnClickListener(this);
        btnShowUser.setOnClickListener(this);
        btnGame.setOnClickListener(this);
        btnShowAdmin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnform) {
            Intent go = new Intent(this, Register.class);
            startActivity(go);
        } else if (view == btnLoginMain) {
            Intent go = new Intent(this, Login.class);
            startActivity(go);
        } else if (view == btnWebsiteR) {
            Intent go = new Intent(this, WebsiteReview.class); // מעבר ל-WebsiteReview
            startActivity(go);
        }
         else if (view ==btnAttractionActivity ) {
        Intent go = new Intent(this, ShowAttractionActivity.class); // מעבר ל-WebsiteReview
        startActivity(go);
        }
        else if (view ==btnAddAttraction ) {
            Intent go = new Intent(this, AddAttraction.class); // מעבר ל-WebsiteReview
            startActivity(go);
        }
        else if (view ==btnMangerLoginPage ) {
            Intent go = new Intent(this, DeleteUser.class); // מעבר ל-WebsiteReview
            startActivity(go);

        }
        else if (view ==btnShowUser ) {
            Intent go = new Intent(this, AdminShowUser.class); // מעבר ל-WebsiteReview
            startActivity(go);

        }
        else if (view ==btnGame ) {
            Intent go = new Intent(this, SuitcasesAndBombs.class); // מעבר ל-WebsiteReview
            startActivity(go);

        }
        else if (view ==btnShowAdmin ) {
            Intent go = new Intent(this, AdminShowAttraction.class); // מעבר ל-WebsiteReview
            startActivity(go);

        }




    }






}