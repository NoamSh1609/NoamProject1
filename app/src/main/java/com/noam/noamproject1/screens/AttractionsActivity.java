package com.noam.noamproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noam.noamproject1.R;
import com.noam.noamproject1.models.Attraction;

import java.util.ArrayList;
import java.util.List;

public class AttractionsActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView attractionsRecyclerView;
    private com.noam.noamproject1.screens.AttractionAdapter attractionAdapter;
    private List<Attraction> attractionList;

    Button btnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attractions);

        // אתחול ה-RecyclerView
        attractionsRecyclerView = findViewById(R.id.attractionsRecyclerView);
        attractionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // אתחול הרשימה עם דוגמת נתונים
        attractionList = new ArrayList<>();


        // אתחול ה-Adapter
        attractionAdapter = new com.noam.noamproject1.screens.AttractionAdapter(attractionList);

        // קישור ה-Adapter ל-RecyclerView
        attractionsRecyclerView.setAdapter(attractionAdapter);

        initViews();
    }

    private void initViews() {
        btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnGoBack){
            Intent go = new Intent(this, MainActivity.class);
            startActivity(go);
        }
    }
}
