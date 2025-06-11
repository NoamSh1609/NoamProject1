package com.noam.noamproject1.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyFavoritesActivity extends AppCompatActivity {

    // רכיבי UI
    private RecyclerView rvFavorites;
    private Button btnGoBack;

    // Adapter ורשימת האטרקציות המועדפות להצגה
    private AttractionAdapter attractionAdapter;
    private List<Attraction> favoriteAttractionsList = new ArrayList<>();

    // סט של מחרוזות המכילות את ה-IDs של האטרקציות המועדפות
    private Set<String> favoriteAttractions = new HashSet<>();

    // שירות נתונים לשאיבת מידע מהדאטאבייס
    DatabaseService databaseService;

    // לשמירת פרטי מועדפים מקומית במכשיר
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorites);

        // טיפול ב-edge-to-edge padding עבור מערכת ההפעלה
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // אתחול השירות וה-SharedPreferences
        databaseService = DatabaseService.getInstance();
        sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);

        // טען את המועדפים ששמורים בזיכרון המקומי
        loadFavorites();

        // קישור רכיבי ממשק המשתמש
        btnGoBack = findViewById(R.id.btnGoBack);
        rvFavorites = findViewById(R.id.rvFavorites);
        rvFavorites.setLayoutManager(new LinearLayoutManager(this)); // הגדרת LayoutManager עבור ה-RecyclerView

        // יצירת ה-Adapter עם מאזין לפעולות על האטרקציות
        attractionAdapter = new AttractionAdapter(favoriteAttractionsList, new AttractionAdapter.AttractionListener() {
            @Override
            public void onClick(Attraction attraction) {
                // מעבר למסך פרטי האטרקציה בלחיצה רגילה
                Intent intent = new Intent(MyFavoritesActivity.this, AttractionActivity.class);
                intent.putExtra("attraction_id", attraction.getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(Attraction attraction) {
                // לא מנוצל כרגע, אפשר להוסיף פונקציונליות בעת לחיצה ארוכה
            }

            @Override
            public void onFavoriteClick(Attraction attraction) {
                // בעת לחיצה על אייקון מועדפים - הסרה מהרשימה
                removeFavorite(attraction);
            }
        }, favoriteAttractions);

        // הגדרת ה-Adapter ל-RecyclerView
        rvFavorites.setAdapter(attractionAdapter);

        // שליפת האטרקציות המועדפות מהדאטאבייס לצורך הצגה
        fetchFavoriteAttractions();

        // כפתור חזרה לסיום הפעילות הנוכחית
        btnGoBack.setOnClickListener(v -> finish());
    }

    // פונקציה לשליפת כל האטרקציות מהדאטאבייס וסינון רק של אלו שמועדפות
    private void fetchFavoriteAttractions() {
        databaseService.getAttractionList(new DatabaseService.DatabaseCallback<List<Attraction>>() {
            @Override
            public void onCompleted(List<Attraction> attractions) {
                favoriteAttractionsList.clear();
                for (Attraction attraction : attractions) {
                    if (favoriteAttractions.contains(attraction.getId())) {
                        favoriteAttractionsList.add(attraction);
                    }
                }
                // עדכון ה-Adapter עם הרשימה החדשה
                attractionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception e) {
                // טיפול בשגיאה: לוג והודעה למשתמש
                Log.e("MyFavoritesActivity", e.getMessage());
                Toast.makeText(getApplicationContext(), "Failed to load favorite attractions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // טעינת הסט של המועדפים מ-SharedPreferences
    private void loadFavorites() {
        favoriteAttractions = new HashSet<>(sharedPreferences.getStringSet("favoriteAttractions", new HashSet<>()));
    }

    // הסרת אטרקציה מהרשימה המועדפת
    private void removeFavorite(Attraction attraction) {
        if (favoriteAttractions.contains(attraction.getId())) {
            favoriteAttractions.remove(attraction.getId());
            saveFavorites();         // שמירה מעודכנת ב-SharedPreferences
            fetchFavoriteAttractions(); // ריענון הרשימה
            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
        }
    }

    // שמירת הסט המעודכן של המועדפים בזיכרון המקומי
    private void saveFavorites() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("favoriteAttractions", new HashSet<>(favoriteAttractions));
        editor.apply();
    }
}
