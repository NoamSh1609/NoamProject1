package com.noam.noamproject1.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.noam.noamproject1.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WebsiteReview extends AppCompatActivity {

    private EditText reviewEditText;
    private RatingBar ratingBar;
    private Button submitReviewButton;
    private TextView reviewsTextView;

    private DatabaseReference databaseRef;
    private ArrayList<String> reviewsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website_review);

        // אתחול חיבור ל-Firebase Database
        databaseRef = FirebaseDatabase.getInstance().getReference("reviews");

        // אתחול רכיבי ה-UI
        reviewEditText = findViewById(R.id.reviewEditText);
        ratingBar = findViewById(R.id.ratingBar);
        submitReviewButton = findViewById(R.id.submitReviewButton);
        reviewsTextView = findViewById(R.id.reviewsTextView);

        // טוען חוות דעת קיימות מה-Database
        loadReviews();

        // פעולה כאשר לוחצים על כפתור שליחת חוות דעת
        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });
    }

    // פונקציה לשליחת חוות דעת
    private void submitReview() {
        String reviewText = reviewEditText.getText().toString().trim();
        float rating = ratingBar.getRating();

        // בדיקות תקינות
        if (reviewText.isEmpty()) {
            Toast.makeText(this, "נא כתוב את חוות הדעת שלך!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (rating == 0) {
            Toast.makeText(this, "נא דרג את האתר!", Toast.LENGTH_SHORT).show();
            return;
        }

        // יצירת מזהה ייחודי לכל חוות דעת
        String reviewId = databaseRef.push().getKey();

        // יצירת Map עבור הנתונים
        Map<String, Object> reviewData = new HashMap<>();
        reviewData.put("review", reviewText);
        reviewData.put("rating", rating);
        reviewData.put("timestamp", System.currentTimeMillis());

        // שמירת חוות הדעת ב-Firebase Database
        if (reviewId != null) {
            databaseRef.child(reviewId).setValue(reviewData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "תודה על חוות הדעת שלך!", Toast.LENGTH_SHORT).show();
                        reviewEditText.setText(""); // ניקוי השדה
                        ratingBar.setRating(0); // איפוס הדירוג
                        loadReviews(); // רענון הביקורות
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "שגיאה בשליחת חוות הדעת!", Toast.LENGTH_SHORT).show());
        }
    }

    // פונקציה לטעינת כל הביקורות מה-Database
    private void loadReviews() {
        databaseRef.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String reviewText = dataSnapshot.child("review").getValue(String.class);
                    Double rating = dataSnapshot.child("rating").getValue(Double.class);

                    if (reviewText != null && rating != null) {
                        reviewsList.add("⭐ " + rating + "\n" + reviewText);
                    }
                }
                displayReviews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                reviewsTextView.setText("שגיאה בטעינת הביקורות");
            }
        });
    }

    // פונקציה לעדכון ה-UI עם כל הביקורות
    private void displayReviews() {
        StringBuilder allReviews = new StringBuilder();
        for (String review : reviewsList) {
            allReviews.append(review).append("\n\n");
        }
        reviewsTextView.setText(allReviews.toString().isEmpty() ? "אין ביקורות עדיין." : allReviews.toString());
    }
}
