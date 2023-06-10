package com.tijara;

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
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.tijara.keranjang.DataKeranjang;
import com.tijara.keranjang.KategoriDiskon;
import com.tijara.keranjang.ModelKeranjang;

import java.util.ArrayList;

class AdapterKeranjang extends RecyclerView.Adapter<AdapterKeranjang.RecyclerViewViewHolder>{
    private ArrayList<ModelKeranjang> dataList;
    private Context context;
    RecyclerViewListener listener;

    public AdapterKeranjang(ArrayList<ModelKeranjang> dataList, RecyclerViewListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layout = LayoutInflater.from(parent.getContext());
        View view = layout.inflate(R.layout.content_keranjang, parent, false);
        return new AdapterKeranjang.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
        holder.nama.setText(dataList.get(position).getNama());

        holder.subtotal.setText(Env.formatRupiah(dataList.get(position).getSubtotal()));
        if(dataList.get(position).getDiskon() != null){

            if(dataList.get(position).getDiskon().getKategori() == KategoriDiskon.NOMINAL){
                holder.harga_asli.setText(Env.formatRupiah(dataList.get(position).getHarga()));
                holder.harga.setText(Env.formatRupiah(dataList.get(position).getHarga() - Integer.parseInt(dataList.get(position).getDiskon().getNominal())));
                holder.harga_asli.setPaintFlags(holder.harga_asli.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else if(dataList.get(position).getDiskon().getKategori() == KategoriDiskon.PERSEN){
                holder.harga_asli.setText(Env.formatRupiah(dataList.get(position).getHarga()));
                holder.harga.setText(Env.formatRupiah(dataList.get(position).getHarga() - ( dataList.get(position).getHarga() / 100 * Integer.parseInt(dataList.get(position).getDiskon().getNominal()) ) ));
                holder.harga_asli.setPaintFlags(holder.harga_asli.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else if(dataList.get(position).getDiskon().getKategori() == KategoriDiskon.FREE_PRODUK){
                holder.harga_asli.setText("");
                holder.harga.setText(Env.formatRupiah(dataList.get(position).getHarga()));
            }

        } else {
            holder.harga.setText(Env.formatRupiah(dataList.get(position).getHarga()));
            holder.harga_asli.setText("");
        }
        holder.qty.setText("x" + String.valueOf(dataList.get(position).getQty()));
        Glide.with(context).load(dataList.get(position).getPathImage()).centerCrop().placeholder(R.drawable.tshirt).into(holder.img_tambah_barang);
    }


    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        TextView nama, harga_asli, harga, subtotal, qty;

        ImageView img_tambah_barang, icon_sampah;
        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.name_product);
            harga_asli = itemView.findViewById(R.id.potongan_harga);
            harga = itemView.findViewById(R.id.harga_pcs);
            subtotal = itemView.findViewById(R.id.harga_total);
            qty = itemView.findViewById(R.id.value);
            img_tambah_barang = itemView.findViewById(R.id.img_tambah_barang);

            icon_sampah = itemView.findViewById(R.id.icon_sampah);
            icon_sampah.setOnClickListener(view -> {
                listener.onClick(view, getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            });
        }
    }
}

public class Transaksi extends AppCompatActivity {
    RecyclerView materi;
    TextView kode_produk, jumlahList, jumlahNominalHarga;
    ImageView button_add, backTOMainDashboard;
    AlertDialog builder;
    LinearLayout button_lanjut;

    CustomDialogSetup mDialog;

    private void setupDialog(CustomDialog type){
        mDialog = new CustomDialogSetup(this, R.style.dialog, type);
    }

    private void loadKeranjang(){
        materi.setLayoutManager(new LinearLayoutManager(Transaksi.this));
        AdapterKeranjang adapt = new AdapterKeranjang(DataKeranjang.dataKeranjang, new RecyclerViewListener() {
            @Override
            public void onClick(View view, int position) {
                setupDialog(CustomDialog.CONFIRMATION);
                mDialog.setJudul("Hapus");
                mDialog.setDeskripsi("Apakah anda yakin ingin menghapus data ini dalam keranjang");
                mDialog.setListenerTidak(v -> {
                    mDialog.dismiss();
                });

                mDialog.setListenerOK(v -> {
                    DataKeranjang.dataKeranjang.remove(position);
                    hitungTotal();
                });

                mDialog.show();

            }
        });

        materi.setAdapter(adapt);

        hitungTotal();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int view1 = 1;
        String convertJumlahHarga;
        setContentView(R.layout.activity_transaksi);

        materi = findViewById(R.id.list_pesanan);
        kode_produk = findViewById(R.id.result_kode);
        button_add = findViewById(R.id.add_product);
        backTOMainDashboard = findViewById(R.id.backTOMainDashboard);
        jumlahList = findViewById(R.id.jumlahList);
        jumlahNominalHarga = findViewById(R.id.jumlah_nominal_harga);
        button_lanjut = findViewById(R.id.button_lanjut);

        loadKeranjang();
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(view.getRootView().getContext(), R.style.dialog).create();
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
                        if (ContextCompat.checkSelfPermission(Transaksi.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(Transaksi.this, android.Manifest.permission.CAMERA)) {
                                scanBarcode();
                            } else {
                                ActivityCompat.requestPermissions(Transaksi.this, new String[]{Manifest.permission.CAMERA}, 0);
                            }
                        } else {
                            scanBarcode();
                        }
//                        scan

                    }
                });
                LinearLayout next_dialog_keyboard = dialogView.findViewById(R.id.button_metode_keyboard);
                next_dialog_keyboard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(Transaksi.this, TambahBarangActivity.class);
                        startActivityForResult(in, 1);
                    }
                });
//                Intent intent = new Intent(Transaksi.this, TambahBarangActivity.class);
//                startActivity(intent);
            }
        });

        backTOMainDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < DataKeranjang.dataKeranjang.size(); i++){
                    System.out.println(DataKeranjang.dataKeranjang.get(i).getAllData());
                }
            }
        });

        button_lanjut.setOnClickListener(view -> {
            if(DataKeranjang.dataKeranjang.size() == 0){
                setupDialog(CustomDialog.WARNING);
                mDialog.setJudul("Informasi");
                mDialog.setDeskripsi("Keranjang tidak boleh kosong");
                mDialog.setListenerOK(v -> {
                    mDialog.dismiss();
                });

                mDialog.show();

            } else {
                Intent inten = new Intent(Transaksi.this, PembayaranActivity.class);
                startActivity(inten);
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                if(data.getStringExtra("status").equals("success")){
                    builder.dismiss();
                    loadKeranjang();
                }
            } else if(resultCode == RESULT_CANCELED){
                if(data.getStringExtra("status").equals("back")){
                    builder.dismiss();
                }
            }
        }
    }

    private void scanBarcode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan kode barang");
        options.setBeepEnabled(true);
        options.setCaptureActivity(ScanBarcodeActivity.class);
        options.setOrientationLocked(true);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            Intent in = new Intent(Transaksi.this, TambahBarangActivity.class);
            in.putExtra("kode", result.getContents());
            startActivityForResult(in, 1);
//            field_kode_product.setText(result.getContents());
        }
    });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanBarcode();
            } else {
                setupDialog(CustomDialog.ERROR);
                mDialog.setJudul("Gagal");
                mDialog.setDeskripsi("Gagal mengakses kamera");
                mDialog.setListenerOK(v -> {
                    mDialog.dismiss();
                });

                mDialog.show();
            }
        }

    }

    private void hitungTotal(){
        int total = 0;
        int jumlah = 0;
        for (int i = 0; i < DataKeranjang.dataKeranjang.size(); i++){
            jumlah++;
            total = total + DataKeranjang.dataKeranjang.get(i).getSubtotal();
        }

        jumlahList.setText(String.valueOf(jumlah));
        jumlahNominalHarga.setText("Rp. " + Env.formatRupiah(total));
    }

}
