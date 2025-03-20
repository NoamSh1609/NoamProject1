package com.noam.noamproject1.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.noam.noamproject1.R;

public class ReviewsActivity extends AppCompatActivity {

    private EditText reviewEditText;
    private RatingBar ratingBar;
    private TextView reviewsTextView;
    private Button submitReviewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reviews);

        // הגדרת ה-EdgeToEdge והתאמת Padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // אתחול רכיבי ה-UI
        reviewEditText = findViewById(R.id.reviewEditText);
        ratingBar = findViewById(R.id.ratingBar);
        reviewsTextView = findViewById(R.id.reviewsTextView);
        submitReviewButton = findViewById(R.id.submitReviewButton);

        // טיפול בלחיצה על כפתור השליחה
        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reviewText = reviewEditText.getText().toString().trim();
                float rating = ratingBar.getRating();

                if (reviewText.isEmpty()) {
                    Toast.makeText(ReviewsActivity.this, "אנא כתוב חוות דעת", Toast.LENGTH_SHORT).show();
                } else {
                    // עדכון TextView להצגת חוות הדעת שהתקבלה
                    String reviewDisplay = "חוות דעת: " + reviewText + "\n" + "דירוג: " + rating;
                    reviewsTextView.setText(reviewDisplay);
                    Toast.makeText(ReviewsActivity.this, "חוות דעת נשלחה בהצלחה", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
