package com.example.tgc_members_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SelectionActivity extends AppCompatActivity {
    private Button login, register,with_phone,without_phone;
    private int screen = 0;
    private LinearLayout screen1, screen2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        try {
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        login = findViewById(R.id.bt_selection_login);
        register = findViewById(R.id.bt_selection_register);
        with_phone = findViewById(R.id.bt_selection_adult);
        without_phone = findViewById(R.id.bt_selection_child);

        screen1 = findViewById(R.id.select_action_1);
        screen2 = findViewById(R.id.select_action_2);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectionActivity.this, LoginActivity.class));
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screen = 1;
                Animation fade_out = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_out);
                Animation fade_in = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_in);
                screen1.setVisibility(View.GONE);
                screen1.setAnimation(fade_out);
                screen2.setVisibility(View.VISIBLE);
                screen2.setAnimation(fade_in);
            }
        });

        with_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectionActivity.this, RegisterActivity.class));
                finish();
            }
        });

        without_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SelectionActivity.this, "Still under work", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (screen == 1){
            screen = 0;
            Animation fade_out = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_out);
            Animation fade_in = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_in);
            screen2.setVisibility(View.GONE);
            screen2.setAnimation(fade_out);
            screen1.setVisibility(View.VISIBLE);
            screen1.setAnimation(fade_in);
        }else{
            super.onBackPressed();
        }
    }
}
