package com.noam.noamproject1.screens;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import com.noam.noamproject1.R;
import com.noam.noamproject1.models.Attraction;
import com.noam.noamproject1.services.AuthenticationService;
import com.noam.noamproject1.services.DatabaseService;
import com.noam.noamproject1.utils.ImageUtil;

public class AddAttraction extends AppCompatActivity implements View.OnClickListener {
  //  public static final  TAG = "Add Attraction";
    ImageButton selectImageFromGallery;
    EditText etAttractionCapicity, etAttractionDetails, etAttractionName;
    Button btnAdd;
    Spinner spAttractionCity, spAttractionType, spAttractionArea;

    // Firebase Database reference
    DatabaseService databaseService = DatabaseService.getInstance();
    AuthenticationService authenticationService = AuthenticationService.getInstance();

    // Define ActivityResultLauncher for image selection
    private ActivityResultLauncher<Intent> selectImageLauncher;

    // Variable to hold the selected image URI
    private Uri selectedImageUri;

    /// Activity result launcher for capturing image from camera
    private ActivityResultLauncher<Intent> captureImageLauncher;

    // One Preview Image

    ImageView ivPreviewImage;

    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attraction);

        // Initialize UI components
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
        ivPreviewImage=findViewById(R.id.ivAddAttraction);

        /// request permission for the camera and storage


        ImageUtil.requestPermission(this);

        // Get the string arrays from resources
        String[] attractionCityArray = getResources().getStringArray(R.array.spAttractionCity);
        String[] attractionTypeArray = getResources().getStringArray(R.array.spAttractionType);
        String[] attractionAreaArray = getResources().getStringArray(R.array.spAttractionArea);

        // Set the spinner adapters
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, attractionCityArray);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAttractionCity.setAdapter(cityAdapter);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, attractionTypeArray);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAttractionType.setAdapter(typeAdapter);

        ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, attractionAreaArray);
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAttractionArea.setAdapter(areaAdapter);

        // Initialize ActivityResultLauncher for image selection
        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            selectedImageUri = data.getData();
                            // Set the image to ImageButton
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
            // select image from gallery
            Log.d("TAG", "Select image button clicked");
            selectImageFromGallery();
            return;

        }
    }


    private void saveToDB() {
        // Get data from EditText and Spinner
        String attractionName = etAttractionName.getText().toString().trim();
        String attractionDetails = etAttractionDetails.getText().toString().trim();
        String attractionCapacityString = etAttractionCapicity.getText().toString().trim();
        String attractionCity = spAttractionCity.getSelectedItem().toString();
        String attractionType = spAttractionType.getSelectedItem().toString();
        String attractionArea = spAttractionArea.getSelectedItem().toString();

        String imageBase64 = ImageUtil.convertTo64Base(ivPreviewImage);




        // Validate if fields are filled
        if (attractionName.isEmpty() || attractionDetails.isEmpty() || attractionCapacityString.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            String id = databaseService.generateNewAttractionId();
            Attraction attr = new Attraction(id, attractionName, attractionType, attractionCity, attractionDetails,
                    attractionArea, 0, 0, 0, 0, imageBase64);

            // If image is selected, you can handle the image here before saving it in the database
            if (selectedImageUri != null) {
                // For example, you can upload the image to Firebase Storage and save the image URL in the database
                // You can create a method that uploads the image and saves the URL in the Attraction object
            }

            // Save the attraction to Firebase Database
            databaseService.createNewAttraction(attr, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void object) {
                    Toast.makeText(AddAttraction.this, "הצליח", Toast.LENGTH_SHORT).show();
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


    /// select image from gallery
    private void selectImageFromGallery() {
        //   Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //  selectImageLauncher.launch(intent);

        imageChooser();
    }

    /// capture image from camera
    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImageLauncher.launch(takePictureIntent);
    }





    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }


    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    ivPreviewImage.setImageURI(selectedImageUri);
                }
            }
        }
    }


}
