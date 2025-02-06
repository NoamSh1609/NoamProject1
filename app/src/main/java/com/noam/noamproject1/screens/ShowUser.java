package com.noam.noamproject1.screens;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

public class ShowUser extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private List<User> filteredList;  // רשימה מסוננת של משתמשים

    DatabaseService databaseService;

    private EditText etSearch;  // EditText עבור החיפוש

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);

        databaseService = DatabaseService.getInstance();

        // מציאת האלמנטים
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        etSearch = findViewById(R.id.etSearch);

        userList = new ArrayList<>();
        filteredList = new ArrayList<>();
        userAdapter = new UserAdapter(filteredList);
        recyclerView.setAdapter(userAdapter);

        databaseService.getUserList(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> users) {
                userList.clear();
                userList.addAll(users);
                filterUsers(""); // סינון כללי בתחילת העבודה
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(ShowUser.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
        
        // מאזין לשינויים במילת החיפוש
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterUsers(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    // פונקציה לסינון המשתמשים
    private void filterUsers(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(userList); // אם השדה ריק, מציג את כל המשתמשים
        } else {
            for (User user : userList) {
                if (user.getFname().toLowerCase().contains(query.toLowerCase()) ||
                        user.getLname().toLowerCase().contains(query.toLowerCase()) ||
                        user.getEmail().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(user);
                }
            }
        }
        userAdapter.notifyDataSetChanged(); // עדכון ה-Adapter
    }
}
