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

    private EditText etEditUserName, etEditUserEmail, etEditUserPhone;
    private Button btnSaveUser;
    private DatabaseService databaseService;
    private String userId;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        // אתחול רכיבי UI
        etEditUserName = findViewById(R.id.etEditUserName);
        etEditUserEmail = findViewById(R.id.etEditUserEmail);
        etEditUserPhone = findViewById(R.id.etEditUserPhone);
        btnSaveUser = findViewById(R.id.btnSaveUser);

        // אתחול שירות מסד הנתונים
        databaseService = DatabaseService.getInstance();

        // קבלת המידע של המשתמש הנוכחי (המידע יעבור כ-Intent)
        userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            userId = AuthenticationService.getInstance().getCurrentUserId();
        }

        databaseService.getUser(userId, new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User user) {
                currentUser = user;
                setView(user);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }

    private void setView(User user) {
        etEditUserName.setText(user.getFname() + " " + user.getLname());
        etEditUserEmail.setText(user.getEmail());
        etEditUserPhone.setText(user.getPhone());

        btnSaveUser.setOnClickListener(v -> {
            String updatedName = etEditUserName.getText().toString().trim();
            String updatedEmail = etEditUserEmail.getText().toString().trim();
            String updatedPhone = etEditUserPhone.getText().toString().trim();

            if (TextUtils.isEmpty(updatedName) || TextUtils.isEmpty(updatedEmail) || TextUtils.isEmpty(updatedPhone)) {
                Toast.makeText(EditUserActivity.this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            // עדכון המידע במסד הנתונים
            currentUser.setFname(updatedName.split(" ")[0]);
            currentUser.setLname(updatedName.split(" ")[1]);
            currentUser.setEmail(updatedEmail);
            currentUser.setPhone(updatedPhone);

            // קריאה לשירות המסד כדי לשמור את השינויים
            databaseService.updateUser(currentUser, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void v) {
                    Toast.makeText(EditUserActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // חזרה למסך הקודם לאחר שמירת השינויים
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(EditUserActivity.this, "Failed to update user", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
