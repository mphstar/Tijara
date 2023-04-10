package com.tijara;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class BottonSheetProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_sheet_product);
    }
}

class ModelBottomSheet{
    String nama_produk, harga_pcs, harga_potongan;

    public ModelBottomSheet(String nama_produk, String harga_pcs, String harga_potongan) {
        this.nama_produk = nama_produk;
        this.harga_pcs = harga_pcs;
        this.harga_potongan = harga_potongan;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public String getHarga_pcs() {
        return harga_pcs;
    }

    public void setHarga_pcs(String harga_pcs) {
        this.harga_pcs = harga_pcs;
    }

    public String getHarga_potongan() {
        return harga_potongan;
    }

    public void setHarga_potongan(String harga_potongan) {
        this.harga_potongan = harga_potongan;
    }
}

