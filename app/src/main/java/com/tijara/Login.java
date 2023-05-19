package com.tijara;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class Login extends AppCompatActivity {
    static EditText txtUsername, txtPassword;
    static ImageView showHidePassword;
    static TextView buttonLogin;
    static String nama;
    static JSONObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername = findViewById(R.id.field_username);
        txtPassword = findViewById(R.id.field_password);
        showHidePassword = findViewById(R.id.preview_password);
        buttonLogin = findViewById(R.id.buttonLogin);

        showHidePassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Show password
                        txtPassword.setTransformationMethod(null);
                        break;
                    case MotionEvent.ACTION_UP:
                        // Hide password
                        txtPassword.setTransformationMethod(new PasswordTransformationMethod());
                        break;
                }
                return false;
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = txtUsername.getText().toString();
                String Pass = txtPassword.getText().toString();
                String Message = "Hai "+Email+" Selamat datang Kembali :)";
//            System.out.println(Nama);

                String url = "http://192.168.0.111:8000/api/login";
                StringRequest strinRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if(jsonObject.getString("status").equals("success")){
                                        JSONObject data = new JSONObject(jsonObject.getString("data"));
                                        ToHome(data.getString("nama"));
                                        Intent intent = new Intent(Login.this, Home.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    } else{
                                        Toast.makeText(Login.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
//                                    System.out.println(jsonObject);
//                                    String status = jsonObject.getString("status");
//                                    String massage = jsonObject.getString("msg");
//                                    data = jsonObject.getJSONObject("data");
////                                    JSONObject jsonDATA = new JSONObject(data);
////                                    Login.nama = data.getString("nama");
//                                    if (status.equals("success")) {
////                                    msg.setText(id_pegawai);
//                                        ToHome();
////                                        Toast.makeText(LoginActivity.this, massage, Toast.LENGTH_LONG).show();
////                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
////                                    startActivity(intent);
//                                    }else {
//                                        System.out.println("error");
////                                        Toast.makeText(LoginActivity.this, massage, Toast.LENGTH_LONG).show();
//                                    }
                                }catch (JSONException e) {

                                    throw new RuntimeException(e);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        params.put("apikey","DWuqUHWDUhDQUDadaq");
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
        });
    }

    private void ToHome(String nama) {
        Preferences.setLoggedInUser(getBaseContext(), String.valueOf(nama));
        Preferences.setLoggedInStatus(getBaseContext(),true);
//        startActivity(new Intent(getBaseContext(), Home.class));
//        finish();
    }
}