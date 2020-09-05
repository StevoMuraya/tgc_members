package com.example.tgc_members_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText name, phone, password, conf_password;
    private Button reg;

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

        name = findViewById(R.id.et_name_reg);
        phone = findViewById(R.id.et_phone_register);
        password = findViewById(R.id.et_password_reg);
        conf_password = findViewById(R.id.et_confirm_password_reg);

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
            //Toast.makeText(this, "Good to go", Toast.LENGTH_SHORT).show();
            //check_phone(name_z,phone_z,pass_z);
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
        return conf.equals(conf_password.getText().toString());
    }

    private void check_phone(final String name_z, final String phone_z, final String password_z){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, clsGlobal.CHECK_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("response_new",response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.optString("success");
                    String message = jsonObject.optString("message");
                    JSONArray jsonArray = jsonObject.getJSONArray("login");

                    if (success.equalsIgnoreCase("1")){
                        new CountDownTimer(2500, 4000) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                            }
                            @Override
                            public void onFinish() {
                                register.setVisibility(View.VISIBLE);
                                loading_screen.animate().alpha(0f).setDuration(500);
                                loading.setVisibility(View.GONE);
                                fname.setEnabled(true);
                                lname.setEnabled(true);
                                email.setEnabled(true);
                                phone.setEnabled(true);
                                county.setEnabled(true);
                                pass.setEnabled(true);
                                confirm.setEnabled(true);
                                duplicateFound(UserRegistrationActivity.this).show();
                            }

                        }.start();
                    }else{
                        registerUser(fname_z,lname_z,email_z,phone_z,county_z,pass_z);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    new CountDownTimer(2500, 4000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                        }
                        @Override
                        public void onFinish() {
                            register.setVisibility(View.VISIBLE);
                            loading_screen.animate().alpha(0f).setDuration(500);
                            loading.setVisibility(View.GONE);
                            fname.setEnabled(true);
                            lname.setEnabled(true);
                            email.setEnabled(true);
                            phone.setEnabled(true);
                            pass.setEnabled(true);
                            confirm.setEnabled(true);
                            Toast.makeText(UserRegistrationActivity.this, "Failed to check details", Toast.LENGTH_SHORT).show();
                            Toast.makeText(UserRegistrationActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
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
                                register.setVisibility(View.VISIBLE);
                                loading_screen.animate().alpha(0f).setDuration(500);
                                loading.setVisibility(View.GONE);
                                fname.setEnabled(true);
                                lname.setEnabled(true);
                                email.setEnabled(true);
                                phone.setEnabled(true);
                                pass.setEnabled(true);
                                confirm.setEnabled(true);
                                Toast.makeText(UserRegistrationActivity.this, "Failed to check details", Toast.LENGTH_SHORT).show();
                                Toast.makeText(UserRegistrationActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                            }

                        }.start();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",email_z);
                params.put("phone",phone_z);
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
