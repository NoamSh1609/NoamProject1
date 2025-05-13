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

    private TextView tvAttractionName,temp, tvAttractionDetail, tvAttractionCapacity, tvAttractionRating;
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
        tvAttractionRating = findViewById(R.id.tvAttractionRating);
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
                tvAttractionRating.setText("Rating: " + attraction.getRating());

                // המרת התמונה מ-Base64 והצגתה ב-ImageView
                pic.setImageBitmap(ImageUtil.convertFrom64base(attraction.getPic()));
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(ShowAttraction.this, "Failed to fetch attraction details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
