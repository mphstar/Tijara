package com.tijara;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
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
import androidx.appcompat.app.AlertDialog;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
    public int imageProduk;
    public int subHarga;
    public static int totalHarga, totalKaliJumBarang, potonganHarga, priceProduct, totalAkhirPalingakhir;
    int view2 = 2, view1 = 1;
    String json, aa;
    static String KodeBarang, NamaBarang, Diskon;
    static int HargaBarang, StokBarang;
    public String nameProduct, kodeProduct;
    static JSONObject Values, freeProduk, varian_diskon;
    static ArrayList<modelProdukFree> modelDataSetGet;
    public ArrayList<ModelAddBarang> datalist;
    public static String BASE_URL = "http://tijara.mphstar.tech/api/product";
    LinearLayout detail_dialog, imageNoProduk, button_lanjut_transaksi, detail_dialog_free;
    EditText searchProdukFree, field_isi_jumlah_barang, field_isi_jumlah_barang2;
    ImageView buttonClose;
    RecyclerView listProdukFree;
    ArrayList<modelProdukFree> dataModel;
    ArrayList<String> dataModel2;
    RecyclerView materi, materi2;
    AdapterProdukFree adapterProdukFree;
    AdapterProdukFree2 adapterProdukFree2;
    BottomSheetDialog bottomSheetDialog;
}

class ambilValues{

    static int image_produk;
    static String nama_produk, harga_produk, jumlah_pesanan, diskon_produk, harga_asli_produk, nominal_diskon, array_produk_free, jsonData;
    static String hargaProduk, jumlahPesanan, namaProduk, hargaAsliProduk;
    static JSONArray dataProdukFree;
}

class AdapterProdukFree extends RecyclerView.Adapter<AdapterProdukFree.ViewHolder> {

    private ArrayList<modelProdukFree> datalist;
    private int nilai = 0;

    public AdapterProdukFree(ArrayList<modelProdukFree> datalist){
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.content_produk_free, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        modelProdukFree jumlah = datalist.get(position);

        holder.txtNamaProduk.setText(datalist.get(position).getName_product());
        holder.txtValues.setText(String.valueOf(datalist.get(position).getValues()));
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(allTypeData.gratis) == allTypeData.modelDataSetGet.size()){
                    AdapterProdukFree2.showToast(view.getContext(), "Produk yang dipilih melebihi batas");
                }else {
                    jumlah.setValues(jumlah.getValues() + 1);
                    holder.txtValues.setText(String.valueOf(jumlah.getValues()));
                }
            }
        });
        holder.minButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jumlah.getValues() > 0) {
                    jumlah.setValues(jumlah.getValues() - 1);
                    holder.txtValues.setText(String.valueOf(jumlah.getValues()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() :0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNamaProduk, txtValues;
        private ImageView addButton, minButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            addButton = itemView.findViewById(R.id.add_jumlah);
            minButton = itemView.findViewById(R.id.min_jumlah);
            txtNamaProduk = itemView.findViewById(R.id.name_product);
            txtValues = itemView.findViewById(R.id.jumlah_pcs);
        }
    }
}

class AdapterProdukFree2 extends RecyclerView.Adapter<AdapterProdukFree2.ViewHolder> {

    private ArrayList<modelProdukFree> datalist;
    public String dataNamaProduk;
    RecyclerView materi;
    AdapterProdukFree adapterProdukFree;
    ArrayList<modelProdukFree> modelDataSetGet;

    public AdapterProdukFree2(ArrayList<modelProdukFree> datalist){
        this.datalist = datalist;
    }

    public ArrayList<modelProdukFree> getData() {
        return datalist;
    }

    public void filterList(ArrayList<modelProdukFree> filterdNames, LinearLayout linearLayout, RecyclerView materi) {
        this.datalist = filterdNames;;
        if (datalist.isEmpty()){
            materi.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
        System.out.println(datalist);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.content_produk_free, parent, false);
        return new ViewHolder(view);
    }

    static public void showToast(Context context, String message) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast, null);

        Toast toast = new Toast(context);

//        ImageView imageView = (ImageView) layout.findViewById(R.id.imageView);
//        imageView.setImageResource(R.drawable.ic_info);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String nama_produk_dipilih = datalist.get(position).getName_product();
        holder.txtNamaProduk.setText(datalist.get(position).getName_product());
        holder.txtValues.setText(String.valueOf(datalist.get(position).getValues()));
        holder.plus_minus_values.setVisibility(View.GONE);

        holder.content_pilih_produk_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataNamaProduk = nama_produk_dipilih;
                System.out.println(nama_produk_dipilih);
                showToast(view.getContext(), dataNamaProduk);
//                Toast.makeText(view.getContext(), "Produk "+dataNamaProduk, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() :0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNamaProduk, txtValues;
        private FlexboxLayout plus_minus_values;

        private LinearLayout content_pilih_produk_free;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            content_pilih_produk_free = itemView.findViewById(R.id.content_pilih_produk_free);
            plus_minus_values = itemView.findViewById(R.id.plus_minus_values);
            txtNamaProduk = itemView.findViewById(R.id.name_product);
            txtValues = itemView.findViewById(R.id.jumlah_pcs);
        }
    }
}

class AdapterAddBarang extends RecyclerView.Adapter<AdapterAddBarang.MahasiswaViewHolder>{

    allTypeData allTypeData = new allTypeData();

    public AdapterAddBarang(ArrayList<ModelAddBarang> datalist, Context applicationContext){
        this.allTypeData.datalist = datalist;
    }

    private void filter(String teks) {

        System.out.println(teks.toString().length());
        ArrayList<modelProdukFree> dataModels2 = new ArrayList<>();
        //looping through existing elements
        for (modelProdukFree s : allTypeData.dataModel) {
            //if the existing elements contains the search input
            if (s.getName_product().toLowerCase().contains(teks.toLowerCase())) {
                //adding the element to filtered list
                dataModels2.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        allTypeData.adapterProdukFree2.filterList(dataModels2, allTypeData.imageNoProduk, allTypeData.materi2);
    }

    public void filterList(ArrayList<ModelAddBarang> filterdNames, LinearLayout linearLayout, RecyclerView materi) {
        this.allTypeData.datalist = filterdNames;;
        if (allTypeData.datalist.isEmpty()){
            materi.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
        System.out.println(allTypeData.datalist);
        notifyDataSetChanged();
    }

//    public int getNominalDiskon(JSONObject jsonObject) throws JSONException{
//        return jsonObject.getInt("nominal");
//    }
//
//    public JSONObject getProdukFree(JSONObject jsonObject) throws JSONException{
//        return jsonObject.getJSONObject("free_product");
//    }

    @NonNull
    @Override
    public MahasiswaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.content_tambah_barang, parent, false);
        return new MahasiswaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MahasiswaViewHolder holder, int position) {
        String name_product = allTypeData.datalist.get(position).getNama_produk();
        String kode_product = allTypeData.datalist.get(position).getKode_produk();
        int price_product = allTypeData.datalist.get(position).getHarga();
        String value = allTypeData.datalist.get(position).getStok_barang();
        int diskon = allTypeData.datalist.get(position).getNominal_diskon();
        int image_produk = allTypeData.datalist.get(position).getImgs();
//        JSONObject varian_diskon = allTypeData.datalist.get(position).getDiskon();
        JSONObject varian_diskon = allTypeData.datalist.get(position).getDiskon();
        allTypeData.varian_diskon = allTypeData.datalist.get(position).getDiskon();


//        if (allTypeData.varian_diskon == null){
//            System.out.println("ini json null");
//        }else{
//            try {
//                if (allTypeData.varian_diskon.getString("kategori").equals(null)){
//                    System.out.println("ini kategori kosong");
//                } else if (allTypeData.varian_diskon.getString("kategori").equals("nominal")) {
//                    allTypeData.nominal = allTypeData.varian_diskon.getInt("nominal");
//                } else if (allTypeData.varian_diskon.getString("free_product") == null) {
//                    System.out.println("non produk free");
//                } else if (allTypeData.varian_diskon.getString("free_product") != null) {
//                    allTypeData.free_produk = allTypeData.varian_diskon.getString("free_product");
//                    if (allTypeData.free_produk.equals("null")){
//                        System.out.println("ini json kosong");
//                    }else {
//                        allTypeData.freeProduk = new JSONObject(allTypeData.free_produk);
//                        System.out.println(allTypeData.freeProduk);
//                    }
//                }
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
//        }



        int nominalDiskon;

//        try {
//            nominalDiskon = getNominalDiskon(varian_diskon);
//            freeProduk = getProdukFree(varian_diskon);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        holder.txtNamaProduk.setText(allTypeData.datalist.get(position).getNama_produk());
        holder.value.setText(allTypeData.datalist.get(position).getStok_barang());
        holder.img.setImageResource(allTypeData.datalist.get(position).getImgs());
        holder.trush.setImageResource(allTypeData.datalist.get(position).getTrush());
        holder.value.setVisibility(View.GONE);
        holder.trush.setVisibility(View.GONE);
        holder.txtSubHarga.setVisibility(View.GONE);

        if (diskon == 0 && allTypeData.varian_diskon == null){
            holder.txtHarga.setText(allTypeData.format.format(allTypeData.datalist.get(position).getHarga()));
            holder.potonganHarga.setVisibility(View.GONE);
        }else if (diskon != 0 && allTypeData.varian_diskon == null){
            holder.txtHarga.setText(allTypeData.format.format(allTypeData.datalist.get(position).getHarga() - allTypeData.datalist.get(position).getNominal_diskon()));
            holder.potonganHarga.setText(allTypeData.format.format(allTypeData.datalist.get(position).getHarga()));
            holder.potonganHarga.setPaintFlags(holder.potonganHarga.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else if (diskon == 0 && allTypeData.varian_diskon != null) {
            System.out.println(allTypeData.varian_diskon);
            holder.potonganHarga.setVisibility(View.GONE);
            holder.txtHarga.setText(allTypeData.format.format(allTypeData.datalist.get(position).getHarga()));
        }else {
            System.out.println("ini ngga usah ");
        }
//        if (allTypeData.varian_diskon == null){
//            holder.potonganHarga.setVisibility(View.GONE);
//            holder.txtHarga.setText(allTypeData.format.format(allTypeData.datalist.get(position).getHarga()));
//        } else {
//            try {
//                if (allTypeData.varian_diskon.getString("kategori").equals("nominal")){
//                    holder.txtHarga.setText(allTypeData.format.format(allTypeData.datalist.get(position).getHarga() - allTypeData.nominal));
//                    holder.potonganHarga.setText(allTypeData.format.format(allTypeData.datalist.get(position).getHarga()));
//                    holder.potonganHarga.setPaintFlags(holder.potonganHarga.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                }else {
//                    holder.potonganHarga.setVisibility(View.GONE);
//                    holder.txtHarga.setText(allTypeData.format.format(allTypeData.datalist.get(position).getHarga()));
//                }
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
//        }
        allTypeData.modelDataSetGet = new ArrayList<>();
        allTypeData.dataModel2 = new ArrayList<>();
        holder.to_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allTypeData.kodeProduct = kode_product;
                allTypeData.nameProduct = name_product;
                allTypeData.priceProduct = price_product;
                allTypeData.Values = varian_diskon;
                allTypeData.potonganHarga = diskon;
                allTypeData.imageProduk = image_produk;
//                allTypeData.subHarga = sub_harga;
                BottomSheet(view);
            }
        });
    }

    private void GetProdukFree(View view){

        // Akses API
        RequestQueue Queue;
        allTypeData.dataModel = new ArrayList<>();
        Queue = Volley.newRequestQueue(view.getContext());

        String url = "http://tijara.mphstar.tech/api/product";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    JSONObject productObject, valueDiskon, valueProdukFreeJson;
                    String valueProdukFreeString;
                    System.out.println(response.length());
                    for (int i = 0; i < response.length(); i++) {
                        System.out.println(response.getJSONObject(i));
                        productObject = response.getJSONObject(i);
                        if (productObject.getString("jenis").equals("free")){
                            allTypeData.dataModel.add(new modelProdukFree(productObject.getString("nama_br"), 1));
                        } else {
                            System.out.println("ini produk Frew");
                        }
                    }
                    allTypeData.adapterProdukFree2 = new AdapterProdukFree2(allTypeData.dataModel);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
                    allTypeData.materi2.setLayoutManager(layoutManager);
                    allTypeData.materi2.setAdapter(allTypeData.adapterProdukFree2);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error dalam mengambil data JSON
                Log.e("Error", "Error: " + error.getMessage());
            }});

        Queue.add(jsonArrayRequest);

        //Akhir Akses Api

//        allTypeData.dataModel = new ArrayList<>();
//        allTypeData.dataModel.add(new modelProdukFree("Dress Panjang Kondangan K..", 1));
//        allTypeData.dataModel.add(new modelProdukFree("Dress Casual Pink", 2));
//        allTypeData.dataModel.add(new modelProdukFree("Celana Chinos Buat Perang ...", 4));
//
//        allTypeData.adapterProdukFree2 = new AdapterProdukFree2(allTypeData.dataModel);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
//        allTypeData.materi2.setLayoutManager(layoutManager);
//        allTypeData.materi2.setAdapter(allTypeData.adapterProdukFree2);
    }

    private void BottomSheet(View view){

        if (allTypeData.potonganHarga == 0 && allTypeData.Values == null){
            System.out.println("no gratis produk");
            non_produkFree(view);
        }else if (allTypeData.potonganHarga != 0 && allTypeData.Values == null){
            System.out.println("no gratis produk");
            non_produkFree(view);
        } else if (allTypeData.potonganHarga == 0 && allTypeData.Values != null) {
            System.out.println("gratis produk");
            produkFree(view);
        }
    }

    void produkFree(View view){
        Transaksi.addProductUsing = 1;
        if (allTypeData.materi != null){
            allTypeData.view1 = 3;
        }else {
            allTypeData.view1 = 1;
        }
        showBottomSheet(view);
    }

    public void showBottomSheet(View view){
        Gson gson = new Gson();
//        JSONObject obj = new JSONObject();
        kirimValues kirimValues = new kirimValues();
        Transaksi mainActivity = new Transaksi();

        allTypeData.bottomSheetDialog = new BottomSheetDialog(view.getRootView().getContext(), R.style.BottomSheetDialogTheme);
        allTypeData.bottomSheetDialog.setContentView(R.layout.bottom_sheet_product);
        allTypeData.bottomSheetDialog.show();
        allTypeData.bottomSheetDialog.create();

        TextView kode_produk, nama_produk, harga_produk, tambah_barang;
        LinearLayout button_back_to_transaksi;
        FlexboxLayout diskon_produk;
        RecyclerView list_produk_gratis;

        allTypeData.materi = allTypeData.bottomSheetDialog.findViewById(R.id.produk_gratis);
        allTypeData.detail_dialog = allTypeData.bottomSheetDialog.findViewById(R.id.detail_dialog);
        allTypeData.field_isi_jumlah_barang2 = allTypeData.bottomSheetDialog.findViewById(R.id.field_isi_jumlah_barang);
        allTypeData.sub_total_harga = allTypeData.bottomSheetDialog.findViewById(R.id.sub_total);
        allTypeData.free_didapat = allTypeData.bottomSheetDialog.findViewById(R.id.free_produk_didapat);
        button_back_to_transaksi = allTypeData.bottomSheetDialog.findViewById(R.id.button_back_to_transaksi);
        tambah_barang = allTypeData.bottomSheetDialog.findViewById(R.id.tambah_produk);
        kode_produk = allTypeData.bottomSheetDialog.findViewById(R.id.kode_produk);
        nama_produk = allTypeData.bottomSheetDialog.findViewById(R.id.nama_produk);
        harga_produk = allTypeData.bottomSheetDialog.findViewById(R.id.harga_produk);
        diskon_produk = allTypeData.bottomSheetDialog.findViewById(R.id.line_diskon);
        list_produk_gratis = allTypeData.bottomSheetDialog.findViewById(R.id.list_produk_gratis);

        kode_produk.setText(allTypeData.kodeProduct);
        nama_produk.setText(allTypeData.nameProduct);
        harga_produk.setText(allTypeData.format.format(allTypeData.priceProduct));
        allTypeData.free_didapat.setText(String.valueOf(0));
        allTypeData.sub_total_harga.setText(String.valueOf(0));
        diskon_produk.setVisibility(View.GONE);

        allTypeData.materi = allTypeData.bottomSheetDialog.findViewById(R.id.produk_gratis);

        if (allTypeData.view1 == 1){
            allTypeData.materi.setVisibility(View.GONE);
            System.out.println("a");
        }else if (allTypeData.view1 == 2){
            System.out.println("b");
            allTypeData.materi = allTypeData.bottomSheetDialog.findViewById(R.id.produk_gratis);
            allTypeData.materi.setVisibility(View.VISIBLE);

//            if (allTypeData.adapterProdukFree2.dataNamaProduk != null){
//
//                boolean Datas = TambahBarangActivity.arr.contains(allTypeData.adapterProdukFree2.dataNamaProduk);
//                System.out.println(Datas);
//
//                if (Datas){
//                    System.out.println("sudah ada data");
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
//                    allTypeData.materi.setLayoutManager(layoutManager);
//                    allTypeData.materi.setAdapter(allTypeData.adapterProdukFree);
//                }else {
//                    allTypeData.modelDataSetGet.add(new modelProdukFree(allTypeData.adapterProdukFree2.dataNamaProduk,1));
//                    allTypeData.adapterProdukFree = new AdapterProdukFree(allTypeData.modelDataSetGet);
//                    allTypeData.adapterProdukFree.notifyDataSetChanged();
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
//                    allTypeData.materi.setLayoutManager(layoutManager);
//                    allTypeData.materi.setAdapter(allTypeData.adapterProdukFree);
//                }
//            }else {
//                System.out.println("fffff");
//            }

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
            allTypeData.materi.setLayoutManager(layoutManager);
            allTypeData.materi.setAdapter(allTypeData.adapterProdukFree);

            allTypeData.json = gson.toJson(allTypeData.modelDataSetGet);
            allTypeData.aa = "\"nama_produk\":\""+allTypeData.adapterProdukFree2.dataNamaProduk+"\", \"jumlah_pesanan\":\"1\"";
//            System.out.println(arr);
            allTypeData.dataModel2.add(allTypeData.json);
            System.out.println(allTypeData.aa+ "a");
            System.out.println(allTypeData.modelDataSetGet.toArray().toString());
            System.out.println(allTypeData.json+"b");
        }else if (allTypeData.view1 == 3){
            System.out.println("c");
            if (allTypeData.modelDataSetGet != null){
                allTypeData.materi.setVisibility(View.GONE);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
                allTypeData.materi.setLayoutManager(layoutManager);
                allTypeData.materi.setAdapter(allTypeData.adapterProdukFree);
            }
        }

        allTypeData.field_isi_jumlah_barang2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() != 0){
                    allTypeData.jenisProduk = 2;
                    allTypeData.jumlah_barang = editable.toString();
                    eksekusi_jumlah_barang(editable.toString());
                }else if (editable.toString().length() == 0){
                    allTypeData.jenisProduk = 2;
                    eksekusi_jumlah_barang(String.valueOf(0));
                }
            }
        });
        allTypeData.field_isi_jumlah_barang2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allTypeData.field_isi_jumlah_barang2.setText("");
            }
        });

        tambah_barang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Integer.parseInt(allTypeData.gratis) == allTypeData.modelDataSetGet.size()){
                    AdapterProdukFree2.showToast(view.getContext(), "Produk yang dipilih melebihi batas");
                }else if (Integer.parseInt(allTypeData.buy) == Integer.parseInt(allTypeData.jum_product) || Integer.parseInt(allTypeData.jum_product) > Integer.parseInt(allTypeData.buy)  ){
                    showAlernDialog(view);
                    allTypeData.bottomSheetDialog.dismiss();
                }else if (Integer.parseInt(allTypeData.gratis) != allTypeData.modelDataSetGet.size()){
                    AdapterProdukFree2.showToast(view.getContext(), "Isikan jumlah barang terlebih dahulu");
                }else {
                    showAlernDialog(view);
                    allTypeData.bottomSheetDialog.dismiss();
                }
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

                JSONObject jsonObject = new JSONObject();
                JSONObject jsonObject1 = new JSONObject();
                System.out.println(allTypeData.modelDataSetGet.toArray().toString()+"aadasd");
//                JSONArray arr = allTypeData.modelDataSetGet.toArray();
                ArrayList<modelProdukFree> arr = allTypeData.modelDataSetGet;
//                System.out.println(arr.get(0).name_product + "zxcv");
//                System.out.println(arr.get(1).name_product + "zxcv");
//                System.out.println(arr.size());

                JSONArray jsonArray = new JSONArray();
//                ArrayList<JSONObject> ls = new ArrayList<>();
                try {
                    for (int a = 0; a < arr.size(); a++){
                        jsonObject1.put("nama", arr.get(a).name_product);
                        jsonObject1.put("value", arr.get(a).values);
                        jsonArray.put(jsonObject1);
                        jsonObject1 = new JSONObject();
//                        jsonArray.put(a,jsonObject1);
                    }
                    System.out.println(jsonArray+"ssjj");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


                try {
                    jsonObject.put("hargaProduk", String.valueOf(allTypeData.priceProduct));
                    jsonObject.put("hargaSetelahDikaliPerpcs", allTypeData.totalKaliJumBarang);
                    jsonObject.put("jumlahPesanan", String.valueOf(allTypeData.field_isi_jumlah_barang2.getText()));
                    jsonObject.put("namaProduk", String.valueOf(nama_produk.getText()));
                    jsonObject.put("image_produk", allTypeData.imageProduk);
                    jsonObject.put("dataProdukFree", jsonArray);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                System.out.println(arr);
                System.out.println(jsonObject);

                try {
                    ambilValues.hargaProduk = jsonObject.getString("hargaProduk");
                    ambilValues.hargaAsliProduk = jsonObject.getString("hargaSetelahDikaliPerpcs");
                    ambilValues.jumlahPesanan = jsonObject.getString("jumlahPesanan");
                    ambilValues.namaProduk  = jsonObject.getString("namaProduk");
                    ambilValues.dataProdukFree  = jsonObject.getJSONArray("dataProdukFree");
                    ambilValues.image_produk = jsonObject.getInt("image_produk");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                allTypeData.bottomSheetDialog.dismiss();
                TambahBarangActivity tambahBarangActivity = new TambahBarangActivity();
                tambahBarangActivity.toMain();
            }
        });
    }
    void showAlernDialog(View view){
        AlertDialog builder = new AlertDialog.Builder(view.getRootView().getContext(), R.style.dialog).create();
        View dialogView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.content_dialog_transaksi, null);
        builder.setView(dialogView);
        builder.setCancelable(true);
        builder.show();

        allTypeData.materi2 = dialogView.findViewById(R.id.list_produk_gratis);
        GetProdukFree(view);
        allTypeData.imageNoProduk = dialogView.findViewById(R.id.image_no_value);
        allTypeData.searchProdukFree = dialogView.findViewById(R.id.field_kode_product);
        allTypeData.buttonClose = dialogView.findViewById(R.id.button_close);
        allTypeData.button_lanjut_transaksi = dialogView.findViewById(R.id.button_lanjut_transaksi);
        allTypeData.detail_dialog_free = dialogView.findViewById(R.id.detail_dialog);

        allTypeData.imageNoProduk.setVisibility(View.GONE);
        allTypeData.materi2.setVisibility(View.GONE);

        allTypeData.buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
                showBottomSheet(view);
                allTypeData.field_isi_jumlah_barang2.setText(allTypeData.jumlah_barang);
            }
        });

        allTypeData.button_lanjut_transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (allTypeData.adapterProdukFree2.dataNamaProduk != null){

                    boolean Datas = TambahBarangActivity.arr.contains(allTypeData.adapterProdukFree2.dataNamaProduk);
                    System.out.println(Datas);

                    if (Datas){
                        System.out.println("sudah ada data");
                        AdapterProdukFree2.showToast(view.getContext(), "Produk yang sama sudah terdaftar dalam list");
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
                        allTypeData.materi.setLayoutManager(layoutManager);
                        allTypeData.materi.setAdapter(allTypeData.adapterProdukFree);
                    }else {
                        allTypeData.modelDataSetGet.add(new modelProdukFree(allTypeData.adapterProdukFree2.dataNamaProduk,1));
                        allTypeData.adapterProdukFree = new AdapterProdukFree(allTypeData.modelDataSetGet);
                        allTypeData.adapterProdukFree.notifyDataSetChanged();

                        builder.dismiss();
                        allTypeData.view1 = 2;
                        showBottomSheet(view);
                        TambahBarangActivity.arr.add(allTypeData.adapterProdukFree2.dataNamaProduk);
                        allTypeData.field_isi_jumlah_barang2.setText(allTypeData.jumlah_barang);
                    }
                }else {
                    System.out.println("fffff");
                }


            }
        });

        allTypeData.searchProdukFree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() != 0){
                    allTypeData.materi2.setVisibility(View.VISIBLE);
                    allTypeData.imageNoProduk.setVisibility(View.GONE);
                    filter(editable.toString());
                }else if (editable.toString().length() == 0){
                    allTypeData.materi2.setVisibility(View.GONE);
                    allTypeData.imageNoProduk.setVisibility(View.GONE);
                }
            }
        });

        LinearLayout next_dialog_keyboard = dialogView.findViewById(R.id.button_metode_keyboard);
        next_dialog_keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allTypeData.view2 == 2) {
                    System.out.println("view input barang");
                    allTypeData.detail_dialog_free.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    void eksekusi_jumlah_barang(String jumlah){
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

    void non_produkFree(View view){
        Transaksi.addProductUsing = 2;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getRootView().getContext(), R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_product);
        bottomSheetDialog.show();

        TextView kode_produk, nama_produk, harga_produk, diskon_produk;
        RecyclerView list_produk_gratis;
        LinearLayout button_back_to_transaksi;
        FlexboxLayout values;

        allTypeData.field_isi_jumlah_barang = bottomSheetDialog.findViewById(R.id.field_isi_jumlah_barang);
        allTypeData.detail_dialog = bottomSheetDialog.findViewById(R.id.detail_dialog);
        allTypeData.sub_total_harga = bottomSheetDialog.findViewById(R.id.sub_total);
        allTypeData.sub_total_harga.setText(String.valueOf(0));
        values = bottomSheetDialog.findViewById(R.id.info_pesanan);
        kode_produk = bottomSheetDialog.findViewById(R.id.kode_produk);
        nama_produk = bottomSheetDialog.findViewById(R.id.nama_produk);
        diskon_produk = bottomSheetDialog.findViewById(R.id.diskon_produk);
        harga_produk = bottomSheetDialog.findViewById(R.id.harga_produk);
        list_produk_gratis = bottomSheetDialog.findViewById(R.id.list_produk_gratis);

        values.setVisibility(View.GONE);
        kode_produk.setText(allTypeData.kodeProduct);
        nama_produk.setText(allTypeData.nameProduct);
        harga_produk.setText(allTypeData.format.format(allTypeData.priceProduct));
        diskon_produk.setText(allTypeData.format.format(allTypeData.potonganHarga));
        System.out.println(allTypeData.totalHarga);

        allTypeData.field_isi_jumlah_barang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() != 0){
                    allTypeData.jenisProduk = 1;
                    eksekusi_jumlah_barang(editable.toString());
                }else if (editable.toString().length() == 0){
                    allTypeData.jenisProduk = 2;
                    eksekusi_jumlah_barang(String.valueOf(0));
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

                JSONObject jsonObject = new JSONObject();
                JSONArray arr = new JSONArray(allTypeData.dataModel2);

                try {
                    jsonObject.put("harga_produk", allTypeData.priceProduct);
                    jsonObject.put("nominal_diskon", allTypeData.potonganHarga);
                    jsonObject.put("jumlah_pesanan", String.valueOf(allTypeData.field_isi_jumlah_barang.getText()));
                    jsonObject.put("nama_produk", allTypeData.nameProduct);
                    jsonObject.put("diskon_produk", allTypeData.potonganHarga);
                    jsonObject.put("harga_setelah_dikali_perpcs", allTypeData.totalKaliJumBarang);
                    jsonObject.put("harga_setelah_diskon", allTypeData.totalHarga);
                    jsonObject.put("image_produk", allTypeData.imageProduk);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                System.out.println(jsonObject);

                try {
                    ambilValues.harga_produk = jsonObject.getString("harga_setelah_dikali_perpcs");
                    ambilValues.nominal_diskon = jsonObject.getString("nominal_diskon");
                    ambilValues.harga_asli_produk = jsonObject.getString("harga_produk");
                    ambilValues.jumlah_pesanan = jsonObject.getString("jumlah_pesanan");
                    ambilValues.nama_produk  = jsonObject.getString("nama_produk");
                    ambilValues.diskon_produk = jsonObject.getString("harga_setelah_diskon");
                    ambilValues.image_produk = jsonObject.getInt("image_produk");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                System.out.println(ambilValues.nama_produk);

                bottomSheetDialog.dismiss();
                TambahBarangActivity tambahBarangActivity = new TambahBarangActivity();
                tambahBarangActivity.toMain();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (allTypeData.datalist != null) ? allTypeData.datalist.size() :0;
    }


    public class MahasiswaViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNamaProduk, txtHarga, potonganHarga, value, txtSubHarga;
        private ImageView img, trush;
        private LinearLayout to_detail;
        private TextView tambah_produk;

        public MahasiswaViewHolder(@NonNull View itemView) {
            super(itemView);
            tambah_produk = itemView.findViewById(R.id.tambah_produk);
            to_detail = itemView.findViewById(R.id.to_detail);
            txtNamaProduk = itemView.findViewById(R.id.name_product);
            txtHarga = itemView.findViewById(R.id.harga_pcs);
            potonganHarga = itemView.findViewById(R.id.potongan_harga);
            value = itemView.findViewById(R.id.value);
            txtSubHarga = itemView.findViewById(R.id.harga_total);
            img = itemView.findViewById(R.id.img_tambah_barang);
            trush = itemView.findViewById(R.id.icon_sampah);
        }
    }

    public static class DataSetGet {

        private String data;

        public DataSetGet(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }

        public void setData(ArrayList<String> data) {
            this.data = String.valueOf(data);
        }
    }

}

public class TambahBarangActivity extends AppCompatActivity {
    ArrayList<ModelAddBarang> dataModels;
    RecyclerView materi;
    LinearLayout linearLayout ;
    EditText kode, searchView;
    ImageView searchScanKode;
    static ImageView backTOMainTransaksi;
    TextView anu;
    String setKEY_KODE;
    ModelAddBarang modelAddBarang;
    private String getKEY_KODE, KEY_KODE = "KODE_BARANG";
    private RequestQueue mQueue;
    static ArrayList<String> arr = new ArrayList<>();
    private static AdapterAddBarang adapterTransaksi;

    public void toMain(){
        backTOMainTransaksi.performClick();
    }

    private void loadProduct(){

        // Akses API
        mQueue = Volley.newRequestQueue(TambahBarangActivity.this);

        String url = "http://tijara.mphstar.tech/api/product";

        dataModels = new ArrayList<>();
//        StringRequest strinRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray jsonArray = new JSONArray(response);
//                            dataModels = new ArrayList<>();
//                            for (int i = 0; i < jsonArray.length(); i++){
//                                JSONObject productObject = jsonArray.getJSONObject(i);
//                                dataModels.add(new ModelAddBarang(productObject.getString("kode_br"), productObject.getString("nama_br"), productObject.getInt("harga"), productObject.getString("stok"), R.drawable.dress_purple, R.drawable.shape_white, productObject.getJSONObject("diskon")));
////                                modelAddBarang = new ModelAddBarang();
////                                modelAddBarang.setKode_produk(productObject.getString("kode_br"));
////                                modelAddBarang.setNama_produk(productObject.getString("nama_br"));
////                                modelAddBarang.setHarga(productObject.getInt("harga"));
////                                modelAddBarang.setStok_barang(productObject.getString("stok"));
////                                modelAddBarang.setDiskon(productObject.getJSONObject("diskon"));
////                                ModelAddBarang product = new ModelAddBarang(
////                                        productObject.getString("kode_br"),
////                                        productObject.getString("nama_br"),
////                                        productObject.getInt("harga"),
////                                        productObject.getString("stok"),
////                                        productObject.getInt("gambar"),
////                                        productObject.getInt("gambar"),
////                                        productObject.optJSONObject("diskon")
////                                );
////                                dataModels.add(modelAddBarang);
//                            }
//                            adapterTransaksi = new AdapterAddBarang(dataModels, TambahBarangActivity.this);
//                            materi = findViewById(R.id.list_barang);
//                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TambahBarangActivity.this, LinearLayoutManager.VERTICAL, false);
//                            materi.setLayoutManager(layoutManager);
//                            materi.setAdapter(adapterTransaksi);
//                            mQueue.getCache().clear();
//                        }catch (JSONException e) {
////                                Toast.makeText(SendNotificationActivity.this, e.toString(), Toast.LENGTH_LONG).show();
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
////                    Toast.makeText(SendNotificationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
//        mQueue.add(strinRequest);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    JSONObject productObject, valueDiskon, valueProdukFreeJson;
                    String valueProdukFreeString;
                    System.out.println(response.length());
                    for (int i = 0; i < response.length(); i++) {
                        System.out.println(response.getJSONObject(i));
                        productObject = response.getJSONObject(i);
                        if (productObject.getString("jenis").equals("jual")){
                            if (productObject.isNull("diskon")){
                                System.out.println("ini produk tanpa diskon");
                                dataModels.add(new ModelAddBarang(productObject.getString("kode_br"), productObject.getString("nama_br"),  productObject.getString("stok"), productObject.getInt("harga"), R.drawable.dress_purple, R.drawable.shape_white, 0, null));
                            }else {
                                System.out.println("produk dengan diskon");
                                valueDiskon = productObject.getJSONObject("diskon");
                                if (valueDiskon.getString("kategori").equals("nominal")){
                                    System.out.println("ini produk diskon nominal");
                                    dataModels.add(new ModelAddBarang(productObject.getString("kode_br"), productObject.getString("nama_br"),  productObject.getString("stok"), productObject.getInt("harga"), R.drawable.dress_purple, R.drawable.shape_white, valueDiskon.getInt("nominal"), null));
                                } else if (valueDiskon.getString("free_product") != null && valueDiskon.getString("kategori").equals("free")){
                                    System.out.println("ini produk diskon produk free");
                                    valueProdukFreeString = valueDiskon.getString("free_product");
                                    valueProdukFreeJson = new JSONObject(valueProdukFreeString);
                                    dataModels.add(new ModelAddBarang(productObject.getString("kode_br"), productObject.getString("nama_br"),  productObject.getString("stok"), productObject.getInt("harga"), R.drawable.dress_purple, R.drawable.shape_white, 0, valueProdukFreeJson));
                                } else {
                                    System.out.println("ini produk dengan diskon lainnya");
                                }
                            }
                        } else {
                            System.out.println("ini produk Frew");
                        }
                    }
                    adapterTransaksi = new AdapterAddBarang(dataModels, TambahBarangActivity.this);
                    materi = findViewById(R.id.list_barang);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TambahBarangActivity.this);
                    materi.setLayoutManager(layoutManager);
                    materi.setAdapter(adapterTransaksi);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error dalam mengambil data JSON
                Log.e("Error", "Error: " + error.getMessage());
            }});

        mQueue.add(jsonArrayRequest);


//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                try {
//                    JSONArray data = response.getJSONArray("results");
//                    for (int i = 0; i < data.length(); i++) {
//                        JSONObject productObject = data.getJSONObject(i);
//                        dataModels.add(new ModelAddBarang(productObject.getString("title"), productObject.getString("title"), productObject.getInt("vote_count"), productObject.getString("title"), R.drawable.dress_purple, R.drawable.shape_white));
////                        String email = jsonObject.getString("kode_br");
////                        String firstName = jsonObject.getString("nama_br");
////                        String lastName = jsonObject.getString("stok");
//
//                        // tampilkan data di dalam array
////                        Log.d("Data Array", "email: " + email + ", first_name: " + firstName + ", last_name: " + lastName );
//
//
//                    }
//                    adapterTransaksi = new AdapterAddBarang(dataModels, TambahBarangActivity.this);
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TambahBarangActivity.this, LinearLayoutManager.VERTICAL, false);
//                    materi.setLayoutManager(layoutManager);
//                    materi.setAdapter(adapterTransaksi);
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
//        mQueue.add(jsonObjectRequest);

//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray data = response.getJSONArray();
//                    JSONObject users;
//                    for (int i = 0; i < data.length(); i++) {
//                        users = data.getJSONObject(i);
//                        System.out.println(users.getString("email"));
//                        dataModels.add(new ModelAddBarang(users.getString("id"), users.getString("name"), 100000, "10",  R.drawable.dress_purple, R.drawable.dress_purple));
//                    }
//                    adapterTransaksi = new AdapterAddBarang(dataModels, TambahBarangActivity.this);
//                    materi = findViewById(R.id.list_barang);
//                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TambahBarangActivity.this);
//                    materi.setLayoutManager(layoutManager);
//                    materi.setAdapter(adapterTransaksi);
//                } catch (JSONException e) {
//                    Toast.makeText(TambahBarangActivity.this, " You clicked Cancel ", Toast.LENGTH_LONG).show();
//                    throw new RuntimeException(e);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(TambahBarangActivity.this, error.toString(), Toast.LENGTH_LONG).show();
////                Toast.makeText(RestApiActivity.this, "Tidak Ada Koneksi", Toast.LENGTH_LONG).show();
//            }
//        }
//        );
//
//        mQueue.add(jsonObjectRequest);

        //Akhir Akses Api

//        dataModels.add(new ModelAddBarang("","Dress Panjang Kondangan K..",150000, "1", R.drawable.dress_purple, R.drawable.shape_white, null));
//        dataModels.add(new ModelAddBarang("","Dress Casual Pink",210000, "2", R.drawable.dress_casual_pink, R.drawable.shape_white,null));
//        dataModels.add(new ModelAddBarang("","Celana Chinos Buat Perang ...", 70000,"1", R.drawable.celana_chinos, R.drawable.shape_white,null));
//        adapterTransaksi = new AdapterAddBarang(dataModels, this);
//        materi = findViewById(R.id.list_barang);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TambahBarangActivity.this);
//        materi.setLayoutManager(layoutManager);
//        materi.setAdapter(adapterTransaksi);
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

    @Override
    protected void onStart() {
        super.onStart();
        searchView.setText(setKEY_KODE);
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
        materi = findViewById(R.id.list_barang);

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
                Intent intent = new Intent(TambahBarangActivity.this, Transaksi.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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

class modelProdukFree {
    String name_product;
    int values;

    public modelProdukFree(String name_product, int values) {
        this.name_product = name_product;
        this.values = values;
    }

    public String getName_product() {
        return name_product;
    }
    public void setName_product(String name_product) {
        this.name_product = name_product;
    }
    public int getValues() {
        return values;
    }
    public void setValues(int values) {
        this.values = values;
    }
//    @Override
//    public boolean equals(Object obj) {
//        modelProdukFree other = null;
//        if (obj == null) return false;
//        if (!(obj instanceof modelProdukFree)) return false;
//
//        other = (modelProdukFree) obj;
//
//        if (name_product == null) {
//            return other.getName_product() == null;
//        } else {
//            return name_product.equals(other.getName_product());
//        }
//    }
}

class ModelAddBarang {

    String kode_produk;
    String nama_produk;
    String stok_barang;
    int harga;
    int img;
    int trush;
    int nominal_diskon;
    JSONObject listProdukFree;

    public ModelAddBarang(String kode_produk, String nama_produk, String stok_barang, int harga, int img, int trush, @Nullable int nominal_diskon, @Nullable JSONObject listProdukFree) {
        this.kode_produk = kode_produk;
        this.nama_produk = nama_produk;
        this.stok_barang = stok_barang;
        this.harga = harga;
        this.img = img;
        this.trush = trush;
        this.nominal_diskon = nominal_diskon;
        this.listProdukFree = listProdukFree;
    }

    //    public ModelAddBarang(String kode_produk, String nama_produk, int harga, String stok_barang, int img, int trush, @Nullable JSONObject listProdukFree) {
//        this.kode_produk = kode_produk;
//        this.nama_produk = nama_produk;
//        this.harga = harga;
//        this.stok_barang = stok_barang;
//        this.img = img;
//        this.trush = trush;
//        this.listProdukFree = listProdukFree;
//    }

//    public ModelAddBarang(String kode_produk, String nama_produk, int harga, String stok_barang, int img, int trush) {
//        this.kode_produk = kode_produk;
//        this.nama_produk = nama_produk;
//        this.harga = harga;
//        this.stok_barang = stok_barang;
//        this.img = img;
//        this.trush = trush;
//        this.listProdukFree = listProdukFree;
//    }

    public void setKode_produk(String kode_produk) {
        this.kode_produk = kode_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public void setStok_barang(String stok_barang) {
        this.stok_barang = stok_barang;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setTrush(int trush) {
        this.trush = trush;
    }

    public void setDiskon(JSONObject listProdukFree) {
        this.listProdukFree = listProdukFree;
    }

    public String getKode_produk() {
        return kode_produk;
    }
    public String getStok_barang() {
        return stok_barang;
    }
    public String getNama_produk() {
        return nama_produk;
    }
    public int getHarga() {
        return harga;
    }
    public int getImgs() {
        return img;
    }
    public int getTrush() {
        return trush;
    }
    public int getNominal_diskon() {
        return nominal_diskon;
    }
    public JSONObject getDiskon() {
        return listProdukFree;
    }

}

class Product {
    private String kode_br;
    private String kategori;
    private String kode_barang_tag;
    private String nama_br;
    private int stok;
    private String gambar;
    private int harga;
    private String ukuran;
    private String warna;
    private String jenis;
    private String created_at;
    private String updated_at;
    private String diskon;

    public String getKode_br() {
        return kode_br;
    }

    public void setKode_br(String kode_br) {
        this.kode_br = kode_br;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getKode_barang_tag() {
        return kode_barang_tag;
    }

    public void setKode_barang_tag(String kode_barang_tag) {
        this.kode_barang_tag = kode_barang_tag;
    }

    public String getNama_br() {
        return nama_br;
    }

    public void setNama_br(String nama_br) {
        this.nama_br = nama_br;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getUkuran() {
        return ukuran;
    }

    public void setUkuran(String ukuran) {
        this.ukuran = ukuran;
    }

    public String getWarna() {
        return warna;
    }

    public void setWarna(String warna) {
        this.warna = warna;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDiskon() {
        return diskon;
    }

    public void setDiskon(String diskon) {
        this.diskon = diskon;
    }
}