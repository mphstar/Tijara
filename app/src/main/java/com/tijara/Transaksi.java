package com.tijara;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

class kirimValues{
    JsonObject jsonBawaData;
}
class AdapterTransaksi extends RecyclerView.Adapter<AdapterTransaksi.TransaksiViewHolder>{

    private ArrayList<ModelTransaksi> datalist;

    public AdapterTransaksi(ArrayList<ModelTransaksi> datalist, Context applicationContext){
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public TransaksiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_content, parent, false);
        return new TransaksiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaksiViewHolder holder, int position) {
        String name_product = datalist.get(position).getNama_produk();
        String price_product = datalist.get(position).getHarga();
        holder.txtNamaProduk.setText(datalist.get(position).getNama_produk());
        holder.txtHarga.setText(datalist.get(position).getHarga());
        holder.potonganHarga.setText(datalist.get(position).getPotongan_harga());
        holder.value.setText(datalist.get(position).getValue());
        holder.txtSubHarga.setText(datalist.get(position).getSub_harga());
        holder.img.setImageResource(datalist.get(position).getImgs());
        holder.trush.setImageResource(datalist.get(position).getTrush());
    }

    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() :0;
    }

    public class TransaksiViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNamaProduk, txtHarga, potonganHarga, value, txtSubHarga;
        private ImageView img, trush;
        private LinearLayout to_detail;
        private TextView tambah_produk;

        public TransaksiViewHolder(@NonNull View itemView) {
            super(itemView);
            tambah_produk = itemView.findViewById(R.id.tambah_produk);
            to_detail = itemView.findViewById(R.id.to_detail);
            txtNamaProduk = itemView.findViewById(R.id.name_product);
            txtHarga = itemView.findViewById(R.id.harga_pcs);
            potonganHarga = itemView.findViewById(R.id.potongan_harga);
            value = itemView.findViewById(R.id.value);
            txtSubHarga = itemView.findViewById(R.id.harga_total);
            img = itemView.findViewById(R.id.img);
            trush = itemView.findViewById(R.id.icon_sampah);
        }
    }
}

public class Transaksi extends AppCompatActivity {
    RecyclerView materi;
    TextView kode_produk;
    static JSONObject jsonObject;
    static JSONArray jsonArray;
    JSONArray nonProdukFree = null;
    static int view = 1, addProductUsing = 1;
    static ArrayList<ModelTransaksi> dataModels;
    private String KEY_KODE = "KODE_BARANG";
    private static AdapterTransaksi adapterTransaksi;

    void loadProduct(){

        if (addProductUsing == 1){
            dataModels.add(new ModelTransaksi(ambilValues.namaProduk, " ", ambilValues.hargaProduk, "Rp.150.000", "0"," ", ambilValues.jumlahPesanan, ambilValues.dataProdukFree, R.drawable.trush, R.drawable.trush));
            adapterTransaksi = new AdapterTransaksi(dataModels, getApplicationContext());

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Transaksi.this);
            materi.setLayoutManager(layoutManager);
            materi.setAdapter(adapterTransaksi);

            try {
                for (int i = 0; i < dataModels.size(); i++){
                    jsonObject.put("hargaProduk", dataModels.get(i).getHarga());
                    jsonObject.put("jumlahPesanan", dataModels.get(i).getValue());
                    jsonObject.put("namaProduk", dataModels.get(i).getNama_produk());
                    jsonObject.put("diskonProduk", dataModels.get(i).getPotongan_harga());
                    jsonObject.put("dataProdukFree", dataModels.get(i).getListProdukFree());
                }
                jsonArray.put(jsonObject);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else if (addProductUsing == 2){
            dataModels.add(new ModelTransaksi(ambilValues.nama_produk, ambilValues.diskon_produk, ambilValues.harga_produk, "Rp.150.000", "0"," ",ambilValues.jumlah_pesanan, nonProdukFree, R.drawable.trush, R.drawable.trush));
            adapterTransaksi = new AdapterTransaksi(dataModels, getApplicationContext());

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Transaksi.this);
            materi.setLayoutManager(layoutManager);
            materi.setAdapter(adapterTransaksi);

            try {
                for (int i = 0; i < dataModels.size(); i++){
                    jsonObject.put("hargaProduk", dataModels.get(i).getHarga());
                    jsonObject.put("jumlahPesanan", dataModels.get(i).getValue());
                    jsonObject.put("namaProduk", dataModels.get(i).getNama_produk());
                    jsonObject.put("diskonProduk", dataModels.get(i).getPotongan_harga());
                    jsonObject.put("dataProdukFree", dataModels.get(i).getListProdukFree());
                }
                jsonArray.put(jsonObject);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int view1 = 1;
        setContentView(R.layout.activity_transaksi);

        materi = findViewById(R.id.list_pesanan);
        kode_produk = findViewById(R.id.result_kode);
        ImageView button_add = findViewById(R.id.add_product);
        LinearLayout button_lanjut = findViewById(R.id.button_lanjut);

        if (dataModels == null){
//            materi.setVisibility(View.INVISIBLE);
            dataModels = new ArrayList<>();
            jsonArray = new JSONArray(dataModels);
            System.out.println("a");
        }else if (dataModels != null){
            if (view == 1){
                System.out.println("b");
                loadProduct();
            } else if (view == 2){
                System.out.println("b2");
                jsonObject = new JSONObject();
//                materi.setVisibility(View.VISIBLE);
//                System.out.println(ambilValues.jsonData);
                loadProduct();
                System.out.println("b3");
            }
        }

        // Space JsonArray

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog builder = new AlertDialog.Builder(view.getRootView().getContext(), R.style.dialog).create();
                View dialogView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.content_dialog_transaksi, null);
                builder.setView(dialogView);
                builder.setCancelable(true);
                builder.show();

                LinearLayout detail_dialog = dialogView.findViewById(R.id.detail_dialog);
                detail_dialog.setVisibility(View.GONE);
                ImageView close_button =dialogView.findViewById(R.id.button_close);
                close_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder.dismiss();
                    }
                });
                LinearLayout next_dialog_barcode = dialogView.findViewById(R.id.button_metode_barcode);
                next_dialog_barcode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ContextCompat.checkSelfPermission(Transaksi.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(Transaksi.this, Manifest.permission.CAMERA)) {
                                scanBarcode();
                            } else {
                                ActivityCompat.requestPermissions(Transaksi.this, new String[]{Manifest.permission.CAMERA}, 0);
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
                            Intent intent = new Intent(Transaksi.this, TambahBarangActivity.class);
                            startActivity(intent);
                        }
                    }
                });
//                Intent intent = new Intent(Transaksi.this, TambahBarangActivity.class);
//                startActivity(intent);
            }
        });

        button_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Transaksi.this, PembayaranActivity.class);
                startActivity(intent);
                System.out.println(jsonArray.toString() + " ini data JSONarray");
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
                Intent intent = new Intent(Transaksi.this, TambahBarangActivity.class);
                intent.putExtra(KEY_KODE, kode_produk.getText().toString());
                System.out.println(kode_produk.getText().toString());
                startActivity(intent);
//            AlertDialog.Builder builder = new AlertDialog.Builder(Transaksi.this);
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
    JSONArray ListProdukFree;
    String total_voucher;
    int img;
    int trush;
    String feature;

    public ModelTransaksi(String nama_produk, String potongan_harga, String harga, String sub_harga,  String total_harga_barang, String total_voucher, String value, JSONArray listProdukFree, int img, int trush) {
        this.nama_produk=nama_produk;
        this.potongan_harga=potongan_harga;
        this.harga=harga;
        this.sub_harga=sub_harga;
        this.total_harga_barang=total_harga_barang;
        this.total_voucher=total_voucher;
        this.value=value;
        this.ListProdukFree=listProdukFree;
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
    public JSONArray getListProdukFree(){
        return ListProdukFree;
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