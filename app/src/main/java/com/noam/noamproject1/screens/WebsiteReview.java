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

import java.util.ArrayList;

public class WebsiteReview extends AppCompatActivity {

    private EditText reviewEditText;
    private RatingBar ratingBar;
    private Button submitReviewButton;
    private TextView reviewsTextView;  // TextView חדש להצגת כל הביקורות

    // רשימה לאחסון כל הביקורות שנשלחו
    private ArrayList<String> reviewsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website_review);

        // אתחול של רכיבי ה-UI
        reviewEditText = findViewById(R.id.reviewEditText);
        ratingBar = findViewById(R.id.ratingBar);
        submitReviewButton = findViewById(R.id.submitReviewButton);
        reviewsTextView = findViewById(R.id.reviewsTextView); // אתחול של ה-TextView להצגת כל הביקורות

        // פעולה כשנלחץ על כפתור שליחת חוות דעת
        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // קבלת חוות הדעת והדירוג
                String reviewText = reviewEditText.getText().toString();
                float rating = ratingBar.getRating();

                // בדיקה אם הדירוג לא ריק ושהטקסט לא ריק
                if (reviewText.isEmpty()) {
                    Toast.makeText(WebsiteReview.this, "נא כתוב את חוות הדעת שלך!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (rating == 0) {
                    Toast.makeText(WebsiteReview.this, "נא דרג את האתר!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // הוספת חוות הדעת לרשימה
                String fullReview = "דרוג: " + rating + "\n" + reviewText;
                reviewsList.add(fullReview);

                // עדכון ה-UI עם כל הביקורות
                displayReviews();

                // הצגת הודעה תודה
                Toast.makeText(WebsiteReview.this, "תודה על חוות הדעת שלך!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // פונקציה לעדכון ה-UI עם כל הביקורות
    private void displayReviews() {
        StringBuilder allReviews = new StringBuilder();

        // מבנה כל הביקורות להצגה ב-TextView
        for (String review : reviewsList) {
            allReviews.append(review).append("\n\n");
        }

        // הצגת כל הביקורות ב-TextView
        reviewsTextView.setText(allReviews.toString());
    }
}
