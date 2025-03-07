package com.noam.noamproject1.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

public class ShowAttractionsActivity extends AppCompatActivity {

    private RecyclerView rvAttractions;
    private AttractionAdapter attractionAdapter;
    private List<Attraction> attractionList = new ArrayList<>();
    private List<Attraction> fullAttractionList = new ArrayList<>();
    private Set<String> favoriteAttractions = new HashSet<>();
    private EditText etSearchAttraction;
    private Button btnGoBack;
    private ImageButton btnViewFavorites;
    private DatabaseService databaseService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_attractions);

        databaseService = DatabaseService.getInstance();
        sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        loadFavorites();

        etSearchAttraction = findViewById(R.id.etSearchAttraction);
        btnGoBack = findViewById(R.id.btnGoBack);
        btnViewFavorites = findViewById(R.id.btnViewFavorites);
        rvAttractions = findViewById(R.id.rvAttractionDetails);
        rvAttractions.setLayoutManager(new LinearLayoutManager(this));

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

            @Override
            public void onFavoriteClick(Attraction attraction) {
                toggleFavorite(attraction);
            }
        }, favoriteAttractions);
        rvAttractions.setAdapter(attractionAdapter);

        fetchAttractions();

        etSearchAttraction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterAttractions(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnGoBack.setOnClickListener(v -> finish());

        btnViewFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(ShowAttractionsActivity.this, MyFavoritesActivity.class);
            startActivity(intent);
        });
    }

    private void fetchAttractions() {
        databaseService.getAttractionList(new DatabaseService.DatabaseCallback<List<Attraction>>() {
            @Override
            public void onCompleted(List<Attraction> attractions) {
                fullAttractionList.clear();
                fullAttractionList.addAll(attractions);
                filterAttractions(etSearchAttraction.getText().toString());
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("ShowAttractionsActivity", "Error fetching attractions: ", e);
                Toast.makeText(getApplicationContext(), "Failed to load attractions.", Toast.LENGTH_SHORT).show();
            }
        });
    }

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

    private void toggleFavorite(Attraction attraction) {
        String attractionId = attraction.getId();
        if (favoriteAttractions.contains(attractionId)) {
            favoriteAttractions.remove(attractionId);
            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
        } else {
            favoriteAttractions.add(attractionId);
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
        }
        saveFavorites();
        attractionAdapter.notifyDataSetChanged();
    }

    private void loadFavorites() {
        // TODO load favorites from the DB
        favoriteAttractions = new HashSet<>(sharedPreferences.getStringSet("favoriteAttractions", new HashSet<>()));
    }

    private void saveFavorites() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("favoriteAttractions", new HashSet<>(favoriteAttractions));
        editor.apply();
    }
}
