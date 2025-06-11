package com.noam.noamproject1.screens;

import android.annotation.SuppressLint;
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

    private FirebaseAuth mAuth;  // אובייקט של Firebase לאימות משתמשים
    private Button btnDeleteUser;  // כפתור מחיקת משתמש

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);  // קובע את התצוגה מהקובץ activity_delete_user.xml

        mAuth = FirebaseAuth.getInstance();  // אתחול האובייקט שמנהל את האימות
        btnDeleteUser = findViewById(R.id.btnDeleteUser);  // קישור הכפתור מהמסך לקוד

        // מאזין ללחיצה על הכפתור
        btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog(); // כשלוחצים, מראה תיבת אישור לפני שמוחקים
            }
        });
    }

    // מציג חלון דיאלוג לשאלה אם באמת רוצים למחוק את המשתמש
    private void showConfirmationDialog() {
        new AlertDialog.Builder(DeleteUser.this)
                .setTitle("אישור מחיקה")  // כותרת החלון
                .setMessage("האם אתה בטוח שברצונך למחוק את המשתמש? לא ניתן להחזיר זאת.")  // ההודעה למשתמש
                .setPositiveButton("כן", (dialog, which) -> deleteUser())  // אם לוחצים "כן" - מבצעים מחיקה
                .setNegativeButton("לא", null)  // אם לוחצים "לא" - פשוט סוגרים את החלון
                .show();  // מראה את החלון
    }

    // פונקציה שמוחקת את המשתמש בפועל
    private void deleteUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();  // מקבל את המשתמש הנוכחי המחובר
        if (currentUser != null) {
            currentUser.delete()  // מנסה למחוק את המשתמש מפיירבייס
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // אם המחיקה הצליחה - מראה הודעה ומעביר את המשתמש למסך התחברות
                            Toast.makeText(DeleteUser.this, "המשתמש נמחק בהצלחה", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DeleteUser.this, Login.class);  // מעביר למסך התחברות
                            startActivity(intent);
                            finish();  // סוגר את המסך הנוכחי
                        } else {
                            // אם המחיקה נכשלה - מראה הודעה מתאימה
                            Toast.makeText(DeleteUser.this, "לא ניתן למחוק את המשתמש", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // אם אין משתמש מחובר - מראה הודעה
            Toast.makeText(DeleteUser.this, "אין משתמש מחובר", Toast.LENGTH_SHORT).show();
        }
    }
}
