package com.noam.noamproject1.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.noam.noamproject1.R;
import com.noam.noamproject1.models.Review;
import com.noam.noamproject1.services.DatabaseService;

public class ReviewsActivity extends AppCompatActivity {

    private EditText reviewEditText;
    private RatingBar ratingBar;
    private TextView reviewsTextView;
    private Button submitReviewButton;
    private String attractionId; // ID של האטרקציה שהמשתמש מדרג

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        // אתחול רכיבי ה-UI
        reviewEditText = findViewById(R.id.reviewEditText);
        ratingBar = findViewById(R.id.ratingBar);
        reviewsTextView = findViewById(R.id.reviewsTextView);
        submitReviewButton = findViewById(R.id.submitReviewButton);

        // קבלת ה-ID של האטרקציה (נניח שה-ID הזה מועבר דרך Intent)
        attractionId = getIntent().getStringExtra("ATTRACTION_ID");

        // טיפול בלחיצה על כפתור השליחה
        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reviewText = reviewEditText.getText().toString().trim();
                float rating = ratingBar.getRating();

                if (reviewText.isEmpty()) {
                    Toast.makeText(ReviewsActivity.this, "אנא כתוב חוות דעת", Toast.LENGTH_SHORT).show();
                } else {
                    // שמירה ב-DatabaseService
                    String userId = "user123"; // יש להחליף בערך אמיתי של מזהה המשתמש
                    DatabaseService.getInstance().saveReview(attractionId, userId, reviewText, rating, new DatabaseService.DatabaseCallback<Void>() {
                        @Override
                        public void onCompleted(Void aVoid) {
                            Toast.makeText(ReviewsActivity.this, "חוות דעת נשלחה בהצלחה", Toast.LENGTH_SHORT).show();

                            // עדכון TextView להצגת חוות הדעת שהתקבלה
                            String reviewDisplay = "חוות דעת: " + reviewText + "\n" + "דירוג: " + rating;
                            reviewsTextView.setText(reviewDisplay);
                        }

                        @Override
                        public void onFailed(Exception e) {
                            Toast.makeText(ReviewsActivity.this, "הייתה בעיה בשמירת חוות הדעת", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
