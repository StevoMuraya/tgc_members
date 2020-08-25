package com.example.tgc_members_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView name,name2;
    private CardView logo;
    private ImageView logo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        name = findViewById(R.id.page_splash_name);
        name2 = findViewById(R.id.page_splash_name2);
        logo = findViewById(R.id.splash_logo_card);
        logo2 = findViewById(R.id.splash_logo);

        name.animate().alpha(1f).setDuration(1000);
        name2.animate().alpha(1f).setDuration(1300);
        logo.animate().alpha(1f).setDuration(1700);
        logo2.animate().alpha(1f).setDuration(1700);

        new CountDownTimer(2000, 4000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                Animation rotate  = AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotate);
                logo2.setVisibility(View.VISIBLE);
                logo2.setAnimation(rotate);new CountDownTimer(2500, 4000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                    }
                    @Override
                    public void onFinish() {
                        startActivity(new Intent(MainActivity.this, SelectionActivity.class));
                        finish();
                    }

                }.start();
            }

        }.start();

    }
}
