package com.noam.noamproject1.screens;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.noam.noamproject1.R;

public class WebsiteReview extends AppCompatActivity {

    private EditText reviewEditText;
    private RatingBar ratingBar;
    private Button submitReviewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website_review);

        // אתחול של רכיבי ה-UI
        reviewEditText = findViewById(R.id.reviewEditText);
        ratingBar = findViewById(R.id.ratingBar);
        submitReviewButton = findViewById(R.id.submitReviewButton);

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

                // הוספת לוגיקה לשליחה/שימור של חוות הדעת
                // לדוגמה, אתה יכול לשלוח את המידע לשרת או לשמור את חוות הדעת במבנה מקומי

                Toast.makeText(WebsiteReview.this, "תודה על חוות הדעת שלך!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

