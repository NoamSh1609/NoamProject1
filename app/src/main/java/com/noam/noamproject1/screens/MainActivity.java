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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnform, btnLoginMain,btnWebsiteR,btnAttractionActivity,btnAddAttraction,btnMangerLoginPage,btnShowUser;

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

    //    if (!AuthenticationService.getInstance().isUserSignedIn()) {
    //        Intent intent = new Intent(this, LandingPage.class);
      //      startActivity(intent);
        //    finish();
        //}

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

        btnAddAttraction.setOnClickListener(this);
        btnform.setOnClickListener(this);
        btnLoginMain.setOnClickListener(this);
        btnWebsiteR.setOnClickListener(this);
        btnAttractionActivity.setOnClickListener(this);
        btnMangerLoginPage.setOnClickListener(this);
        btnShowUser.setOnClickListener(this);

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
            Intent go = new Intent(this, ShowUser.class); // מעבר ל-WebsiteReview
            startActivity(go);

        }





    }






}