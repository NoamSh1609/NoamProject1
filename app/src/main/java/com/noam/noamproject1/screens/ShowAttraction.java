package com.noam.noamproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.noam.noamproject1.R;
import com.noam.noamproject1.models.Attraction;
import com.noam.noamproject1.services.DatabaseService;
import com.noam.noamproject1.utils.ImageUtil;

public class ShowAttraction extends AppCompatActivity {

    private TextView tvAttractionName, tvAttractionDetail, tvAttractionCapacity;
    private ImageView pic;
    private MaterialButton commentButton;
    private String attractionId;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_attraction);

        initializeViews();
        setupToolbar();
        
        attractionId = getIntent().getStringExtra("attraction_id");
        if (attractionId != null) {
            fetchAttractionDetails(attractionId);
        } else {
            Toast.makeText(this, "מזהה האטרקציה חסר", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupClickListeners();
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
            getSupportActionBar().setTitle("");  // Will be set when attraction details are loaded
        }
    }

    private void setupClickListeners() {
        commentButton.setOnClickListener(v -> {
            Intent intent = new Intent(ShowAttraction.this, ReviewsActivity.class);
            intent.putExtra("ATTRACTION_ID", attractionId);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void fetchAttractionDetails(String attractionId) {
        DatabaseService databaseService = DatabaseService.getInstance();
        databaseService.getAttractionDetails(attractionId, new DatabaseService.DatabaseCallback<Attraction>() {
            @Override
            public void onCompleted(Attraction attraction) {
                updateUI(attraction);
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(ShowAttraction.this, "שגיאה בטעינת פרטי האטרקציה", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void updateUI(Attraction attraction) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(attraction.getName());
        }

        tvAttractionName.setText(attraction.getName());
        tvAttractionDetail.setText(attraction.getDetail());
        tvAttractionCapacity.setText(String.format("%d מבקרים", attraction.getCapacity()));

        if (attraction.getPic() != null && !attraction.getPic().isEmpty()) {
            pic.setImageBitmap(ImageUtil.convertFrom64base(attraction.getPic()));
        }
    }
}
