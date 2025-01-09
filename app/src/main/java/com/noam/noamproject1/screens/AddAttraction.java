package com.noam.noamproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.noam.noamproject1.R;
import com.noam.noamproject1.services.DatabaseService;

public class AddAttraction extends AppCompatActivity {
    EditText etAttractionCapicity, etAttractionDetails,etAttractionName;
    Button btAdd;

    /// Activity result launcher for selecting image from gallery
    private ActivityResultLauncher<Intent> selectImageLauncher;

    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attraction);

        // Get the string arrays from the resources
        String[] cityArray = getResources().getStringArray(R.array.Arrcity);
        String[] attractionCityArray = getResources().getStringArray(R.array.spAttractionCity);
        String[] attractionTypeArray = getResources().getStringArray(R.array.spAttractionType);
        String[] attractionAreaArray = getResources().getStringArray(R.array.spAttractionArea);

        // Example: Set the "Arrcity" array into a Spinner

        // You can similarly set other arrays into spinners or other UI components as needed
    }
}




