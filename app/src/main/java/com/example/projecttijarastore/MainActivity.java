package com.example.projecttijarastore;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<ModelTransaksi> dataModels;
    RecyclerView materi;
    private static AdapterTransaksi adapterTransaksi;
    TextView kode_produk;
    private String KEY_KODE = "KODE_BARANG";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int view1 = 1;
        setContentView(R.layout.activity_main);

        ImageView button_add = findViewById(R.id.add_product);
        LinearLayout button_lanjut = findViewById(R.id.button_lanjut);

        materi = findViewById(R.id.list_pesanan);
        kode_produk = findViewById(R.id.result_kode);
        dataModels = new ArrayList<>();
        for (int i = 0; i <10; i++){
            dataModels.add(new ModelTransaksi("Dress Casual Pink", "Rp.210.000", "Rp.210.000", "Rp.150.000",  "0"," ","3", R.drawable.trush, R.drawable.trush));
        }
        dataModels.add(new ModelTransaksi("Dress Panjang Kondangan K..", "Rp.150.000", "Rp.150.000", "Rp.150.000", "0"," ","1", R.drawable.trush, R.drawable.trush));
        dataModels.add(new ModelTransaksi("Dress Casual Pink", "Rp.210.000", "Rp.210.000", "Rp.150.000", "0"," ","2", R.drawable.trush, R.drawable.trush));
        dataModels.add(new ModelTransaksi("Celana Chinos Buat Perang ...", "Rp.70.000", "Rp.70.000", "Rp.150.000", "0"," ","1", R.drawable.trush, R.drawable.trush));
        adapterTransaksi = new AdapterTransaksi(dataModels, getApplicationContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        materi.setLayoutManager(layoutManager);
        materi.setAdapter(adapterTransaksi);

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext(), R.style.dialog);
                View dialogView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.content_dialog_transaksi, null);
                builder.setView(dialogView);
                builder.setCancelable(true);
                builder.show();

                LinearLayout detail_dialog = dialogView.findViewById(R.id.detail_dialog);
                detail_dialog.setVisibility(View.GONE);
                final AlertDialog Close_dialog = builder.create();
                ImageView close_button =dialogView.findViewById(R.id.button_close);
                close_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                LinearLayout next_dialog_barcode = dialogView.findViewById(R.id.button_metode_barcode);
                next_dialog_barcode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                                scanBarcode();
                            } else {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                            }
                        } else {
                            scanBarcode();
                        }
                    }
                });
                LinearLayout next_dialog_keyboard = dialogView.findViewById(R.id.button_metode_keyboard);
                next_dialog_keyboard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view1 == 1) {
                            Intent intent = new Intent(MainActivity.this, TambahBarangActivity.class);
                            startActivity(intent);
                        }
                    }
                });
//                Intent intent = new Intent(MainActivity.this, TambahBarangActivity.class);
//                startActivity(intent);
            }
        });

        button_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PembayaranActivity.class);
                startActivity(intent);
            }
        });
    }

    private void scanBarcode() {
//        Intent intent = new Intent(getApplicationContext(), ScanBarcodeActivity.class);
//        startActivityForResult(intent, 1);
        ScanOptions options = new ScanOptions();
        options.setPrompt("Pindai Kode Produk");
        options.setBeepEnabled(true);
        options.setCaptureActivity(ScanBarcodeActivity.class);
        options.setOrientationLocked(true);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            kode_produk.setText(result.getContents());
//            String code_product = kode_produk.toString();
                Intent intent = new Intent(MainActivity.this, TambahBarangActivity.class);
                intent.putExtra(KEY_KODE, kode_produk.toString());
                startActivity(intent);
//            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//            builder.setTitle("Result");
//            builder.setMessage(result.getContents());
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.dismiss();
//                }
//            }).show();
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

class ModelTransaksi {

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

    public ModelTransaksi(String nama_produk, String potongan_harga, String harga, String sub_harga,  String total_harga_barang, String total_voucher, String value, int img, int trush) {
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