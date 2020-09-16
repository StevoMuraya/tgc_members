package com.example.tgc_members_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText phone, password;
    private Button login;
    private ProgressBar loading;
    private RelativeLayout loading_screen;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String shared_user_id,shared_name,shared_phone,shared_date_time, shared_qr_code;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = getSharedPreferences("uza.conf", Context.MODE_PRIVATE);
        editor = pref.edit();

        shared_user_id = pref.getString("shared_user_id","");
        clsGlobal.shared_user_id = shared_user_id;

        shared_name = pref.getString("shared_name","");
        clsGlobal.shared_name = shared_name;

        shared_phone = pref.getString("shared_phone","");
        clsGlobal.shared_phone = shared_phone;

        shared_date_time = pref.getString("shared_date_time","");
        clsGlobal.shared_date_time = shared_date_time;

        shared_qr_code = pref.getString("shared_qr_code","");
        clsGlobal.shared_qr_code = shared_qr_code;


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

        loading = findViewById(R.id.loading_log);
        loading_screen = findViewById(R.id.loading_screen_login);

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
            Animation fade_in = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_in);

            phone.setEnabled(false);
            password.setEnabled(false);
            login.setVisibility(View.GONE);
            loading_screen.setVisibility(View.VISIBLE);
            loading.setVisibility(View.VISIBLE);
            loading.setAnimation(fade_in);
            loading_screen.setAnimation(fade_in);

            user_login(phone_z,pass_z);
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


    private void user_login(final String phone_z, final String pass_z){
        Animation fade_in = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_in);
        final Animation fade_out = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_out);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, clsGlobal.LOGIN_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("respone_new",response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("login");

                    if (success.equals("1")){
                        for (int i = 0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            String user_id_x = object.getString("user_id").trim();
                            String name_x = object.getString("name").trim();
                            String phone_x = object.getString("phone").trim();
                            String date_x = object.getString("date_time").trim();
                            String code_x = object.getString("qr_code").trim();

                            editor.putString("shared_user_id",user_id_x);
                            editor.putString("shared_name",name_x);
                            editor.putString("shared_phone", phone_x);
                            editor.putString("shared_date_time", date_x);
                            editor.putString("shared_qr_code", code_x);
                            editor.apply();

                            new CountDownTimer(2500, 4000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                }
                                @Override
                                public void onFinish() {
                                    login.setVisibility(View.VISIBLE);
                                    loading_screen.setVisibility(View.GONE);
                                    loading.setVisibility(View.GONE);
                                    loading_screen.setAnimation(fade_out);
                                    phone.setEnabled(true);
                                    password.setEnabled(true);
                                    Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();


                                    startActivity(new Intent(LoginActivity.this,HomePageActivity.class));
                                    finish();
                                }
                            }.start();
                        }
                    }else {
                        new CountDownTimer(2500, 4000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }
                            @Override
                            public void onFinish() {
                                login.setVisibility(View.VISIBLE);
                                loading_screen.setVisibility(View.GONE);
                                loading.setVisibility(View.GONE);
                                loading_screen.setAnimation(fade_out);
                                phone.setEnabled(true);
                                password.setEnabled(true);
                                Toast.makeText(LoginActivity.this, "Incorrect details used", Toast.LENGTH_SHORT).show();
                                Toast.makeText(LoginActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }.start();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    new CountDownTimer(2500, 4000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }
                        @Override
                        public void onFinish() {
                            login.setVisibility(View.VISIBLE);
                            loading_screen.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            loading_screen.setAnimation(fade_out);
                            phone.setEnabled(true);
                            password.setEnabled(true);
                            Toast.makeText(LoginActivity.this, "Failed to Login", Toast.LENGTH_SHORT).show();
                            Toast.makeText(LoginActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }.start();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new CountDownTimer(2500, 4000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }
                            @Override
                            public void onFinish() {
                                login.setVisibility(View.VISIBLE);
                                loading_screen.setVisibility(View.GONE);
                                loading.setVisibility(View.GONE);
                                loading_screen.setAnimation(fade_out);
                                phone.setEnabled(true);
                                password.setEnabled(true);
                                Toast.makeText(LoginActivity.this, "Failed to Login", Toast.LENGTH_SHORT).show();
                                Toast.makeText(LoginActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }.start();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone",phone_z);
                params.put("password",pass_z);
                return params;
            }
        };

        RequestQueue requestQueus = Volley.newRequestQueue(this);
        requestQueus.add(stringRequest);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this, SelectionActivity.class));
        finish();
    }
}