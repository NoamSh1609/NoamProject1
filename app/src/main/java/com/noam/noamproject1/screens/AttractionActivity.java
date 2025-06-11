package com.noam.noamproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.noam.noamproject1.R;
import com.noam.noamproject1.models.Attraction;
import com.noam.noamproject1.services.DatabaseService;
import com.noam.noamproject1.utils.ImageUtil;

public class AttractionActivity extends AppCompatActivity {

    private TextView tvAttractionName, tvAttractionDetail, tvAttractionCapacity;
    private ImageView pic;
    private Button commentButton;
    private String attractionId;
    private Toolbar toolbar;

    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_attraction);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseService = DatabaseService.getInstance();

        // אתחול רכיבי ה-UI
        initializeViews();
        setupToolbar();

        // קבלת ה-ID של האטרקציה שנבחרה
        attractionId = getIntent().getStringExtra("attraction_id");

        if (attractionId != null) {
            fetchAttractionDetails(attractionId);
        } else {
            Toast.makeText(this, "Attraction ID is missing", Toast.LENGTH_SHORT).show();
            finish();
        }

        // מעבר לעמוד חוות דעת בעת לחיצה על כפתור התגובות
        commentButton.setOnClickListener(v -> {
            Intent intent = new Intent(AttractionActivity.this, ReviewsActivity.class);
            intent.putExtra("ATTRACTION_ID", attractionId);
            startActivity(intent);
        });

        updateVisitorsCount();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        tvAttractionName = findViewById(R.id.tvAttractionName);
        tvAttractionDetail = findViewById(R.id.tvAttractionDetail);
        tvAttractionCapacity = findViewById(R.id.tvAttractionCapacity);
        pic = findViewById(R.id.pic);
        commentButton = findViewById(R.id.button);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    // שליפת המידע על האטרקציה לפי ה-ID
    private void fetchAttractionDetails(String attractionId) {
        databaseService.getAttractionDetails(attractionId, new DatabaseService.DatabaseCallback<Attraction>() {
            @Override
            public void onCompleted(Attraction attraction) {
                updateUI(attraction);
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(AttractionActivity.this, "Failed to fetch attraction details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(Attraction attraction) {
        // עדכון כותרת ה-Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(attraction.getName());
        }

        // עדכון UI עם המידע שהתקבל
        tvAttractionName.setText(attraction.getName());
        tvAttractionDetail.setText(attraction.getDetail());
        tvAttractionCapacity.setText(String.format("%d מבקרים", attraction.getCurrentVisitors()));

        // המרת התמונה מ-Base64 והצגתה ב-ImageView
        pic.setImageBitmap(ImageUtil.convertFrom64base(attraction.getPic()));
    }

    public void updateVisitorsCount() {
        databaseService.increaseAttractionVisitors(attractionId, new DatabaseService.DatabaseCallback<Attraction>() {
            @Override
            public void onCompleted(Attraction attraction) {
//                updateUI(attraction);
            }

            @Override
            public void onFailed(Exception e) {
                e.printStackTrace();
                Log.d("!!!!!!!!!!!!!!!!!!!!!!!!!!!", e.toString());
            }
        });
    }

    // הסבר פשוט למפתח:
    // הקובץ הזה הוא מסך שמראה את כל הפרטים של אטרקציה מסוימת (שם, תיאור, תמונה, כמות מבקרים וכו').
    // יש בו גם כפתור למעבר למסך חוות דעת של המשתמשים.
    // כשפותחים את המסך, מקבלים מזהה של אטרקציה מהמסך הקודם, ומביאים את המידע מבסיס הנתונים.
    // ברגע שמישהו נכנס לאטרקציה, נספר עוד מבקר בבסיס הנתונים.
}
