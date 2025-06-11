// אומר למחשב איפה הקובץ הזה נמצא בתוך הפרויקט
package com.noam.noamproject1.screens;

// מייבא כל מיני דברים חשובים מהמערכת של אנדרואיד כדי שנוכל להשתמש בהם
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

// עוד דברים שקשורים למסך
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// עיצוב של toolbar מלמעלה
import com.google.android.material.appbar.MaterialToolbar;

// מחברים את הקובץ הזה עם הקבצים האחרים בפרויקט
import com.noam.noamproject1.R;
import com.noam.noamproject1.services.AuthenticationService;
import com.noam.noamproject1.utils.SharedPreferencesUtil;

// זו המחלקה שמטפלת במסך שנפתח אחרי שהמשתמש נכנס (login)
public class After_Login extends AppCompatActivity implements View.OnClickListener {

    // משתנים שמייצגים את כל הכפתורים שיש במסך
    Button btnAddAttraction, btnAttractionActivity, My_FavAt, btnLogout, btnEditUser;

    // מה קורה כשנכנסים למסך הזה
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // קורא לפעולה של המחלקה שממנה ירשנו
        EdgeToEdge.enable(this); // גורם למסך להיות ברוחב מלא (בלי שוליים)
        setContentView(R.layout.activity_after_login); // מחבר את הקוד לעיצוב מה-XML

        // שומר מקום בתצוגה לדברים של מערכת (כמו שורת סטטוס או כפתורי ניווט)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // אם המשתמש לא מחובר – נעביר אותו למסך הראשון
        if (!AuthenticationService.getInstance().isUserSignedIn()) {
            Intent intent = new Intent(this, TheFirstViewOfTheApp.class);
            startActivity(intent);
            finish(); // נסגור את המסך הזה
            return;
        }

        initViews(); // נקרא לפעולה שמחברת בין המשתנים לבין הכפתורים במסך
    }

    // פעולה שמאתרת את כל הכפתורים ומחברת אליהם "מאזינים" ללחיצות
    private void initViews() {
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar); // מגדיר את ה-toolbar כתפריט הראשי

        // מוצא כל כפתור לפי ה-id שלו בקובץ העיצוב
        btnLogout = findViewById(R.id.btnLogout);
        btnAttractionActivity = findViewById(R.id.btnAttractionActivity);
        btnAddAttraction = findViewById(R.id.btnAddAttraction);
        My_FavAt = findViewById(R.id.My_FavAt);
        btnEditUser = findViewById(R.id.btnEditUser);

        // קובע מה יקרה כשלוחצים על כל כפתור
        btnAddAttraction.setOnClickListener(this);
        btnAttractionActivity.setOnClickListener(this);
        My_FavAt.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnEditUser.setOnClickListener(this);
    }

    // פעולה שמתבצעת כשמישהו לוחץ על כפתור
    @Override
    public void onClick(View view) {
        if (view == btnAttractionActivity) {
            // מעבר למסך של רשימת האטרקציות
            startActivity(new Intent(this, AttractionListActivity.class));
        } else if (view == btnAddAttraction) {
            // מעבר למסך של הוספת אטרקציה
            startActivity(new Intent(this, AddAttraction.class));
        } else if (view == My_FavAt) {
            // מעבר למסך של המועדפים שלי
            startActivity(new Intent(this, MyFavoritesActivity.class));
        } else if (view == btnLogout) {
            // אם לחצו על יציאה – קרא לפעולה של יציאה
            handleLogout();
        } else if (view == btnEditUser) {
            // מסך לעריכת פרטי המשתמש
            startActivity(new Intent(this, EditUserActivity.class));
        }
    }

    // פעולה שמתבצעת כשמבצעים התנתקות מהמערכת
    private void handleLogout() {
        AuthenticationService.getInstance().signOut(); // התנתקות מהמשתמש
        SharedPreferencesUtil.clear(this); // ניקוי נתונים שמורים
        Intent intent = new Intent(this, TheFirstViewOfTheApp.class); // חזרה למסך הראשי
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // מנקה את ההיסטוריה
        startActivity(intent); // מתחיל את המסך
    }

    // === זה בשביל תפריט שלוש נקודות למעלה ===

    // יוצר את התפריט במסך (שלוש נקודות למעלה)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); // מוסיף את התפריט מתיקיית res/menu
        return true;
    }

    // מה לעשות כשמישהו לוחץ על פריט בתפריט
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // אם לחצו על "התנתקות" מהתפריט
        if (id == R.id.action_logout) {
            handleLogout();
            return true;
        }

        // אם לחצו על "אודות"
        else if (id == R.id.action_about) {
            startActivity(new Intent(this, wondermate.class)); // מעבר למסך אודות
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
