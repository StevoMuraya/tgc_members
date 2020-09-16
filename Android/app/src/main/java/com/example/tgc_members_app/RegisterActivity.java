package com.example.tgc_members_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
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
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, phone, password, conf_password;
    private Button reg;
    private ProgressBar loading;
    private RelativeLayout loading_screen;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String shared_user_id,shared_name,shared_phone,shared_date_time, shared_qr_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        try {
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



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

        name = findViewById(R.id.et_name_reg);
        phone = findViewById(R.id.et_phone_register);
        password = findViewById(R.id.et_password_reg);
        conf_password = findViewById(R.id.et_confirm_password_reg);

        loading = findViewById(R.id.loading_reg);
        loading_screen = findViewById(R.id.loading_screen_reg);

        reg = findViewById(R.id.bt_reg_register);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempt_register();
            }
        });
    }


    public void attempt_register(){
        String name_z       = name.getText().toString();
        String phone_z       = phone.getText().toString();
        String pass_z       = password.getText().toString();
        String conf_pass_z      = conf_password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name_z)) {
            name.setError("Your name is required here");
            focusView = name;
            cancel = true;
        }else if (TextUtils.isEmpty(phone_z)) {
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
        }else if (TextUtils.isEmpty(conf_pass_z)) {
            password.setError("Please confirm your password");
            focusView = password;
            cancel = true;
        }else if (!confirmPass(conf_pass_z)) {
            conf_password.setError("Passwords don't match!");
            focusView = conf_password;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }else{
            Animation fade_in = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_in);

            name.setEnabled(false);
            phone.setEnabled(false);
            password.setEnabled(false);
            conf_password.setEnabled(false);
            reg.setVisibility(View.GONE);
            loading_screen.setVisibility(View.VISIBLE);
            loading.setVisibility(View.VISIBLE);
            loading.setAnimation(fade_in);
            loading_screen.setAnimation(fade_in);

            check_phone(name_z,phone_z,pass_z);
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
    private boolean confirmPass(String conf) {
        //TODO: Replace this with your own logic
        return conf.equals(password.getText().toString());
    }

    private void check_phone(final String name_z, final String phone_z, final String password_z){

        Animation fade_in = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_in);
        final Animation fade_out = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_out);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, clsGlobal.CHECK_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response_new",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.optString("success");
                    String message = jsonObject.optString("message");
                    JSONArray jsonArray = jsonObject.getJSONArray("login");

                    if (success.equalsIgnoreCase("1")){
                        new CountDownTimer(2000, 4000) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                            }
                            @Override
                            public void onFinish() {
                                reg.setVisibility(View.VISIBLE);
                                loading_screen.setVisibility(View.GONE);
                                loading.setVisibility(View.GONE);
                                loading_screen.setAnimation(fade_out);
                                name.setEnabled(true);
                                phone.setEnabled(true);
                                password.setEnabled(true);
                                conf_password.setEnabled(true);

                                duplicateFound(RegisterActivity.this).show();
                            }

                        }.start();
                    }else{
                        registerUser(name_z, phone_z, password_z);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    new CountDownTimer(2000, 4000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                        }
                        @Override
                        public void onFinish() {
                            reg.setVisibility(View.VISIBLE);
                            loading_screen.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            loading_screen.setAnimation(fade_out);
                            name.setEnabled(true);
                            phone.setEnabled(true);
                            password.setEnabled(true);
                            conf_password.setEnabled(true);
                            Toast.makeText(RegisterActivity.this, "Failed to check details", Toast.LENGTH_SHORT).show();
                            Toast.makeText(RegisterActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                        }

                    }.start();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new CountDownTimer(2000, 4000) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                            }
                            @Override
                            public void onFinish() {
                                reg.setVisibility(View.VISIBLE);
                                loading_screen.setVisibility(View.GONE);
                                loading.setVisibility(View.GONE);
                                loading_screen.setAnimation(fade_out);
                                name.setEnabled(true);
                                phone.setEnabled(true);
                                password.setEnabled(true);
                                conf_password.setEnabled(true);
                                Toast.makeText(RegisterActivity.this, "Failed to check details", Toast.LENGTH_SHORT).show();
                                Toast.makeText(RegisterActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                            }

                        }.start();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone",phone_z);
                return params;
            }
        };

        RequestQueue requestQueus = Volley.newRequestQueue(this);
        requestQueus.add(stringRequest);
    }




    private void registerUser(final String name_x, final String phone_x, final String password_x){
        Animation fade_in = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_in);
        final Animation fade_out = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_out);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, clsGlobal.REGISTER_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        if (response.equalsIgnoreCase("Successfully Registered")){
                            user_login(phone_x,password_x);
                            Toast.makeText(RegisterActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
                        }else{
                            new CountDownTimer(2500, 4000) {

                                @Override
                                public void onTick(long millisUntilFinished) {
                                }
                                @Override
                                public void onFinish() {
                                    reg.setVisibility(View.VISIBLE);
                                    loading_screen.setVisibility(View.GONE);
                                    loading.setVisibility(View.GONE);
                                    loading_screen.setAnimation(fade_out);
                                    name.setEnabled(true);
                                    phone.setEnabled(true);
                                    password.setEnabled(true);
                                    conf_password.setEnabled(true);
                                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
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
                                reg.setVisibility(View.VISIBLE);
                                loading_screen.setVisibility(View.GONE);
                                loading.setVisibility(View.GONE);
                                loading_screen.setAnimation(fade_out);
                                name.setEnabled(true);
                                phone.setEnabled(true);
                                password.setEnabled(true);
                                conf_password.setEnabled(true);
                                Toast.makeText(RegisterActivity.this, "Failed to send details", Toast.LENGTH_SHORT).show();
                                Toast.makeText(RegisterActivity.this, "Please try again later", Toast.LENGTH_SHORT).show();
                            }

                        }.start();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name",name_x);
                params.put("phone",phone_x);
                params.put("password",password_x);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.add(stringRequest);
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
                                    reg.setVisibility(View.VISIBLE);
                                    loading_screen.setVisibility(View.GONE);
                                    loading.setVisibility(View.GONE);
                                    loading_screen.setAnimation(fade_out);
                                    name.setEnabled(true);
                                    phone.setEnabled(true);
                                    password.setEnabled(true);
                                    conf_password.setEnabled(true);
                                    Toast.makeText(RegisterActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();


                                    startActivity(new Intent(RegisterActivity.this,HomePageActivity.class));
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
                                reg.setVisibility(View.VISIBLE);
                                loading_screen.setVisibility(View.GONE);
                                loading.setVisibility(View.GONE);
                                loading_screen.setAnimation(fade_out);
                                name.setEnabled(true);
                                phone.setEnabled(true);
                                password.setEnabled(true);
                                conf_password.setEnabled(true);
                                Toast.makeText(RegisterActivity.this, "Incorrect details used", Toast.LENGTH_SHORT).show();
                                Toast.makeText(RegisterActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
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
                            reg.setVisibility(View.VISIBLE);
                            loading_screen.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            loading_screen.setAnimation(fade_out);
                            name.setEnabled(true);
                            phone.setEnabled(true);
                            password.setEnabled(true);
                            conf_password.setEnabled(true);
                            Toast.makeText(RegisterActivity.this, "Failed to Login", Toast.LENGTH_SHORT).show();
                            Toast.makeText(RegisterActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
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
                                reg.setVisibility(View.VISIBLE);
                                loading_screen.setVisibility(View.GONE);
                                loading.setVisibility(View.GONE);
                                loading_screen.setAnimation(fade_out);
                                name.setEnabled(true);
                                phone.setEnabled(true);
                                password.setEnabled(true);
                                conf_password.setEnabled(true);
                                Toast.makeText(RegisterActivity.this, "Failed to Login", Toast.LENGTH_SHORT).show();
                                Toast.makeText(RegisterActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
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





    public AlertDialog.Builder duplicateFound(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage("The phone number you used to register already exists in the system,\n" +
                "Please try again using a different phone number");
        builder.setTitle("Duplicate Found");

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Animation fade_out = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_out);
                reg.setVisibility(View.VISIBLE);
                loading_screen.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                loading_screen.setAnimation(fade_out);
                name.setEnabled(true);
                phone.setEnabled(true);
                password.setEnabled(true);
                conf_password.setEnabled(true);
                phone.setText("");
                password.setText("");
                conf_password.setText("");
            }
        });

        builder.setNegativeButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        });
        return builder;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, SelectionActivity.class));
        finish();
    }
}
