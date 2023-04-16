package com.tijara;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.flexbox.FlexboxLayout;

public class MainActivity extends AppCompatActivity {

    FlexboxLayout to_transaksi, to_return;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        to_transaksi = findViewById(R.id.to_transaksi);
        to_return = findViewById(R.id.to_return);

        to_transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Transaksi.class);
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
    }
}