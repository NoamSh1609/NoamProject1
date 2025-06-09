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

    private RecyclerView rvFavorites;
    private AttractionAdapter attractionAdapter;
    private List<Attraction> favoriteAttractionsList = new ArrayList<>();
    private Set<String> favoriteAttractions = new HashSet<>();
    private Button btnGoBack;
    DatabaseService databaseService;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorites);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseService = DatabaseService.getInstance();
        sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        loadFavorites();

        btnGoBack = findViewById(R.id.btnGoBack);
        rvFavorites = findViewById(R.id.rvFavorites);
        rvFavorites.setLayoutManager(new LinearLayoutManager(this));

        attractionAdapter = new AttractionAdapter(favoriteAttractionsList, new AttractionAdapter.AttractionListener() {
            @Override
            public void onClick(Attraction attraction) {
                Intent intent = new Intent(MyFavoritesActivity.this, AttractionActivity.class);
                intent.putExtra("attraction_id", attraction.getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(Attraction attraction) {

            }

            @Override
            public void onFavoriteClick(Attraction attraction) {
                removeFavorite(attraction);
            }
        }, favoriteAttractions);
        rvFavorites.setAdapter(attractionAdapter);

        fetchFavoriteAttractions();

        btnGoBack.setOnClickListener(v -> finish());
    }

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
                attractionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("MyFavoritesActivity", e.getMessage());
                Toast.makeText(getApplicationContext(), "Failed to load favorite attractions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFavorites() {
        favoriteAttractions = new HashSet<>(sharedPreferences.getStringSet("favoriteAttractions", new HashSet<>()));
    }

    private void removeFavorite(Attraction attraction) {
        if (favoriteAttractions.contains(attraction.getId())) {
            favoriteAttractions.remove(attraction.getId());
            saveFavorites();
            fetchFavoriteAttractions();
            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveFavorites() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("favoriteAttractions", new HashSet<>(favoriteAttractions));
        editor.apply();
    }
}