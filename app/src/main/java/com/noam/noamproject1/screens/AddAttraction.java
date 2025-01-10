package com.noam.noamproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.noam.noamproject1.R;
import com.noam.noamproject1.models.Attraction;

public class AddAttraction extends AppCompatActivity implements View.OnClickListener {
    ImageButton selectImageFromGallery;

    EditText etAttractionCapicity, etAttractionDetails, etAttractionName;
    Button btnAdd;
    Spinner spAttractionCity, spAttractionType, spAttractionArea;

    // Firebase Database reference
    DatabaseReference mDatabase;

    private ActivityResultLauncher<Intent> selectImageLauncher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attraction);

        // Initialize Firebase Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference("Attractions");

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
    }


    @Override
    public void onClick(View v) {
        if (v == btnAdd) {
            saveToDB();
            return;
        }
        if (v == selectImageFromGallery) {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            selectImageLauncher.launch(intent);


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


        // Validate if fields are filled
        if (attractionName.isEmpty() || attractionDetails.isEmpty() || attractionCapacityString.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            // Convert capacity to an integer
            int attractionCapacity = Integer.parseInt(attractionCapacityString);

            // Create a new Attraction object
            String id = mDatabase.push().getKey(); // Generate a unique ID for the attraction
            Attraction attraction = new Attraction(id, attractionName, attractionType, attractionCity, attractionDetails, attractionArea, attractionCapacity, 0.0, 0.0, 0);

            // Save the attraction data to Firebase
            if (id != null) {
                mDatabase.child(id).setValue(attraction)
                        .addOnSuccessListener(aVoid -> Toast.makeText(AddAttraction.this, "Attraction Added", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(AddAttraction.this, "Failed to add attraction", Toast.LENGTH_SHORT).show());
            }
        }
    }
}
