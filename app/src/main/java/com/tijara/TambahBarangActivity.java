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

    static TextView sub_total_harga;
    static int jenisProduk;
    static String jumlah_barang;
    static NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    public int imageProduk, priceProduct, potonganHarga, subHarga, totalHarga, totalKaliJumBarang;
    int view2 = 2, view1 = 1;
    String json, aa;
    public String nameProduct, Values;
    public ArrayList<ModelAddBarang> datalist;
    LinearLayout detail_dialog, imageNoProduk, button_lanjut_transaksi, detail_dialog_free;
    EditText searchProdukFree, field_isi_jumlah_barang, field_isi_jumlah_barang2;
    ImageView buttonClose;
    RecyclerView listProdukFree;
    ArrayList<modelProdukFree> dataModel;
    ArrayList<String> dataModel2;
    RecyclerView materi, materi2;
    AdapterProdukFree adapterProdukFree;
    AdapterProdukFree2 adapterProdukFree2;
    ArrayList<modelProdukFree> modelDataSetGet;
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
                jumlah.setValues(jumlah.getValues() + 1);
                holder.txtValues.setText(String.valueOf(jumlah.getValues()));
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

    public void showToast(Context context, String message) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast, null);

//        ImageView imageView = (ImageView) layout.findViewById(R.id.imageView);
//        imageView.setImageResource(R.drawable.ic_info);

        TextView textView = (TextView) layout.findViewById(R.id.nama_produk_free);
        textView.setText(message);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
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
        int price_product = allTypeData.datalist.get(position).getHarga();
        String value = allTypeData.datalist.get(position).getValue();
        int diskon = allTypeData.datalist.get(position).getPotongan_harga();
        int image_produk = allTypeData.datalist.get(position).getImgs();
        holder.txtNamaProduk.setText(allTypeData.datalist.get(position).getNama_produk());
        holder.value.setText(allTypeData.datalist.get(position).getValue());
        holder.img.setImageResource(allTypeData.datalist.get(position).getImgs());
        holder.trush.setImageResource(allTypeData.datalist.get(position).getTrush());
        holder.value.setVisibility(View.GONE);
        holder.trush.setVisibility(View.GONE);
        holder.txtSubHarga.setVisibility(View.GONE);
        if (allTypeData.datalist.get(position).getPotongan_harga() == 0){
            holder.potonganHarga.setVisibility(View.GONE);
            holder.txtHarga.setText(allTypeData.format.format(allTypeData.datalist.get(position).getHarga()));
            holder.potonganHarga.setText(allTypeData.format.format(allTypeData.datalist.get(position).getPotongan_harga()));
        } else {
            holder.txtHarga.setText(allTypeData.format.format(allTypeData.datalist.get(position).getHarga() - allTypeData.datalist.get(position).getPotongan_harga()));
            holder.potonganHarga.setText(allTypeData.format.format(allTypeData.datalist.get(position).getHarga()));
            holder.potonganHarga.setPaintFlags(holder.potonganHarga.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        allTypeData.modelDataSetGet = new ArrayList<>();
        allTypeData.dataModel2 = new ArrayList<>();
        holder.to_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allTypeData.nameProduct = name_product;
                allTypeData.priceProduct = price_product;
                allTypeData.Values = value;
                allTypeData.potonganHarga = diskon;
                allTypeData.imageProduk = image_produk;
//                allTypeData.subHarga = sub_harga;
                BottomSheet(view);
            }
        });
    }

    private void BottomSheet(View view){

        if (allTypeData.Values == "2"){
            produkFree(view);
            System.out.println("gratis produk");
        }else if (allTypeData.Values == "1") {
            non_produkFree(view);
            System.out.println("no gratis");
        }

    }

    private void GetProdukFree(View view){

        allTypeData.dataModel = new ArrayList<>();
        allTypeData.dataModel.add(new modelProdukFree("Dress Panjang Kondangan K..", 1));
        allTypeData.dataModel.add(new modelProdukFree("Dress Casual Pink", 2));
        allTypeData.dataModel.add(new modelProdukFree("Celana Chinos Buat Perang ...", 4));

        allTypeData.adapterProdukFree2 = new AdapterProdukFree2(allTypeData.dataModel);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        allTypeData.materi2.setLayoutManager(layoutManager);
        allTypeData.materi2.setAdapter(allTypeData.adapterProdukFree2);

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

        TextView kode_produk, nama_produk, harga_produk;
        TextView tambah_barang;
        LinearLayout button_back_to_transaksi;
        FlexboxLayout diskon_produk;
        RecyclerView list_produk_gratis;

        allTypeData.materi = allTypeData.bottomSheetDialog.findViewById(R.id.produk_gratis);
        allTypeData.detail_dialog = allTypeData.bottomSheetDialog.findViewById(R.id.detail_dialog);
        allTypeData.field_isi_jumlah_barang2 = allTypeData.bottomSheetDialog.findViewById(R.id.field_isi_jumlah_barang);
        allTypeData.sub_total_harga = allTypeData.bottomSheetDialog.findViewById(R.id.sub_total);
        allTypeData.sub_total_harga.setText(String.valueOf(0));
        button_back_to_transaksi = allTypeData.bottomSheetDialog.findViewById(R.id.button_back_to_transaksi);
        tambah_barang = allTypeData.bottomSheetDialog.findViewById(R.id.tambah_produk);
        nama_produk = allTypeData.bottomSheetDialog.findViewById(R.id.nama_produk);
        harga_produk = allTypeData.bottomSheetDialog.findViewById(R.id.harga_produk);
        diskon_produk = allTypeData.bottomSheetDialog.findViewById(R.id.line_diskon);
        list_produk_gratis = allTypeData.bottomSheetDialog.findViewById(R.id.list_produk_gratis);

        nama_produk.setText(allTypeData.nameProduct);
        harga_produk.setText(allTypeData.format.format(allTypeData.priceProduct));
        diskon_produk.setVisibility(View.GONE);

        allTypeData.materi = allTypeData.bottomSheetDialog.findViewById(R.id.produk_gratis);

        if (allTypeData.view1 == 1){
            allTypeData.materi.setVisibility(View.GONE);
            System.out.println("a");
        }else if (allTypeData.view1 == 2){
            System.out.println("b");
            allTypeData.materi = allTypeData.bottomSheetDialog.findViewById(R.id.produk_gratis);
            allTypeData.materi.setVisibility(View.VISIBLE);

            allTypeData.modelDataSetGet.add(new modelProdukFree(allTypeData.adapterProdukFree2.dataNamaProduk,1));
            allTypeData.adapterProdukFree = new AdapterProdukFree(allTypeData.modelDataSetGet);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
            allTypeData.adapterProdukFree.notifyDataSetChanged();
            allTypeData.materi.setLayoutManager(layoutManager);
            allTypeData.materi.setAdapter(allTypeData.adapterProdukFree);
            allTypeData.json = gson.toJson(allTypeData.modelDataSetGet);
            allTypeData.aa = "\"nama_produk\":\""+allTypeData.adapterProdukFree2.dataNamaProduk+"\", \"jumlah_pesanan\":\"1\"";
//            System.out.println(arr);
            allTypeData.dataModel2.add(allTypeData.json);
            System.out.println(allTypeData.aa+ "kon");
            System.out.println(allTypeData.modelDataSetGet);
            System.out.println(allTypeData.json+"tol");
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

        tambah_barang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlernDialog(view);
                allTypeData.bottomSheetDialog.dismiss();
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
                builder.dismiss();
                allTypeData.view1 = 2;
                showBottomSheet(view);
                allTypeData.field_isi_jumlah_barang2.setText(allTypeData.jumlah_barang);
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
        nama_produk = bottomSheetDialog.findViewById(R.id.nama_produk);
        diskon_produk = bottomSheetDialog.findViewById(R.id.diskon_produk);
        harga_produk = bottomSheetDialog.findViewById(R.id.harga_produk);
        list_produk_gratis = bottomSheetDialog.findViewById(R.id.list_produk_gratis);

        values.setVisibility(View.GONE);
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
                    allTypeData.jenisProduk = 1;
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
    private String getKEY_KODE, KEY_KODE = "KODE_BARANG";
    private static AdapterAddBarang adapterTransaksi;

    public void toMain(){
        backTOMainTransaksi.performClick();
    }

    void loadProduct(){

        dataModels = new ArrayList<>();
        dataModels.add(new ModelAddBarang("Dress Panjang Kondangan K..", 15000, 150000, "1", R.drawable.dress_purple, R.drawable.shape_white));
        dataModels.add(new ModelAddBarang("Dress Casual Pink", 0, 210000, "2", R.drawable.dress_casual_pink, R.drawable.shape_white));
        dataModels.add(new ModelAddBarang("Celana Chinos Buat Perang ...", 7000, 70000,"1", R.drawable.celana_chinos, R.drawable.shape_white));
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
}

class ModelAddBarang {

    String nama_produk;
    int potongan_harga;
    int harga;
    String stok_barang;
    int img;
    int trush;
    String feature;

    public ModelAddBarang(String nama_produk, @Nullable int potongan_harga, int harga, String stok_barang, int img, int trush) {
        this.nama_produk=nama_produk;
        this.potongan_harga=potongan_harga;
        this.harga=harga;
        this.stok_barang=stok_barang;
        this.img=img;
        this.trush=trush;

    }

    public String getNama_produk() {
        return nama_produk;
    }
    public int getPotongan_harga() {
        return potongan_harga;
    }
    public int getHarga() {
        return harga;
    }

    public String getValue() {
        return stok_barang;
    }

    public int getImgs() {
        return img;
    }

    public int getTrush() {
        return trush;
    }

}