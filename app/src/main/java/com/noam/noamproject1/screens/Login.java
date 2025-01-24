package com.noam.noamproject1.screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.noam.noamproject1.R;
import com.noam.noamproject1.models.User;
import com.noam.noamproject1.services.AuthenticationService;
import com.noam.noamproject1.services.DatabaseService;
import com.noam.noamproject1.utils.SharedPreferencesUtil;
import com.noam.noamproject1.utils.Validator;

public class Login extends AppCompatActivity implements View.OnClickListener {

    // קבועים עבור מידע של מנהל
    private static final String MANAGER_EMAIL = "noamabc10@gmail.com";
    private static final String MANAGER_PASSWORD = "NOAM2019";

    String email, password, managerPassword;
    EditText etEmail, etPassword;
    Button btnLogin, btnGoToMain;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    AuthenticationService authenticationService;
    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initviews();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        etEmail.setText(sharedpreferences.getString("email", ""));
        etPassword.setText(sharedpreferences.getString("password", ""));
    }

    private void initviews() {
        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();

        btnLogin = findViewById(R.id.btnSignIn);
        btnGoToMain = findViewById(R.id.btnGoToMain);
        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);

        btnLogin.setOnClickListener(this);
        btnGoToMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            email = etEmail.getText().toString();
            password = etPassword.getText().toString();
            managerPassword = etPassword.getText().toString();  // קבלת סיסמת מנהל

            if (!chkEmail(email) || !chkPassword(password)) {
                return;
            }

            if (managerPassword.equals(MANAGER_PASSWORD) && email.equals(MANAGER_EMAIL)) {
                // אם סיסמת מנהל נכונה, נוודא שהמשתמש הוא מנהל
                Intent goToAdminPage = new Intent(Login.this, ManagerActivity.class);
                startActivity(goToAdminPage);
                return;
            }

            // אם לא מנהל, נבצע את תהליך ההתחברות הרגיל
            authenticationService.signIn(email, password, new AuthenticationService.AuthCallback<String>() {
                @Override
                public void onCompleted(String id) {
                    Log.d("TAG", "signInWithCustomToken:success");

                    databaseService.getUser(id, new DatabaseService.DatabaseCallback<User>() {
                        @Override
                        public void onCompleted(User user) {
                            SharedPreferencesUtil.saveUser(Login.this, user);
                            Intent goToMain = new Intent(Login.this, MainActivity.class);
                            startActivity(goToMain);
                        }

                        @Override
                        public void onFailed(Exception e) {
                            authenticationService.signOut();
                        }
                    });
                }

                @Override
                public void onFailed(Exception e) {
                    Log.w("TAG", "signInWithCustomToken:failure", e);
                    Toast.makeText(Login.this, "התחברות נכשלה", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //email
    private boolean isQuot(String txt) {
        return txt.indexOf('"') != -1 || txt.indexOf("'") != -1;
    }

    private boolean isValidEmail(String email) {
        return Validator.isEmailValid(email);
    }

    private boolean isHebrew(String email) {
        int len = email.length();
        for (int i = 0; i < len; i++) {
            if (email.charAt(i) >= 'א' && email.charAt(i) <= 'ת') {
                return false;
            }
        }
        return true;
    }

    private boolean chkEmail(String email) {
        int size = email.length();
        int atSign = email.indexOf('@');
        int dotSign = email.indexOf('.', atSign);
        boolean check = true;

        String msg = "";
        if (size > 30) {
            msg = "כתובת הדוא'ל ארוכה מדי";
            etEmail.setError(msg);
            check = false;
        } else if (size < 6) {
            msg = "כתובת הדוא'ל קצרה מדי או לא קיימת";
            etEmail.setError(msg);
            check = false;
        } else if (atSign == -1) {
            msg = "סימן @ לא קיים בכתובת";
            etEmail.setError(msg);
            check = false;
        } else if (atSign != email.lastIndexOf('@')) {
            msg = "אסור יותר מ - @ אחד בכתובת דוא'ל";
            etEmail.setError(msg);
            check = false;
        } else if (atSign < 2 || email.lastIndexOf('@') == size - 1) {
            msg = "סימן ה-@ לא יכול להיות במקום הראשון או האחרון";
            etEmail.setError(msg);
            check = false;
        } else if (email.indexOf('.') == 0 || email.lastIndexOf('.') == size - 1) {
            msg = "נקודה לא יכולה להיות תו ראשון או אחרון בכתובת";
            etEmail.setError(msg);
            check = false;
        } else if (dotSign - atSign <= 1) {
            msg = "נקודה חייבת להיות לפחות 2 תווים אחרי סימן ה-@";
            etEmail.setError(msg);
            check = false;
        } else if (isQuot(email)) {
            msg = "כתובת דוא'ל לא יכולה להכיל גרש או גרשיים";
            etEmail.setError(msg);
            check = false;
        } else if (!isValidEmail(email)) {
            msg = " כתובת דוא'ל לא יכולה להכיל תווים אסורים כגון #$%^&*-!:; []{}<>?";
            etEmail.setError(msg);
            check = false;
        } else if (!isHebrew(email)) {
            msg = "כתובת דוא'ל לא יכולה להכיל עברית";
            etEmail.setError(msg);
            check = false;
        }

        return check;
    }

    //password
    private boolean chkPassword(String password) {
        boolean check = true;
        if (password.length() < 6) {
            etPassword.setError("סיסמה חייבת להיות בעלת 6 תווים לפחות");
            check = false;
        }
        if (password.length() > 10) {
            etPassword.setError("אורך סיסמה לא יהיה יותר מ-10 תווים");
            check = false;
        }

        return check;
    }
}
