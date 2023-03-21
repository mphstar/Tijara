package com.example.projecttijarastore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;

public class TambahBarangActivity extends AppCompatActivity {
    ArrayList<ModelAddBarang> dataModels;
    RecyclerView materi;
    private static AdapterAddBarang adapterTransaksi;
    EditText kode, searchView;
    TextView anu;
    private String getKEY_KODE, KEY_KODE = "KODE_BARANG";
    ImageView searchScanKode, backTOMainTransaksi;
    LinearLayout linearLayout ;


    void loadProduct(){

        dataModels = new ArrayList<>();
        //        for (int i = 0; i <10; i++){
//            dataModels.add(new ModelTransaksi("Dress Casual Pink", "Rp.210.000", "Rp.210.000", "Rp.150.000",  "0"," ","3", R.drawable.shape_white, R.drawable.shape_white));
//        }
        dataModels.add(new ModelAddBarang("Dress Panjang Kondangan K..", "Rp.150.000", "Rp.150.000", "Rp.150.000", "0"," ","1", R.drawable.shape_white, R.drawable.shape_white));
        dataModels.add(new ModelAddBarang("Dress Casual Pink", "Rp.210.000", "Rp.210.000", "Rp.150.000", "0"," ","2", R.drawable.shape_white, R.drawable.shape_white));
        dataModels.add(new ModelAddBarang("Celana Chinos Buat Perang ...", "Rp.70.000", "Rp.70.000", "Rp.150.000", "0"," ","1", R.drawable.shape_white, R.drawable.shape_white));
        adapterTransaksi = new AdapterAddBarang(dataModels, getApplicationContext());
        materi = findViewById(R.id.list_barang);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TambahBarangActivity.this);
        materi.setLayoutManager(layoutManager);
        materi.setAdapter(adapterTransaksi);
    }

    private void filter(String teks) {

        System.out.println(teks.toString().length());
        ArrayList<ModelAddBarang> dataModels2 = new ArrayList<>();
        //looping through existing elements
        for (ModelAddBarang s : dataModels) {
            //if the existing elements contains the search input
            if (s.getNama_produk().toLowerCase().contains(teks.toLowerCase())) {
                //adding the element to filtered list
                dataModels2.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapterTransaksi.filterList(dataModels2, linearLayout, materi);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_barang);

        linearLayout = findViewById(R.id.image_no_value);
        searchScanKode = findViewById(R.id.scan_produk);
        backTOMainTransaksi = findViewById(R.id.back_to_main_transaksi);
        searchView = findViewById(R.id.field_kode_product);

        loadProduct();

        int invisible = linearLayout.getVisibility();
        if (invisible == View.VISIBLE && dataModels.stream().toArray().length == 0) {
            linearLayout.setVisibility(View.VISIBLE);
        }else {
            linearLayout.setVisibility(View.GONE);
        }

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            getKEY_KODE = extra.getString(KEY_KODE);
            searchView.setText(getKEY_KODE);
        }

        backTOMainTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() != 0){
                    materi.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    filter(editable.toString());
                }else if (editable.toString().length() == 0){
                    materi.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    loadProduct();
                }
            }
        });

        searchScanKode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(TambahBarangActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(TambahBarangActivity.this, Manifest.permission.CAMERA)) {
                        scanBarcode();
                    } else {
                        ActivityCompat.requestPermissions(TambahBarangActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                    }
                } else {
                    scanBarcode();
                }
            }
        });
    }

    private void scanBarcode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("scanBarcode1");
        options.setBeepEnabled(true);
        options.setCaptureActivity(ScanBarcodeActivity.class);
        options.setOrientationLocked(true);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            searchView.setText(result.getContents());
        }
    });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanBarcode();
            } else {
                Toast.makeText(this, "Gagal Memuat Kamera!!...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

class ModelAddBarang {

    String nama_produk;
    String potongan_harga;
    String harga;
    String sub_harga;
    String value;
    String total_harga_barang;
    String total_voucher;
    int img;
    int trush;
    String feature;

    public ModelAddBarang(String nama_produk, String potongan_harga, String harga, String sub_harga,  String total_harga_barang, String total_voucher, String value, int img, int trush) {
        this.nama_produk=nama_produk;
        this.potongan_harga=potongan_harga;
        this.harga=harga;
        this.sub_harga=sub_harga;
        this.total_harga_barang=total_harga_barang;
        this.total_voucher=total_voucher;
        this.value=value;
        this.img=img;
        this.trush=trush;
        this.feature=feature;

    }

    public String getNama_produk() {
        return nama_produk;
    }
    public String getPotongan_harga() {
        return potongan_harga;
    }

    public String getHarga() {
        return harga;
    }

    public String getSub_harga() {
        return sub_harga;
    }
    public String getValue() {
        return value;
    }


    public String getjumlahBarangPilih() {
        return total_harga_barang;
    }
    public String getTotal_harga_barang() {
        return total_voucher;
    }

    public int getImgs() {
        return img;
    }

    public int getTrush() {
        return trush;
    }

    public String getFeatures() {
        return feature;
    }

}