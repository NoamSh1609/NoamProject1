package com.noam.noamproject1.screens;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.noam.noamproject1.R;
import com.noam.noamproject1.models.User;
import com.noam.noamproject1.services.AuthenticationService;
import com.noam.noamproject1.services.DatabaseService;

public class EditUserActivity extends AppCompatActivity {

    // רכיבי ממשק משתמש: שדות טקסט וכפתור שמירה
    private EditText etEditUserName, etEditUserEmail, etEditUserPhone;
    private Button btnSaveUser;

    private DatabaseService databaseService;  // שירות לתקשורת עם מסד הנתונים
    private String userId;                    // מזהה המשתמש שאותו נערוך
    private User currentUser;                 // האובייקט של המשתמש הנוכחי

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);  // טוען את תצוגת המסך

        // קישור רכיבי הממשק מה-XML לקוד
        etEditUserName = findViewById(R.id.etEditUserName);
        etEditUserEmail = findViewById(R.id.etEditUserEmail);
        etEditUserPhone = findViewById(R.id.etEditUserPhone);
        btnSaveUser = findViewById(R.id.btnSaveUser);

        // אתחול שירות מסד הנתונים
        databaseService = DatabaseService.getInstance();

        // מקבל את ה-userId מה-Intent (אם הגיע)
        userId = getIntent().getStringExtra("userId");

        // אם לא קיבלנו userId ב-Intent, נקבל את ה-ID של המשתמש המחובר כרגע
        if (userId == null) {
            userId = AuthenticationService.getInstance().getCurrentUserId();
        }

        // קורא למסד הנתונים ומבקש לקבל את פרטי המשתמש לפי ה-userId
        databaseService.getUser(userId, new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User user) {
                currentUser = user; // שומר את המשתמש שקיבלנו
                setView(user);      // מעדכן את המסך עם פרטי המשתמש
            }

            @Override
            public void onFailed(Exception e) {
                // אפשר להוסיף טיפול בשגיאה במקרה של כישלון בקריאה למסד
            }
        });
    }

    // פונקציה שמעדכנת את שדות הטקסט עם הנתונים של המשתמש
    private void setView(User user) {
        etEditUserName.setText(user.getFname() + " " + user.getLname());  // שם פרטי ושם משפחה בשדה אחד
        etEditUserEmail.setText(user.getEmail());
        etEditUserPhone.setText(user.getPhone());

        // מאזין ללחיצה על כפתור השמירה
        btnSaveUser.setOnClickListener(v -> {
            String updatedName = etEditUserName.getText().toString().trim();  // שם מלא שהוקלד
            String updatedEmail = etEditUserEmail.getText().toString().trim();
            String updatedPhone = etEditUserPhone.getText().toString().trim();

            // בדיקה שכל השדות מלאים
            if (TextUtils.isEmpty(updatedName) || TextUtils.isEmpty(updatedEmail) || TextUtils.isEmpty(updatedPhone)) {
                Toast.makeText(EditUserActivity.this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            // מפריד את השם המלא לשם פרטי ושם משפחה (בהנחה שיש שם אחד ושם משפחה אחד)
            currentUser.setFname(updatedName.split(" ")[0]);
            currentUser.setLname(updatedName.split(" ")[1]);
            currentUser.setEmail(updatedEmail);
            currentUser.setPhone(updatedPhone);

            // שולח את המשתמש המעודכן לשירות המסד כדי לשמור את השינויים
            databaseService.updateUser(currentUser, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void v) {
                    Toast.makeText(EditUserActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                    finish();  // סוגר את המסך ומחזיר למסך הקודם
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(EditUserActivity.this, "Failed to update user", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
