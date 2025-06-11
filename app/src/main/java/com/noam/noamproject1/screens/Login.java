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

    // כתובת דואר אלקטרוני וסיסמה של מנהל מוגדרים בקוד (לא מומלץ לשמור כך בסביבת ייצור)
    private static final String MANAGER_EMAIL = "noamabc10@gmail.com";
    private static final String MANAGER_PASSWORD = "NOAM2019";

    String email, password, managerPassword;
    EditText etEmail, etPassword;
    Button btnLogin, btnGoToMain;

    // שם SharedPreferences לשמירת פרטי משתמש
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    // שירותי אימות ונתונים
    AuthenticationService authenticationService;
    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // הפעלת מצב EdgeToEdge (לממשק עם אינדנטציה למערכת)
        EdgeToEdge.enable(this);

        // טעינת הממשק מתוך קובץ ה-XML
        setContentView(R.layout.activity_login);

        // טיפול בהטמעת פדינג סביב התצוגה בהתחשב ב-insets של מערכת ההפעלה (כמו מצב סורק טביעת אצבע, שורת מערכת וכו')
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initviews();  // אתחול אלמנטים בקוד

        // טוען מה SharedPreferences את האימייל והסיסמה (אם נשמרו בעבר) ומציב אותם בשדות הקלט
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        etEmail.setText(sharedpreferences.getString("email", ""));
        etPassword.setText(sharedpreferences.getString("password", ""));
    }

    // אתחול המשתנים שמקשרים לאלמנטים ב-XML והגדרת מאזינים
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

    // טיפול בלחיצות על הכפתורים
    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            email = etEmail.getText().toString();
            password = etPassword.getText().toString();
            managerPassword = etPassword.getText().toString();  // מקבל סיסמה לשם בדיקת מנהל

            // בדיקת תקינות אימייל וסיסמה באמצעות פונקציות ייעודיות
            if (!chkEmail(email) || !chkPassword(password)) {
                return; // במידה לא תקין, יציאה מהפונקציה
            }

            // בדיקה אם המשתמש הוא מנהל (האימייל והסיסמה תואמים את המנהל)
            if (managerPassword.equals(MANAGER_PASSWORD) && email.equals(MANAGER_EMAIL)) {
                // אם כן, מעביר לדף מנהל
                Intent goToAdminPage = new Intent(Login.this, AdminMainPage.class);
                startActivity(goToAdminPage);
                return;
            }

            // אם לא מנהל, מבצע התחברות רגילה דרך שירות האימות
            authenticationService.signIn(email, password, new AuthenticationService.AuthCallback<String>() {
                @Override
                public void onCompleted(String id) {
                    Log.d("TAG", "signInWithCustomToken:success");

                    // לאחר ההתחברות מוציא את פרטי המשתמש מהדאטה בייס
                    databaseService.getUser(id, new DatabaseService.DatabaseCallback<User>() {
                        @Override
                        public void onCompleted(User user) {
                            // שומר את פרטי המשתמש ב-SharedPreferences
                            SharedPreferencesUtil.saveUser(Login.this, user);
                            // פותח את המסך הראשי לאחר כניסה מוצלחת
                            Intent goToMain = new Intent(Login.this, After_Login.class);
                            startActivity(goToMain);
                        }

                        @Override
                        public void onFailed(Exception e) {
                            // במקרה של שגיאה מקדם יציאה מהמערכת
                            authenticationService.signOut();
                        }
                    });
                }

                @Override
                public void onFailed(Exception e) {
                    Log.w("TAG", "signInWithCustomToken:failure", e);
                    // מציג הודעת טוסט על כישלון בהתחברות
                    Toast.makeText(Login.this, "התחברות נכשלה", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // --- פונקציות עזר לבדיקת תקינות אימייל וסיסמה ---

    // בודק אם הטקסט מכיל גרשיים (אולי לצורך מניעת SQL Injection או דומיו)
    private boolean isQuot(String txt) {
        return txt.indexOf('"') != -1 || txt.indexOf("'") != -1;
    }

    // בודק אם האימייל תקין (מיעזר במחלקת Validator)
    private boolean isValidEmail(String email) {
        return Validator.isEmailValid(email);
    }

    // בודק אם יש תווים בעברית (כדי למנוע זאת)
    private boolean isHebrew(String email) {
        int len = email.length();
        for (int i = 0; i < len; i++) {
            if (email.charAt(i) >= 'א' && email.charAt(i) <= 'ת') {
                return false;
            }
        }
        return true;
    }

    // בודק תקינות אימייל בעזרת Validator
    private boolean chkEmail(String email) {
        return Validator.isEmailValid(email);
    }

    // בודק תקינות סיסמה בעזרת Validator
    private boolean chkPassword(String password) {
        return Validator.isPasswordValid(password);
    }
}
