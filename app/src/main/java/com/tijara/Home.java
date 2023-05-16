package com.tijara;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class Home extends AppCompatActivity {

    FlexboxLayout to_transaksi, to_return;
    TextView nama;
    static String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        to_transaksi = findViewById(R.id.to_transaksi);
        to_return = findViewById(R.id.to_return);
        nama = findViewById(R.id.nama_user);

        to_transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Transaksi.class);
                startActivity(intent);
                if (Transaksi.dataModels != null){
                    if (ambilValues.namaProduk != null){
                        System.out.println("adadad");
                        Transaksi.view = 2;
                    } else if (ambilValues.nama_produk == null){
                        Transaksi.dataModels = null;
                    }
                } else if (ambilValues.namaProduk == null){
                    Transaksi.dataModels = null;
                    System.out.println("bbbbbbbbbbbbb");
                }
            }
        });

//        try {
//            JSONObject jsonObject = new JSONObject(Preferences.getLoggedInUser(getBaseContext()));
//            name = jsonObject.getString("nama");
//            System.out.println(jsonObject);
//            nama.setText("Hello "+name);
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
    }
}