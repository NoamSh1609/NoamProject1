// מגדיר לאן הקובץ הזה שייך בפרויקט
package com.noam.noamproject1.screens;

// ייבוא של דברים חשובים מהמערכת שצריך בשביל לבנות את המסך
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// ייבוא של קבצים אחרים מהאפליקציה שלנו
import com.noam.noamproject1.R;
import com.noam.noamproject1.screens.Admin.AdminShowAttraction;
import com.noam.noamproject1.screens.Admin.AdminShowUser;
import com.noam.noamproject1.services.AuthenticationService;
import com.noam.noamproject1.utils.SharedPreferencesUtil;

// מחלקה שמייצגת את המסך הראשי של מנהל (admin)
public class AdminMainPage extends AppCompatActivity implements View.OnClickListener {

    // משתנים שמייצגים את הכפתורים במסך
    Button btnShowUser, btnShowAdmin, btnLogout;

    // הפעולה הזו מתבצעת כשפותחים את המסך
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // קריאה לפונקציה של המחלקה שממנה ירשנו
        EdgeToEdge.enable(this); // מאפשר עיצוב של מסך מלא בלי מסגרות
        setContentView(R.layout.activity_admin_main_page); // מחבר בין הקוד למסך בעיצוב XML

        // דואג שהמסך ייראה טוב גם כשיש כפתורי מערכת כמו "חזור"
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews(); // קורא לפונקציה שמחברת בין המשתנים לכפתורים
    }

    // פעולה שמחברת את המשתנים לכפתורים האמיתיים על המסך ומוסיפה להם הקשבה ללחיצה
    private void initViews() {
        btnShowUser = findViewById(R.id.btnShowUser);     // כפתור להצגת משתמשים
        btnShowAdmin = findViewById(R.id.btnShowAdmin);   // כפתור להצגת אטרקציות
        btnLogout = findViewById(R.id.btnLogout);         // כפתור להתנתקות

        // כל כפתור מקבל "מאזין" – מה לעשות כשילחצו עליו
        btnShowUser.setOnClickListener(this);
        btnShowAdmin.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    // פעולה שמתבצעת כשאחד הכפתורים נלחץ
    @Override
    public void onClick(View view) {
        if (view == btnShowUser) {
            // אם נלחץ כפתור משתמשים – נעבור למסך שמציג את המשתמשים
            Intent go = new Intent(this, AdminShowUser.class);
            startActivity(go);
        } else if (view == btnShowAdmin) {
            // אם נלחץ כפתור אטרקציות – נעבור למסך שמציג את האטרקציות
            Intent go = new Intent(this, AdminShowAttraction.class);
            startActivity(go);
        } else if (view == btnLogout) {
            // אם נלחץ כפתור יציאה – נתנתק ונעבור למסך הפתיחה של האפליקציה
            AuthenticationService.getInstance().signOut(); // התנתקות מהמערכת
            SharedPreferencesUtil.clear(this); // ניקוי פרטים שנשמרו
            Intent intent = new Intent(this, TheFirstViewOfTheApp.class); // מעבר למסך הראשון
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // מסיר את כל המסכים הקודמים
            startActivity(intent); // מפעיל את המסך הראשון
            finish(); // סוגר את המסך הנוכחי
            return;
        }
    }
}
