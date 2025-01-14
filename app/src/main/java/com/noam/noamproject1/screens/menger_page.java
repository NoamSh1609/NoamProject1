package com.noam.noamproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.noam.noamproject1.R;

public class menger_page extends AppCompatActivity {

    EditText etMangerPassword;
    Button btnLogin;

    // הסיסמה שצריך להזין
    private static final String CORRECT_PASSWORD = "OmerTheMonkey69"; // החלף עם הסיסמה הנכונה

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menger_page);

        // אתחול המשתנים
        etMangerPassword = findViewById(R.id.etMangerPassword);
        btnLogin = findViewById(R.id.button);

        // מאזין לכפתור
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // קבלת הסיסמה שהוזנה
                String enteredPassword = etMangerPassword.getText().toString().trim();

                // בדיקת אם הסיסמה נכונה
                if (enteredPassword.equals(CORRECT_PASSWORD)) {
                    // אם הסיסמה נכונה, נווט לעמוד הבא
                    Intent intent = new Intent(menger_page.this, AdminMainPage.class); // החלף את NextActivity עם שם הפעילות הבאה
                    startActivity(intent);
                } else {
                    // אם הסיסמה לא נכונה, הצג הודעת שגיאה
                    Toast.makeText(menger_page.this, "סיסמה לא נכונה", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}