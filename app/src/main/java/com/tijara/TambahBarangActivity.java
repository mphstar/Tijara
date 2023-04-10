package com.tijara;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class allTypeData{

    int view2 = 2, view1 = 1;
    String json;
    public String nameProduct, priceProduct, Values, potonganHarga;
    public ArrayList<ModelAddBarang> datalist;
    LinearLayout detail_dialog, imageNoProduk, button_lanjut_transaksi, detail_dialog_free;
    EditText searchProdukFree;
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
    static String nama_produk, harga_produk, jumlah_pesanan, diskon_produk, array_produk_free, jsonData;
}

class AdapterProdukFree extends RecyclerView.Adapter<AdapterProdukFree.ViewHolder> {

    private ArrayList<modelProdukFree> datalist;

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
        holder.txtNamaProduk.setText(datalist.get(position).getName_product());
        holder.txtValues.setText(datalist.get(position).getValues());

    }

    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() :0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNamaProduk, txtValues;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String nama_produk_dipilih = datalist.get(position).getName_product();
        holder.txtNamaProduk.setText(datalist.get(position).getName_product());
        holder.txtValues.setText(datalist.get(position).getValues());
        holder.plus_minus_values.setVisibility(View.GONE);

        holder.content_pilih_produk_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataNamaProduk = nama_produk_dipilih;
                System.out.println(nama_produk_dipilih);
                Toast.makeText(view.getContext(), "Produk "+dataNamaProduk, Toast.LENGTH_SHORT).show();
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
        String price_product = allTypeData.datalist.get(position).getHarga();
        String value = allTypeData.datalist.get(position).getValue();
        String diskon = allTypeData.datalist.get(position).getPotongan_harga();
        holder.txtNamaProduk.setText(allTypeData.datalist.get(position).getNama_produk());
        holder.txtHarga.setText(allTypeData.datalist.get(position).getHarga());
        holder.potonganHarga.setText(allTypeData.datalist.get(position).getPotongan_harga());
        holder.value.setText(allTypeData.datalist.get(position).getValue());
        holder.txtSubHarga.setText(allTypeData.datalist.get(position).getSub_harga());
        holder.img.setImageResource(allTypeData.datalist.get(position).getImgs());
        holder.trush.setImageResource(allTypeData.datalist.get(position).getTrush());
        holder.value.setVisibility(View.GONE);
        holder.trush.setVisibility(View.GONE);
        holder.txtSubHarga.setVisibility(View.GONE);
        allTypeData.modelDataSetGet = new ArrayList<>();
//        holder.tambah_produk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext(), R.style.dialog);
//                View dialogView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.content_dialog_transaksi, null);
//                builder.setView(dialogView);
//                builder.setCancelable(true);
//                builder.show();
//            }
//        });
        holder.to_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allTypeData.nameProduct = name_product;
                allTypeData.priceProduct = price_product;
                allTypeData.Values = value;
                allTypeData.potonganHarga = diskon;
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
//        for (int i = 0; i <10; i++){
//            dataModel.add(new modelProdukFree("Dress Casual Pink", "1"));
//        }
        allTypeData.dataModel.add(new modelProdukFree("Dress Panjang Kondangan K..", "1"));
        allTypeData.dataModel.add(new modelProdukFree("Dress Casual Pink", "2"));
        allTypeData.dataModel.add(new modelProdukFree("Celana Chinos Buat Perang ...", "4"));

        allTypeData.adapterProdukFree2 = new AdapterProdukFree2(allTypeData.dataModel);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        allTypeData.materi2.setLayoutManager(layoutManager);
        allTypeData.materi2.setAdapter(allTypeData.adapterProdukFree2);

    }

    private void SetProdukFree(View view){
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getRootView().getContext(), R.style.BottomSheetDialogTheme);
//        bottomSheetDialog.setContentView(R.layout.bottom_sheet_product);
//        bottomSheetDialog.show();
//
//        materi = bottomSheetDialog.findViewById(R.id.produk_gratis);
//        dataModel2 = new ArrayList<>();
//        adapterProdukFree = new AdapterProdukFree(dataModel2);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
//        materi.setLayoutManager(layoutManager);
//        materi.setAdapter(adapterProdukFree);
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
        kirimValues kirimValues = new kirimValues();
        Transaksi mainActivity = new Transaksi();
        allTypeData.bottomSheetDialog = new BottomSheetDialog(view.getRootView().getContext(), R.style.BottomSheetDialogTheme);
        allTypeData.bottomSheetDialog.setContentView(R.layout.bottom_sheet_product);
        allTypeData.bottomSheetDialog.show();
        allTypeData.bottomSheetDialog.create();

        TextView kode_produk, nama_produk, harga_produk;
        EditText field_isi_jumlah_barang;
        FlexboxLayout diskon_produk;
        RecyclerView list_produk_gratis;

        allTypeData.detail_dialog = allTypeData.bottomSheetDialog.findViewById(R.id.detail_dialog);
        field_isi_jumlah_barang = allTypeData.bottomSheetDialog.findViewById(R.id.field_isi_jumlah_barang);
        nama_produk = allTypeData.bottomSheetDialog.findViewById(R.id.nama_produk);
        harga_produk = allTypeData.bottomSheetDialog.findViewById(R.id.harga_produk);
        diskon_produk = allTypeData.bottomSheetDialog.findViewById(R.id.line_diskon);
        list_produk_gratis = allTypeData.bottomSheetDialog.findViewById(R.id.list_produk_gratis);
        allTypeData.materi = allTypeData.bottomSheetDialog.findViewById(R.id.produk_gratis);

        nama_produk.setText(allTypeData.nameProduct);
        harga_produk.setText(allTypeData.priceProduct);
        diskon_produk.setVisibility(View.GONE);

        allTypeData.materi = allTypeData.bottomSheetDialog.findViewById(R.id.produk_gratis);

        if (allTypeData.view1 == 1){
            allTypeData.materi.setVisibility(View.GONE);
            System.out.println("in");
        }else if (allTypeData.view1 == 2){
            System.out.println("out");
            allTypeData.materi = allTypeData.bottomSheetDialog.findViewById(R.id.produk_gratis);
            allTypeData.materi.setVisibility(View.VISIBLE);

            allTypeData.modelDataSetGet.add(new modelProdukFree(allTypeData.adapterProdukFree2.dataNamaProduk,"1"));
            allTypeData.adapterProdukFree = new AdapterProdukFree(allTypeData.modelDataSetGet);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
            allTypeData.materi.setLayoutManager(layoutManager);
            allTypeData.materi.setAdapter(allTypeData.adapterProdukFree);
            allTypeData.json = gson.toJson(allTypeData.modelDataSetGet);
            System.out.println(allTypeData.json);
        }else if (allTypeData.view1 == 3){
            if (allTypeData.modelDataSetGet != null){
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
                allTypeData.materi.setLayoutManager(layoutManager);
                allTypeData.materi.setAdapter(allTypeData.adapterProdukFree);
            }
        }

        TextView tambah_barang;
        LinearLayout button_back_to_transaksi;

        tambah_barang = allTypeData.bottomSheetDialog.findViewById(R.id.tambah_produk);
        tambah_barang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlernDialog(view);
                allTypeData.bottomSheetDialog.dismiss();
            }
        });

        button_back_to_transaksi = allTypeData.bottomSheetDialog.findViewById(R.id.button_back_to_transaksi);
        button_back_to_transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Transaksi.dataModels == null){
                    Transaksi.view = 1;
                }else if (Transaksi.dataModels != null){
                    Transaksi.view = 2;
                }
                ambilValues.array_produk_free = allTypeData.json;
                ambilValues.harga_produk = String.valueOf(harga_produk.getText());
                ambilValues.nama_produk = String.valueOf(nama_produk.getText());
                ambilValues.jumlah_pesanan = String.valueOf(field_isi_jumlah_barang.getText());

                ambilValues.jsonData = "{\"harga_produk\":\""+String.valueOf(harga_produk.getText())+"\",\"jumlah_pesanan\":\""+String.valueOf(field_isi_jumlah_barang.getText())+"\",\"nama_produk\":\""+String.valueOf(nama_produk.getText())+"\",\"data_produk_free\":\""+allTypeData.json+"\"}";

//                try {
//                    mainActivity.jsonObject = new JSONObject(ambilValues.jsonData);
//                    mainActivity.nama_produk = mainActivity.jsonObject.getString("nama_produk");
//                    mainActivity.harga_produk = mainActivity.jsonObject.getString("harga_produk");
//                    mainActivity.jumlah_pesanan = mainActivity.jsonObject.getString("jumlah_pesanan");
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }

                System.out.println(ambilValues.nama_produk);
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
            }
        });

        allTypeData.button_lanjut_transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
                allTypeData.view1 = 2;
                showBottomSheet(view);
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

    void non_produkFree(View view){
        Transaksi.addProductUsing = 2;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getRootView().getContext(), R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_product);
        bottomSheetDialog.show();

        TextView kode_produk, nama_produk, harga_produk, diskon_produk;
        RecyclerView list_produk_gratis;
        LinearLayout button_back_to_transaksi;
        FlexboxLayout values;
        EditText field_isi_jumlah_barang;

        field_isi_jumlah_barang = bottomSheetDialog.findViewById(R.id.field_isi_jumlah_barang);
        allTypeData.detail_dialog = bottomSheetDialog.findViewById(R.id.detail_dialog);
        values = bottomSheetDialog.findViewById(R.id.info_pesanan);
        nama_produk = bottomSheetDialog.findViewById(R.id.nama_produk);
        diskon_produk = bottomSheetDialog.findViewById(R.id.diskon_produk);
        harga_produk = bottomSheetDialog.findViewById(R.id.harga_produk);
        list_produk_gratis = bottomSheetDialog.findViewById(R.id.list_produk_gratis);

        values.setVisibility(View.GONE);
        nama_produk.setText(allTypeData.nameProduct);
        harga_produk.setText(allTypeData.priceProduct);
        diskon_produk.setText(allTypeData.potonganHarga);

        button_back_to_transaksi = bottomSheetDialog.findViewById(R.id.button_back_to_transaksi);
        button_back_to_transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Transaksi.dataModels == null){
                    Transaksi.view = 1;
                }else if (Transaksi.dataModels != null){
                    Transaksi.view = 2;
                }
                ambilValues.array_produk_free = allTypeData.json;
                ambilValues.harga_produk = String.valueOf(harga_produk.getText());
                ambilValues.nama_produk = String.valueOf(nama_produk.getText());
                ambilValues.jumlah_pesanan = String.valueOf(field_isi_jumlah_barang.getText());
                ambilValues.diskon_produk = String.valueOf(diskon_produk.getText());

                ambilValues.jsonData = "{\"harga_produk\":\""+String.valueOf(harga_produk.getText())+"\",\"jumlah_pesanan\":\""+String.valueOf(field_isi_jumlah_barang.getText())+"\",\"nama_produk\":\""+String.valueOf(nama_produk.getText())+"\",\"data_produk_free\":\""+allTypeData.json+"\"}";

//                try {
//                    mainActivity.jsonObject = new JSONObject(ambilValues.jsonData);
//                    mainActivity.nama_produk = mainActivity.jsonObject.getString("nama_produk");
//                    mainActivity.harga_produk = mainActivity.jsonObject.getString("harga_produk");
//                    mainActivity.jumlah_pesanan = mainActivity.jsonObject.getString("jumlah_pesanan");
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }

                System.out.println(ambilValues.nama_produk);
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
            img = itemView.findViewById(R.id.img);
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




//
//    private ArrayList<ModelTransaksi> dataSet;
//    Context mContext;
//
//    // View lookup cache
//    private static class ViewHolder {
//        TextView txtNamaProduk;
//        TextView txtHarga;
//        TextView potonganHarga;
//        TextView value;
//        TextView txtSubHarga;
//        ImageView img;
//        ImageView trush;
//    }
//
//    public AdapterTransaksi(ArrayList<ModelTransaksi> data, Context context) {
//        super(context, R.layout.activity_content, data);
//        this.dataSet = data;
//        this.mContext=context;
//
//    }
//
//    private int lastPosition = -1;
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // Get the data item for this position
//        ModelTransaksi model = getItem(position);
//        // Check if an existing view is being reused, otherwise inflate the view
//        ViewHolder viewHolder; // view lookup cache stored in tag
//
//        final View result;
//
//        if (convertView == null) {
//
//            viewHolder = new ViewHolder();
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(R.layout.activity_content, parent, false);
//            viewHolder.txtNamaProduk = (TextView) convertView.findViewById(R.id.name_product);
//            viewHolder.txtHarga = (TextView) convertView.findViewById(R.id.harga_pcs);
//            viewHolder.potonganHarga = (TextView) convertView.findViewById(R.id.potongan_harga);
//            viewHolder.value = (TextView) convertView.findViewById(R.id.value);
//            viewHolder.txtSubHarga = (TextView) convertView.findViewById(R.id.harga_total);
//            viewHolder.trush = (ImageView) convertView.findViewById(R.id.icon_sampah);
//            viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
//
//            result=convertView;
//
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//            result=convertView;
//        }
//
////        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
////        result.startAnimation(animation);
////        lastPosition = position;
//
//        viewHolder.txtNamaProduk.setText(model.getNama_produk());
//        viewHolder.txtHarga.setText(model.getHarga());
//        viewHolder.potonganHarga.setText(model.getHarga());
//        viewHolder.value.setText(model.getHarga());
//        viewHolder.txtSubHarga.setText(model.getSub_harga());
//        viewHolder.img.setImageResource(model.getImgs());
//        viewHolder.trush.setImageResource(model.getImgs());
////        viewHolder.img.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext(), R.style.dialog);
////                View diaView1 = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.activity_dialog, null);
////                TextView textLorem;
////                ImageView ImageProfile;
//////                textLorem = (TextView) diaView1.findViewById(R.id.text_lorem);
////                ImageProfile = (ImageView) diaView1.findViewById(R.id.pop_up_image);
//////                textLorem.setText(model.getMessage());
////                ImageProfile.setImageResource(model.getImgs());
////                builder.setView(diaView1);
////                builder.setCancelable(true);
////                builder.show();
////            }
////        });
//        viewHolder.img.setTag(position);
//        // Return the completed view to render on screen
//        return convertView;
//    }
}

class modelProdukFree {
    String name_product;
    String values;

    public modelProdukFree(String name_product, String values) {
        this.name_product = name_product;
        this.values = values;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }
}
public class TambahBarangActivity extends AppCompatActivity {
    ArrayList<ModelAddBarang> dataModels;
    RecyclerView materi;
    LinearLayout linearLayout ;
    EditText kode, searchView;
    ImageView searchScanKode, backTOMainTransaksi;
    TextView anu;
    String setKEY_KODE;
    private String getKEY_KODE, KEY_KODE = "KODE_BARANG";
    private static AdapterAddBarang adapterTransaksi;

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