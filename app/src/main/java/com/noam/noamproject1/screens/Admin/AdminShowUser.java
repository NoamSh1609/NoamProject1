package com.noam.noamproject1.screens.Admin;

import android.content.Intent;
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
import com.noam.noamproject1.screens.EditUserActivity;
import com.noam.noamproject1.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class AdminShowUser extends AppCompatActivity {

    private static final String TAG = "AdminShowUser";


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

        Log.d(TAG, "onCreate");
        etSearchUser = findViewById(R.id.etAdminSearchUser);
        btnGoBack = findViewById(R.id.btnGoBack);
        rvUsers = findViewById(R.id.rvUserDetails);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));

        databaseService = DatabaseService.getInstance();

        // הגדרת ה-Adapter עם פונקציות למחיקת משתמש ועריכת משתמש
        userAdapter = new UserAdapter(userList, this::onUserDelete, this::onUserEdit);
        rvUsers.setAdapter(userAdapter);

        fetchUsers();

        etSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnGoBack.setOnClickListener(v -> finish());
    }

    private void fetchUsers() {
        databaseService.getUserList(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> users) {
                if (users != null) {
                    fullUserList.clear();
                    fullUserList.addAll(users);
                    filterUsers("");
                }
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("AdminShowUser", e.getMessage());
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

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

    public void onUserDelete(User user) {
        databaseService.deleteUser(user.getId(), new DatabaseService.DatabaseCallback<Boolean>() {
            @Override
            public void onCompleted(Boolean success) {
                if (success) {
                    Toast.makeText(AdminShowUser.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                    removeUserFromList(user);
                } else {
                    Toast.makeText(AdminShowUser.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(AdminShowUser.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onUserEdit(User user) {
        // שליחה למסך עריכה עם פרטי המשתמש
        Intent intent = new Intent(this, EditUserActivity.class);
        intent.putExtra("userId", user.getId());
        startActivity(intent);
    }

    private void removeUserFromList(User user) {
        int index = -1;
        for (int i = 0; i < fullUserList.size(); i++) {
            if (fullUserList.get(i).getId().equals(user.getId())) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            fullUserList.remove(index);
        }
        filterUsers(etSearchUser.getText().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        etSearchUser.addTextChangedListener(null);
    }
}
