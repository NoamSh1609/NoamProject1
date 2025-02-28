package com.noam.noamproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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

public class ShowAttractionsActivity extends AppCompatActivity {

    private RecyclerView rvAttractions;
    private AttractionAdapter attractionAdapter;
    // attractionList היא הרשימה המוצגת, fullAttractionList מכילה את כל האטרקציות
    private List<Attraction> attractionList = new ArrayList<>();
    private List<Attraction> fullAttractionList = new ArrayList<>();
    private EditText etSearchAttraction;
    private Button btnGoBack;
    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_attractions);

        databaseService = DatabaseService.getInstance();

        // אתחול רכיבי ה-XML
        etSearchAttraction = findViewById(R.id.etSearchAttraction);
        btnGoBack = findViewById(R.id.btnGoBack);
        rvAttractions = findViewById(R.id.rvAttractionDetails);
        rvAttractions.setLayoutManager(new LinearLayoutManager(this));

        // אתחול ה-Adapter עם הרשימה שתעודכן לפי החיפוש
        attractionAdapter = new AttractionAdapter(attractionList, new AttractionAdapter.AttractionListener() {
            @Override
            public void onClick(Attraction attraction) {
                Intent intent = new Intent(getApplicationContext(), ShowAttraction.class);
                intent.putExtra("attraction_id", attraction.getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(Attraction attraction) {

            }
        });
        rvAttractions.setAdapter(attractionAdapter);

        // קריאה לאטרקציות מ-Firebase
        fetchAttractions();

        // מאזין לשינויי טקסט בשדה החיפוש
        etSearchAttraction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // לא נדרש כאן
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterAttractions(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // לא נדרש כאן
            }
        });

        // דוגמה לטיפול בלחיצה על כפתור "back"
        btnGoBack.setOnClickListener(v -> finish());
    }

    private void fetchAttractions() {
        databaseService.getAttractionList(new DatabaseService.DatabaseCallback<List<Attraction>>() {
            @Override
            public void onCompleted(List<Attraction> attractions) {
                // עדכון הרשימות עם הנתונים החדשים
                fullAttractionList.clear();
                fullAttractionList.addAll(attractions);
                // התחלת סינון עם שאילתת ריקה – מציג את כל האטרקציות
                filterAttractions("");
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("ShowAttractionActivity", e.getMessage());
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // פונקציית סינון לפי שם האטרקציה
    private void filterAttractions(String query) {
        attractionList.clear();
        if (query.isEmpty()) {
            attractionList.addAll(fullAttractionList);
        } else {
            for (Attraction attraction : fullAttractionList) {
                if (attraction.getName().toLowerCase().contains(query.toLowerCase())) {
                    attractionList.add(attraction);
                }
            }
        }
        attractionAdapter.notifyDataSetChanged();
    }
}
