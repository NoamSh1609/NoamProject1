package com.noam.noamproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.noam.noamproject1.R;

public class ManagerActivity extends AppCompatActivity {

    EditText etMangerPassword;
    Button btn_admin_main_page;

    // הסיסמה שצריך להזין
    private static final String CORRECT_PASSWORD = "OmerTheMonkey69"; // החלף עם הסיסמה הנכונה

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        // אתחול המשתנים
        etMangerPassword = findViewById(R.id.etMangerPassword);
        btn_admin_main_page = findViewById(R.id.btn_admin_main_page);

        // מאזין לכפתור
        btn_admin_main_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // קבלת הסיסמה שהוזנה
                String enteredPassword = etMangerPassword.getText().toString().trim();

                // בדיקת אם הסיסמה נכונה
                if (enteredPassword.equals(CORRECT_PASSWORD)) {
                    // אם הסיסמה נכונה, נווט לעמוד הבא
                    Intent intent = new Intent(ManagerActivity.this, AdminMainPage.class); // החלף את AdminMainPage עם שם הפעילות הבאה
                    startActivity(intent);
                } else {
                    // אם הסיסמה לא נכונה, הצג הודעת שגיאה
                    Toast.makeText(ManagerActivity.this, "סיסמה לא נכונה", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
