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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noam.noamproject1.R;
import com.noam.noamproject1.adapters.AttractionAdapter;
import com.noam.noamproject1.models.Attraction;
import com.noam.noamproject1.services.DatabaseService;
import com.noam.noamproject1.services.WeatherApiService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class AttractionListActivity extends AppCompatActivity {

    // רכיבים גרפיים במסך
    private RecyclerView rvAttractions;  // רשימת אטרקציות להצגה
    private AttractionAdapter attractionAdapter;  // המתאם בין הנתונים לרשימה
    private EditText etSearchAttraction;  // שדה חיפוש למקומות
    private Button btnGoBack;  // כפתור חזרה למסך הקודם
    private ImageButton btnViewFavorites;  // כפתור מעבר לרשימת המועדפים

    // נתונים
    private List<Attraction> attractionList = new ArrayList<>();  // רשימה שמוצגת כרגע (אחרי סינון)
    private List<Attraction> fullAttractionList = new ArrayList<>();  // רשימה מלאה של כל האטרקציות
    private Set<String> favoriteAttractions = new HashSet<>();  // סט של מזהי אטרקציות שמועדפות

    // שירותים לקבלת מידע חיצוני
    private DatabaseService databaseService;  // שירות לבקשת רשימת אטרקציות מהמסד
    private SharedPreferences sharedPreferences;  // אחסון מקומי לשמירת מועדפים

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_attractions);  // קובע את התצוגה של המסך (xml)

        // מגדיר שוליים של המסך (כדי שהמסך לא יחתך מתחת לשורת הסטטוס וכפתורי המערכת)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // אתחול השירותים לקבלת מידע
        databaseService = DatabaseService.getInstance();

        // אתחול SharedPreferences לשמירת מועדפים במכשיר
        sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        loadFavorites();  // טוען את המועדפים שנשמרו קודם

        // מחפש את הרכיבים הגרפיים מתוך העיצוב
        etSearchAttraction = findViewById(R.id.etSearchAttraction);
        btnGoBack = findViewById(R.id.btnGoBack);
        btnViewFavorites = findViewById(R.id.btnViewFavorites);
        rvAttractions = findViewById(R.id.rvAttractionDetails);
        rvAttractions.setLayoutManager(new LinearLayoutManager(this));  // מגדיר שהרשימה תהיה אנכית

        // אתחול המתאם (Adapter) שמקשר בין הרשימה לנתונים, עם מאזין לאירועים
        attractionAdapter = new AttractionAdapter(attractionList, new AttractionAdapter.AttractionListener() {
            @Override
            public void onClick(Attraction attraction) {
                // כשלוחצים על אטרקציה - פותחים את המסך של האטרקציה עם פרטים
                Intent intent = new Intent(getApplicationContext(), AttractionActivity.class);
                intent.putExtra("attraction_id", attraction.getId());  // מעבירים את המזהה של האטרקציה
                startActivity(intent);
            }

            @Override
            public void onLongClick(Attraction attraction) {
                // לא משתמשים כרגע
            }

            @Override
            public void onFavoriteClick(Attraction attraction) {
                // כשלוחצים על הכוכב - מוסיפים או מסירים מהמועדפים
                toggleFavorite(attraction);
            }
        }, favoriteAttractions);
        rvAttractions.setAdapter(attractionAdapter);  // מצמידים את המתאם לרשימה

        fetchAttractions();  // מביאים את רשימת האטרקציות מהמסד

        // מאזין לשינויים בשדה החיפוש כדי לסנן את הרשימה בזמן אמת
        etSearchAttraction.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterAttractions(s.toString());  // מסנן לפי הטקסט שהוקלד
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // כפתור חזרה - סוגר את המסך הנוכחי
        btnGoBack.setOnClickListener(v -> finish());

        // כפתור הצגת המועדפים - פותח את המסך שמציג רק את המועדפים
        btnViewFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(AttractionListActivity.this, MyFavoritesActivity.class);
            startActivity(intent);
        });
    }

    // מביא את כל האטרקציות מהמסד הנתונים
    private void fetchAttractions() {
        databaseService.getAttractionList(new DatabaseService.DatabaseCallback<List<Attraction>>() {
            @Override
            public void onCompleted(List<Attraction> attractions) {
                fullAttractionList.clear();
                fullAttractionList.addAll(attractions);  // מעדכן את הרשימה המלאה

                // עבור כל אטרקציה מביא את מזג האוויר שלה
                for (Attraction attraction : fullAttractionList) {
                    fetchWeatherForAttraction(attraction);
                }

                // מעדכן את הרשימה שמוצגת לפי הטקסט בחיפוש (אם יש)
                filterAttractions(etSearchAttraction.getText().toString());
            }

            @Override
            public void onFailed(Exception e) {
                // אם יש תקלה - מציג הודעת שגיאה בקונסול ובהודעה למשתמש
                Log.e("ShowAttractionsActivity", "Error fetching attractions: ", e);
                Toast.makeText(getApplicationContext(), "Failed to load attractions.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // מביא את מזג האוויר לאטרקציה מסוימת ומעדכן את הטמפרטורה ברשימה
    private void fetchWeatherForAttraction(Attraction attraction) {
        attraction.getWeatherTemp(this, new Consumer<Double>() {
            @Override
            public void accept(Double temp) {
                runOnUiThread(() -> {
                    // אם לא מצליח לקבל טמפרטורה, מציג 25 כברירת מחדל
                    attraction.setTemp(String.format("טמפרטורה: %.1f°C", temp == null ? 25 : temp));
                    attractionAdapter.notifyDataSetChanged();  // מעדכן את הרשימה שהטמפרטורה התעדכנה
                });
            }
        });
    }

    // מסנן את הרשימה לפי טקסט חיפוש
    private void filterAttractions(String query) {
        attractionList.clear();  // מנקה את הרשימה המוצגת
        if (query.isEmpty()) {
            attractionList.addAll(fullAttractionList);  // אם אין חיפוש - מציג הכל
        } else {
            // מוסיף רק את האטרקציות שהשם שלהן מכיל את הטקסט שהוקלד
            for (Attraction attraction : fullAttractionList) {
                if (attraction.getName().toLowerCase().contains(query.toLowerCase())) {
                    attractionList.add(attraction);
                }
            }
        }
        attractionAdapter.notifyDataSetChanged();  // מעדכן את הרשימה
    }

    // מוסיף או מסיר אטרקציה מהמועדפים ושומר את השינויים
    private void toggleFavorite(Attraction attraction) {
        String attractionId = attraction.getId();
        if (favoriteAttractions.contains(attractionId)) {
            favoriteAttractions.remove(attractionId);
            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
        } else {
            favoriteAttractions.add(attractionId);
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
        }
        saveFavorites();  // שומר את המועדפים לאחר השינוי
        attractionAdapter.notifyDataSetChanged();  // מעדכן את הרשימה
    }

    // טוען את רשימת המועדפים מהאחסון המקומי
    private void loadFavorites() {
        favoriteAttractions = new HashSet<>(sharedPreferences.getStringSet("favoriteAttractions", new HashSet<>()));
    }

    // שומר את רשימת המועדפים באחסון המקומי
    private void saveFavorites() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("favoriteAttractions", new HashSet<>(favoriteAttractions));
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // כאן אפשר לשחרר משאבים אם צריך (כרגע אין צורך)
    }
}
