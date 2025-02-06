package com.noam.noamproject1.screens.Admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.noam.noamproject1.R;
import com.noam.noamproject1.adapters.UserAdapter;
import com.noam.noamproject1.models.User;
import com.noam.noamproject1.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class AdminShowUser extends AppCompatActivity {

    private RecyclerView rvUsers;
    private UserAdapter userAdapter;
    private List<User> userList = new ArrayList<>();
    private List<User> fullUserList = new ArrayList<>();
    private EditText etSearchUser;
    private Button btnGoBack;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_show_user);

        // אתחול רכיבי ה-UI
        etSearchUser = findViewById(R.id.etSearchUser);
        btnGoBack = findViewById(R.id.btnGoBack);
        rvUsers = findViewById(R.id.rvUserDetails);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));

        // אתחול ה-Adapter עם הרשימה שתעודכן לפי החיפוש
        userAdapter = new UserAdapter(userList);
        rvUsers.setAdapter(userAdapter);

        databaseService = DatabaseService.getInstance();
        fetchUsers();

        // מאזין לשינויי טקסט בשדה החיפוש
        etSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // לא נדרש כאן
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // לא נדרש כאן
            }
        });

        // טיפול בלחיצה על כפתור "חזור"
        btnGoBack.setOnClickListener(v -> finish());
    }

    // קריאה למשתמשים מהמסד נתונים
    private void fetchUsers() {
        databaseService.getUserList(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> users) {
                fullUserList.clear();
                fullUserList.addAll(users);
                // סינון ראשוני עם שאילתת ריקה – מציג את כל המשתמשים
                filterUsers("");
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("AdminShowUser", e.getMessage());
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // פונקציית סינון לפי שם המשתמש
    private void filterUsers(String query) {
        userList.clear();
        if (query.isEmpty()) {
            userList.addAll(fullUserList);
        } else {
            for (User user : fullUserList) {
                if (user.getFname().toLowerCase().contains(query.toLowerCase())) {
                    userList.add(user);
                }
            }
        }
        userAdapter.notifyDataSetChanged();
    }
}
