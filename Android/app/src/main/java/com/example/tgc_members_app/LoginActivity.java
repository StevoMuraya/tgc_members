package com.example.tgc_members_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText phone, password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        phone = findViewById(R.id.et_phone_login);
        password = findViewById(R.id.et_password_login);

        login = findViewById(R.id.bt_log_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempt_login();
            }
        });

    }



    public void attempt_login(){
        String phone_z       = phone.getText().toString();
        String pass_z       = password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(phone_z)) {
            phone.setError("Enter your phone number to proceed");
            focusView = phone;
            cancel = true;
        }else if (!isPhoneValid(phone_z)) {
            phone.setError("Phone number is too short");
            focusView = phone;
            cancel = true;
        }else if (!isPhoneValid2(phone_z)) {
            phone.setError("Phone number is invalid");
            focusView = phone;
            cancel = true;
        }else if (TextUtils.isEmpty(pass_z)) {
            password.setError("Set a new password to proceed");
            focusView = password;
            cancel = true;
        }else if (!isPassValid(pass_z)) {
            password.setError("password is too short");
            focusView = password;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }else{
            Toast.makeText(this, "Good to go", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPhoneValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 9;
    }
    private boolean isPhoneValid2(String phone) {
        //TODO: Replace this with your own logic
        return phone.startsWith("07");
    }
    private boolean isPassValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this, SelectionActivity.class));
        finish();
    }
}