package com.noam.noamproject1.screens;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.noam.noamproject1.R;
import com.noam.noamproject1.models.Attraction;
import com.noam.noamproject1.services.AuthenticationService;
import com.noam.noamproject1.services.DatabaseService;
import com.noam.noamproject1.utils.ImageUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class AddAttraction extends AppCompatActivity implements View.OnClickListener {

    ImageButton selectImageFromGallery;
    EditText etAttractionCapicity, etAttractionDetails, etAttractionName;
    Button btnAdd;
    Spinner spAttractionCity, spAttractionType, spAttractionArea,spCapsity;

    DatabaseService databaseService = DatabaseService.getInstance();
    AuthenticationService authenticationService = AuthenticationService.getInstance();

    private ActivityResultLauncher<Intent> selectImageLauncher;
    private ActivityResultLauncher<Intent> captureImageLauncher;
    private Uri selectedImageUri;
    ImageView ivPreviewImage;

    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attraction);

        btnAdd = findViewById(R.id.btnAdd);
        selectImageFromGallery = findViewById(R.id.select_image_button);
        btnAdd.setOnClickListener(this);
        selectImageFromGallery.setOnClickListener(this);

        etAttractionCapicity = findViewById(R.id.etAttractionCapicity);
        etAttractionDetails = findViewById(R.id.etAttractionDetails);
        etAttractionName = findViewById(R.id.etAttractionName);
        spAttractionCity = findViewById(R.id.spAttractionCity);
        spAttractionType = findViewById(R.id.spAttractionType);
        spAttractionArea = findViewById(R.id.spAttractionArea);
        ivPreviewImage = findViewById(R.id.ivAddAttraction);

        ImageUtil.requestPermission(this);

        String[] attractionCityArray = getResources().getStringArray(R.array.spAttractionCity);
        String[] attractionTypeArray = getResources().getStringArray(R.array.spAttractionType);
        String[] attractionAreaArray = getResources().getStringArray(R.array.spAttractionArea);

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, attractionCityArray);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAttractionCity.setAdapter(cityAdapter);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, attractionTypeArray);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAttractionType.setAdapter(typeAdapter);

        ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, attractionAreaArray);
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAttractionArea.setAdapter(areaAdapter);

        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            selectedImageUri = data.getData();
                            selectImageFromGallery.setImageURI(selectedImageUri);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == btnAdd) {
            saveToDB();
            return;
        }
        if (v == selectImageFromGallery) {
            selectImageFromGallery();
            return;
        }
    }

    private void saveToDB() {

        String attractionName = etAttractionName.getText().toString().trim();
        String attractionDetails = etAttractionDetails.getText().toString().trim();
        String attractionCapacityString = etAttractionCapicity.getText().toString().trim();
        String attractionCity = spAttractionCity.getSelectedItem().toString();
        String attractionType = spAttractionType.getSelectedItem().toString();
        String attractionArea = spAttractionArea.getSelectedItem().toString();

        String imageBase64 = ImageUtil.convertTo64Base(ivPreviewImage);

        if (attractionName.isEmpty() || attractionDetails.isEmpty() || attractionCapacityString.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            String id = databaseService.generateNewAttractionId();
            Attraction attr = new Attraction(id, attractionName, attractionType, attractionCity, attractionDetails,
                    attractionArea, 0, 0, "", new ArrayList<>(), imageBase64);

            databaseService.createNewAttraction(attr, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void object) {
                    Toast.makeText(AddAttraction.this, "הצליח", Toast.LENGTH_SHORT).show();
                    sendNewHikeNotification(); // שליחת ההתראה
                    finish();
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(AddAttraction.this, "נכשל", Toast.LENGTH_SHORT).show();
                    Log.e("AddAttraction", e.getMessage());
                }
            });
        }
    }

    private void selectImageFromGallery() {
        imageChooser();
    }

    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImageLauncher.launch(takePictureIntent);
    }

    void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    ivPreviewImage.setImageURI(selectedImageUri);
                }
            }
        }
    }

    private void sendNewHikeNotification() {
        String channelId = "hike_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Hike Notification Channel";
            String description = "Channel for hike notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("יש טיול חדש!")
                .setContentText("הרגע נוסף טיול חדש למערכת 🥾")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(2001, builder.build());
        }
    }
}
