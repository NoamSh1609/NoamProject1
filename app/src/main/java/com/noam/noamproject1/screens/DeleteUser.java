package com.noam.noamproject1.screens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.noam.noamproject1.R;

public class DeleteUser extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btnDeleteUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);

        // אתחול משתנים
        mAuth = FirebaseAuth.getInstance();
        btnDeleteUser = findViewById(R.id.btnDeleteUser);

        // לחצן מחיקת משתמש
        btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog(); // הצגת דיאלוג לאישור מחיקה
            }
        });
    }

    // הצגת דיאלוג לאישור מחיקה
    private void showConfirmationDialog() {
        new AlertDialog.Builder(DeleteUser.this)
                .setTitle("אישור מחיקה")
                .setMessage("האם אתה בטוח שברצונך למחוק את המשתמש? לא ניתן להחזיר זאת.")
                .setPositiveButton("כן", (dialog, which) -> deleteUser()) // אם המשתמש אישר
                .setNegativeButton("לא", null) // אם המשתמש ביטל
                .show();
    }

    // מחיקת המשתמש
    private void deleteUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.delete() // מחיקת המשתמש
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(DeleteUser.this, "המשתמש נמחק בהצלחה", Toast.LENGTH_SHORT).show();
                            // לחזור לדף ההתחברות או דף ראשי
                            Intent intent = new Intent(DeleteUser.this, Login.class); // ניתן לשנות לדף שברצונך לחזור אליו
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(DeleteUser.this, "לא ניתן למחוק את המשתמש", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(DeleteUser.this, "אין משתמש מחובר", Toast.LENGTH_SHORT).show();
        }
    }
}
