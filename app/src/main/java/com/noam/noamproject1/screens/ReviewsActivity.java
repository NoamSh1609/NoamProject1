package com.noam.noamproject1.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noam.noamproject1.R;
import com.noam.noamproject1.adapters.CommentAdapter;
import com.noam.noamproject1.models.Attraction;
import com.noam.noamproject1.models.Comment;
import com.noam.noamproject1.models.Review;
import com.noam.noamproject1.models.User;
import com.noam.noamproject1.services.AuthenticationService;
import com.noam.noamproject1.services.DatabaseService;

import java.util.Date;
import java.util.List;

public class ReviewsActivity extends AppCompatActivity {

    private EditText reviewEditText;
    private RatingBar ratingBar;
    private Button submitReviewButton;
    private String attractionId;
    private RecyclerView recyclerView;

    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        // קבלת ה-ID של האטרקציה (נניח שה-ID הזה מועבר דרך Intent)
        attractionId = getIntent().getStringExtra("ATTRACTION_ID");

        // אתחול רכיבי ה-UI
        reviewEditText = findViewById(R.id.reviewEditText);
        ratingBar = findViewById(R.id.ratingBar);
        submitReviewButton = findViewById(R.id.submitReviewButton);
        recyclerView = findViewById(R.id.rv_comments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        commentAdapter = new CommentAdapter();
        recyclerView.setAdapter(commentAdapter);

        loadComments();


        // טיפול בלחיצה על כפתור השליחה
        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reviewText = reviewEditText.getText().toString().trim();
                float rating = ratingBar.getRating();

                if (reviewText.isEmpty()) {
                    Toast.makeText(ReviewsActivity.this, "אנא כתוב חוות דעת", Toast.LENGTH_SHORT).show();
                    return;
                }
                // שמירה ב-DatabaseService
                String userId = AuthenticationService.getInstance().getCurrentUserId();
                Comment comment = new Comment(userId, reviewText, rating);
                DatabaseService.getInstance().writeNewComment(attractionId, comment, new DatabaseService.DatabaseCallback<Attraction>() {
                    @Override
                    public void onCompleted(Attraction attraction) {
                        Toast.makeText(ReviewsActivity.this, "חוות דעת נשלחה בהצלחה", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Toast.makeText(ReviewsActivity.this, "הייתה בעיה בשמירת חוות הדעת", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }


    public void loadComments() {
        DatabaseService.getInstance().getAttraction(attractionId, new DatabaseService.DatabaseCallback<Attraction>() {
            @Override
            public void onCompleted(Attraction attraction) {
                commentAdapter.setCommentList(attraction.getComments());
            }

            @Override
            public void onFailed(Exception e) {

            }
        });

        DatabaseService.getInstance().getUserList(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> object) {
                commentAdapter.setUsers(object);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }
}
