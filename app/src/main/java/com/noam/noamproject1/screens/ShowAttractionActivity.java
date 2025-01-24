package com.noam.noamproject1.screens;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noam.noamproject1.R;
import com.noam.noamproject1.adapters.AttractionAdapter;
import com.noam.noamproject1.models.Attraction;
import com.noam.noamproject1.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class ShowAttractionActivity extends AppCompatActivity {

    private RecyclerView rvAttractions;
    private AttractionAdapter attractionAdapter;
    private List<Attraction> attractionList = new ArrayList<>();
    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_attractions); // שים לב שה-XML שלך מכיל את ה-RecyclerView

        databaseService = DatabaseService.getInstance();

        // אתחול של ה-RecyclerView
        attractionList = new ArrayList<>();
        attractionAdapter = new AttractionAdapter(attractionList);
        rvAttractions = findViewById(R.id.attractionsRecyclerView); // RecyclerView להצגת האטרקציות
        rvAttractions.setLayoutManager(new LinearLayoutManager(this)); // הגדרת LayoutManager
        rvAttractions.setAdapter(attractionAdapter);

        // קריאה לאטרקציות מ-Firebase
        fetchAttractions();
    }

    
    private void fetchAttractions() {
        databaseService.getAttractionList(new DatabaseService.DatabaseCallback<List<Attraction>>() {
            @Override
            public void onCompleted(List<Attraction> attractions) {
                attractionList.clear();
                attractionList.addAll(attractions);
                attractionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("ShowAttractionActivity", e.getMessage());
                Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
