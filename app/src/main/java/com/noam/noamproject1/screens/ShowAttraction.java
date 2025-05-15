package com.noam.noamproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.noam.noamproject1.R;
import com.noam.noamproject1.models.Attraction;
import com.noam.noamproject1.services.DatabaseService;
import com.noam.noamproject1.utils.ImageUtil;

public class ShowAttraction extends AppCompatActivity {

    private TextView tvAttractionName, tvAttractionDetail, tvAttractionCapacity;
    private ImageView pic;
    private Button commentButton;
    private String attractionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_attraction);



        // אתחול רכיבי ה-UI
        tvAttractionName = findViewById(R.id.tvAttractionName);
        tvAttractionDetail = findViewById(R.id.tvAttractionDetail);
        tvAttractionCapacity = findViewById(R.id.tvAttractionCapacity);
//        tvAttractionRating = findViewById(R.id.tvAttractionRating);
        pic = findViewById(R.id.pic);
        commentButton = findViewById(R.id.button); // ודא שקיימת כפתור עם id="button" ב-XML

        // קבלת ה-ID של האטרקציה שנבחרה
        attractionId = getIntent().getStringExtra("attraction_id");

        if (attractionId != null) {
            fetchAttractionDetails(attractionId);
        } else {
            Toast.makeText(this, "Attraction ID is missing", Toast.LENGTH_SHORT).show();
        }

        // מעבר לעמוד חוות דעת בעת לחיצה על כפתור התגובות
        commentButton.setOnClickListener(v -> {
            Intent intent = new Intent(ShowAttraction.this, ReviewsActivity.class);
            startActivity(intent);
        });
    }

    // שליפת המידע על האטרקציה לפי ה-ID
    private void fetchAttractionDetails(String attractionId) {
        DatabaseService databaseService = DatabaseService.getInstance();
        databaseService.getAttractionDetails(attractionId, new DatabaseService.DatabaseCallback<Attraction>() {
            @Override
            public void onCompleted(Attraction attraction) {
                // עדכון UI עם המידע שהתקבל
                tvAttractionName.setText(attraction.getName());
                tvAttractionDetail.setText(attraction.getDetail());
                tvAttractionCapacity.setText("Capacity: " + attraction.getCapacity());
//                tvAttractionRating.setText("Rating: " + attraction.getRating());

                // המרת התמונה מ-Base64 והצגתה ב-ImageView
                pic.setImageBitmap(ImageUtil.convertFrom64base(attraction.getPic()));
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(ShowAttraction.this, "Failed to fetch attraction details", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void translateCityToEnglishAndFetchTemp(String hebrewCityName) {
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocationName(hebrewCityName, 1);
            if (addresses != null && !addresses.isEmpty()) {
                String englishCityName = addresses.get(0).getLocality();
                if (englishCityName == null || englishCityName.isEmpty()) {
                    // fallback: use address line
                    englishCityName = addresses.get(0).getAddressLine(0);
                }
                if (englishCityName != null) {
                    Log.d("CityTranslation", "Translated to: " + englishCityName);
                    getTemp(englishCityName); // ממשיך לפעולה של קבלת טמפרטורה
                } else {
                    Toast.makeText(this, "לא ניתן לתרגם את העיר", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "עיר לא נמצאה", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "שגיאה בתרגום העיר", Toast.LENGTH_SHORT).show();
        }

    }
    private void getTemp(String hebrewCityName) {
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocationName(hebrewCityName, 1);
            if (addresses != null && !addresses.isEmpty()) {
                String englishCityName = addresses.get(0).getLocality();
                if (englishCityName == null || englishCityName.isEmpty()) {
                    englishCityName = addresses.get(0).getAddressLine(0);
                }

                if (englishCityName != null) {
                    String apiKey = "YOUR_API_KEY"; // ← כאן שים את ה-API key שלך
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" +
                            Uri.encode(englishCityName) +
                            "&units=metric&appid=" + apiKey;

                    RequestQueue queue = Volley.newRequestQueue(this);

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            response -> {
                                try {
                                    JSONObject main = response.getJSONObject("main");
                                    double temp = main.getDouble("temp");
                                    Toast.makeText(this, "הטמפרטורה ב" + englishCityName + ": " + temp + "°C", Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(this, "שגיאה בקריאת הנתונים", Toast.LENGTH_SHORT).show();
                                }
                            },
                            error -> {
                                error.printStackTrace();
                                Toast.makeText(this, "שגיאה בגישה לשרת", Toast.LENGTH_SHORT).show();
                            });

                    queue.add(jsonObjectRequest);
                } else {
                    Toast.makeText(this, "לא ניתן לתרגם את שם העיר", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "עיר לא נמצאה", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "שגיאה בתרגום שם העיר", Toast.LENGTH_SHORT).show();
        }
    }


}
