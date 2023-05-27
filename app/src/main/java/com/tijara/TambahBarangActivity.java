package com.tijara;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

class allTypeData{

    static TextView sub_total_harga, free_didapat;
    static int jenisProduk, isDuplicateData = 1, nominal;;
    static String jumlah_barang, free_produk, buy, gratis, jum_product;
    static NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    public String imageProduk;
    public int subHarga;
    public static int totalHarga, totalKaliJumBarang, potonganHarga, priceProduct, totalAkhirPalingakhir;
    int view2 = 2, view1 = 1;
    String json, aa;
    static String KodeBarang, NamaBarang, Diskon;
    static int HargaBarang, StokBarang;
    public String nameProduct, kodeProduct;
    static JSONObject Values, freeProduk, varian_diskon;
//    static ArrayList<modelProdukFree> modelDataSetGet;
    static EditText searchProdukFree;
    public ArrayList<ModelAddBarang> datalist;
    public static String BASE_URL = "http://tijara.mphstar.tech/api/product";
    LinearLayout detail_dialog, imageNoProduk, button_lanjut_transaksi, detail_dialog_free;
    EditText field_isi_jumlah_barang, field_isi_jumlah_barang2;
    ImageView buttonClose;
    RecyclerView listProdukFree;
//    ArrayList<modelProdukFree> dataModel;
    ArrayList<String> dataModel2;
    RecyclerView materi, materi2;
//    AdapterProdukFree adapterProdukFree;
//    AdapterProdukFree2 adapterProdukFree2;
    BottomSheetDialog bottomSheetDialog;
}

class ambilValues{

    static String image_produk;
    static String kode_produk, nama_produk, harga_produk, jumlah_pesanan, diskon_produk, harga_asli_produk, nominal_diskon, array_produk_free, jsonData;
    static String kodeProduk, hargaProduk, jumlahPesanan, namaProduk, hargaAsliProduk;
    static JSONArray dataProdukFree;
}

//class AdapterProdukFree extends RecyclerView.Adapter<AdapterProdukFree.ViewHolder> {
//
//    private ArrayList<modelProdukFree> datalist;
//    private int nilai = 0;
//
//    public AdapterProdukFree(ArrayList<modelProdukFree> datalist){
//        this.datalist = datalist;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View view = layoutInflater.inflate(R.layout.content_produk_free, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        modelProdukFree jumlah = datalist.get(position);
//        String gambar_produk_pilih = datalist.get(position).getImage_product();
//        Glide.with(holder.imgProduk).load(gambar_produk_pilih).centerCrop().placeholder(R.drawable.tshirt).into(holder.imgProduk);
//        holder.txtNamaProduk.setText(datalist.get(position).getName_product());
//        holder.txtValues.setText(String.valueOf(datalist.get(position).getValues()));
//        holder.addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Integer.parseInt(allTypeData.gratis) == allTypeData.modelDataSetGet.size()){
//                    AdapterProdukFree2.showToast(view.getContext(), "Produk yang dipilih melebihi batas");
//                }else {
//                    jumlah.setValues(jumlah.getValues() + 1);
//                    holder.txtValues.setText(String.valueOf(jumlah.getValues()));
//                }
//            }
//        });
//        holder.minButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (jumlah.getValues() > 0) {
//                    jumlah.setValues(jumlah.getValues() - 1);
//                    holder.txtValues.setText(String.valueOf(jumlah.getValues()));
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return (datalist != null) ? datalist.size() :0;
//    }
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private TextView txtNamaProduk, txtValues;
//        private ImageView addButton, minButton, imgProduk;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            addButton = itemView.findViewById(R.id.add_jumlah);
//            minButton = itemView.findViewById(R.id.min_jumlah);
//            imgProduk = itemView.findViewById(R.id.img_free_produk);
//            txtNamaProduk = itemView.findViewById(R.id.name_product);
//            txtValues = itemView.findViewById(R.id.jumlah_pcs);
//        }
//    }
//}

//class AdapterProdukFree2 extends RecyclerView.Adapter<AdapterProdukFree2.ViewHolder> {
//
//    private ArrayList<modelProdukFree> datalist;
//    public String dataNamaProduk, dataGambarProduk, dataKodeProduk;
//    RecyclerView materi;
//    AdapterProdukFree adapterProdukFree;
//    ArrayList<modelProdukFree> modelDataSetGet;
//
//    public AdapterProdukFree2(ArrayList<modelProdukFree> datalist){
//        this.datalist = datalist;
//    }
//
//    public ArrayList<modelProdukFree> getData() {
//        return datalist;
//    }
//
//    public void filterList(ArrayList<modelProdukFree> filterdNames, LinearLayout linearLayout, RecyclerView materi) {
//        this.datalist = filterdNames;;
//        if (datalist.isEmpty()){
//            materi.setVisibility(View.GONE);
//            linearLayout.setVisibility(View.VISIBLE);
//        }
//        System.out.println(datalist);
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View view = layoutInflater.inflate(R.layout.content_produk_free, parent, false);
//        return new ViewHolder(view);
//    }
//
//    static public void showToast(Context context, String message) {
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View layout = inflater.inflate(R.layout.custom_toast, null);
//
//        Toast toast = new Toast(context);
//
////        ImageView imageView = (ImageView) layout.findViewById(R.id.imageView);
////        imageView.setImageResource(R.drawable.ic_info);
//        if (message == "Produk yang sama sudah terdaftar dalam list"){
//            LottieAnimationView lottieAnimationView = (LottieAnimationView) layout.findViewById(R.id.image_json);
//            lottieAnimationView.setAnimation(R.raw.failed_add);
//            TextView Message = (TextView) layout.findViewById(R.id.message_toast);
//            Message.setText(message);
//            TextView Title = (TextView) layout.findViewById(R.id.title_toast);
//            Title.setVisibility(View.GONE);
//            toast.setGravity(Gravity.TOP, 0, 0);
//
//        }else if (message == "Tidak ada Produk yang dipilih"){
//            LottieAnimationView lottieAnimationView = (LottieAnimationView) layout.findViewById(R.id.image_json);
//            lottieAnimationView.setAnimation(R.raw.failed_add);
//            TextView Message = (TextView) layout.findViewById(R.id.message_toast);
//            Message.setText(message);
//            TextView Title = (TextView) layout.findViewById(R.id.title_toast);
//            Title.setVisibility(View.GONE);
//            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//
//        }else if (message == "Produk yang dipilih melebihi batas"){
//            LottieAnimationView lottieAnimationView = (LottieAnimationView) layout.findViewById(R.id.image_json);
//            lottieAnimationView.setAnimation(R.raw.failed_add);
//            TextView Message = (TextView) layout.findViewById(R.id.message_toast);
//            Message.setText(message);
//            TextView Title = (TextView) layout.findViewById(R.id.title_toast);
//            Title.setVisibility(View.GONE);
//            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//
//        }else if (message == "Isikan jumlah barang terlebih dahulu"){
//            LottieAnimationView lottieAnimationView = (LottieAnimationView) layout.findViewById(R.id.image_json);
//            lottieAnimationView.setAnimation(R.raw.failed_add);
//            TextView Message = (TextView) layout.findViewById(R.id.message_toast);
//            Message.setText(message);
//            TextView Title = (TextView) layout.findViewById(R.id.title_toast);
//            Title.setVisibility(View.GONE);
//            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//
//        }else if (message == "Pembayaran Berhasil"){
//            LottieAnimationView lottieAnimationView = (LottieAnimationView) layout.findViewById(R.id.image_json);
//            lottieAnimationView.setAnimation(R.raw.add_product);
//            TextView Message = (TextView) layout.findViewById(R.id.message_toast);
//            Message.setText(message);
//            TextView Title = (TextView) layout.findViewById(R.id.title_toast);
//            Title.setText(message);
//            toast.setGravity(Gravity.TOP, 0, 0);
//        }else {
//            LottieAnimationView lottieAnimationView = (LottieAnimationView) layout.findViewById(R.id.image_json);
//            lottieAnimationView.setAnimation(R.raw.add_product);
//            TextView Title = (TextView) layout.findViewById(R.id.title_toast);
//            Title.setText(message);
//            toast.setGravity(Gravity.TOP, 0, 0);
//        }
//
//
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.setView(layout);
//        toast.show();
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        String nama_produk_dipilih = datalist.get(position).getName_product();
//        String gambar_produk_pilih = datalist.get(position).getImage_product();
//        String kode_produk_pilih = datalist.get(position).getKode_produk();
//        Glide.with(holder.txtGambarProduk).load(gambar_produk_pilih).centerCrop().placeholder(R.drawable.tshirt).into(holder.txtGambarProduk);
//        holder.txtNamaProduk.setText(datalist.get(position).getName_product());
//        holder.txtValues.setText(String.valueOf(datalist.get(position).getValues()));
//        holder.plus_minus_values.setVisibility(View.GONE);
//
//        holder.content_pilih_produk_free.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dataNamaProduk = nama_produk_dipilih;
//                dataGambarProduk = gambar_produk_pilih;
//                dataKodeProduk = kode_produk_pilih;
//                System.out.println(nama_produk_dipilih);
//                showToast(view.getContext(), dataNamaProduk);
////                Toast.makeText(view.getContext(), "Produk "+dataNamaProduk, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return (datalist != null) ? datalist.size() :0;
//    }
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private TextView txtNamaProduk, txtValues;
//        private FlexboxLayout plus_minus_values;
//        private ImageView txtGambarProduk;
//        private LinearLayout content_pilih_produk_free;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            content_pilih_produk_free = itemView.findViewById(R.id.content_pilih_produk_free);
//            plus_minus_values = itemView.findViewById(R.id.plus_minus_values);
//            txtGambarProduk = itemView.findViewById(R.id.img_free_produk);
//            txtNamaProduk = itemView.findViewById(R.id.name_product);
//            txtValues = itemView.findViewById(R.id.jumlah_pcs);
//        }
//    }
//}

//class AdapterAddBarang extends RecyclerView.Adapter<AdapterAddBarang.MahasiswaViewHolder>{
//
//    allTypeData allTypeData = new allTypeData();
//
//    private void filter(String teks) {
//
//        System.out.println(teks.toString().length());
//        ArrayList<modelProdukFree> dataModels2 = new ArrayList<>();
//        //looping through existing elements
//        for (modelProdukFree s : allTypeData.dataModel) {
//            //if the existing elements contains the search input
//            if (s.getKode_produk().toLowerCase().contains(teks.toLowerCase())) {
//                //adding the element to filtered list
//                dataModels2.add(s);
//            }
//        }
//
//        //calling a method of the adapter class and passing the filtered list
//        allTypeData.adapterProdukFree2.filterList(dataModels2, allTypeData.imageNoProduk, allTypeData.materi2);
//    }
//
//    public void filterList(ArrayList<ModelAddBarang> filterdNames, LinearLayout linearLayout, RecyclerView materi) {
//        this.allTypeData.datalist = filterdNames;;
//        if (allTypeData.datalist.isEmpty()){
//            materi.setVisibility(View.GONE);
//            linearLayout.setVisibility(View.VISIBLE);
//        }
//        System.out.println(allTypeData.datalist);
//        notifyDataSetChanged();
//    }
//
//    private void GetProdukFree(View view){
//
//        // Akses API
//        RequestQueue Queue;
//        allTypeData.dataModel = new ArrayList<>();
//        Queue = Volley.newRequestQueue(view.getContext());
//
//        String url = "http://tijara.mphstar.tech/api/product";
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//
//                try {
//                    JSONObject productObject, valueDiskon, valueProdukFreeJson;
//                    String valueProdukFreeString;
//                    System.out.println(response.length());
//                    for (int i = 0; i < response.length(); i++) {
//                        System.out.println(response.getJSONObject(i));
//                        productObject = response.getJSONObject(i);
//                        if (productObject.getString("jenis").equals("free")){
//                            allTypeData.dataModel.add(new modelProdukFree(productObject.getString("nama_br"), "http://tijara.mphstar.tech/uploads/products/"+productObject.getString("gambar")+"", productObject.getString("kode_br"),1));
//                        } else {
//                            System.out.println("ini produk Frew");
//                        }
//                    }
//                    allTypeData.adapterProdukFree2 = new AdapterProdukFree2(allTypeData.dataModel);
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
//                    allTypeData.materi2.setLayoutManager(layoutManager);
//                    allTypeData.materi2.setAdapter(allTypeData.adapterProdukFree2);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // error dalam mengambil data JSON
//                Log.e("Error", "Error: " + error.getMessage());
//            }});
//
//        Queue.add(jsonArrayRequest);
//
//        //Akhir Akses Api
//
////        allTypeData.dataModel = new ArrayList<>();
////        allTypeData.dataModel.add(new modelProdukFree("Dress Panjang Kondangan K..", 1));
////        allTypeData.dataModel.add(new modelProdukFree("Dress Casual Pink", 2));
////        allTypeData.dataModel.add(new modelProdukFree("Celana Chinos Buat Perang ...", 4));
////
////        allTypeData.adapterProdukFree2 = new AdapterProdukFree2(allTypeData.dataModel);
////        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
////        allTypeData.materi2.setLayoutManager(layoutManager);
////        allTypeData.materi2.setAdapter(allTypeData.adapterProdukFree2);
//    }
//
//    void eksekusi_jumlah_barang(String jumlah){
//        allTypeData.totalKaliJumBarang = allTypeData.priceProduct * Integer.parseInt(jumlah);
//        allTypeData.totalHarga = allTypeData.totalKaliJumBarang - allTypeData.potonganHarga;
//        if (allTypeData.jenisProduk == 1){
//            allTypeData.sub_total_harga.setText(allTypeData.format.format(allTypeData.totalHarga));
//        }else {
//            JSONObject value = allTypeData.Values.optJSONObject("value");
//            allTypeData.buy = value.optString("buy");
//            allTypeData.gratis = value.optString("gratis");
//            int free_plus = Integer.parseInt(allTypeData.gratis)+1;
//            int num= Integer.parseInt(jumlah);
//            int faktor = 4;
//            if (Integer.parseInt(jumlah) == Integer.parseInt(allTypeData.buy)){
//                allTypeData.free_didapat.setText(allTypeData.gratis);
//                allTypeData.jum_product = jumlah;
//            }else if (Integer.parseInt(jumlah) < Integer.parseInt(allTypeData.buy)){
//                allTypeData.free_didapat.setText("0");
//                allTypeData.jum_product = jumlah;
//            }else if (Integer.parseInt(jumlah) > Integer.parseInt(allTypeData.buy)){
//                if (num % faktor == 0){
//                    allTypeData.free_didapat.setText(String.valueOf(free_plus));
//                    allTypeData.jum_product = jumlah;
//                }
//            }
//            allTypeData.sub_total_harga.setText(allTypeData.format.format(allTypeData.totalKaliJumBarang));
//        }
//        System.out.println(allTypeData.totalHarga);
//    }
//
//    private void BottomSheet(View view){
//
//        if (allTypeData.potonganHarga == 0 && allTypeData.Values == null){
//            System.out.println("no gratis produk");
//            non_produkFree(view);
//        }else if (allTypeData.potonganHarga != 0 && allTypeData.Values == null){
//            System.out.println("no gratis produk");
//            non_produkFree(view);
//        } else if (allTypeData.potonganHarga == 0 && allTypeData.Values != null) {
//            System.out.println("gratis produk");
//            produkFree(view);
//        }
//    }
//
//
//    // Awal Code Gratis Produk
//    void produkFree(View view){
//        Transaksi.addProductUsing = 1;
//        if (allTypeData.materi != null){
//            allTypeData.view1 = 3;
//        }else {
//            allTypeData.view1 = 1;
//        }
//        showBottomSheet(view);
//    }
//    public void showBottomSheet(View view){
//        Gson gson = new Gson();
////        JSONObject obj = new JSONObject();
//        kirimValues kirimValues = new kirimValues();
//        Transaksi mainActivity = new Transaksi();
//
//        allTypeData.bottomSheetDialog = new BottomSheetDialog(view.getRootView().getContext(), R.style.BottomSheetDialogStyle);
//        allTypeData.bottomSheetDialog.setContentView(R.layout.bottom_sheet_product);
//
//        allTypeData.bottomSheetDialog.show();
//        allTypeData.bottomSheetDialog.create();
//
//        TextView kode_produk, nama_produk, harga_produk, tambah_barang;
//        LinearLayout button_back_to_transaksi;
//        FlexboxLayout diskon_produk;
//        RecyclerView list_produk_gratis;
//
//        allTypeData.materi = allTypeData.bottomSheetDialog.findViewById(R.id.produk_gratis);
//        allTypeData.detail_dialog = allTypeData.bottomSheetDialog.findViewById(R.id.detail_dialog);
//        allTypeData.field_isi_jumlah_barang2 = allTypeData.bottomSheetDialog.findViewById(R.id.field_isi_jumlah_barang);
//        allTypeData.sub_total_harga = allTypeData.bottomSheetDialog.findViewById(R.id.sub_total);
//        allTypeData.free_didapat = allTypeData.bottomSheetDialog.findViewById(R.id.free_produk_didapat);
//        button_back_to_transaksi = allTypeData.bottomSheetDialog.findViewById(R.id.button_back_to_transaksi);
//        tambah_barang = allTypeData.bottomSheetDialog.findViewById(R.id.tambah_produk);
//        kode_produk = allTypeData.bottomSheetDialog.findViewById(R.id.kode_produk);
//        nama_produk = allTypeData.bottomSheetDialog.findViewById(R.id.nama_produk);
//        harga_produk = allTypeData.bottomSheetDialog.findViewById(R.id.harga_produk);
//        diskon_produk = allTypeData.bottomSheetDialog.findViewById(R.id.line_diskon);
//        list_produk_gratis = allTypeData.bottomSheetDialog.findViewById(R.id.list_produk_gratis);
//
//        kode_produk.setText(allTypeData.kodeProduct);
//        nama_produk.setText(allTypeData.nameProduct);
//        harga_produk.setText(allTypeData.format.format(allTypeData.priceProduct));
//        allTypeData.free_didapat.setText(String.valueOf(0));
//        allTypeData.sub_total_harga.setText(String.valueOf(0));
//        diskon_produk.setVisibility(View.GONE);
//
//        allTypeData.materi = allTypeData.bottomSheetDialog.findViewById(R.id.produk_gratis);
//
//        if (allTypeData.view1 == 1){
//            allTypeData.materi.setVisibility(View.GONE);
//            System.out.println("a");
//        }else if (allTypeData.view1 == 2){
//            System.out.println("b");
//            allTypeData.materi = allTypeData.bottomSheetDialog.findViewById(R.id.produk_gratis);
//            allTypeData.materi.setVisibility(View.VISIBLE);
//
////            if (allTypeData.adapterProdukFree2.dataNamaProduk != null){
////
////                boolean Datas = TambahBarangActivity.arr.contains(allTypeData.adapterProdukFree2.dataNamaProduk);
////                System.out.println(Datas);
////
////                if (Datas){
////                    System.out.println("sudah ada data");
////                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
////                    allTypeData.materi.setLayoutManager(layoutManager);
////                    allTypeData.materi.setAdapter(allTypeData.adapterProdukFree);
////                }else {
////                    allTypeData.modelDataSetGet.add(new modelProdukFree(allTypeData.adapterProdukFree2.dataNamaProduk,1));
////                    allTypeData.adapterProdukFree = new AdapterProdukFree(allTypeData.modelDataSetGet);
////                    allTypeData.adapterProdukFree.notifyDataSetChanged();
////                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
////                    allTypeData.materi.setLayoutManager(layoutManager);
////                    allTypeData.materi.setAdapter(allTypeData.adapterProdukFree);
////                }
////            }else {
////                System.out.println("fffff");
////            }
//
//            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
//            allTypeData.materi.setLayoutManager(layoutManager);
//            allTypeData.materi.setAdapter(allTypeData.adapterProdukFree);
//
//            allTypeData.json = gson.toJson(allTypeData.modelDataSetGet);
//            allTypeData.aa = "\"nama_produk\":\""+allTypeData.adapterProdukFree2.dataNamaProduk+"\", \"jumlah_pesanan\":\"1\"";
////            System.out.println(arr);
//            allTypeData.dataModel2.add(allTypeData.json);
//            System.out.println(allTypeData.aa+ "a");
//            System.out.println(allTypeData.modelDataSetGet.toArray().toString());
//            System.out.println(allTypeData.json+"b");
//        }else if (allTypeData.view1 == 3){
//            System.out.println("c");
//            if (allTypeData.modelDataSetGet != null){
//                allTypeData.materi.setVisibility(View.GONE);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
//                allTypeData.materi.setLayoutManager(layoutManager);
//                allTypeData.materi.setAdapter(allTypeData.adapterProdukFree);
//            }
//        }
//
//        allTypeData.field_isi_jumlah_barang2.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (editable.toString().length() != 0){
//                    allTypeData.jenisProduk = 2;
//                    allTypeData.jumlah_barang = editable.toString();
//                    eksekusi_jumlah_barang(editable.toString());
//                }else if (editable.toString().length() == 0){
//                    allTypeData.jenisProduk = 2;
//                    eksekusi_jumlah_barang(String.valueOf(0));
//                }
//            }
//        });
//        allTypeData.field_isi_jumlah_barang2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                allTypeData.field_isi_jumlah_barang2.setText("");
//            }
//        });
//
//
//        tambah_barang.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (allTypeData.sub_total_harga.toString().length() == 0){
//                    allTypeData.field_isi_jumlah_barang2.setText("");
//                }else if (allTypeData.sub_total_harga.toString().length() != 0){
//                    allTypeData.field_isi_jumlah_barang2.setText(allTypeData.jumlah_barang);
//                }
//
//                if (Integer.parseInt(allTypeData.gratis) == allTypeData.modelDataSetGet.size()){
//                    AdapterProdukFree2.showToast(view.getContext(), "Produk yang dipilih melebihi batas");
//                }else if (Integer.parseInt(allTypeData.buy) == Integer.parseInt(allTypeData.jum_product) || Integer.parseInt(allTypeData.jum_product) > Integer.parseInt(allTypeData.buy)  ){
//                    showAlernDialog(view);
//                    allTypeData.bottomSheetDialog.dismiss();
//                }else if (Integer.parseInt(allTypeData.gratis) != allTypeData.modelDataSetGet.size()){
//                    AdapterProdukFree2.showToast(view.getContext(), "Isikan jumlah barang terlebih dahulu");
//                }else {
//                    showAlernDialog(view);
//                    allTypeData.bottomSheetDialog.dismiss();
//                }
//            }
//        });
//
//        button_back_to_transaksi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Transaksi.dataModels == null){
//                    Transaksi.view = 1;
//                }else if (Transaksi.dataModels != null){
//                    Transaksi.view = 2;
//                }
//
//                if (allTypeData.totalHarga == 0){
//                    System.out.println("kosong");
//                }else {
//                    JSONObject jsonObject = new JSONObject();
//                    JSONObject jsonObject1 = new JSONObject();
//                    System.out.println(allTypeData.modelDataSetGet.toArray().toString()+"aadasd");
////                JSONArray arr = allTypeData.modelDataSetGet.toArray();
//                    ArrayList<modelProdukFree> arr = allTypeData.modelDataSetGet;
////                System.out.println(arr.get(0).name_product + "zxcv");
////                System.out.println(arr.get(1).name_product + "zxcv");
////                System.out.println(arr.size());
//
//                    JSONArray jsonArray = new JSONArray();
////                ArrayList<JSONObject> ls = new ArrayList<>();
//                    try {
//                        for (int a = 0; a < arr.size(); a++){
//                            jsonObject1.put("nama", arr.get(a).name_product);
//                            jsonObject1.put("value", arr.get(a).values);
//                            jsonArray.put(jsonObject1);
//                            jsonObject1 = new JSONObject();
////                        jsonArray.put(a,jsonObject1);
//                        }
//                        System.out.println(jsonArray+"ssjj");
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//
//
//                    try {
//                        jsonObject.put("kodeProduk", allTypeData.kodeProduct);
//                        jsonObject.put("hargaProduk", String.valueOf(allTypeData.priceProduct));
//                        jsonObject.put("hargaSetelahDikaliPerpcs", allTypeData.totalKaliJumBarang);
//                        jsonObject.put("jumlahPesanan", String.valueOf(allTypeData.field_isi_jumlah_barang2.getText()));
//                        jsonObject.put("namaProduk", String.valueOf(nama_produk.getText()));
//                        jsonObject.put("image_produk", allTypeData.imageProduk);
//                        jsonObject.put("dataProdukFree", jsonArray);
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//
//                    System.out.println(arr);
//                    System.out.println(jsonObject);
//
//                    try {
//                        ambilValues.kodeProduk = jsonObject.getString("kodeProduk");
//                        ambilValues.hargaProduk = jsonObject.getString("hargaProduk");
//                        ambilValues.hargaAsliProduk = jsonObject.getString("hargaSetelahDikaliPerpcs");
//                        ambilValues.jumlahPesanan = jsonObject.getString("jumlahPesanan");
//                        ambilValues.namaProduk  = jsonObject.getString("namaProduk");
//                        ambilValues.dataProdukFree  = jsonObject.getJSONArray("dataProdukFree");
//                        ambilValues.image_produk = jsonObject.getString("image_produk");
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//
//                    allTypeData.bottomSheetDialog.dismiss();
//                    TambahBarangActivity tambahBarangActivity = new TambahBarangActivity();
//                    tambahBarangActivity.toMain();
//                }
//
//            }
//        });
//    }
//    void showAlernDialog(View view){
//        AlertDialog builder = new AlertDialog.Builder(view.getRootView().getContext(), R.style.dialog).create();
//        View dialogView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.content_dialog_transaksi, null);
//        builder.setView(dialogView);
//        builder.setCancelable(true);
//        builder.show();
//
//        allTypeData.materi2 = dialogView.findViewById(R.id.list_produk_gratis);
//        GetProdukFree(view);
//        allTypeData.imageNoProduk = dialogView.findViewById(R.id.image_no_value);
//        allTypeData.searchProdukFree = dialogView.findViewById(R.id.field_kode_product);
//        allTypeData.buttonClose = dialogView.findViewById(R.id.button_close);
//        allTypeData.button_lanjut_transaksi = dialogView.findViewById(R.id.button_lanjut_transaksi);
//        allTypeData.detail_dialog_free = dialogView.findViewById(R.id.detail_dialog);
//
//        allTypeData.imageNoProduk.setVisibility(View.GONE);
//        allTypeData.materi2.setVisibility(View.GONE);
//
//        allTypeData.buttonClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                builder.dismiss();
//                showBottomSheet(view);
//            }
//        });
//
//        allTypeData.button_lanjut_transaksi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (allTypeData.adapterProdukFree2.dataNamaProduk != null){
//
//                    boolean Datas = TambahBarangActivity.arr.contains(allTypeData.adapterProdukFree2.dataNamaProduk);
//                    System.out.println(Datas);
//
//                    if (Datas){
//                        System.out.println("sudah ada data");
//                        AdapterProdukFree2.showToast(view.getContext(), "Produk yang sama sudah terdaftar dalam list");
//                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
//                        allTypeData.materi.setLayoutManager(layoutManager);
//                        allTypeData.materi.setAdapter(allTypeData.adapterProdukFree);
//                    }else {
//                        allTypeData.modelDataSetGet.add(new modelProdukFree(allTypeData.adapterProdukFree2.dataNamaProduk, allTypeData.adapterProdukFree2.dataGambarProduk, allTypeData.adapterProdukFree2.dataKodeProduk, 1));
//                        allTypeData.adapterProdukFree = new AdapterProdukFree(allTypeData.modelDataSetGet);
//                        allTypeData.adapterProdukFree.notifyDataSetChanged();
//
//                        builder.dismiss();
//                        allTypeData.view1 = 2;
//                        showBottomSheet(view);
//                        TambahBarangActivity.arr.add(allTypeData.adapterProdukFree2.dataNamaProduk);
//                        allTypeData.field_isi_jumlah_barang2.setText(allTypeData.jumlah_barang);
//                    }
//                }else {
//                    System.out.println("fffff");
//                }
//
//
//            }
//        });
//
//        allTypeData.searchProdukFree.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                if (editable.toString().length() != 0){
//                    allTypeData.materi2.setVisibility(View.VISIBLE);
//                    allTypeData.imageNoProduk.setVisibility(View.GONE);
//                    filter(editable.toString());
//                }else if (editable.toString().length() == 0){
//                    allTypeData.materi2.setVisibility(View.GONE);
//                    allTypeData.imageNoProduk.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        LinearLayout next_dialog_keyboard = dialogView.findViewById(R.id.button_metode_keyboard);
//        next_dialog_keyboard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (allTypeData.view2 == 2) {
//                    System.out.println("view input barang");
//                    allTypeData.detail_dialog_free.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//
//        LinearLayout next_dialog_barcode = dialogView.findViewById(R.id.button_metode_barcode);
//        next_dialog_barcode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TambahBarangActivity.scanProduk = "produkFree";
//                allTypeData.detail_dialog_free.setVisibility(View.VISIBLE);
//                TambahBarangActivity.scan3();
//            }
//        });
//    }
//    // Akhir Code Gratis Produk
//
//
//
//    // Awal Code Non Produk
//    void non_produkFree(View view){
//        Transaksi.addProductUsing = 2;
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getRootView().getContext(), R.style.BottomSheetDialogTheme);
//        bottomSheetDialog.setContentView(R.layout.bottom_sheet_product);
//        bottomSheetDialog.show();
//
//        TextView kode_produk, nama_produk, harga_produk, diskon_produk;
//        RecyclerView list_produk_gratis;
//        LinearLayout button_back_to_transaksi;
//        FlexboxLayout values;
//
//        allTypeData.field_isi_jumlah_barang = bottomSheetDialog.findViewById(R.id.field_isi_jumlah_barang);
//        allTypeData.detail_dialog = bottomSheetDialog.findViewById(R.id.detail_dialog);
//        allTypeData.sub_total_harga = bottomSheetDialog.findViewById(R.id.sub_total);
//        allTypeData.sub_total_harga.setText(String.valueOf(0));
//        values = bottomSheetDialog.findViewById(R.id.info_pesanan);
//        kode_produk = bottomSheetDialog.findViewById(R.id.kode_produk);
//        nama_produk = bottomSheetDialog.findViewById(R.id.nama_produk);
//        diskon_produk = bottomSheetDialog.findViewById(R.id.diskon_produk);
//        harga_produk = bottomSheetDialog.findViewById(R.id.harga_produk);
//        list_produk_gratis = bottomSheetDialog.findViewById(R.id.list_produk_gratis);
//
//        values.setVisibility(View.GONE);
//        kode_produk.setText(allTypeData.kodeProduct);
//        nama_produk.setText(allTypeData.nameProduct);
//        harga_produk.setText(allTypeData.format.format(allTypeData.priceProduct));
//        diskon_produk.setText(allTypeData.format.format(allTypeData.potonganHarga));
//        System.out.println(allTypeData.totalHarga);
//
//        allTypeData.field_isi_jumlah_barang.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (editable.toString().length() != 0){
//                    allTypeData.jenisProduk = 1;
//                    eksekusi_jumlah_barang(editable.toString());
//                }else if (editable.toString().length() == 0){
////                    allTypeData.jenisProduk = 2;
//                    eksekusi_jumlah_barang(String.valueOf(0));
//                }
//
//            }
//        });
//
//        button_back_to_transaksi = bottomSheetDialog.findViewById(R.id.button_back_to_transaksi);
//        button_back_to_transaksi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Transaksi.dataModels == null){
//                    Transaksi.view = 1;
//                }else if (Transaksi.dataModels != null){
//                    Transaksi.view = 2;
//                }
//
//
//                if (allTypeData.totalHarga == 0){
//                    System.out.println("kosong");
//                }else {
//                    JSONObject jsonObject = new JSONObject();
//                    JSONArray arr = new JSONArray(allTypeData.dataModel2);
//
//                    try {
//                        jsonObject.put("kode_produk", allTypeData.kodeProduct);
//                        jsonObject.put("harga_produk", allTypeData.priceProduct);
//                        jsonObject.put("nominal_diskon", allTypeData.potonganHarga);
//                        jsonObject.put("jumlah_pesanan", String.valueOf(allTypeData.field_isi_jumlah_barang.getText()));
//                        jsonObject.put("nama_produk", allTypeData.nameProduct);
//                        jsonObject.put("diskon_produk", allTypeData.potonganHarga);
//                        jsonObject.put("harga_setelah_dikali_perpcs", allTypeData.totalKaliJumBarang);
//                        jsonObject.put("harga_setelah_diskon", allTypeData.totalHarga);
//                        jsonObject.put("image_produk", allTypeData.imageProduk);
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//
//                    System.out.println(jsonObject);
//
//                    try {
//                        ambilValues.kode_produk = jsonObject.getString("kode_produk");
//                        ambilValues.harga_produk = jsonObject.getString("harga_setelah_dikali_perpcs");
//                        ambilValues.nominal_diskon = jsonObject.getString("nominal_diskon");
//                        ambilValues.harga_asli_produk = jsonObject.getString("harga_produk");
//                        ambilValues.jumlah_pesanan = jsonObject.getString("jumlah_pesanan");
//                        ambilValues.nama_produk  = jsonObject.getString("nama_produk");
//                        ambilValues.diskon_produk = jsonObject.getString("harga_setelah_diskon");
//                        ambilValues.image_produk = jsonObject.getString("image_produk");
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//
//                    System.out.println(ambilValues.nama_produk);
//
//                    bottomSheetDialog.dismiss();
//                    TambahBarangActivity tambahBarangActivity = new TambahBarangActivity();
//                    tambahBarangActivity.toMain();
//                }
//
//
//            }
//        });
//    }
//    // Akhir Code Non Produk
//
//    public AdapterAddBarang(ArrayList<ModelAddBarang> datalist, Context applicationContext){
//        this.allTypeData.datalist = datalist;
//    }
//
//    @NonNull
//    @Override
//    public MahasiswaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View view = layoutInflater.inflate(R.layout.content_tambah_barang, parent, false);
//        return new MahasiswaViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MahasiswaViewHolder holder, int position) {
//        String name_product = allTypeData.datalist.get(position).getNama_produk();
//        String kode_product = allTypeData.datalist.get(position).getKode_produk();
//        String value = allTypeData.datalist.get(position).getStok_barang();
//        String jenisDiskon = allTypeData.datalist.get(position).getJenisDiskon();
//        String image_produk = allTypeData.datalist.get(position).getImgs();
//        int nominalDiskon;
//        int price_product = allTypeData.datalist.get(position).getHarga();
//        int diskon = allTypeData.datalist.get(position).getNominal_diskon();
////        JSONObject varian_diskon = allTypeData.datalist.get(position).getDiskon();
//        JSONObject varian_diskon = allTypeData.datalist.get(position).getDiskon();
//        allTypeData.varian_diskon = allTypeData.datalist.get(position).getDiskon();
//        Glide.with(holder.img).load(image_produk).centerCrop().placeholder(R.drawable.tshirt).into(holder.img);
//        holder.txtNamaProduk.setText(allTypeData.datalist.get(position).getNama_produk());
//        holder.value.setText(allTypeData.datalist.get(position).getStok_barang());
////        holder.img.setImageResource(allTypeData.datalist.get(position).getImgs());
//        holder.trush.setImageResource(allTypeData.datalist.get(position).getTrush());
//        holder.value.setVisibility(View.GONE);
//        holder.trush.setVisibility(View.GONE);
//        holder.txtSubHarga.setVisibility(View.GONE);
//
//        if (diskon == 0 && allTypeData.varian_diskon == null){
//            holder.txtHarga.setText(allTypeData.format.format(allTypeData.datalist.get(position).getHarga()));
//            holder.potonganHarga.setVisibility(View.GONE);
//        }else if (diskon != 0 && allTypeData.varian_diskon == null){
//            if (jenisDiskon.equals("nominal")){
//                System.out.println("ini nominal");
//                holder.txtHarga.setText(allTypeData.format.format(allTypeData.datalist.get(position).getHarga() - allTypeData.datalist.get(position).getNominal_diskon()));
//                holder.potonganHarga.setText(allTypeData.format.format(allTypeData.datalist.get(position).getHarga()));
//                holder.potonganHarga.setPaintFlags(holder.potonganHarga.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            } else if (jenisDiskon.equals("persen")) {
//                System.out.println("ini persen");
//                double bagi100 = (double) allTypeData.datalist.get(position).getNominal_diskon() / 100;
//                double kali100 = allTypeData.datalist.get(position).getHarga() * bagi100;
//                double kurang_harga = allTypeData.datalist.get(position).getHarga() - kali100;
//                System.out.println(bagi100);
//                holder.txtHarga.setText(allTypeData.format.format(kurang_harga));
//                holder.potonganHarga.setText(allTypeData.format.format(allTypeData.datalist.get(position).getHarga()));
//                holder.potonganHarga.setPaintFlags(holder.potonganHarga.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            }
//        } else if (diskon == 0 && allTypeData.varian_diskon != null) {
//            System.out.println(allTypeData.varian_diskon);
//            holder.potonganHarga.setVisibility(View.GONE);
//            holder.txtHarga.setText(allTypeData.format.format(allTypeData.datalist.get(position).getHarga()));
//        }else {
//            System.out.println("ini ngga usah ");
//        }
////        if (allTypeData.varian_diskon == null){
////            holder.potonganHarga.setVisibility(View.GONE);
////            holder.txtHarga.setText(allTypeData.format.format(allTypeData.datalist.get(position).getHarga()));
////        } else {
////            try {
////                if (allTypeData.varian_diskon.getString("kategori").equals("nominal")){
////                    holder.txtHarga.setText(allTypeData.format.format(allTypeData.datalist.get(position).getHarga() - allTypeData.nominal));
////                    holder.potonganHarga.setText(allTypeData.format.format(allTypeData.datalist.get(position).getHarga()));
////                    holder.potonganHarga.setPaintFlags(holder.potonganHarga.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
////                }else {
////                    holder.potonganHarga.setVisibility(View.GONE);
////                    holder.txtHarga.setText(allTypeData.format.format(allTypeData.datalist.get(position).getHarga()));
////                }
////            } catch (JSONException e) {
////                throw new RuntimeException(e);
////            }
////        }
//        allTypeData.modelDataSetGet = new ArrayList<>();
//        allTypeData.dataModel2 = new ArrayList<>();
//        holder.to_detail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                allTypeData.kodeProduct = kode_product;
//                allTypeData.nameProduct = name_product;
//                allTypeData.priceProduct = price_product;
//                allTypeData.Values = varian_diskon;
//                allTypeData.potonganHarga = diskon;
//                allTypeData.imageProduk = image_produk;
////                allTypeData.subHarga = sub_harga;
//                BottomSheet(view);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return (allTypeData.datalist != null) ? allTypeData.datalist.size() :0;
//    }
//
//
//    public class MahasiswaViewHolder extends RecyclerView.ViewHolder {
//        private TextView txtNamaProduk, txtHarga, potonganHarga, value, txtSubHarga;
//        private ImageView img, trush;
//        private LinearLayout to_detail;
//        private TextView tambah_produk;
//
//        public MahasiswaViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tambah_produk = itemView.findViewById(R.id.tambah_produk);
//            to_detail = itemView.findViewById(R.id.to_detail);
//            txtNamaProduk = itemView.findViewById(R.id.name_product);
//            txtHarga = itemView.findViewById(R.id.harga_pcs);
//            potonganHarga = itemView.findViewById(R.id.potongan_harga);
//            value = itemView.findViewById(R.id.value);
//            txtSubHarga = itemView.findViewById(R.id.harga_total);
//            img = itemView.findViewById(R.id.img_tambah_barang);
//            trush = itemView.findViewById(R.id.icon_sampah);
//        }
//    }
//
//
//    public static class DataSetGet {
//
//        private String data;
//
//        public DataSetGet(String data) {
//            this.data = data;
//        }
//
//        public String getData() {
//            return data;
//        }
//
//        public void setData(ArrayList<String> data) {
//            this.data = String.valueOf(data);
//        }
//    }
//
//}

public class TambahBarangActivity extends AppCompatActivity implements RecyclerViewListener, TextWatcher{
    ArrayList<ModelDetailBarang> dataModels;
    ArrayList<ModelProdukFree> dataModelsFreeSama;
    ArrayList<ModelProdukFree> dataModelsFreeBebas;
    ArrayList<ModelProdukFree> dataModelsFreeBebasDetail;
    JSONObject detail_barang_sama, detail_barang_bebas;
    JSONArray list_detail_barang_sama, list_detail_barang_bebas;
    ArrayList<ModelProdukFree> dataModelsFilter;
    RecyclerView materi;
    LinearLayout linearLayout ;
    EditText kode, searchView;
    String setKEY_KODE;
    TextView kode_produk, nama_produk, harga_produk, tambah_barang;
    LinearLayout button_back_to_transaksi;
    FlexboxLayout diskon_produk;
    EditText field_isi_jumlah_barang;
    static RecyclerView list_produk_gratis, list_produk_free_dialog;
    static TextView sub_total_harga, free_didapat;
    static String nama_barang, kode_barang, gambar_barang;
    static int field = 0, free = 0, sub_total = 0;
    int view2 = 2, view1 = 1;
    static int jumlah_value_tiap_produkfree;
    static ImageView searchScanKode;
    static ImageView backTOMainTransaksi;
    static TextView scan1, scan2, scan3;
    static double hasilDiskonPersen;
    static JSONArray list_data;
    static int hasilDiskonNominal;
    static String scanProduk, mode;
    private String getKEY_KODE, KEY_KODE = "KODE_BARANG";
    private RequestQueue mQueue;
    static ArrayList<String> arr = new ArrayList<>();
    public void toMain(){
        backTOMainTransaksi.performClick();
    }
    private int availableStock;
    static AddProductFree adapt;
    public void StockTextWatcher(int stock) {
        availableStock = stock;
    }
    static void scan1(){
        TambahBarangActivity.scan1.performClick();
    }
    static void scan2(){
        TambahBarangActivity.searchScanKode.performClick();
    }
    static void scan3(){
        TambahBarangActivity.scan3.performClick();
    }

//    ModelAddBarang modelAddBarang;
//    private static AdapterAddBarang adapterTransaksi;

//    private void loadProduct(){
//
//        // Akses API
//        mQueue = Volley.newRequestQueue(TambahBarangActivity.this);
//
//        String url = "http://tijara.mphstar.tech/api/product";
//
//        dataModels = new ArrayList<>();
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//
//                try {
//                    JSONObject productObject, valueDiskon, valueProdukFreeJson;
//                    String valueProdukFreeString;
//                    System.out.println(response.length());
//                    for (int i = 0; i < response.length(); i++) {
//                        System.out.println(response.getJSONObject(i));
//                        productObject = response.getJSONObject(i);
//                        if (productObject.getString("jenis").equals("jual")){
//                            if (productObject.isNull("diskon")){
//                                System.out.println("ini produk tanpa diskon");
//                                dataModels.add(new ModelAddBarang(productObject.getString("kode_br"), productObject.getString("nama_br"),  productObject.getString("stok"), productObject.getInt("harga"), "http://tijara.mphstar.tech/uploads/products/"+productObject.getString("gambar")+"", null, R.drawable.shape_white, null, 0, null));
//                            }else {
//                                System.out.println("produk dengan diskon");
//                                valueDiskon = productObject.getJSONObject("diskon");
//                                if (valueDiskon.getString("kategori").equals("nominal")){
//                                    System.out.println("ini produk diskon nominal");
//                                    dataModels.add(new ModelAddBarang(productObject.getString("kode_br"), productObject.getString("nama_br"),  productObject.getString("stok"), productObject.getInt("harga"), "http://tijara.mphstar.tech/uploads/products/"+productObject.getString("gambar")+"", valueDiskon.getString("kategori"), R.drawable.shape_white, productObject.getString("kode_diskon"), valueDiskon.getInt("nominal"), null));
//                                } else if (valueDiskon.getString("free_product") != null && valueDiskon.getString("kategori").equals("free")){
//                                    System.out.println("ini produk diskon produk free");
//                                    valueProdukFreeString = valueDiskon.getString("free_product");
//                                    valueProdukFreeJson = new JSONObject(valueProdukFreeString);
//                                    dataModels.add(new ModelAddBarang(productObject.getString("kode_br"), productObject.getString("nama_br"),  productObject.getString("stok"), productObject.getInt("harga"), "http://tijara.mphstar.tech/uploads/products/"+productObject.getString("gambar")+"", valueDiskon.getString("kategori"), R.drawable.shape_white, productObject.getString("kode_diskon"),0, valueProdukFreeJson));
//                                } else {
//                                    System.out.println("ini produk dengan diskon lainnya");
//                                    dataModels.add(new ModelAddBarang(productObject.getString("kode_br"), productObject.getString("nama_br"),  productObject.getString("stok"), productObject.getInt("harga"), "http://tijara.mphstar.tech/uploads/products/"+productObject.getString("gambar")+"", valueDiskon.getString("kategori"), R.drawable.shape_white, productObject.getString("kode_diskon"), valueDiskon.getInt("nominal"), null));
//                                }
//                            }
//                        } else {
//                            System.out.println("ini produk Free");
//                        }
//                    }
//                    AddProduct addProduct = new AddProduct(dataModels, TambahBarangActivity.this);
//                    materi = findViewById(R.id.list_barang);
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TambahBarangActivity.this);
//                    materi.setLayoutManager(layoutManager);
//                    materi.setAdapter(addProduct);
////                    adapterTransaksi = new AdapterAddBarang(dataModels, TambahBarangActivity.this);
////                    materi = findViewById(R.id.list_barang);
////                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TambahBarangActivity.this);
////                    materi.setLayoutManager(layoutManager);
////                    materi.setAdapter(adapterTransaksi);
//
////                    filter(searchView.getText().toString());
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // error dalam mengambil data JSON
//                Log.e("Error", "Error: " + error.getMessage());
//            }});
//
//        mQueue.add(jsonArrayRequest);
//        //Akhir Akses Api
//
//    }

//    private void filter(String teks) {
//
//        System.out.println(teks.toString().length());
//        ArrayList<ModelAddBarang> dataModels2 = new ArrayList<>();
//        //looping through existing elements
//        for (ModelAddBarang s : dataModels) {
//            //if the existing elements contains the search input
//            if (s.getKode_produk().toLowerCase().contains(teks.toLowerCase())) {
//                //adding the element to filtered list
//                dataModels2.add(s);
//            }
//        }
//
//        //calling a method of the adapter class and passing the filtered list
//        adapterTransaksi.filterList(dataModels2, linearLayout, materi);
//    }

    private void GetProdukFreeDialog(View view){

        // Akses API
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, "https://tijara.mphstar.tech/api/product/free", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataModelsFreeBebas = new ArrayList<>();
                try {
                    JSONArray list_barang = new JSONArray(response);
                    JSONObject mapping_list;
                    for (int i = 0; i < list_barang.length(); i++){
                        mapping_list = list_barang.getJSONObject(i);
                        if (mapping_list.getInt("stok") != 0 ){
                            ModelProdukFree barang = new ModelProdukFree(mapping_list.getString("nama_br"), mapping_list.getString("gambar"), mapping_list.getString("kode_br"), mapping_list.getInt("stok"));
//                            Toast.makeText(TambahBarangActivity.this, mapping_list.getString("diskon"), Toast.LENGTH_SHORT).show();
                            Log.d("tes error", mapping_list.getString("diskon"));
                            try {
                                JSONObject mapping_diskon = new JSONObject(mapping_list.getString("diskon"));
                                if (mapping_diskon.getString("kategori").equals("free")){
                                    barang.setDetailBarangFree(new DetailBarangFree(mapping_diskon.getString("free_product"), mapping_diskon.getString("kode_diskon"), Integer.valueOf(mapping_diskon.getString("buy")), Integer.valueOf(mapping_diskon.getString("free"))));
                                }

                            } catch (Exception e){

                            }
                            dataModelsFreeBebas.add(barang);
                        }
                    }

                    adapt = new AddProductFree(dataModelsFreeBebas, TambahBarangActivity.this);
                    list_produk_free_dialog.setLayoutManager(new LinearLayoutManager(TambahBarangActivity.this));
                    list_produk_free_dialog.setAdapter(adapt);

                    queue.getCache().clear();
                } catch (Exception e){
                    Toast.makeText(TambahBarangActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error dalam mengambil data JSON
                Log.e("Error", "Error: " + error.getMessage());
            }});

        queue.add(request);
    }

    private void GetProdukFreeSama(String nama_produk){

        // Akses API
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, "https://tijara.mphstar.tech/api/product/jual", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataModelsFreeSama = new ArrayList<>();
                try {
                    JSONArray list_barang = new JSONArray(response);
                    JSONObject mapping_list;
                    for (int i = 0; i < list_barang.length(); i++){
                        mapping_list = list_barang.getJSONObject(i);
                        if (mapping_list.getInt("stok") != 0 && mapping_list.getString("nama_br").equals(nama_produk)){
                            ModelProdukFree barang = new ModelProdukFree(mapping_list.getString("nama_br"), mapping_list.getString("gambar"), mapping_list.getString("kode_br"), mapping_list.getInt("stok"));
//                            Toast.makeText(TambahBarangActivity.this, mapping_list.getString("diskon"), Toast.LENGTH_SHORT).show();
                            Log.d("tes error", mapping_list.getString("diskon"));
                            try {
                                JSONObject mapping_diskon = new JSONObject(mapping_list.getString("diskon"));
                                if (mapping_diskon.getString("kategori").equals("free")){
                                    barang.setDetailBarangFree(new DetailBarangFree(mapping_diskon.getString("free_product"), mapping_diskon.getString("kode_diskon"), Integer.valueOf(mapping_diskon.getString("buy")), Integer.valueOf(mapping_diskon.getString("free"))));
                                }

                            } catch (Exception e){

                            }
                            dataModelsFreeSama.add(barang);
                        }
                    }

                    adapt = new AddProductFree(dataModelsFreeSama, TambahBarangActivity.this);
                    list_produk_gratis.setLayoutManager(new LinearLayoutManager(TambahBarangActivity.this));
                    list_produk_gratis.setAdapter(adapt);

                    queue.getCache().clear();
                } catch (Exception e){
                    Toast.makeText(TambahBarangActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

                adapt.notifyDataSetChanged();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error dalam mengambil data JSON
                Log.e("Error", "Error: " + error.getMessage());
            }});

        queue.add(request);
    }

    private void loadProduct(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, "https://tijara.mphstar.tech/api/product/jual", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataModels = new ArrayList<>();
                try {
                    JSONArray list_barang = new JSONArray(response);
                    JSONObject mapping_list;
                    for (int i = 0; i < list_barang.length(); i++){
                        mapping_list = list_barang.getJSONObject(i);
                        if (mapping_list.getInt("stok") != 0){
                            ModelDetailBarang barang = new ModelDetailBarang(mapping_list.getString("nama_br"), mapping_list.getString("gambar"), mapping_list.getString("kode_br"), mapping_list.getString("kode_diskon"), Integer.valueOf(mapping_list.getString("harga")), mapping_list.getInt("stok"));
//                            Toast.makeText(TambahBarangActivity.this, mapping_list.getString("diskon"), Toast.LENGTH_SHORT).show();
                            Log.d("tes error", mapping_list.getString("diskon"));
                            try {
                                JSONObject mapping_diskon = new JSONObject(mapping_list.getString("diskon"));
                                if(mapping_diskon.getString("kategori").equals("nominal") || mapping_diskon.getString("kategori").equals("persen")){
                                    barang.setDetailDiskon(new DetailDiskon(mapping_diskon.getString("kategori"), Integer.valueOf(mapping_diskon.getString("nominal")), null));
//                                Toast.makeText(PilihBarangActivity.this, mapping_list.getString("nominal"), Toast.LENGTH_SHORT).show();
                                }else if (mapping_diskon.getString("kategori").equals("free")){
                                    barang.setDetailDiskon(new DetailDiskon(mapping_diskon.getString("kategori"), 0, new DetailBarangFree(mapping_diskon.getString("free_product"), mapping_diskon.getString("kode_diskon"), Integer.valueOf(mapping_diskon.getString("buy")), Integer.valueOf(mapping_diskon.getString("free")))));
                                }

                            } catch (Exception e){

                            }
                            dataModels.add(barang);
                        }
                    }

                    AddProduct adapt = new AddProduct(dataModels, TambahBarangActivity.this);
                    materi.setLayoutManager(new LinearLayoutManager(TambahBarangActivity.this));
                    materi.setAdapter(adapt);

                    queue.getCache().clear();
                } catch (Exception e){
                    Toast.makeText(TambahBarangActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.VolleyError error) {
                Toast.makeText(TambahBarangActivity.this, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ambilValues.namaProduk != null){
            System.out.println("adadad");
            Transaksi.view = 2;
        } else if (ambilValues.nama_produk == null){
            Transaksi.dataModels = null;
        }
        Intent intent = new Intent(TambahBarangActivity.this, Transaksi.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_barang);

        linearLayout = findViewById(R.id.image_no_value);
        searchScanKode = findViewById(R.id.scan_produk);
        scan1 = findViewById(R.id.scan1);
        scan2 = findViewById(R.id.scan2);
        scan3 = findViewById(R.id.scan3);
        backTOMainTransaksi = findViewById(R.id.back_to_main_transaksi);
        searchView = findViewById(R.id.field_kode_product);
        materi = findViewById(R.id.list_barang);

        loadProduct();

        if (dataModelsFreeBebasDetail == null){
            dataModelsFreeBebasDetail = new ArrayList<>();
            detail_barang_sama = new JSONObject();
            list_detail_barang_sama = new JSONArray();
        }

        int invisible = linearLayout.getVisibility();
        if (invisible == View.VISIBLE) {
            linearLayout.setVisibility(View.GONE);
        }else if (invisible == View.VISIBLE && dataModels.stream().toArray().length == 0) {
            linearLayout.setVisibility(View.VISIBLE);
        }else {
            linearLayout.setVisibility(View.GONE);
        }

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            getKEY_KODE = extra.getString(KEY_KODE);
            setKEY_KODE = getKEY_KODE;
        }

        backTOMainTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ambilValues.namaProduk != null){
                    System.out.println("adadad");
                    Transaksi.view = 2;
                } else if (ambilValues.nama_produk == null){
                    Transaksi.dataModels = null;
                }
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        scan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TambahBarangActivity.scanProduk = "produkJual";
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

        searchScanKode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TambahBarangActivity.scanProduk = "produkJual1";
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

        scan3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TambahBarangActivity.scanProduk = "produkFree";
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

    @Override
    protected void onStart() {
        super.onStart();
        searchView.setText(setKEY_KODE);
    }

    private void scanBarcode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan Produk");
        options.setBeepEnabled(true);
        options.setCaptureActivity(ScanBarcodeActivity.class);
        options.setOrientationLocked(true);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            if (TambahBarangActivity.scanProduk == "produkJual1"){
                searchView.setText(result.getContents());
            }else if (TambahBarangActivity.scanProduk == "produkJual"){
                searchView.setText(result.getContents());
            }else if (TambahBarangActivity.scanProduk == "produkFree"){
                allTypeData.searchProdukFree.setText(result.getContents());
            }
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
    static public void showToast(Context context, String message) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast, null);

        Toast toast = new Toast(context);

        if (message == "Produk yang sama sudah terdaftar dalam list"){
            LottieAnimationView lottieAnimationView = (LottieAnimationView) layout.findViewById(R.id.image_json);
            lottieAnimationView.setAnimation(R.raw.failed_add);
            TextView Message = (TextView) layout.findViewById(R.id.message_toast);
            Message.setText(message);
            TextView Title = (TextView) layout.findViewById(R.id.title_toast);
            Title.setVisibility(View.GONE);
            toast.setGravity(Gravity.TOP, 0, 0);

        }else if (message == "Tidak ada Produk yang dipilih"){
            LottieAnimationView lottieAnimationView = (LottieAnimationView) layout.findViewById(R.id.image_json);
            lottieAnimationView.setAnimation(R.raw.failed_add);
            TextView Message = (TextView) layout.findViewById(R.id.message_toast);
            Message.setText(message);
            TextView Title = (TextView) layout.findViewById(R.id.title_toast);
            Title.setVisibility(View.GONE);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

        }else if (message == "Produk yang dipilih melebihi batas"){
            LottieAnimationView lottieAnimationView = (LottieAnimationView) layout.findViewById(R.id.image_json);
            lottieAnimationView.setAnimation(R.raw.failed_add);
            TextView Message = (TextView) layout.findViewById(R.id.message_toast);
            Message.setText(message);
            TextView Title = (TextView) layout.findViewById(R.id.title_toast);
            Title.setVisibility(View.GONE);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

        }else if (message == "Isikan jumlah barang terlebih dahulu"){
            LottieAnimationView lottieAnimationView = (LottieAnimationView) layout.findViewById(R.id.image_json);
            lottieAnimationView.setAnimation(R.raw.failed_add);
            TextView Message = (TextView) layout.findViewById(R.id.message_toast);
            Message.setText(message);
            TextView Title = (TextView) layout.findViewById(R.id.title_toast);
            Title.setVisibility(View.GONE);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

        }else if (message == "Pembayaran Berhasil"){
            LottieAnimationView lottieAnimationView = (LottieAnimationView) layout.findViewById(R.id.image_json);
            lottieAnimationView.setAnimation(R.raw.add_product);
            TextView Message = (TextView) layout.findViewById(R.id.message_toast);
            Message.setText(message);
            TextView Title = (TextView) layout.findViewById(R.id.title_toast);
            Title.setText(message);
            toast.setGravity(Gravity.TOP, 0, 0);
        }else {
            LottieAnimationView lottieAnimationView = (LottieAnimationView) layout.findViewById(R.id.image_json);
            lottieAnimationView.setAnimation(R.raw.add_product);
            TextView Title = (TextView) layout.findViewById(R.id.title_toast);
            Title.setText(message);
            toast.setGravity(Gravity.TOP, 0, 0);
        }


        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public void onClick(View view, int position) {
        if (dataModelsFreeBebas != null){
            if (dataModelsFilter.get(position).getKode_produk() == dataModelsFilter.get(position).getKode_produk()){
                showToast(view.getContext(), dataModelsFilter.get(position).getName_product());
                nama_barang = dataModelsFilter.get(position).getName_product();
                kode_barang = dataModelsFilter.get(position).getKode_produk();
                gambar_barang = dataModelsFilter.get(position).getImage_product();
            }

        }else if (dataModels != null){
            if (dataModels.get(position).getNama() == dataModels.get(position).getNama()){
                BottomSheet(view, position);
            }
        }

    }

    private void BottomSheet(View view, int position) {
        if (dataModels.get(position).getDetailDiskon() != null){
            if (dataModels.get(position).getDetailDiskon().jenis_diskon.equals("nominal")){
                System.out.println("no gratis produk");
                non_produkFree(view, position);
            }else if (dataModels.get(position).getDetailDiskon().jenis_diskon.equals("persen")){
                System.out.println("no gratis produk");
                non_produkFree(view, position);
            }else if (dataModels.get(position).getDetailDiskon().jenis_diskon.equals("free")){
                System.out.println("gratis produk");
                produkFree(view, position);
            }
        }else {
//            Toast.makeText(this, "Ini no diskon", Toast.LENGTH_SHORT).show();
            non_produkFree(view, position);
        }
    }

    // Awal Code Diskon Persen, dan Nominal
    void non_produkFree(View view, int position){
        Transaksi.addProductUsing = 2;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getRootView().getContext(), R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_product);
        bottomSheetDialog.show();

        TextView kode_produk, nama_produk, harga_produk, diskon_produk, field_isi_jumlah_barang, sub_total_harga;
        LinearLayout button_back_to_transaksi;
        FlexboxLayout values;

        field_isi_jumlah_barang = bottomSheetDialog.findViewById(R.id.field_isi_jumlah_barang);
        sub_total_harga = bottomSheetDialog.findViewById(R.id.sub_total);
        sub_total_harga.setText(String.valueOf(0));
        values = bottomSheetDialog.findViewById(R.id.info_pesanan);
        kode_produk = bottomSheetDialog.findViewById(R.id.kode_produk);
        nama_produk = bottomSheetDialog.findViewById(R.id.nama_produk);
        diskon_produk = bottomSheetDialog.findViewById(R.id.diskon_produk);
        harga_produk = bottomSheetDialog.findViewById(R.id.harga_produk);

        values.setVisibility(View.GONE);
        kode_produk.setText(dataModels.get(position).getKode_br());
        nama_produk.setText(dataModels.get(position).getNama());
        harga_produk.setText(Env.formatRupiah(dataModels.get(position).getHarga_awal()));

        if (dataModels.get(position).getDetailDiskon() == null){
            diskon_produk.setText(Env.formatRupiah(0));
        }else {
            if (dataModels.get(position).getDetailDiskon().jenis_diskon.equals("nominal")){
                diskon_produk.setText(Env.formatRupiah(dataModels.get(position).getDetailDiskon().nominal));
            } else if (dataModels.get(position).getDetailDiskon().jenis_diskon.equals("persen")) {
                diskon_produk.setText(Env.formatRupiah(dataModels.get(position).getDetailDiskon().nominal)+"%");
            }
        }

        field_isi_jumlah_barang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    int number = Integer.parseInt(s.toString());
                    int maxStok = dataModels.get(position).getStok();

                    if (maxStok != 0) {
                        // Periksa apakah angka berada dalam rentang yang diinginkan
                        if (number < 1 || number > maxStok) {
                            // Angka di luar rentang, hapus teks terakhir
                            String newText = s.subSequence(0, s.length() - 1).toString();
                            field_isi_jumlah_barang.setText(newText);
                        }
                    }else {
                        if (maxStok < number ){
                            String newText = s.subSequence(0, s.length() - 1).toString();
                            field_isi_jumlah_barang.setText(String.valueOf(Integer.valueOf(newText)*0));
                        }
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() != 0){
                    if (dataModels.get(position).getDetailDiskon() == null){
                        sub_total_harga.setText(allTypeData.format.format(dataModels.get(position).getHarga_awal() * Integer.parseInt(field_isi_jumlah_barang.getText().toString())));
                    }else {
                        if (dataModels.get(position).getDetailDiskon().jenis_diskon.equals("nominal")){
                            sub_total_harga.setText(allTypeData.format.format((dataModels.get(position).getHarga_awal() - dataModels.get(position).getDetailDiskon().nominal) * Integer.parseInt(field_isi_jumlah_barang.getText().toString())));
                        } else if (dataModels.get(position).getDetailDiskon().jenis_diskon.equals("persen")) {
                            double bagi100 = (double) dataModels.get(position).getDetailDiskon().nominal / 100;
                            double kali100 = dataModels.get(position).getHarga_awal() * bagi100;
                            sub_total_harga.setText(allTypeData.format.format((dataModels.get(position).getHarga_awal() - Double.valueOf(kali100).intValue()) * Integer.parseInt(field_isi_jumlah_barang.getText().toString())));
                        }
                    }

                }else if (s.toString().length() == 0){
                    allTypeData.jenisProduk = 2;
                    sub_total_harga.setText(String.valueOf(0));
                }
            }
        });

        button_back_to_transaksi = bottomSheetDialog.findViewById(R.id.button_back_to_transaksi);
        button_back_to_transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Transaksi.dataModels == null){
                    Transaksi.view = 1;
                }else if (Transaksi.dataModels != null){
                    Transaksi.view = 2;
                }

                Intent inten = new Intent();
                JSONObject data = new JSONObject();
                list_data = new JSONArray();
                try {
                    data.put("kode_br", dataModels.get(position).getKode_br());
                    data.put("nama_br", dataModels.get(position).getNama());
                    data.put("harga_br", dataModels.get(position).getHarga_awal());
                    if (dataModels.get(position).getDetailDiskon() == null){
                        data.put("jenis_diskon", "tidak_diskon");
                        data.put("kode_diskon_br", "0");
                        data.put("potongan_harga_br", 0);
                        data.put("harga_total_akhir_br", dataModels.get(position).getHarga_awal() * Integer.parseInt(field_isi_jumlah_barang.getText().toString()));
                        data.put("qty_br", field_isi_jumlah_barang.getText().toString());
                        data.put("list_produk_didapat", "null");
                        data.put("diskon", 0);
                    }else {
                        data.put("kode_diskon_br", dataModels.get(position).getKode_diskon());
                        if (dataModels.get(position).getDetailDiskon().jenis_diskon.equals("nominal")){
                            data.put("jenis_diskon", dataModels.get(position).getDetailDiskon().jenis_diskon);
                            data.put("potongan_harga_br", dataModels.get(position).getHarga_awal() - dataModels.get(position).getDetailDiskon().nominal);
                            data.put("harga_total_akhir_br", (dataModels.get(position).getHarga_awal() - dataModels.get(position).getDetailDiskon().nominal) * Integer.parseInt(field_isi_jumlah_barang.getText().toString()));
                            data.put("qty_br", field_isi_jumlah_barang.getText().toString());
                            data.put("list_produk_didapat", "null");
                            data.put("diskon", dataModels.get(position).getDetailDiskon().nominal);
                        } else if (dataModels.get(position).getDetailDiskon().jenis_diskon.equals("persen")) {
                            double bagi100 = (double) dataModels.get(position).getDetailDiskon().nominal / 100;
                            double kali100 = dataModels.get(position).getHarga_awal() * bagi100;
                            double kurang_harga = dataModels.get(position).getHarga_awal() - kali100;
                            data.put("jenis_diskon", dataModels.get(position).getDetailDiskon().jenis_diskon);
                            data.put("potongan_harga_br", Double.valueOf(kurang_harga).intValue());
                            data.put("harga_total_akhir_br", Double.valueOf(kurang_harga).intValue() * Integer.parseInt(field_isi_jumlah_barang.getText().toString()));
                            data.put("qty_br", field_isi_jumlah_barang.getText().toString());
                            data.put("list_produk_didapat", "null");
                            data.put("diskon", dataModels.get(position).getDetailDiskon().nominal);
                        } else if (dataModels.get(position).getDetailDiskon().jenis_diskon.equals("free")){
                            if (dataModels.get(position).getDetailDiskon().getDetailBarangFree().jenis_pilihan.equals("bebas")){
                                data.put("jenis_diskon", dataModels.get(position).getDetailDiskon().jenis_diskon);
                                data.put("potongan_harga_br", 0);
                                data.put("harga_total_akhir_br", dataModels.get(position).getHarga_awal() * Integer.parseInt(field_isi_jumlah_barang.getText().toString()));
                                data.put("qty_br", field_isi_jumlah_barang.getText().toString());
                                data.put("list_produk_didapat", list_detail_barang_bebas);
                                data.put("list_produk_didapat", "null");
                                data.put("diskon", 0);
                            }else if (dataModels.get(position).getDetailDiskon().getDetailBarangFree().jenis_pilihan.equals("sama")){
                                data.put("jenis_diskon", dataModels.get(position).getDetailDiskon().jenis_diskon);
                                data.put("potongan_harga_br", 0);
                                data.put("harga_total_akhir_br", dataModels.get(position).getHarga_awal() * Integer.parseInt(field_isi_jumlah_barang.getText().toString()));
                                data.put("qty_br", field_isi_jumlah_barang.getText().toString());
                                data.put("list_produk_didapat", list_detail_barang_sama);
                                data.put("list_produk_didapat", "null");
                                data.put("diskon", 0);
                            }
                        }
                    }
                    data.put("gambar_br", dataModels.get(position).getGambar());

                } catch (Exception e){
                    Toast.makeText(TambahBarangActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

                list_data.put(data);

                inten.putExtra("data", data.toString());
                inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                setResult(RESULT_OK, inten);
                finish();

            }
        });
    }
    // Akhir Code Diskon Persen, dan Nominal

    // Awal Code Gratis Produk
    public void produkFree(View view, int position){
        Transaksi.addProductUsing = 1;
        if (list_produk_gratis != null){
            view1 = 3;
        }else {
            view1 = 1;
        }
        showBottomSheet(view, position);
    }
    public void showBottomSheet(View view, int position){
        Gson gson = new Gson();
        kirimValues kirimValues = new kirimValues();
        Transaksi mainActivity = new Transaksi();
        BottomSheetDialog bottomSheetDialog;

        bottomSheetDialog = new BottomSheetDialog(view.getRootView().getContext(), R.style.BottomSheetDialogStyle);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_product);

        bottomSheetDialog.show();
        bottomSheetDialog.create();

        field_isi_jumlah_barang = bottomSheetDialog.findViewById(R.id.field_isi_jumlah_barang);
        sub_total_harga = bottomSheetDialog.findViewById(R.id.sub_total);
        free_didapat = bottomSheetDialog.findViewById(R.id.free_produk_didapat);
        button_back_to_transaksi = bottomSheetDialog.findViewById(R.id.button_back_to_transaksi);
        tambah_barang = bottomSheetDialog.findViewById(R.id.tambah_produk);
        kode_produk = bottomSheetDialog.findViewById(R.id.kode_produk);
        nama_produk = bottomSheetDialog.findViewById(R.id.nama_produk);
        harga_produk = bottomSheetDialog.findViewById(R.id.harga_produk);
        diskon_produk = bottomSheetDialog.findViewById(R.id.line_diskon);
        list_produk_gratis = bottomSheetDialog.findViewById(R.id.produk_gratis);

        if (dataModels.get(position).getDetailDiskon().getDetailBarangFree().jenis_pilihan.equals("sama")){
            tambah_barang.setVisibility(View.GONE);
        }else {
            tambah_barang.setVisibility(View.VISIBLE);
        }

        kode_produk.setText(dataModels.get(position).getKode_br());
        nama_produk.setText(dataModels.get(position).getNama());
        harga_produk.setText(Env.formatRupiah(dataModels.get(position).getHarga_awal()));
        diskon_produk.setVisibility(View.GONE);
        if (field == 0){
            free_didapat.setText(String.valueOf(0));
            sub_total_harga.setText(String.valueOf(0));
        }else if (field != 0){
            list_produk_gratis.setVisibility(View.VISIBLE);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
            list_produk_gratis.setLayoutManager(layoutManager);
            list_produk_gratis.setAdapter(adapt);

            free_didapat.setText(String.valueOf(free));
            field_isi_jumlah_barang.setText(String.valueOf(field));
            sub_total_harga.setText(Env.formatRupiah(sub_total));
        }
        String nama_produk_free = dataModels.get(position).getNama();


//        if (view1 == 1){
//            list_produk_gratis.setVisibility(View.GONE);
//            System.out.println("a");
//        }else if (view1 == 2){
//            System.out.println("b");
//            list_produk_gratis.setVisibility(View.VISIBLE);
//        }else if (view1 == 3){
//            System.out.println("c");
////            if (allTypeData.modelDataSetGet != null){
////                list_produk_gratis.setVisibility(View.GONE);
////                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
////                list_produk_gratis.setLayoutManager(layoutManager);
////                list_produk_gratis.setAdapter();
////            }
//        }

        field_isi_jumlah_barang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    int number = Integer.parseInt(charSequence.toString());
                    int maxStok = dataModels.get(position).getStok();

                    if (maxStok != 0) {
                        // Periksa apakah angka berada dalam rentang yang diinginkan
                        if (number < 1 || number > maxStok) {
                            // Angka di luar rentang, hapus teks terakhir
                            String newText = charSequence.subSequence(0, charSequence.length() - 1).toString();
                            field_isi_jumlah_barang.setText(newText);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() != 0){
                    allTypeData.jenisProduk = 2;
                    if (dataModels.get(position).getDetailDiskon() == null){
                        sub_total_harga.setText(allTypeData.format.format(dataModels.get(position).getHarga_awal() * Integer.parseInt(field_isi_jumlah_barang.getText().toString())));
                    }else {
                        if (dataModels.get(position).getDetailDiskon().jenis_diskon.equals("nominal")){
                            sub_total_harga.setText(allTypeData.format.format((dataModels.get(position).getHarga_awal() - dataModels.get(position).getDetailDiskon().nominal) * Integer.parseInt(field_isi_jumlah_barang.getText().toString())));
                        } else if (dataModels.get(position).getDetailDiskon().jenis_diskon.equals("persen")) {
                            double bagi100 = (double) dataModels.get(position).getDetailDiskon().nominal / 100;
                            double kali100 = dataModels.get(position).getHarga_awal() * bagi100;
                            sub_total_harga.setText(allTypeData.format.format((dataModels.get(position).getHarga_awal() - Double.valueOf(kali100).intValue()) * Integer.parseInt(field_isi_jumlah_barang.getText().toString())));
                        } else if (dataModels.get(position).getDetailDiskon().jenis_diskon.equals("free")) {
                            if (dataModels.get(position).getDetailDiskon().getDetailBarangFree().jenis_pilihan.equals("sama")){

                                list_produk_gratis.setVisibility(View.VISIBLE);
                                GetProdukFreeSama(nama_produk_free);
                                int num = Integer.parseInt(editable.toString());
                                int buyQuantity = dataModels.get(position).getDetailDiskon().getDetailBarangFree().buy;
                                int modulus = num % buyQuantity;

                                if (num == buyQuantity) {
                                    free_didapat.setText(String.valueOf(dataModels.get(position).getDetailDiskon().getDetailBarangFree().free));
                                } else if (num < buyQuantity) {
                                    free_didapat.setText("0");
                                } else if (num > buyQuantity) {
                                    if (modulus == 0) {
                                        int freeQuantity = (num / buyQuantity) * dataModels.get(position).getDetailDiskon().getDetailBarangFree().free;
                                        free_didapat.setText(String.valueOf(freeQuantity));
                                    } else {
                                        int freeQuantity = (num / buyQuantity) * dataModels.get(position).getDetailDiskon().getDetailBarangFree().free;
                                        free_didapat.setText(String.valueOf(freeQuantity));
                                    }
                                }

                                update();
                            }else if (dataModels.get(position).getDetailDiskon().getDetailBarangFree().jenis_pilihan.equals("bebas")){
                                int num = Integer.parseInt(editable.toString());
                                int buyQuantity = dataModels.get(position).getDetailDiskon().getDetailBarangFree().buy;
                                int modulus = num % buyQuantity;

                                if (num == buyQuantity) {
                                    free_didapat.setText(String.valueOf(dataModels.get(position).getDetailDiskon().getDetailBarangFree().free));
                                    free = Integer.valueOf(String.valueOf(dataModels.get(position).getDetailDiskon().getDetailBarangFree().free));
                                    field = Integer.parseInt(editable.toString());
                                } else if (num < buyQuantity) {
                                    free_didapat.setText("0");
                                } else if (num > buyQuantity) {
                                    if (modulus == 0) {
                                        int freeQuantity = (num / buyQuantity) * dataModels.get(position).getDetailDiskon().getDetailBarangFree().free;
                                        free_didapat.setText(String.valueOf(freeQuantity));
                                        free = Integer.valueOf(String.valueOf(freeQuantity));
                                        field = Integer.parseInt(editable.toString());
                                    } else {
                                        int freeQuantity = (num / buyQuantity) * dataModels.get(position).getDetailDiskon().getDetailBarangFree().free;
                                        free_didapat.setText(String.valueOf(freeQuantity));
                                        free = Integer.valueOf(String.valueOf(freeQuantity));
                                        field = Integer.parseInt(editable.toString());
                                    }
                                }
                            }

                            sub_total_harga.setText(Env.formatRupiah(dataModels.get(position).getHarga_awal() * Integer.parseInt(field_isi_jumlah_barang.getText().toString())));
                            sub_total = dataModels.get(position).getHarga_awal() * Integer.parseInt(field_isi_jumlah_barang.getText().toString());
                        }
                    }
                }else if (editable.toString().length() == 0){
                    allTypeData.jenisProduk = 2;
                    free_didapat.setText("0");
                    sub_total_harga.setText(Env.formatRupiah(0));
                }
            }
        });

        tambah_barang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sub_total_harga.toString().length() == 0){
                    field_isi_jumlah_barang.setText("");
                }else if (sub_total_harga.toString().length() != 0){
                    field_isi_jumlah_barang.setText(field_isi_jumlah_barang.getText().toString());
                }

                if (Integer.valueOf(free_didapat.getText().toString()) > TambahBarangActivity.jumlah_value_tiap_produkfree){
                    showAlernDialog(view, position);
                    bottomSheetDialog.dismiss();
                }else if (TambahBarangActivity.jumlah_value_tiap_produkfree == Integer.valueOf(free_didapat.getText().toString())){
                    showToast(view.getContext(), "Produk yang dipilih melebihi batas");
                }else if (Integer.valueOf(free_didapat.getText().toString()) == TambahBarangActivity.jumlah_value_tiap_produkfree){
                    showToast(view.getContext(), "Isikan jumlah barang terlebih dahulu");
                }
//                else {
//                    showAlernDialog(view);
//                    bottomSheetDialog.dismiss();
//                }
            }
        });

        button_back_to_transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Transaksi.dataModels == null){
                    Transaksi.view = 1;
                }else if (Transaksi.dataModels != null){
                    Transaksi.view = 2;
                }

                free = 0;
                field = 0;

                Intent inten = new Intent();
                JSONObject data = new JSONObject();
                list_data = new JSONArray();
                try {
                    data.put("kode_br", dataModels.get(position).getKode_br());
                    data.put("nama_br", dataModels.get(position).getNama());
                    data.put("harga_br", dataModels.get(position).getHarga_awal());
                    if (dataModels.get(position).getDetailDiskon() == null){
                        data.put("jenis_diskon", "tidak_diskon");
                        data.put("kode_diskon_br", "null");
                        data.put("potongan_harga_br", 0);
                        data.put("harga_total_akhir_br", dataModels.get(position).getHarga_awal() * Integer.parseInt(field_isi_jumlah_barang.getText().toString()));
                        data.put("qty_br", field_isi_jumlah_barang.getText().toString());
                        data.put("list_produk_didapat", "null");
                        data.put("diskon", 0);
                    }else {
                        data.put("kode_diskon_br", dataModels.get(position).getKode_diskon());
                        if (dataModels.get(position).getDetailDiskon().jenis_diskon.equals("nominal")){
                            data.put("jenis_diskon", dataModels.get(position).getDetailDiskon().jenis_diskon);
                            data.put("kode_diskon_br", dataModels.get(position).getDetailDiskon().getDetailBarangFree().kode_diskon);
                            data.put("potongan_harga_br", dataModels.get(position).getHarga_awal() - dataModels.get(position).getDetailDiskon().nominal);
                            data.put("harga_total_akhir_br", (dataModels.get(position).getHarga_awal() - dataModels.get(position).getDetailDiskon().nominal) * Integer.parseInt(field_isi_jumlah_barang.getText().toString()));
                            data.put("qty_br", field_isi_jumlah_barang.getText().toString());
                            data.put("list_produk_didapat", "null");
                            data.put("diskon", 0);
                        } else if (dataModels.get(position).getDetailDiskon().jenis_diskon.equals("persen")) {
                            double bagi100 = (double) dataModels.get(position).getDetailDiskon().nominal / 100;
                            double kali100 = dataModels.get(position).getHarga_awal() * bagi100;
                            double kurang_harga = dataModels.get(position).getHarga_awal() - kali100;
                            data.put("jenis_diskon", dataModels.get(position).getDetailDiskon().jenis_diskon);
                            data.put("kode_diskon_br", dataModels.get(position).getDetailDiskon().getDetailBarangFree().kode_diskon);
                            data.put("potongan_harga_br", Double.valueOf(kurang_harga).intValue());
                            data.put("harga_total_akhir_br", Double.valueOf(kurang_harga).intValue() * Integer.parseInt(field_isi_jumlah_barang.getText().toString()));
                            data.put("qty_br", field_isi_jumlah_barang.getText().toString());
                            data.put("list_produk_didapat", "null");
                            data.put("diskon", 0);
                        } else if (dataModels.get(position).getDetailDiskon().jenis_diskon.equals("free")){
                            if (dataModels.get(position).getDetailDiskon().getDetailBarangFree().jenis_pilihan.equals("bebas")){
                                try {
                                    list_detail_barang_bebas = new JSONArray();
                                    for (int a = 0; a < dataModelsFreeBebasDetail.size(); a++){
                                        detail_barang_bebas = new JSONObject();
                                        detail_barang_bebas.put("nama", dataModelsFreeBebasDetail.get(a).getName_product());
                                        detail_barang_bebas.put("kode", dataModelsFreeBebasDetail.get(a).getKode_produk());
                                        detail_barang_bebas.put("qty", dataModelsFreeBebasDetail.get(a).getValues());
                                        list_detail_barang_bebas.put(detail_barang_bebas);
                                    }

                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                                data.put("jenis_diskon", dataModels.get(position).getDetailDiskon().getDetailBarangFree().jenis_pilihan);
                                data.put("kode_diskon_br", dataModels.get(position).getDetailDiskon().getDetailBarangFree().kode_diskon);
                                data.put("potongan_harga_br", 0);
                                data.put("harga_total_akhir_br", dataModels.get(position).getHarga_awal() * Integer.parseInt(field_isi_jumlah_barang.getText().toString()));
                                data.put("qty_br", field_isi_jumlah_barang.getText().toString());
                                data.put("list_produk_didapat", list_detail_barang_bebas);
                                data.put("diskon", 0);
                            }else if (dataModels.get(position).getDetailDiskon().getDetailBarangFree().jenis_pilihan.equals("sama")){
                                try {
                                    for (int a = 0; a < dataModelsFreeSama.size(); a++){
                                        detail_barang_sama.put("nama", dataModelsFreeSama.get(a).getName_product());
                                        detail_barang_sama.put("kode", dataModelsFreeSama.get(a).getKode_produk());
                                        detail_barang_sama.put("qty", TambahBarangActivity.free_didapat.getText().toString());
                                        list_detail_barang_sama.put(detail_barang_sama);
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                data.put("jenis_diskon", dataModels.get(position).getDetailDiskon().getDetailBarangFree().jenis_pilihan);
                                data.put("kode_diskon_br", dataModels.get(position).getDetailDiskon().getDetailBarangFree().kode_diskon);
                                data.put("potongan_harga_br", 0);
                                data.put("harga_total_akhir_br", dataModels.get(position).getHarga_awal() * Integer.parseInt(field_isi_jumlah_barang.getText().toString()));
                                data.put("qty_br", field_isi_jumlah_barang.getText().toString());
                                data.put("list_produk_didapat", list_detail_barang_sama);
                                data.put("diskon", 0);
                            }
                        }
                    }
                    data.put("gambar_br", dataModels.get(position).getGambar());

                } catch (Exception e){
                    Toast.makeText(TambahBarangActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

                System.out.println(data.toString()+" hayyuk");

                inten.putExtra("data", data.toString());
                inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                setResult(RESULT_OK, inten);
                finish();

            }
        });
    }
    void showAlernDialog(View view, int position){
        AlertDialog builder = new AlertDialog.Builder(view.getRootView().getContext(), R.style.dialog).create();
        View dialogView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.content_dialog_transaksi, null);
        builder.setView(dialogView);
        builder.setCancelable(true);
        builder.show();

        LinearLayout image_no_produk, button_lanjut_transaksi, detail_dialog_free;
        EditText search_produk_free;
        ImageView button_close;

        list_produk_free_dialog = dialogView.findViewById(R.id.list_produk_gratis);
        image_no_produk = dialogView.findViewById(R.id.image_no_value);
        search_produk_free = dialogView.findViewById(R.id.field_kode_product);
        button_close = dialogView.findViewById(R.id.button_close);
        button_lanjut_transaksi = dialogView.findViewById(R.id.button_lanjut_transaksi);
        detail_dialog_free = dialogView.findViewById(R.id.detail_dialog);

        image_no_produk.setVisibility(View.GONE);
        list_produk_free_dialog.setVisibility(View.GONE);

        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
                showBottomSheet(view, position);
            }
        });

        button_lanjut_transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dataModelsFreeBebas != null){

                    boolean Datas = TambahBarangActivity.arr.contains(kode_barang);

                    if (Datas){
                        System.out.println("sudah ada data");
                        showToast(view.getContext(), "Produk yang sama sudah terdaftar dalam list");
                    }else {
                        dataModelsFreeBebasDetail.add(new ModelProdukFree(nama_barang, gambar_barang, kode_barang,1));
                        adapt = new AddProductFree(dataModelsFreeBebasDetail, TambahBarangActivity.this);
                        adapt.notifyDataSetChanged();

                        builder.dismiss();
                        showBottomSheet(view, position);

                        TambahBarangActivity.arr.add(kode_barang);

                    }

                }else {

                }

            }
        });

        search_produk_free.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//
                if (editable.toString().length() != 0){
                    detail_dialog_free.setVisibility(View.VISIBLE);
                    list_produk_free_dialog.setVisibility(View.VISIBLE);
                    image_no_produk.setVisibility(View.GONE);
                    dataModelsFilter = new ArrayList<>();

                    //looping through existing elements
                    for (ModelProdukFree s : dataModelsFreeBebas) {
                        //if the existing elements contains the search input
                        if (s.getKode_produk().toLowerCase().contains(editable.toString().toLowerCase())) {
                            //adding the element to filtered list
                            dataModelsFilter.add(s);
                        }
                    }

                    //calling a method of the adapter class and passing the filtered list
                    adapt.filterList(dataModelsFilter, linearLayout, materi);
                }else if (editable.toString().length() == 0){
                    materi.setVisibility(View.GONE);
                    image_no_produk.setVisibility(View.GONE);
                }
            }
        });

        LinearLayout next_dialog_keyboard = dialogView.findViewById(R.id.button_metode_keyboard);
        next_dialog_keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetProdukFreeDialog(view);
                detail_dialog_free.setVisibility(View.VISIBLE);
            }
        });

        LinearLayout next_dialog_barcode = dialogView.findViewById(R.id.button_metode_barcode);
        next_dialog_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TambahBarangActivity.scanProduk = "produkFree";
//                allTypeData.detail_dialog_free.setVisibility(View.VISIBLE);
//                TambahBarangActivity.scan3();
            }
        });
    }
    // Akhir Code Gratis Produk

    void update() {
        synchronized (list_produk_gratis) {
            list_produk_gratis.notify();
        }
    }

    private void eksekusi_jumlah_barang(String jumlah) {
        allTypeData.totalKaliJumBarang = allTypeData.priceProduct * Integer.parseInt(jumlah);
        allTypeData.totalHarga = allTypeData.totalKaliJumBarang - allTypeData.potonganHarga;
        if (allTypeData.jenisProduk == 1){
            allTypeData.sub_total_harga.setText(allTypeData.format.format(allTypeData.totalHarga));
        }else {
            JSONObject value = allTypeData.Values.optJSONObject("value");
            allTypeData.buy = value.optString("buy");
            allTypeData.gratis = value.optString("gratis");
            int free_plus = Integer.parseInt(allTypeData.gratis)+1;
            int num= Integer.parseInt(jumlah);
            int faktor = 4;
            if (Integer.parseInt(jumlah) == Integer.parseInt(allTypeData.buy)){
                allTypeData.free_didapat.setText(allTypeData.gratis);
                allTypeData.jum_product = jumlah;
            }else if (Integer.parseInt(jumlah) < Integer.parseInt(allTypeData.buy)){
                allTypeData.free_didapat.setText("0");
                allTypeData.jum_product = jumlah;
            }else if (Integer.parseInt(jumlah) > Integer.parseInt(allTypeData.buy)){
                if (num % faktor == 0){
                    allTypeData.free_didapat.setText(String.valueOf(free_plus));
                    allTypeData.jum_product = jumlah;
                }
            }
            allTypeData.sub_total_harga.setText(allTypeData.format.format(allTypeData.totalKaliJumBarang));
        }
        System.out.println(allTypeData.totalHarga);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        int quantity;
        try {
            quantity = Integer.parseInt(s.toString());
        } catch (NumberFormatException e) {
            quantity = 0;
        }

        if (quantity > availableStock) {
            // Jika jumlah stok melebihi stok yang tersedia, ubah ke nilai stok yang tersedia
            EditText editText = (EditText) getCurrentFocus();
            if (editText != null) {
                editText.setText(String.valueOf(availableStock));
                editText.setSelection(editText.getText().length()); // Memindahkan kursor ke akhir teks
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() != 0){
            allTypeData.jenisProduk = 1;
            eksekusi_jumlah_barang(s.toString());
        }else if (s.toString().length() == 0){
//                    allTypeData.jenisProduk = 2;
            eksekusi_jumlah_barang(String.valueOf(0));
        }
    }
}


class AddProduct extends RecyclerView.Adapter<AddProduct.RecyclerViewViewHolder>{
    private ArrayList<ModelDetailBarang> dataList;
    RecyclerViewListener listener;
    private Context context;

    public AddProduct(ArrayList<ModelDetailBarang> dataList, RecyclerViewListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layout = LayoutInflater.from(parent.getContext());
        View view = layout.inflate(R.layout.content_tambah_barang, parent, false);
        return new AddProduct.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
        holder.nama.setText(dataList.get(position).getNama());
        holder.harga.setText(Env.formatRupiah(dataList.get(position).getHarga_awal()));
        holder.icon_sampah.setVisibility(View.GONE);
        Glide.with(context).load(dataList.get(position).getGambar()).centerCrop().placeholder(R.drawable.tshirt).into(holder.img_tambah_barang);
        if (dataList.get(position).getDetailDiskon() == null){
            holder.harga.setText(Env.formatRupiah(dataList.get(position).getHarga_awal()));
            holder.potongan_harga.setVisibility(View.GONE);
        }else if (dataList.get(position).getDetailDiskon() != null){
            if (dataList.get(position).getDetailDiskon().jenis_diskon.equals("nominal")){
                System.out.println("ini nominal");
                TambahBarangActivity.hasilDiskonNominal = dataList.get(position).getHarga_awal() - dataList.get(position).getDetailDiskon().nominal;
                holder.harga.setText(Env.formatRupiah(dataList.get(position).getHarga_awal() - dataList.get(position).getDetailDiskon().nominal));
                holder.potongan_harga.setText(Env.formatRupiah(dataList.get(position).getHarga_awal()));
                holder.potongan_harga.setPaintFlags(holder.potongan_harga.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                dataList.get(position).setHarga_diskon(dataList.get(position).getHarga_awal() - dataList.get(position).getDetailDiskon().nominal);
            } else if (dataList.get(position).getDetailDiskon().jenis_diskon.equals("persen")) {
                System.out.println("ini persen");
                double bagi100 = (double) dataList.get(position).getDetailDiskon().nominal / 100;
                double kali100 = dataList.get(position).getHarga_awal() * bagi100;
                double kurang_harga = dataList.get(position).getHarga_awal() - kali100;
                TambahBarangActivity.hasilDiskonPersen = kurang_harga;
                System.out.println(bagi100);
                holder.harga.setText(Env.formatRupiah(Double.valueOf(kurang_harga).intValue()));
                holder.potongan_harga.setText(Env.formatRupiah(dataList.get(position).getHarga_awal()));
                holder.potongan_harga.setPaintFlags(holder.potongan_harga.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                dataList.get(position).setHarga_diskon(Double.valueOf(dataList.get(position).getHarga_awal() - kali100).intValue());
            } else if (dataList.get(position).getDetailDiskon().jenis_diskon.equals("free")){
                holder.harga.setText(Env.formatRupiah(dataList.get(position).getHarga_awal()));
                holder.potongan_harga.setVisibility(View.GONE);
            }
        }
        holder.layout_harga_total.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nama, harga, potongan_harga;
        LinearLayout layout_harga_total, to_detail;
        ImageView img_tambah_barang, icon_sampah;
        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.name_product);
            harga = itemView.findViewById(R.id.harga_pcs);
            potongan_harga = itemView.findViewById(R.id.potongan_harga);
            layout_harga_total = itemView.findViewById(R.id.layout_harga_total);
            img_tambah_barang = itemView.findViewById(R.id.img_tambah_barang);
            icon_sampah = itemView.findViewById(R.id.icon_sampah);
            to_detail = itemView.findViewById(R.id.to_detail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

}

class AddProductFree extends RecyclerView.Adapter<AddProductFree.RecyclerViewViewHolder>{
    private ArrayList<ModelProdukFree> dataList;
    static RecyclerViewListener listener;
    private Context context;

    public AddProductFree(ArrayList<ModelProdukFree> dataList, RecyclerViewListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    public void filterList(ArrayList<ModelProdukFree> filterdNames, LinearLayout linearLayout, RecyclerView materi) {
        this.dataList = filterdNames;;
        if (dataList.isEmpty()){
            materi.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layout = LayoutInflater.from(parent.getContext());
        View view = layout.inflate(R.layout.content_produk_free, parent, false);
        return new AddProductFree.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getImage_product()).centerCrop().placeholder(R.drawable.tshirt).into(holder.img_free_produk);
        holder.nama.setText(dataList.get(position).getName_product());
        if (dataList.get(position).getDetailBarangFree() == null){
            holder.plus_minus_values.setVisibility(View.GONE);
            if (TambahBarangActivity.list_produk_gratis != null){
                holder.plus_minus_values.setVisibility(View.VISIBLE);
                for (int i = 0; i < dataList.size(); i++){
                    TambahBarangActivity.jumlah_value_tiap_produkfree = 0;
                    TambahBarangActivity.jumlah_value_tiap_produkfree += dataList.get(i).getValues();
                }
            }
        }else if (dataList.get(position).getDetailBarangFree().jenis_pilihan.equals("sama")){
            holder.plus_minus_values.setVisibility(View.VISIBLE);
            holder.min_jumlah.setVisibility(View.GONE);
            holder.add_jumlah.setVisibility(View.GONE);
            holder.qty.setText(String.valueOf(TambahBarangActivity.free_didapat.getText().toString()));
        }

        holder.add_jumlah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TambahBarangActivity.jumlah_value_tiap_produkfree == Integer.valueOf(TambahBarangActivity.free_didapat.getText().toString())){
                    TambahBarangActivity.showToast(view.getContext(), "Produk yang dipilih melebihi batas");
                }else {
                    dataList.get(position).setValues(dataList.get(position).getValues() + 1);
                    TambahBarangActivity.jumlah_value_tiap_produkfree = dataList.get(position).getValues();
                    holder.qty.setText(String.valueOf(dataList.get(position).getValues()));
                }
            }
        });
        holder.min_jumlah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataList.get(position).getValues() > 0) {
                    dataList.get(position).setValues(dataList.get(position).getValues() - 1);
                    holder.qty.setText(String.valueOf(dataList.get(position).getValues()));
                }else if (dataList.get(position).getValues() == 0){
                    holder.qty.setText(String.valueOf(0));
                    TambahBarangActivity.jumlah_value_tiap_produkfree = 0;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nama, qty;
        FlexboxLayout plus_minus_values;
        ImageView min_jumlah, add_jumlah, img_free_produk;
        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            plus_minus_values = itemView.findViewById(R.id.plus_minus_values);
            img_free_produk = itemView.findViewById(R.id.img_free_produk);
            nama = itemView.findViewById(R.id.name_product);
            qty = itemView.findViewById(R.id.qty);
            add_jumlah = itemView.findViewById(R.id.add_jumlah);
            min_jumlah = itemView.findViewById(R.id.min_jumlah);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

}

class ModelDetailBarang{
    String nama, gambar, kode_br, kode_diskon;
    int harga_awal, harga_diskon, harga_total_akhir, qty, stok;
    DetailDiskon detailDiskon;

    public ModelDetailBarang(String nama, String gambar, String kode_br, String kode_diskon, int harga_awal, int stok) {
        this.nama = nama;
        this.gambar = gambar;
        this.kode_br = kode_br;
        this.kode_diskon = kode_diskon;
        this.harga_awal = harga_awal;
        this.stok = stok;
    }

    public String getNama() {
        return nama;
    }

    public String getGambar() {
        return gambar;
    }

    public String getKode_br() {
        return kode_br;
    }

    public String getKode_diskon() {
        return kode_diskon;
    }

    public int getHarga_awal() {
        return harga_awal;
    }

    public int getStok() {
        return stok;
    }

    public DetailDiskon getDetailDiskon() {
        return detailDiskon;
    }

    public void setHarga_diskon(int harga_diskon) {
        this.harga_diskon = harga_diskon;
    }

    public void setHarga_total_akhir(int harga_total_akhir) {
        this.harga_total_akhir = harga_total_akhir;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setDetailDiskon(DetailDiskon detailDiskon) {
        this.detailDiskon = detailDiskon;
    }
}

class DetailDiskon{
    String jenis_diskon, detail_perolehan_produk_free;
    int nominal;
    DetailBarangFree detailBarangFree;

    public DetailDiskon(String jenis_diskon, @Nullable int nominal, @Nullable DetailBarangFree detailBarangFree) {
        this.jenis_diskon = jenis_diskon;
        this.nominal = nominal;
        this.detailBarangFree = detailBarangFree;
    }

    public String getJenis_diskon() {
        return jenis_diskon;
    }

    public int getNominal() {
        return nominal;
    }

    public String getDetail_perolehan_produk_free() {
        return detail_perolehan_produk_free;
    }

    public DetailBarangFree getDetailBarangFree() {
        return detailBarangFree;
    }

    public void setDetailBarangFree(DetailBarangFree detailBarangFree) {
        this.detailBarangFree = detailBarangFree;
    }
}

class DetailBarangFree{
    String jenis_pilihan, kode_diskon;
    int buy, free;

    public DetailBarangFree(String jenis_pilihan, String kode_diskon, int buy, int free) {
        this.jenis_pilihan = jenis_pilihan;
        this.kode_diskon = kode_diskon;
        this.buy = buy;
        this.free = free;
    }
}

class ModelAddBarang {

    String nama, gambar, kode_br, kode_diskon;
    int harga_asli;

    DiskonNominal diskon_nomina;

    public ModelAddBarang(String nama, String kode_diskon, String gambar, String kode_br, int harga_asli) {
        this.nama = nama;
        this.kode_diskon = kode_diskon;
        this.gambar = gambar;
        this.kode_br = kode_br;
        this.harga_asli = harga_asli;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getKode_br() {
        return kode_br;
    }

    public String getKode_diskon() {
        return kode_diskon;
    }

    public void setKode_br(String kode_br) {
        this.kode_br = kode_br;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHarga_asli() {
        return harga_asli;
    }

    public void setHarga_asli(int harga_asli) {
        this.harga_asli = harga_asli;
    }

    public DiskonNominal getDiskon_nomina() {
        return diskon_nomina;
    }

    public void setDiskon_nomina(DiskonNominal diskon_nomina) {
        this.diskon_nomina = diskon_nomina;
    }

}

class ModelProdukFree {
    String name_product;
    String image_product;
    String kode_produk;
    int values;
    DetailBarangFree detailBarangFree;

    public ModelProdukFree(String name_product, String image_product, String kode_produk, int values) {
        this.name_product = name_product;
        this.image_product = image_product;
        this.kode_produk = kode_produk;
        this.values = values;
    }

    public String getKode_produk() {
        return kode_produk;
    }
    public String getImage_product() {
        return image_product;
    }
    public String getName_product() {
        return name_product;
    }
    public int getValues() {
        return values;
    }
    public DetailBarangFree getDetailBarangFree() {
        return detailBarangFree;
    }
    public void setDetailBarangFree(DetailBarangFree detailBarangFree) {
        this.detailBarangFree = detailBarangFree;
    }
    public void setValues(int values) {
        this.values = values;
    }
}


//class modelProdukFree {
//    String name_product;
//    String image_product;
//    String kode_produk;
//    int values;
//
//    public modelProdukFree(String name_product, String image_product, String kode_produk, int values) {
//        this.name_product = name_product;
//        this.image_product = image_product;
//        this.kode_produk = kode_produk;
//        this.values = values;
//    }
//
//    public String getKode_produk() {
//        return kode_produk;
//    }
//    public String getImage_product() {
//        return image_product;
//    }
//    public String getName_product() {
//        return name_product;
//    }
//    public int getValues() {
//        return values;
//    }
//    public void setValues(int values) {
//        this.values = values;
//    }
//}

//class ModelAddBarang {
//
//    String kode_produk;
//    String nama_produk;
//    String stok_barang;
//    int harga;
//    String img;
//    String jenisDiskon;
//    int trush;
//    int nominal_diskon;
//    JSONObject listProdukFree;
//
//    public ModelAddBarang(String kode_produk, String nama_produk, String stok_barang, int harga, String img, @Nullable String jenisDiskon, int trush, @Nullable int nominal_diskon, @Nullable JSONObject listProdukFree) {
//        this.kode_produk = kode_produk;
//        this.nama_produk = nama_produk;
//        this.stok_barang = stok_barang;
//        this.harga = harga;
//        this.img = img;
//        this.jenisDiskon = jenisDiskon;
//        this.trush = trush;
//        this.nominal_diskon = nominal_diskon;
//        this.listProdukFree = listProdukFree;
//    }
//
//    public void setKode_produk(String kode_produk) {
//        this.kode_produk = kode_produk;
//    }
//
//    public void setNama_produk(String nama_produk) {
//        this.nama_produk = nama_produk;
//    }
//
//    public void setStok_barang(String stok_barang) {
//        this.stok_barang = stok_barang;
//    }
//
//    public void setHarga(int harga) {
//        this.harga = harga;
//    }
//
//    public void setImg(String img) {
//        this.img = img;
//    }
//
//    public void setTrush(int trush) {
//        this.trush = trush;
//    }
//
//    public void setDiskon(JSONObject listProdukFree) {
//        this.listProdukFree = listProdukFree;
//    }
//
//    public String getKode_produk() {
//        return kode_produk;
//    }
//    public String getStok_barang() {
//        return stok_barang;
//    }
//    public String getNama_produk() {
//        return nama_produk;
//    }
//    public int getHarga() {
//        return harga;
//    }
//    public String getImgs() {
//        return img;
//    }
//    public String getJenisDiskon() {
//        return jenisDiskon;
//    }
//    public int getTrush() {
//        return trush;
//    }
//    public int getNominal_diskon() {
//        return nominal_diskon;
//    }
//    public JSONObject getDiskon() {
//        return listProdukFree;
//    }
//
//}