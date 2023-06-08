package com.tijara;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {
    static EditText txtUsername, txtPassword;
    static ImageView showHidePassword;
    static TextView buttonLogin;
    static String nama;
    static JSONObject data;
    boolean isSHowPassword = false;

    CustomDialogSetup mDialog;

    private void setupDialog(CustomDialog type){
        mDialog = new CustomDialogSetup(this, R.style.dialog, type);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUsername = findViewById(R.id.field_username);
        txtPassword = findViewById(R.id.field_password);
        showHidePassword = findViewById(R.id.preview_password);
        buttonLogin = findViewById(R.id.buttonLogin);

        showHidePassword.setOnClickListener(view -> {
            if(isSHowPassword){
                txtPassword.setTransformationMethod(new PasswordTransformationMethod());
                showHidePassword.setImageResource(R.drawable.eye_show);
            } else {
                txtPassword.setTransformationMethod(null);
                showHidePassword.setImageResource(R.drawable.eye_no_show);
            }

            isSHowPassword = !isSHowPassword;
        });


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = txtUsername.getText().toString();
                String Pass = txtPassword.getText().toString();
                String errorM = "";
                if(Email.equals("")){
                    errorM = "Username wajib diisi";
                } else if(Pass.equals("")){
                    errorM = "Password wajib diisi";
                }

                if(!errorM.equals("")){
                    setupDialog(CustomDialog.WARNING);
                    mDialog.setJudul("Informasi");
                    mDialog.setDeskripsi(errorM);
                    mDialog.setListenerOK(v -> {
                        mDialog.dismiss();
                    });
                    mDialog.show();
                } else {

                    setupDialog(CustomDialog.LOADING);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setJudul("Loading");
                    mDialog.show();

                    String url = Env.BASE_URL + "login";
                    StringRequest strinRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    mDialog.dismiss();
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if(jsonObject.getString("status").equals("success")){
                                            JSONObject data = new JSONObject(jsonObject.getString("data"));
                                            setupDialog(CustomDialog.SUCCESS);
                                            mDialog.setJudul("Berhasil");
                                            mDialog.setDeskripsi(jsonObject.getString("message"));
                                            mDialog.setListenerOK(v -> {
                                                try {
                                                    ToHome(data.getString("nama"));
                                                } catch (JSONException e) {
                                                }
                                                Intent intent = new Intent(Login.this, Home.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            });
                                            mDialog.setCanceledOnTouchOutside(false);
                                            mDialog.show();

                                        } else{
                                            setupDialog(CustomDialog.ERROR);
                                            mDialog.setJudul("Gagal Login");
                                            mDialog.setDeskripsi(jsonObject.getString("message"));
                                            mDialog.setListenerOK(v -> {
                                                mDialog.dismiss();
                                            });
                                            mDialog.show();
                                        }
                                    }catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mDialog.dismiss();
                            setupDialog(CustomDialog.WARNING);
                            mDialog.setJudul("Informasi");
                            mDialog.setDeskripsi("Tidak ada koneksi");
                            mDialog.setListenerOK(v -> {
                                mDialog.dismiss();
                            });
                            mDialog.show();
                        }
                    }){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();

                            params.put("apikey", Env.API_KEY);
//                        params.put("ID", "1");
                            params.put("username",Email);
                            params.put("password",Pass);
//                        params.put("txt_title","SI Gandrung");
//                    params.put("txt_Message", Message);

                            return params;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(strinRequest);
                }

            }
        });
    }

    private void ToHome(String nama) {
        Preferences.setLoggedInUser(getBaseContext(), String.valueOf(nama));
        Preferences.setLoggedInStatus(getBaseContext(),true);
//        startActivity(new Intent(getBaseContext(), Home.class));
//        finish();
    }
}