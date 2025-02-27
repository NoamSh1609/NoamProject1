package com.noam.noamproject1.screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.noam.noamproject1.R;
import com.noam.noamproject1.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.noam.noamproject1.utils.Validator;


public class Register extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText etFname, etLname, etPhone, etEmail, etPassword;
    Button btnReg, btnGoToMain;
    String city;
    Spinner spCity;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        initViews();
        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        mAuth = FirebaseAuth.getInstance();
    }

    private void initViews() {
        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnReg = findViewById(R.id.btnReg);
        btnGoToMain = findViewById(R.id.btnGoToMain);
        spCity=findViewById(R.id.spcity);

        btnReg.setOnClickListener(this);
        btnGoToMain.setOnClickListener(this);
        spCity.setOnItemSelectedListener(this);
    }


    @Override
    public void onClick(View view) {
        if(view == btnReg) {
            String fname = etFname.getText().toString();
            String lname = etLname.getText().toString();
            String phone = etPhone.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if (checkInputFields(fname, lname, phone, email, password)) {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");

                            FirebaseUser User_fireuser = mAuth.getCurrentUser();
                            User newUser = new User(mAuth.getUid(), fname, lname, phone, email, password);//,false);
                            myRef.child(mAuth.getUid()).setValue(newUser);
                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putString("email", email);
                            editor.putString("password", password);
                            editor.commit();

                            Intent goLog = new Intent(Register.this, Login.class);
                            startActivity(goLog);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "הרשמה נכשלה",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                });

            }
        }
        else {
            Intent goToMain = new Intent(this, TheFirstViewOfTheApp.class);
            startActivity(goToMain);
        }
    }
    private boolean checkInputFields(String fname, String lname, String phone,  String email, String password) {
        return chkFirstName(fname) && chkLastName(lname) && chkPhone(phone) && chkEmail(email) && chkPassword(password);
    }




    //fname
    private boolean isBadChars(String txt) {
        String badChar = "~!@#$%^&:;. *()+=-/[,?]{}0123456789|";  // \!
        for (int i = 0; i < txt.length(); i++) {
            if (badChar.indexOf(txt.charAt(i)) != -1)
                return true;
        }
        return false;
    }
    private boolean isQuot(String txt) {
        if (txt.indexOf('"') != -1 || txt.indexOf("'") != -1)
            return true;
        return false;
    }
    private boolean chkFirstName(String txt) {
        String msg = "";
        boolean check = true;
        if (txt.length() < 2) {
            msg = "שם פרטי קצר מדי או לא קיים";
            etFname.setError(msg);
            check = false;
        }
        else if (txt.length() > 12) {
            msg = "שם פרטי יהיה עד 12 תווים";
            etFname.setError(msg);
            check = false;
        }
        else if (isBadChars(txt))
        {
            msg = "שם פרטי לא יכול להכיל ספרות או תווים אסורים כגון: ~!@#$%^&:;. *()+=-|[,?]{}";
            etFname.setError(msg);
            check = false;
        }
        else if (isQuot(txt)) {
            msg = "שם פרטי אינו יכול להכיל גרש או גרשיים";
            etFname.setError(msg);
            check = false;
        }

        return check;
    }

    //lname
    private boolean chkLastName(String txt) {
        String msg = "";
        boolean check = true;
        if (txt.length() < 2) {
            msg = "שם משפחה קצר מדי או לא קיים";
            etLname.setError(msg);
            check = false;
        }
        else if (txt.length() > 12) {
            msg = "שם פרטי יהיה עד 12 תווים";
            etLname.setError(msg);
            check = false;
        }
        else if (isBadChars(txt))
        {
            msg = "שם משפחה לא יכול להכיל ספרות או תווים אסורים כגון: ~!@#$%^&:;. *()+=-|[,?]{}";
            etLname.setError(msg);
            check = false;
        }
        else if (isQuot(txt)) {
            msg = "שם משפחה אינו יכול להכיל גרש או גרשיים";
            etLname.setError(msg);
            check = false;
        }

        return check;
    }

    //phone number
    private boolean chkPhone(String phone_number) {
        boolean check = true;
        if(phone_number.length() != 10) {
            etPhone.setError("מספר טלפון יהיה בעל 10 ספרות");
            check = false;
        }

        return check;
    }

    //email
    private boolean isValidEmail(String email) {
        String badChar = "~!#$%^&:; *()+=-/[,?]{}|<>";  // \!
        for (int i = 0; i < email.length(); i++) {
            if (badChar.indexOf(email.charAt(i)) != -1)
                return true;
        }
        return false;
    }
    private boolean isHebrew(String email) {
        int len = email.length();
        int i = 0, ch;
        while (i < len) {
            ch = email.charAt(i);
            if (ch >= 'א' && ch <= 'ת')
                return false;
            i++;
        }
        return true;
    }
    private boolean chkEmail(String email){
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //password
    private boolean chkPassword(String password) {
        return Validator.isPasswordValid(password);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        city=(String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        city=(String) parent.getItemAtPosition(0);

    }
}