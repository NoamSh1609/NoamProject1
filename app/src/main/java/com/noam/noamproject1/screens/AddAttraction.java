package com.noam.noamproject1.screens;

// ייבוא דברים שהאפליקציה צריכה להשתמש בהם
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.noam.noamproject1.R;
import com.noam.noamproject1.models.Attraction;
import com.noam.noamproject1.services.AuthenticationService;
import com.noam.noamproject1.services.DatabaseService;
import com.noam.noamproject1.utils.ImageUtil;

import java.util.ArrayList;

public class AddAttraction extends AppCompatActivity implements View.OnClickListener {

    // הגדרות של כפתורים ושדות למסך הוספת אטרקציה
    MaterialButton selectImageFromGallery;
    TextInputLayout etAttractionCapicity;
    EditText etAttractionDetails, etAttractionName;
    Button btnAdd;
    Spinner spAttractionCity, spAttractionType, spAttractionArea, spCapsity;

    // שירותים שיעזרו לנו לשמור נתונים ולבדוק משתמש
    DatabaseService databaseService = DatabaseService.getInstance();
    AuthenticationService authenticationService = AuthenticationService.getInstance();

    // משתנים שקשורים לבחירת תמונה
    private ActivityResultLauncher<Intent> selectImageLauncher;
    private ActivityResultLauncher<Intent> captureImageLauncher;
    private Uri selectedImageUri;
    ImageView ivPreviewImage;

    int SELECT_PICTURE = 200; // קוד לזיהוי הפעולה של בחירת תמונה

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attraction); // מציב את העיצוב של המסך

        // מחבר את הכפתורים והשדות מהמסך לקוד שלנו
        btnAdd = findViewById(R.id.btnAdd);
        selectImageFromGallery = findViewById(R.id.btnSelectImage);
        btnAdd.setOnClickListener(this);
        selectImageFromGallery.setOnClickListener(this);

        etAttractionCapicity = findViewById(R.id.tilAttractionCapacity);
        etAttractionDetails = findViewById(R.id.etAttractionDetails);
        etAttractionName = findViewById(R.id.etAttractionName);
        spAttractionCity = findViewById(R.id.spAttractionCity);
        spAttractionType = findViewById(R.id.spAttractionType);
        spAttractionArea = findViewById(R.id.spAttractionArea);
        ivPreviewImage = findViewById(R.id.ivAddAttraction);

        // מבקש הרשאה לתמונות
        ImageUtil.requestPermission(this);

        // מביא את רשימות הערכים מהמשאבים
        String[] attractionCityArray = getResources().getStringArray(R.array.city_names);
        String[] attractionTypeArray = getResources().getStringArray(R.array.spAttractionType);
        String[] attractionAreaArray = getResources().getStringArray(R.array.spAttractionArea);

        // מחבר את הרשימות לתפריטים במסך
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, attractionCityArray);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAttractionCity.setAdapter(cityAdapter);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, attractionTypeArray);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAttractionType.setAdapter(typeAdapter);

        ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, attractionAreaArray);
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAttractionArea.setAdapter(areaAdapter);

        // מקבל את התמונה שהמשתמש בחר בגלריה
        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            selectedImageUri = data.getData();
                            ivPreviewImage.setImageURI(selectedImageUri);
                        }
                    }
                });
    }

    // מה קורה כשמשתמש לוחץ על כפתור
    @Override
    public void onClick(View v) {
        if (v == btnAdd) {
            saveToDB(); // שומר למסד הנתונים
            return;
        }
        if (v == selectImageFromGallery) {
            selectImageFromGallery(); // בוחר תמונה מהגלריה
            return;
        }
    }

    // שומר את כל המידע למסד הנתונים
    private void saveToDB() {

        // מקבל את הערכים מהשדות
        String attractionName = etAttractionName.getText().toString().trim();
        String attractionDetails = etAttractionDetails.getText().toString().trim();
        String attractionCapacityString = etAttractionCapicity.getEditText().getText().toString().trim();
        String attractionCity = spAttractionCity.getSelectedItem().toString();
        String attractionType = spAttractionType.getSelectedItem().toString();
        String attractionArea = spAttractionArea.getSelectedItem().toString();

        // ממיר את התמונה לטקסט מיוחד שניתן לשמור
        String imageBase64 = ImageUtil.convertTo64Base(ivPreviewImage);

        // בודק אם כל השדות מלאים
        if (attractionName.isEmpty() || attractionDetails.isEmpty() || attractionCapacityString.isEmpty() || imageBase64 == null) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            // יוצר מזהה חדש ושומר את האטרקציה למסד נתונים
            String id = databaseService.generateNewAttractionId();
            int capacity = Integer.parseInt(attractionCapacityString);
            Attraction attr = new Attraction(id, attractionName, attractionType, attractionCity, attractionDetails,
                    attractionArea, capacity , 0, "", new ArrayList<>(), imageBase64);

            databaseService.createNewAttraction(attr, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void object) {
                    Toast.makeText(AddAttraction.this, "הצליח", Toast.LENGTH_SHORT).show();
                    sendNewHikeNotification(); // שולח התראה
                    finish(); // סוגר את המסך
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(AddAttraction.this, "נכשל", Toast.LENGTH_SHORT).show();
                    Log.e("AddAttraction", e.getMessage());
                }
            });
        }
    }

    // פותח את בחירת התמונה בגלריה
    private void selectImageFromGallery() {
        imageChooser();
    }

    // (לא בשימוש כרגע) מצלמה
    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImageLauncher.launch(takePictureIntent);
    }

    // פותח חלון לבחירת תמונה
    void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // מקבל את התמונה שנבחרה ומציג אותה על המסך
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

    // שולח התראה למשתמש שיש טיול חדש
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