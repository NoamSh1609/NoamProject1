package com.noam.noamproject1.screens.Admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noam.noamproject1.R;
import com.noam.noamproject1.adapters.AttractionAdapter;
import com.noam.noamproject1.models.Attraction;
import com.noam.noamproject1.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class AdminShowAttraction extends AppCompatActivity {

    private RecyclerView rvAttractions;
    private AttractionAdapter attractionAdapter;
    // attractionList היא הרשימה המוצגת, fullAttractionList מכילה את כל האטרקציות
    private List<Attraction> attractionList = new ArrayList<>();
    private List<Attraction> fullAttractionList = new ArrayList<>();
    private EditText etSearchAttraction;
    private Button btnGoBack;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // הפעלת מצב EdgeToEdge
        EdgeToEdge.enable(this);
        // שימוש בקובץ ה-XML המיועד למנהלים (activity_admin_show_attraction.xml)
        setContentView(R.layout.activity_admin_show_attraction);
        // טיפול ב-insets כדי להתאים את התצוגה לממשק מלא (full screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // אתחול רכיבי ה-XML
        etSearchAttraction = findViewById(R.id.etSearchAttraction);
        btnGoBack = findViewById(R.id.btnGoBack);
        rvAttractions = findViewById(R.id.rvAttractionDetails);
        rvAttractions.setLayoutManager(new LinearLayoutManager(this));

        // אתחול ה-Adapter עם הרשימה שתעודכן לפי החיפוש
        attractionAdapter = new AttractionAdapter(attractionList);
        rvAttractions.setAdapter(attractionAdapter);

        databaseService = DatabaseService.getInstance();
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

        // טיפול בלחיצה על כפתור "back"
        btnGoBack.setOnClickListener(v -> finish());
    }

    // קריאה לאטרקציות מהמסד נתונים
    private void fetchAttractions() {
        databaseService.getAttractionList(new DatabaseService.DatabaseCallback<List<Attraction>>() {
            @Override
            public void onCompleted(List<Attraction> attractions) {
                fullAttractionList.clear();
                fullAttractionList.addAll(attractions);
                // סינון ראשוני עם שאילתת ריקה – מציג את כל האטרקציות
                filterAttractions("");
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("AdminShowAttraction", e.getMessage());
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
