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
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

class kirimValues{
    JsonObject jsonBawaData;
    static int total = 0;
}
class AdapterTransaksi extends RecyclerView.Adapter<AdapterTransaksi.TransaksiViewHolder>{

    private ArrayList<ModelTransaksi> datalist;
    RecyclerViewListener listener;
    private Context context;

    public AdapterTransaksi(ArrayList<ModelTransaksi> datalist, RecyclerViewListener listener){
        this.datalist = datalist;
        this.listener = listener;
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
        String image_produk = datalist.get(position).getGambar_br();
        Glide.with(holder.img).load(image_produk).centerCrop().placeholder(R.drawable.tshirt).into(holder.img);
        holder.txtNamaProduk.setText(datalist.get(position).getNama_br());
        holder.txtHargaTotal.setText(allTypeData.format.format(datalist.get(position).getTotal_harga_br()));
        holder.value.setText(String.valueOf(datalist.get(position).getQty()));
        holder.trush.setImageResource(datalist.get(position).getTrash());

        if (datalist.get(position).getJenis_diskon().equals("nominal") || datalist.get(position).getJenis_diskon().equals("persen")){
            holder.potonganHarga.setText(Env.formatRupiah(datalist.get(position).getPotongan_harga_br()));
            holder.txtHargaAsli.setText(Env.formatRupiah(datalist.get(position).getHarga_br()));
            holder.txtHargaAsli.setPaintFlags(holder.potonganHarga.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else if (datalist.get(position).getJenis_diskon().equals("tidak_diskon")){
            holder.potonganHarga.setText(Env.formatRupiah(datalist.get(position).getHarga_br()));
            holder.txtHargaAsli.setVisibility(View.GONE);
        } else {
            holder.potonganHarga.setText(Env.formatRupiah(datalist.get(position).getHarga_br()));
            holder.txtHargaAsli.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() :0;
    }

    public class TransaksiViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private TextView txtNamaProduk, txtHargaTotal, potonganHarga, value, txtHargaAsli;
        private ImageView img, trush;
        private LinearLayout to_detail;
        private TextView tambah_produk;

        public TransaksiViewHolder(@NonNull View itemView) {
            super(itemView);
            tambah_produk = itemView.findViewById(R.id.tambah_produk);
            to_detail = itemView.findViewById(R.id.to_detail);
            txtNamaProduk = itemView.findViewById(R.id.name_product);
            txtHargaTotal = itemView.findViewById(R.id.harga_pcs);
            potonganHarga = itemView.findViewById(R.id.potongan_harga);
            value = itemView.findViewById(R.id.value);
            txtHargaAsli = itemView.findViewById(R.id.harga_total);
            img = itemView.findViewById(R.id.img_keranjang);
            trush = itemView.findViewById(R.id.icon_sampah);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
}

public class Transaksi extends AppCompatActivity implements RecyclerViewListener{
    RecyclerView materi;
    static TextView kode_produk, jumlahList, jumlahNominalHarga;
    ImageView button_add, backTOMainDashboard;
    static JSONObject jsonObject;
    static String kode_barang;
    static JSONArray jsonArray;
    static JSONArray list_produk_dipesan;
    static Map<String, JSONObject> params;
    JSONArray nonProdukFree = null;
    static int view = 1, addProductUsing = 1, total_harga_br = 0;
    static ArrayList<ModelTransaksi> dataModels;
    private String KEY_KODE = "KODE_BARANG";
    private static AdapterTransaksi adapterTransaksi;


//    void loadProduct(){
//
//        if (addProductUsing == 1){
//            if (ambilValues.namaProduk.equals("")){
//                System.out.println("No products selected");
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Transaksi.this);
//                materi.setLayoutManager(layoutManager);
//                materi.setAdapter(adapterTransaksi);
//            }else {
//                dataModels.add(new ModelTransaksi(ambilValues.kodeProduk, ambilValues.namaProduk, ambilValues.hargaAsliProduk,  ambilValues.hargaProduk, "0", "0"," ", ambilValues.jumlahPesanan, ambilValues.dataProdukFree, ambilValues.image_produk, R.drawable.trush));
//                adapterTransaksi = new AdapterTransaksi(dataModels, getApplicationContext());
//
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Transaksi.this);
//                materi.setLayoutManager(layoutManager);
//                materi.setAdapter(adapterTransaksi);
//
//                try {
//                    for (int i = 0; i < dataModels.size(); i++){
//                        kirimValues.total += Integer.valueOf(dataModels.get(i).getPotongan_harga());
//                        jsonObject.put("kodeProduk", dataModels.get(i).getKode_barang());
//                        jsonObject.put("hargaProduk", dataModels.get(i).getHarga());
//                        jsonObject.put("subHarga", dataModels.get(i).getSub_harga());
//                        jsonObject.put("jumlahPesanan", dataModels.get(i).getValue());
//                        jsonObject.put("namaProduk", dataModels.get(i).getNama_produk());
//                        jsonObject.put("diskonProduk", dataModels.get(i).getPotongan_harga());
//                        jsonObject.put("jenisDiskon", "Free_produk");
//                        jsonObject.put("nominalDiskon", dataModels.get(i).getNominal_diskon());
//                        jsonObject.put("dataProdukFree", dataModels.get(i).getListProdukFree());
//                    }
////                    params.put(String.valueOf(dataModels.size()), jsonObject);
//                    jsonArray.put(jsonObject);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//
//                ambilValues.namaProduk = "";
//                ambilValues.hargaProduk = "";
//                ambilValues.dataProdukFree = null;
//                ambilValues.jumlahPesanan = "";
//                ambilValues.image_produk = "0";
//                view = 1;
//            }
//        }else if (addProductUsing == 2){
//            if (ambilValues.nama_produk.equals("")){
//                System.out.println("No Products selected");
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Transaksi.this);
//                materi.setLayoutManager(layoutManager);
//                materi.setAdapter(adapterTransaksi);
//            }else {
//                dataModels.add(new ModelTransaksi(ambilValues.kode_produk, ambilValues.nama_produk, ambilValues.diskon_produk, ambilValues.harga_asli_produk, ambilValues.harga_produk, ambilValues.nominal_diskon," ",ambilValues.jumlah_pesanan, nonProdukFree, ambilValues.image_produk, R.drawable.trush));
//                adapterTransaksi = new AdapterTransaksi(dataModels, getApplicationContext());
//
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Transaksi.this);
//                materi.setLayoutManager(layoutManager);
//                materi.setAdapter(adapterTransaksi);
//
//                try {
//                    for (int i = 0; i < dataModels.size(); i++){
//                        kirimValues.total += Integer.valueOf(dataModels.get(i).getPotongan_harga());
//                        jsonObject.put("kodeProduk", dataModels.get(i).getKode_barang());
//                        jsonObject.put("hargaProduk", dataModels.get(i).getHarga());
//                        jsonObject.put("subHarga", dataModels.get(i).getSub_harga());
//                        jsonObject.put("jumlahPesanan", dataModels.get(i).getValue());
//                        jsonObject.put("namaProduk", dataModels.get(i).getNama_produk());
//                        jsonObject.put("diskonProduk", dataModels.get(i).getPotongan_harga());
//                        jsonObject.put("jenisDiskon", "Non_free_produk");
//                        if (Integer.parseInt(dataModels.get(i).getNominal_diskon()) == 0) {
//                            jsonObject.put("nominalDiskon", "0");
//                        }else {
//                            jsonObject.put("nominalDiskon", dataModels.get(i).getNominal_diskon());
//                        }
//                        if (dataModels.get(i).getListProdukFree() == null){
//                            jsonObject.put("dataProdukFree", "null");
//                        }
//                    }
////                    params.put(String.valueOf(dataModels.size()), jsonObject);
//                    jsonArray.put(jsonObject);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//
//                ambilValues.nama_produk = "";
//                ambilValues.harga_produk = "";
//                ambilValues.diskon_produk = "";
//                ambilValues.jumlah_pesanan = "";
//                ambilValues.image_produk = "";
//                view = 1;
//            }
//        }
//    }

    @SuppressLint("MissingInflatedId")
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
        LinearLayout button_lanjut = findViewById(R.id.button_lanjut);

        if (dataModels == null) {
            System.out.println("nullllll");
            dataModels = new ArrayList<>();
            jsonArray = new JSONArray(dataModels);
            if (TambahBarangActivity.list_data != null){
                System.out.println(TambahBarangActivity.list_data.toString()+"hayyuk");
            }
            System.out.println("a");
        } else {
            System.out.println("ini print 1");
            if (view == 1) {
                System.out.println("ini print");
//                int total_harga_br = 0;
//                for (int i = 0; i < dataModels.size(); i++) {
//                    total_harga_br += dataModels.get(i).getQty() * dataModels.get(i).getHarga_br();
//                }
//                jumlahNominalHarga.setText(String.valueOf(total_harga_br));
//                jumlahList.setText(String.valueOf(dataModels.size()));
//
//                // Perbarui tampilan adapterTransaksi dan RecyclerView
//                adapterTransaksi.notifyDataSetChanged();
//                update();
//                materi.scrollToPosition(dataModels.size() - 1);
            } else if (view == 2) {
//                jsonObject = new JSONObject();
//                int total_harga_br = 0;
//                for (int i = 0; i < dataModels.size(); i++) {
//                    total_harga_br += dataModels.get(i).getQty() * dataModels.get(i).getHarga_br();
//                }
//                jumlahNominalHarga.setText(String.valueOf(total_harga_br));
//                jumlahList.setText(String.valueOf(dataModels.size()));
//                System.out.println("ini print juga");
//
//                // Perbarui tampilan adapterTransaksi dan RecyclerView
//                adapterTransaksi.notifyDataSetChanged();
//                update();
//                materi.scrollToPosition(dataModels.size() - 1);
            }
        }

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
                            Intent inten = new Intent(Transaksi.this, TambahBarangActivity.class);
                            startActivityForResult(inten, 1);
                        }
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
                if (dataModels.isEmpty()){
                    TambahBarangActivity.showToast(view.getContext(), "Tidak ada Produk yang dipilih");
                }else if (dataModels != null){
                    list_produk_dipesan = new JSONArray();
                    for(int i = 0; i < dataModels.size(); i++){
                        list_produk_dipesan.put(dataModels.get(i).get_all_data());
                    }
                    System.out.println(list_produk_dipesan);
                    Intent intent = new Intent(Transaksi.this, PembayaranActivity.class);
                    startActivity(intent);
//                    System.out.println(dataModels + " ini data keranjang" + dataModels.size());
//                    Intent inten = new Intent(Transaksi.this, PembayaranActivity.class);
//                    inten.putExtra("data_keranjang", dataModels.toString());
//                    startActivityForResult(inten, 2);
                }
                }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String getdata = data.getStringExtra("data");
                try {
                    JSONObject data_object = new JSONObject(getdata);

                    // Untuk edit
                    for (int i = 0; i < dataModels.size(); i++) {
                        if (dataModels.get(i).getKode_br().equals(data_object.getString("kode_br")) && dataModels.size() > 0) {
                            Toast.makeText(this, "Ini edit data", Toast.LENGTH_SHORT).show();
                            int oldTotalHargaBr = dataModels.get(i).getTotal_harga_br();
                            int newQty = Integer.valueOf(data_object.getString("qty_br"));
                            int newTotalHargaBr = data_object.getInt("harga_total_akhir_br");
                            total_harga_br = total_harga_br - oldTotalHargaBr + newTotalHargaBr;
                            dataModels.get(i).setQty(newQty);
                            dataModels.get(i).setTotal_harga_br(newTotalHargaBr);
                        }
                    }

                    // Untuk tambah
                    boolean isDataFound = false;
                    for (int j = 0; j < dataModels.size(); j++) {
                        if (dataModels.get(j).getKode_br().equals(data_object.getString("kode_br")) &&
                                dataModels.get(j).getNama_br().equals(data_object.getString("nama_br")) &&
                                dataModels.get(j).getGambar_br().equals(data_object.getString("gambar_br"))) {
                            isDataFound = true;
                            break;
                        }
                    }

                    if (!isDataFound) {

                        if (data_object.getString("jenis_diskon").equals("nominal") || data_object.getString("jenis_diskon").equals("diskon") || data_object.getString("jenis_diskon").equals("tidak_diskon")){
                            dataModels.add(new ModelTransaksi(data_object.getString("gambar_br"), data_object.getString("kode_br"), data_object.getString("nama_br"), data_object.getString("jenis_diskon"), data_object.getString("kode_diskon_br"), data_object.getInt("diskon"), data_object.getInt("harga_br"), data_object.getInt("potongan_harga_br"), data_object.getInt("qty_br"), data_object.getInt("harga_total_akhir_br"), R.drawable.trush));
                        }else {
                            ModelTransaksi barang = new ModelTransaksi(data_object.getString("gambar_br"), data_object.getString("kode_br"), data_object.getString("nama_br"), data_object.getString("jenis_diskon"),  data_object.getString("kode_diskon_br"), data_object.getInt("diskon"), data_object.getInt("harga_br"), data_object.getInt("potongan_harga_br"), data_object.getInt("qty_br"), data_object.getInt("harga_total_akhir_br"), R.drawable.trush);
                            try {
                                JSONArray mapping_diskon_jsonarray = new JSONArray(data_object.getString("list_produk_didapat"));
                                ArrayList<DetailProdukDidapat> produk_gratis = new ArrayList<>();
                                for (int k = 0; k < mapping_diskon_jsonarray.length(); k++){
                                    JSONObject mapping_diskon = mapping_diskon_jsonarray.getJSONObject(k);
                                    produk_gratis.add(new DetailProdukDidapat(mapping_diskon.getString("nama"), mapping_diskon.getString("kode"), mapping_diskon.getString("qty")));
                                }
                                barang.setDetailProdukDidapat(produk_gratis);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }

                            dataModels.add(barang);
                        }

                        total_harga_br += dataModels.get(dataModels.size() - 1).getTotal_harga_br();
                    } else {
                        Toast.makeText(this, "Data sudah ada", Toast.LENGTH_SHORT).show();
                    }

                    adapterTransaksi = new AdapterTransaksi(dataModels, Transaksi.this);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Transaksi.this);
                    materi.setLayoutManager(layoutManager);
                    materi.setAdapter(adapterTransaksi);

                    jumlahList.setText(String.valueOf(dataModels.size()));
                    jumlahNominalHarga.setText(String.valueOf(total_harga_br));
                    adapterTransaksi.notifyDataSetChanged();
                    update();

                } catch (Exception e) {
                    Toast.makeText(this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }
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

    @Override
    public void onClick(View view, int position) {
        if (dataModels.get(position).getTrash() == dataModels.get(position).getTrash()){
            int total_akhir_br = dataModels.get(position).getTotal_harga_br();
            AlertDialog builder = new AlertDialog.Builder(view.getRootView().getContext(), R.style.dialog).create();
            View dialogView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.content_dialog_transaksi, null);
            builder.setView(dialogView);
            builder.setCancelable(true);
            builder.show();

            TextView titleInfo = dialogView.findViewById(R.id.title_info);
            TextView diskripsiInfo = dialogView.findViewById(R.id.diskripsi_info);
            titleInfo.setText("Apakah Anda Yakin");
            diskripsiInfo.setText("Produk yang anda pilih akan di hapus dari List Pesanan");
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
            ImageView imageBarcode = dialogView.findViewById(R.id.icon_barcode);
            TextView textBarcode = dialogView.findViewById(R.id.barcode);
            imageBarcode.setVisibility(View.GONE);
            textBarcode.setText("TIDAK");
            next_dialog_barcode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    builder.dismiss();
                    adapterTransaksi.notifyDataSetChanged();
                }
            });
            LinearLayout next_dialog_keyboard = dialogView.findViewById(R.id.button_metode_keyboard);
            ImageView imageKeyboard = dialogView.findViewById(R.id.icon_keyboard);
            TextView textKeyboard = dialogView.findViewById(R.id.keyboard);
            imageKeyboard.setVisibility(View.GONE);
            textKeyboard.setText("IYA");
            next_dialog_keyboard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    builder.dismiss();
                    dataModels.remove(position);
                    int pengurangan_total_list = Integer.parseInt(Transaksi.jumlahList.getText().toString()) - 1;
                    int pengurangan_total_akhir = Integer.parseInt(Transaksi.jumlahNominalHarga.getText().toString()) - total_akhir_br;
                    Transaksi.jumlahList.setText(String.valueOf(pengurangan_total_list));
                    Transaksi.jumlahNominalHarga.setText(String.valueOf(pengurangan_total_akhir));
                    update();
                    adapterTransaksi.notifyDataSetChanged();

                }
            });
            System.out.println(position);
        }
    }

    void update() {
        synchronized (jumlahList) {
            jumlahList.notify();
        }
        synchronized (jumlahNominalHarga){
            jumlahNominalHarga.notify();
        }
    }
}

class ModelTransaksi {
    String gambar_br, nama_br, jenis_diskon, kode_br, kode_diskon_br;
    int harga_br, potongan_br, potongan_harga_br, qty, total_harga_br, trash;
    ArrayList<DetailProdukDidapat> detailProdukDidapat;
    public JSONObject get_all_data(){
        JSONObject data = new JSONObject();
        try {
            data.put("nama_barang", getNama_br());
            data.put("kode_barang", getKode_br());
            data.put("kode_diskon_br", getKode_diskon_br());
            data.put("qty_barang", getQty());
            data.put("harga_barang", getHarga_br());
            data.put("potongan_barang", getPotongan_br());
            data.put("potongan_harga_barang", getPotongan_harga_br());
            data.put("total_harga_barang", getTotal_harga_br());
            data.put("jenis_diskon", getJenis_diskon());
            if (detailProdukDidapat != null){
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < detailProdukDidapat.size(); i++){
                    jsonArray.put(getDetailProdukDidapat().get(i).get_detail_produk_didapat());
                }
                data.put("detail_produk_didapat", jsonArray);
            }else {
                data.put("detail_produk_didapat", null);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return data;

    }

    public ModelTransaksi(String gambar_br, String kode_br, String nama_br, String jenis_diskon, String kode_diskon_br, int potongan_br, int harga_br, int potongan_harga_br, int qty, int total_harga_br, int trash) {
        this.gambar_br = gambar_br;
        this.kode_br = kode_br;
        this.nama_br = nama_br;
        this.jenis_diskon = jenis_diskon;
        this.kode_diskon_br = kode_diskon_br;
        this.potongan_br = potongan_br;
        this.harga_br = harga_br;
        this.potongan_harga_br = potongan_harga_br;
        this.qty = qty;
        this.total_harga_br = total_harga_br;
        this.trash = trash;
    }

    public String getKode_diskon_br() {
        return kode_diskon_br;
    }

    public int getPotongan_br() {
        return potongan_br;
    }

    public String getGambar_br() {
        return gambar_br;
    }

    public String getKode_br() {
        return kode_br;
    }

    public String getNama_br() {
        return nama_br;
    }

    public String getJenis_diskon() {
        return jenis_diskon;
    }

    public int getHarga_br() {
        return harga_br;
    }

    public int getPotongan_harga_br() {
        return potongan_harga_br;
    }

    public int getQty() {
        return qty;
    }

    public int getTotal_harga_br() {
        return total_harga_br;
    }

    public int getTrash() {
        return trash;
    }

    public void setDetailProdukDidapat(ArrayList<DetailProdukDidapat> detailProdukDidapat) {
        this.detailProdukDidapat = detailProdukDidapat;
    }

    public ArrayList<DetailProdukDidapat> getDetailProdukDidapat() {
        return detailProdukDidapat;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setTotal_harga_br(int total_harga_br) {
        this.total_harga_br = total_harga_br;
    }
}

class DetailProdukDidapat{
    String nama_produk, kode_produk, qty_produk;
    public JSONObject get_detail_produk_didapat(){
        JSONObject data_produk_didapat = new JSONObject();
        try {
            data_produk_didapat.put("nama_produk_free", getNama_produk());
            data_produk_didapat.put("kode_produk_free", getKode_produk());
            data_produk_didapat.put("qty_produk_free", getQty_produk());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return data_produk_didapat;
    }

    public DetailProdukDidapat(String nama_produk, String kode_produk, String qty_produk) {
        this.nama_produk = nama_produk;
        this.kode_produk = kode_produk;
        this.qty_produk = qty_produk;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public String getKode_produk() {
        return kode_produk;
    }

    public String getQty_produk() {
        return qty_produk;
    }
}


//class ModelTransaksi {
//
//    String kode_barang;
//    String nama_produk;
//    String potongan_harga;
//    String harga;
//    String sub_harga;
//    String value;
//    String nominal_diskon;
//    JSONArray ListProdukFree;
//    String total_voucher;
//    String img;
//    int trush;
//    String feature;
//
//    public ModelTransaksi(String kode_barang, String nama_produk, String potongan_harga, String harga, String sub_harga, String nominal_diskon, String total_voucher, String value, JSONArray listProdukFree,  String img, int trush) {
//        this.kode_barang = kode_barang;
//        this.nama_produk = nama_produk;
//        this.potongan_harga = potongan_harga;
//        this.harga = harga;
//        this.sub_harga = sub_harga;
//        this.value = value;
//        this.nominal_diskon = nominal_diskon;
//        this.ListProdukFree = listProdukFree;
//        this.total_voucher = total_voucher;
//        this.img = img;
//        this.trush = trush;
//    }
//
//
//    public String getKode_barang() {
//        return kode_barang;
//    }
//    public String getNama_produk() {
//        return nama_produk;
//    }
//    public String getPotongan_harga() {
//        return potongan_harga;
//    }
//    public String getHarga() {
//        return harga;
//    }
//    public String getSub_harga() {
//        return sub_harga;
//    }
//    public String getNominal_diskon() {
//        return nominal_diskon;
//    }
//    public String getValue() {
//        return value;
//    }
//    public String getjumlahBarangPilih() {
//        return nominal_diskon;
//    }
//    public String getTotal_harga_barang() {
//        return total_voucher;
//    }
//    public JSONArray getListProdukFree(){
//        return ListProdukFree;
//    }
//    public String getImgs() {
//        return img;
//    }
//    public int getTrush() {
//        return trush;
//    }
//    public String getFeatures() {
//        return feature;
//    }
//
//}