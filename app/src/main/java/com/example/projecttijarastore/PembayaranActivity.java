package com.example.projecttijarastore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class PembayaranActivity extends AppCompatActivity {
    ArrayList<ModelTransaksi> dataModels;
    RecyclerView materi;
    private static AdapterPembayaran adapterPembayaran;
    ImageView button_voucher, backTOMainTransaksi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pembayaran);
//
//        dataModels = new ArrayList<>();
//
//        dataModels.add(new ModelTransaksi("Dress Panjang Kondangan K..", "Rp.150.000", "Rp.150.000", "Rp.150.000", "0"," ","1", R.drawable.shape_white, R.drawable.shape_white));
//        dataModels.add(new ModelTransaksi("Dress Casual Pink", "Rp.210.000", "Rp.210.000", "Rp.150.000", "0"," ","2", R.drawable.shape_white, R.drawable.shape_white));
//        dataModels.add(new ModelTransaksi("Celana Chinos Buat Perang ...", "Rp.70.000", "Rp.70.000", "Rp.150.000", "0"," ","1", R.drawable.shape_white, R.drawable.shape_white));
//        for (int i = 0; i <10; i++){
//            dataModels.add(new ModelTransaksi("Dress Casual Pink", "Rp.210.000", "Rp.210.000", "Rp.150.000",  "0"," ","3", R.drawable.shape_white, R.drawable.shape_white));
//        }
//        adapterPembayaran = new AdapterTransaksi(dataModels, getApplicationContext());
//
//        materi = findViewById(R.id.list_barang);
//
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TambahBarangActivity.this);
//        materi.setLayoutManager(layoutManager);
//        materi.setAdapter(adapterTransaksi);
//        backTOMainTransaksi = findViewById(R.id.back_to_view_transaksi);
//        backTOMainTransaksi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        button_voucher= findViewById(R.id.pilih_voucher);

        button_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PembayaranActivity.this, PilihVoucherActivity.class);
                startActivity(intent);
            }
        });
    }
}