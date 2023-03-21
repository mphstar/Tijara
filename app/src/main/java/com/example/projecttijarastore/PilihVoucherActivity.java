package com.example.projecttijarastore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class PilihVoucherActivity extends AppCompatActivity {
    ArrayList<modalVoucher> dataModels;
    RecyclerView voucher_nominal, voucher_persentase;
    private static AdapterVoucher adapterVoucher;

    ImageView backTOMainTransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pilih_voucher);

        dataModels = new ArrayList<>();

        dataModels.add(new modalVoucher("10%", "Rp.20.0000"));
        dataModels.add(new modalVoucher("10%", "Rp.20.0000"));
        dataModels.add(new modalVoucher("", "Rp.20.0000"));
        adapterVoucher = new AdapterVoucher(dataModels, getApplicationContext());

        voucher_nominal = findViewById(R.id.voucher_nominal);
        voucher_nominal.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PilihVoucherActivity.this);
        voucher_nominal.setLayoutManager(layoutManager);
        voucher_nominal.setAdapter(adapterVoucher);

        backTOMainTransaksi = findViewById(R.id.back_to_pembayaran);
        backTOMainTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

class modalVoucher {
    String voucher_persentase;
    String voucher_nominal;

    public modalVoucher(String voucher_persentase, String voucher_nominal) {
        this.voucher_persentase = voucher_persentase;
        this.voucher_nominal = voucher_nominal;
    }

    public String getVoucher_persentase() {
        return voucher_persentase;
    }

    public void setVoucher_persentase(String voucher_persentase) {
        this.voucher_persentase = voucher_persentase;
    }

    public String getVoucher_nominal() {
        return voucher_nominal;
    }

    public void setVoucher_nominal(String voucher_nominal) {
        this.voucher_nominal = voucher_nominal;
    }
}