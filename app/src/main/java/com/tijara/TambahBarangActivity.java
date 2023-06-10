package com.tijara;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.tijara.keranjang.DataKeranjang;
import com.tijara.keranjang.DiskonFreeProduk;
import com.tijara.keranjang.KategoriDiskon;
import com.tijara.keranjang.KategoriFreeProduk;
import com.tijara.keranjang.ModelDiskon;
import com.tijara.keranjang.ModelKeranjang;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import io.supercharge.shimmerlayout.ShimmerLayout;


public class TambahBarangActivity extends AppCompatActivity implements TextWatcher {

    ArrayList<ModelTambahProduk> datalist;


    ArrayList<ModelProdukFree> dataProdukFree;
    ArrayList<ModelProdukFree> dataProdukFreePreview = new ArrayList<>();
    ArrayList<DiskonFreeProduk> arrdata;
    RecyclerView listbarang;
    EditText field_kode_product;
    ImageView scan_produk;
    LinearLayout image_no_value;
    ShimmerLayout loading;
    SwipeRefreshLayout refreshLayout;
    String url = Env.BASE_URL.replace("/api/", "");
    BottomSheetDialog bottomSheetDialog;
    int freeProduk = 0;
    int subTotal = 0;
    int dibeli = 0;

    int jumlahFreeProdukDipilih = 0;

    CustomDialogSetup mDialog;

    private void setupDialog(CustomDialog type){
        mDialog = new CustomDialogSetup(this, R.style.dialog, type);
    }

    private void loadProduct(){
        listbarang.setVisibility(View.GONE);
        image_no_value.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        loading.startShimmerAnimation();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, Env.BASE_URL + "product/jual?apikey=" + Env.API_KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                datalist = new ArrayList<>();
                try {
                    JSONArray res = new JSONArray(response);
                    JSONObject produk;
                    if(res.length() != 0){
                        for(int i = 0; i < res.length(); i++){
                            produk = res.getJSONObject(i);
                            ModelTambahProduk modeltambah = null;
                            if(produk.getString("diskon").equals("null")){
                                modeltambah = new ModelTambahProduk(produk.getString("nama_br"), url + "/uploads/products/" + produk.getString("gambar"), produk.getString("kode_br"), Integer.parseInt(produk.getString("harga")), 0, Integer.parseInt(produk.getString("stok")));
                            } else {
                                JSONObject dataDiskon = new JSONObject(produk.getString("diskon"));
                                ModelBarangDiskon barangdiskon;
                                if(dataDiskon.getString("kategori").equals("nominal")){
                                    barangdiskon = new ModelBarangDiskon(KategoriDiskon.NOMINAL);
                                    barangdiskon.setNominal(dataDiskon.getInt("nominal"));
                                    modeltambah = new ModelTambahProduk(produk.getString("nama_br"), url + "/uploads/products/" + produk.getString("gambar"), produk.getString("kode_br"), Integer.parseInt(produk.getString("harga")), 0, Integer.parseInt(produk.getString("stok")), barangdiskon);

                                } else if(dataDiskon.getString("kategori").equals("persen")){
                                    barangdiskon = new ModelBarangDiskon(KategoriDiskon.PERSEN);
                                    barangdiskon.setNominal(dataDiskon.getInt("nominal"));
                                    modeltambah = new ModelTambahProduk(produk.getString("nama_br"), url + "/uploads/products/" + produk.getString("gambar"), produk.getString("kode_br"), Integer.parseInt(produk.getString("harga")), 0, Integer.parseInt(produk.getString("stok")), barangdiskon);

                                } else if(dataDiskon.getString("kategori").equals("free")){
                                    barangdiskon = new ModelBarangDiskon(KategoriDiskon.FREE_PRODUK);
                                    barangdiskon.setBuy(dataDiskon.getInt("buy"));
                                    barangdiskon.setFree(dataDiskon.getInt("free"));
                                    if(dataDiskon.getString("free_product").equals("sama")){
                                        barangdiskon.setFree_produk(KategoriFreeProduk.SAMA);
                                    } else if(dataDiskon.getString("free_product").equals("bebas")){
                                        barangdiskon.setFree_produk(KategoriFreeProduk.BEBAS);
                                    }
                                    modeltambah = new ModelTambahProduk(produk.getString("nama_br"), url + "/uploads/products/" + produk.getString("gambar"), produk.getString("kode_br"), Integer.parseInt(produk.getString("harga")), 0, Integer.parseInt(produk.getString("stok")), barangdiskon);
                                }

                            }
                            datalist.add(modeltambah);
                        }

                        AdapterTambahProduk dapt = new AdapterTambahProduk(datalist, new RecyclerViewListener() {
                            @Override
                            public void onClick(View view, int position) {
                                boolean cekKeranjang = false;
                                for (int i = 0; i < DataKeranjang.dataKeranjang.size(); i++){
                                    if(datalist.get(position).getKode_br().equals(DataKeranjang.dataKeranjang.get(i).getKode_br())){
                                        cekKeranjang = true;
                                    }
                                }

                                if(cekKeranjang){
                                    setupDialog(CustomDialog.WARNING);
                                    mDialog.setJudul("Informasi");
                                    mDialog.setDeskripsi("Data sudah ada dikeranjang");
                                    mDialog.setListenerOK(v -> {
                                        mDialog.dismiss();
                                    });
                                    mDialog.show();

                                } else {
                                    if(datalist.get(position).getDiskon() != null){
                                        if(datalist.get(position).getDiskon().getKategori() == KategoriDiskon.NOMINAL){
                                            showBottomSheet(datalist.get(position));
//                                        Toast.makeText(TambahBarangActivity.this, "Diskon Nominal", Toast.LENGTH_SHORT).show();
                                        } else if(datalist.get(position).getDiskon().getKategori() == KategoriDiskon.PERSEN){
                                            showBottomSheet(datalist.get(position));
//                                        Toast.makeText(TambahBarangActivity.this, "Diskon Persen", Toast.LENGTH_SHORT).show();
                                        } else if(datalist.get(position).getDiskon().getKategori() == KategoriDiskon.FREE_PRODUK){
                                            showBottomSheet(datalist.get(position));
//                                        Toast.makeText(TambahBarangActivity.this, "Diskon Free Produk", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        showBottomSheet(datalist.get(position));
//                                    Toast.makeText(TambahBarangActivity.this, "Tidak ada diskon", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

                        listbarang.setLayoutManager(new LinearLayoutManager(TambahBarangActivity.this));
                        listbarang.setAdapter(dapt);

                        loading.setVisibility(View.GONE);
                        image_no_value.setVisibility(View.GONE);
                        listbarang.setVisibility(View.VISIBLE);
                    } else {
                        loading.setVisibility(View.GONE);
                        listbarang.setVisibility(View.GONE);
                        image_no_value.setVisibility(View.VISIBLE);
                    }
                    refreshLayout.setRefreshing(false);
                    queue.getCache().clear();
                } catch (Exception e){
                    Toast.makeText(TambahBarangActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.VolleyError error) {
                refreshLayout.setRefreshing(false);
                Toast.makeText(TambahBarangActivity.this, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    private void loadProductSearch(String keyword){
        listbarang.setVisibility(View.GONE);
        image_no_value.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        loading.startShimmerAnimation();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, Env.BASE_URL + "product/jual/" + keyword + "?apikey=" + Env.API_KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                datalist = new ArrayList<>();
                try {
                    JSONArray res = new JSONArray(response);
                    JSONObject produk;
                    if(res.length() != 0){
                        for(int i = 0; i < res.length(); i++){
                            produk = res.getJSONObject(i);
                            ModelTambahProduk modeltambah = null;
                            if(produk.getString("diskon").equals("null")){
                                modeltambah = new ModelTambahProduk(produk.getString("nama_br"), url + "/uploads/products/" + produk.getString("gambar"), produk.getString("kode_br"), Integer.parseInt(produk.getString("harga")), 0, Integer.parseInt(produk.getString("stok")));
                            } else {
                                JSONObject dataDiskon = new JSONObject(produk.getString("diskon"));
                                ModelBarangDiskon barangdiskon;
                                if(dataDiskon.getString("kategori").equals("nominal")){
                                    barangdiskon = new ModelBarangDiskon(KategoriDiskon.NOMINAL);
                                    barangdiskon.setNominal(dataDiskon.getInt("nominal"));
                                    modeltambah = new ModelTambahProduk(produk.getString("nama_br"), url + "/uploads/products/" + produk.getString("gambar"), produk.getString("kode_br"), Integer.parseInt(produk.getString("harga")), 0, Integer.parseInt(produk.getString("stok")), barangdiskon);

                                } else if(dataDiskon.getString("kategori").equals("persen")){
                                    barangdiskon = new ModelBarangDiskon(KategoriDiskon.PERSEN);
                                    barangdiskon.setNominal(dataDiskon.getInt("nominal"));
                                    modeltambah = new ModelTambahProduk(produk.getString("nama_br"), url + "/uploads/products/" + produk.getString("gambar"), produk.getString("kode_br"), Integer.parseInt(produk.getString("harga")), 0, Integer.parseInt(produk.getString("stok")), barangdiskon);

                                } else if(dataDiskon.getString("kategori").equals("free")){
                                    barangdiskon = new ModelBarangDiskon(KategoriDiskon.FREE_PRODUK);
                                    barangdiskon.setBuy(dataDiskon.getInt("buy"));
                                    barangdiskon.setFree(dataDiskon.getInt("free"));
                                    if(dataDiskon.getString("free_product").equals("sama")){
                                        barangdiskon.setFree_produk(KategoriFreeProduk.SAMA);
                                    } else if(dataDiskon.getString("free_product").equals("bebas")){
                                        barangdiskon.setFree_produk(KategoriFreeProduk.BEBAS);
                                    }
                                    modeltambah = new ModelTambahProduk(produk.getString("nama_br"), url + "/uploads/products/" + produk.getString("gambar"), produk.getString("kode_br"), Integer.parseInt(produk.getString("harga")), 0, Integer.parseInt(produk.getString("stok")), barangdiskon);
                                }

                            }
                            datalist.add(modeltambah);
                        }

                        AdapterTambahProduk dapt = new AdapterTambahProduk(datalist, new RecyclerViewListener() {
                            @Override
                            public void onClick(View view, int position) {
                                boolean cekKeranjang = false;
                                for (int i = 0; i < DataKeranjang.dataKeranjang.size(); i++){
                                    if(datalist.get(position).getKode_br().equals(DataKeranjang.dataKeranjang.get(i).getKode_br())){
                                        cekKeranjang = true;
                                    }
                                }

                                if(cekKeranjang){
                                    setupDialog(CustomDialog.WARNING);
                                    mDialog.setJudul("Informasi");
                                    mDialog.setDeskripsi("Data sudah ada dikeranjang");
                                    mDialog.setListenerOK(v -> {
                                        mDialog.dismiss();
                                    });
                                    mDialog.show();

                                } else {
                                    if(datalist.get(position).getDiskon() != null){
                                        if(datalist.get(position).getDiskon().getKategori() == KategoriDiskon.NOMINAL){
                                            showBottomSheet(datalist.get(position));
//                                        Toast.makeText(TambahBarangActivity.this, "Diskon Nominal", Toast.LENGTH_SHORT).show();
                                        } else if(datalist.get(position).getDiskon().getKategori() == KategoriDiskon.PERSEN){
                                            showBottomSheet(datalist.get(position));
//                                        Toast.makeText(TambahBarangActivity.this, "Diskon Persen", Toast.LENGTH_SHORT).show();
                                        } else if(datalist.get(position).getDiskon().getKategori() == KategoriDiskon.FREE_PRODUK){
                                            showBottomSheet(datalist.get(position));
//                                        Toast.makeText(TambahBarangActivity.this, "Diskon Free Produk", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        showBottomSheet(datalist.get(position));
//                                    Toast.makeText(TambahBarangActivity.this, "Tidak ada diskon", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

                        listbarang.setLayoutManager(new LinearLayoutManager(TambahBarangActivity.this));
                        listbarang.setAdapter(dapt);

                        loading.setVisibility(View.GONE);
                        image_no_value.setVisibility(View.GONE);
                        listbarang.setVisibility(View.VISIBLE);
                    } else {
                        loading.setVisibility(View.GONE);
                        listbarang.setVisibility(View.GONE);
                        image_no_value.setVisibility(View.VISIBLE);
                    }

                    refreshLayout.setRefreshing(false);
                    queue.getCache().clear();
                } catch (Exception e){
                    Toast.makeText(TambahBarangActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.VolleyError error) {
                refreshLayout.setRefreshing(false);
                Toast.makeText(TambahBarangActivity.this, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    private void showBottomSheet(ModelTambahProduk model){
        jumlahFreeProdukDipilih = 0;
        dataProdukFreePreview = new ArrayList<>();
        arrdata = new ArrayList<>();
        bottomSheet(model);
    }

    private void bottomSheet(ModelTambahProduk model){
        bottomSheetDialog = new BottomSheetDialog(TambahBarangActivity.this, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_product);
        bottomSheetDialog.setCanceledOnTouchOutside(false);

        TextView kode_produk, nama_produk, harga_produk, diskon_produk, tambah_produk, free_produk_didapat, sub_total, stok_produk;
        EditText field_isi_jumlah_barang;
        FlexboxLayout info_pesanan;
        RecyclerView produk_gratis;
        LinearLayout button_back_to_transaksi;

        kode_produk = bottomSheetDialog.findViewById(R.id.kode_produk);
        nama_produk = bottomSheetDialog.findViewById(R.id.nama_produk);
        diskon_produk = bottomSheetDialog.findViewById(R.id.diskon_produk);
        harga_produk = bottomSheetDialog.findViewById(R.id.harga_produk);
        tambah_produk = bottomSheetDialog.findViewById(R.id.tambah_produk);
        info_pesanan = bottomSheetDialog.findViewById(R.id.info_pesanan);
        free_produk_didapat = bottomSheetDialog.findViewById(R.id.free_produk_didapat);
        produk_gratis = bottomSheetDialog.findViewById(R.id.produk_gratis);
        field_isi_jumlah_barang = bottomSheetDialog.findViewById(R.id.field_isi_jumlah_barang);
        stok_produk = bottomSheetDialog.findViewById(R.id.stok_produk);
        sub_total = bottomSheetDialog.findViewById(R.id.sub_total);
        button_back_to_transaksi = bottomSheetDialog.findViewById(R.id.button_back_to_transaksi);

        produk_gratis.setLayoutManager(new LinearLayoutManager(TambahBarangActivity.this));
        AdapterProdukFreePreview adaptProdekFreePreview = new AdapterProdukFreePreview(dataProdukFreePreview, new ClickedMinPlus() {
            @Override
            public void clickMin(View view, int position) {
                int now = jumlahFreeProdukDipilih;
                if(dataProdukFreePreview.get(position).getQty() == 1){

                } else {
                    jumlahFreeProdukDipilih = jumlahFreeProdukDipilih - 1;
                    dataProdukFreePreview.get(position).setQty(dataProdukFreePreview.get(position).getQty() - 1);
                }
            }

            @Override
            public void clickPlus(View view, int position) {
                int now = jumlahFreeProdukDipilih;
//                Toast.makeText(TambahBarangActivity.this, String.valueOf(now), Toast.LENGTH_SHORT).show();

                if(now == freeProduk){

                } else {
                    jumlahFreeProdukDipilih += 1;
                    int hitung = dataProdukFreePreview.get(position).getQty() + 1;
                    dataProdukFreePreview.get(position).setQty(hitung);
                    model.getDiskon().getDiskonFreeProduks().get(position).setQty(hitung);
                }

            }
        });
        produk_gratis.setAdapter(adaptProdekFreePreview);

        info_pesanan.setVisibility(View.GONE);
        produk_gratis.setVisibility(View.GONE);

        kode_produk.setText(model.getKode_br());
        sub_total.setText("Rp. 0");
        nama_produk.setText(model.getNama());
        stok_produk.setText(String.valueOf(model.getStok()));

        free_produk_didapat.setText("0");
        harga_produk.setText(Env.formatRupiah(model.getHarga_asli()));
        if(model.getDiskon() != null){
            switch (model.getDiskon().getKategori()){
                case PERSEN:
                    field_isi_jumlah_barang.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (!charSequence.toString().isEmpty()) {
                                int number = Integer.parseInt(charSequence.toString());

                                // Periksa apakah angka berada dalam rentang yang diinginkan
                                if (number < 1 || number > model.getStok() ) {
                                    // Angka di luar rentang, hapus teks terakhir

                                    field_isi_jumlah_barang.setText(charSequence.subSequence(0, charSequence.length() - 1));
                                    field_isi_jumlah_barang.setSelection(field_isi_jumlah_barang.getText().length());

                                } else {
                                }
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            freeProduk = 0;
                            if(!editable.toString().isEmpty()){
                                int field = Integer.valueOf(editable.toString());
                                dibeli = field;
                                subTotal = field * (model.getHarga_asli()  - ((model.getHarga_asli() / 100) * model.getDiskon().getNominal()));
                                model.setQty(field);
                            } else {
                                dibeli = 0;
                                subTotal = 0;
                                model.setQty(0);
                            }
                            sub_total.setText("Rp. " + Env.formatRupiah(subTotal));


                        }
                    });
                    diskon_produk.setText(String.valueOf(model.getDiskon().getNominal()) + "%");
                    break;
                case NOMINAL:
                    field_isi_jumlah_barang.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (!charSequence.toString().isEmpty()) {
                                int number = Integer.parseInt(charSequence.toString());

                                // Periksa apakah angka berada dalam rentang yang diinginkan
                                if (number < 1 || number > model.getStok()) {
                                    // Angka di luar rentang, hapus teks terakhir

                                    field_isi_jumlah_barang.setText(charSequence.subSequence(0, charSequence.length() - 1));
                                    field_isi_jumlah_barang.setSelection(field_isi_jumlah_barang.getText().length());

                                } else {
                                }
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            freeProduk = 0;
                            if(!editable.toString().isEmpty()){
                                int field = Integer.valueOf(editable.toString());
                                dibeli = field;
                                subTotal = field * (model.getHarga_asli() - model.getDiskon().getNominal());
                                model.setQty(field);
                            } else {
                                 dibeli = 0;
                                subTotal = 0;
                                model.setQty(0);
                            }
                            sub_total.setText("Rp. " + Env.formatRupiah(subTotal));

                        }
                    });
                    diskon_produk.setText(Env.formatRupiah(model.getDiskon().getNominal()));
                    break;
                case FREE_PRODUK:
                    field_isi_jumlah_barang.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (!charSequence.toString().isEmpty()) {
                                int number = Integer.parseInt(charSequence.toString());

                                // Periksa apakah angka berada dalam rentang yang diinginkan
                                if (number < 1 || number > model.getStok()) {
                                    // Angka di luar rentang, hapus teks terakhir

                                    field_isi_jumlah_barang.setText(charSequence.subSequence(0, charSequence.length() - 1));
                                    field_isi_jumlah_barang.setSelection(field_isi_jumlah_barang.getText().length());



                                } else {
                                }
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            if(!editable.toString().isEmpty()){



                                int beli = Integer.valueOf(model.getDiskon().getBuy());
                                int gratis = Integer.valueOf(model.getDiskon().getFree());
                                int field = Integer.valueOf(editable.toString());

                                if(field > model.getStok()){
                                    return;
                                }
                                dibeli = field;
                                model.setQty(field);

                                subTotal = field * model.getHarga_asli();

                                if(field == beli){
                                    freeProduk = gratis;
                                } else if(field % beli == 0){
                                    int kelipatan = field / beli;
                                    freeProduk = gratis * kelipatan;
                                } else {
                                    if(field >= beli && field % beli != 0){
                                        if(field > model.getStok()){
//                                            freeProduk = gratis * kelipatan;
                                        } else {
                                            freeProduk = (1 * ( field / beli )) * gratis;
                                        }
                                    } else {
                                        freeProduk = 0;
                                    }
                                }

//                                if(Integer.parseInt(editable.toString()) > model.getStok()){
//                                    freeProduk = gratis * kelipatan;
//                                }
                            } else {
                                jumlahFreeProdukDipilih = 0;

                                model.setQty(0);
                                dibeli = 0;
                                freeProduk = 0;
                                subTotal = 0;


                            }
                            sub_total.setText("Rp. " + Env.formatRupiah(subTotal));
                            free_produk_didapat.setText(String.valueOf(freeProduk));
                            if (freeProduk != 0){
                                if(model.getDiskon().getFree_produk() == KategoriFreeProduk.SAMA){

                                    if(dataProdukFreePreview.size() == 0){
                                        arrdata = new ArrayList<>();
                                        arrdata.add(new DiskonFreeProduk(model.getNama(), model.getKode_br(), freeProduk));
                                        model.getDiskon().setDiskonFreeProduks(arrdata);
                                        dataProdukFreePreview.add(new ModelProdukFree(model.getNama(), model.getGambar(), model.getKode_br(), freeProduk, KategoriFreeProduk.SAMA));
                                    } else {
                                        arrdata.get(0).setQty(freeProduk);
                                        dataProdukFreePreview.get(0).setQty(freeProduk);
                                    }
                                    jumlahFreeProdukDipilih = freeProduk;
                                    adaptProdekFreePreview.notifyDataSetChanged();
                                }

                                info_pesanan.setVisibility(View.VISIBLE);
                                produk_gratis.setVisibility(View.VISIBLE);
                            } else {
                                adaptProdekFreePreview.clearData();
                                model.getDiskon().setDiskonFreeProduks(new ArrayList<>());
                                arrdata = new ArrayList<>();

                                info_pesanan.setVisibility(View.GONE);
                                produk_gratis.setVisibility(View.GONE);
                            }



                        }
                    });
                    diskon_produk.setText("Beli " + model.getDiskon().getBuy() + " Gratis " + model.getDiskon().getFree());
                    if(model.getDiskon().getFree_produk() == KategoriFreeProduk.SAMA){
                        tambah_produk.setVisibility(View.GONE);
                    } else if(model.getDiskon().getFree_produk() == KategoriFreeProduk.BEBAS){
                        tambah_produk.setOnClickListener(view -> {
                            if(jumlahFreeProdukDipilih == freeProduk){
                                setupDialog(CustomDialog.WARNING);
                                mDialog.setJudul("Informasi");
                                mDialog.setDeskripsi("Free produk sudah terpenuhi");
                                mDialog.setListenerOK(v -> {
                                    mDialog.dismiss();
                                });
                                mDialog.show();

                            } else {
                                showDialogTambahProduk(adaptProdekFreePreview, model);
                            }
                        });
                    }


                    break;
            }
        } else {
            field_isi_jumlah_barang.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!charSequence.toString().isEmpty()) {
                        int number = Integer.parseInt(charSequence.toString());

                        // Periksa apakah angka berada dalam rentang yang diinginkan
                        if (number < 1 || number > model.getStok()) {
                            // Angka di luar rentang, hapus teks terakhir

                            field_isi_jumlah_barang.setText(charSequence.subSequence(0, charSequence.length() - 1));
                            field_isi_jumlah_barang.setSelection(field_isi_jumlah_barang.getText().length());

                        } else {
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(!editable.toString().isEmpty()){
                        int field = Integer.valueOf(editable.toString());
                        subTotal = field * model.getHarga_asli();
                        model.setQty(field);
                    } else {
                        dibeli = 0;
                        subTotal = 0;
                        model.setQty(0);
                    }
                    sub_total.setText("Rp. " + Env.formatRupiah(subTotal));
                }
            });
            diskon_produk.setText("-");

        }
        button_back_to_transaksi.setOnClickListener(view -> {
            if(field_isi_jumlah_barang.getText().toString().equals("")){
                setupDialog(CustomDialog.WARNING);
                mDialog.setJudul("Informasi");
                mDialog.setDeskripsi("Field jumlah barang wajib diisi");
                mDialog.setListenerOK(v -> {
                    mDialog.dismiss();
                });
                mDialog.show();

            } else {
                if(jumlahFreeProdukDipilih == freeProduk){
//                    Toast.makeText(this, "lanjut aman", Toast.LENGTH_SHORT).show();
                    System.out.println(model.getAllData());
                    if(model.getDiskon() == null){
                        DataKeranjang.dataKeranjang.add(new ModelKeranjang(model.getKode_br(), model.getNama(), model.getGambar(), model.getHarga_asli(), model.getHarga_asli() * model.getQty(), model.getQty()));
                    } else {
                        if(model.getDiskon().getKategori() == KategoriDiskon.NOMINAL){
                            ModelDiskon diskon = new ModelDiskon(KategoriDiskon.NOMINAL);
                            diskon.setNominal(String.valueOf(model.getDiskon().getNominal()));
                            DataKeranjang.dataKeranjang.add(new ModelKeranjang(model.getKode_br(), model.getNama(), model.getGambar(), model.getHarga_asli(), subTotal, model.getQty(), diskon));

                        } else if(model.getDiskon().getKategori() == KategoriDiskon.PERSEN){
                            ModelDiskon diskon = new ModelDiskon(KategoriDiskon.PERSEN);
                            diskon.setNominal(String.valueOf(model.getDiskon().getNominal()));
                            DataKeranjang.dataKeranjang.add(new ModelKeranjang(model.getKode_br(), model.getNama(), model.getGambar(), model.getHarga_asli(), subTotal, model.getQty(), diskon));

                        } else if(model.getDiskon().getKategori() == KategoriDiskon.FREE_PRODUK){
                            ModelDiskon diskon = new ModelDiskon(KategoriDiskon.FREE_PRODUK);
                            diskon.setBuy(model.getDiskon().getBuy());
                            diskon.setFree(model.getDiskon().getFree());
                            diskon.setKategori_free(model.diskon.getFree_produk());
                            ArrayList<DiskonFreeProduk> arrdatafree = new ArrayList<>();
                            for (int k = 0; k < model.getDiskon().getDiskonFreeProduks().size(); k++){
                                arrdatafree.add(new DiskonFreeProduk(model.getDiskon().getDiskonFreeProduks().get(k).getNama(), model.getDiskon().getDiskonFreeProduks().get(k).getKode(), model.getDiskon().getDiskonFreeProduks().get(k).getQty()));
                            }

                            diskon.setDetail_free_produk(arrdatafree);
                            DataKeranjang.dataKeranjang.add(new ModelKeranjang(model.getKode_br(), model.getNama(), model.getGambar(), model.getHarga_asli(), model.getHarga_asli() * model.getQty(), model.getQty(), diskon));

                        }

                    }

                    Intent inten = new Intent();
                    inten.putExtra("status", "success");
                    setResult(RESULT_OK, inten);
                    finish();

                } else {
                    setupDialog(CustomDialog.WARNING);
                    mDialog.setJudul("Informasi");
                    mDialog.setDeskripsi("Masukkan free produk terlebih dahulu");
                    mDialog.setListenerOK(v -> {
                        mDialog.dismiss();
                    });
                    mDialog.show();
                }
            }
        });


        bottomSheetDialog.show();
    }

    EditText fielddd;

    private void showDialogTambahProduk(AdapterProdukFreePreview adapt, ModelTambahProduk model){
        bottomSheetDialog.dismiss();

        AlertDialog builder = new AlertDialog.Builder(TambahBarangActivity.this, R.style.dialog).create();
        View dialogView = LayoutInflater.from(TambahBarangActivity.this).inflate(R.layout.content_dialog_transaksi, null);
        builder.setView(dialogView);
        builder.setCancelable(true);

        LinearLayout detail_dialog, button_metode_barcode;
        ImageView button_close;

        RecyclerView listViewProdukFree;
        ShimmerLayout loadinggg;
        LinearLayout imagenovalue;

        detail_dialog = dialogView.findViewById(R.id.detail_dialog);
        button_metode_barcode = dialogView.findViewById(R.id.button_metode_barcode);
        listViewProdukFree = dialogView.findViewById(R.id.list_produk_gratis);
        loadinggg = dialogView.findViewById(R.id.loading);
        loadinggg.startShimmerAnimation();
        imagenovalue = dialogView.findViewById(R.id.image_no_value);

        fielddd = dialogView.findViewById(R.id.field_kode_product);
        detail_dialog.setVisibility(View.VISIBLE);
        button_close = dialogView.findViewById(R.id.button_close);
        button_close.setOnClickListener(view -> builder.dismiss());

        fielddd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().isEmpty()){
                    loadProductFree(listViewProdukFree, builder, adapt, model, imagenovalue, loadinggg);
                } else {
                    loadProductFreeSearch(listViewProdukFree, builder, adapt, model, imagenovalue, loadinggg, editable.toString());
                }
            }
        });

        button_metode_barcode.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(TambahBarangActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(TambahBarangActivity.this, android.Manifest.permission.CAMERA)) {
                    scanBarcodeFree();
                } else {
                    ActivityCompat.requestPermissions(TambahBarangActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                }
            } else {
                scanBarcodeFree();
            }
        });

        loadProductFree(listViewProdukFree, builder, adapt, model, imagenovalue, loadinggg);

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                bottomSheetDialog.show();
            }
        });

        builder.show();
    }

    private void loadProductFree(RecyclerView view, AlertDialog dialog, AdapterProdukFreePreview adapt, ModelTambahProduk model, LinearLayout imagenovalue, ShimmerLayout loadinggg){
        view.setVisibility(View.GONE);
        imagenovalue.setVisibility(View.GONE);
        loadinggg.setVisibility(View.VISIBLE);
        loadinggg.startShimmerAnimation();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, Env.BASE_URL + "product/free?apikey=" + Env.API_KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataProdukFree = new ArrayList<>();
                try {
                    JSONArray res = new JSONArray(response);
                    if(res.length() != 0){
                        JSONObject produk;
                        String kategori;
                        for(int i = 0; i < res.length(); i++){
                            produk = res.getJSONObject(i);

                            dataProdukFree.add(new ModelProdukFree(produk.getString("nama_br"), url + "/uploads/products/" + produk.getString("gambar"), produk.getString("kode_br"), 1, KategoriFreeProduk.BEBAS));
                        }

                        AdapterProdukFree dapt = new AdapterProdukFree(dataProdukFree, new RecyclerViewListener() {
                            @Override
                            public void onClick(View view, int position) {
                                boolean isError = false;
                                for(int i = 0; i < dataProdukFreePreview.size(); i++){
                                    if(dataProdukFreePreview.get(i).getKode_br().equals(dataProdukFree.get(position).getKode_br())){
                                        isError = true;
                                    }
                                }

                                if(isError){
                                    setupDialog(CustomDialog.WARNING);
                                    mDialog.setJudul("Informasi");
                                    mDialog.setDeskripsi("Data sudah dimasukkan");
                                    mDialog.setListenerOK(v -> {
                                        mDialog.dismiss();
                                    });
                                    mDialog.show();


                                } else {
                                    dataProdukFreePreview.add(new ModelProdukFree(dataProdukFree.get(position).getNama(), dataProdukFree.get(position).getPath_gambar(), dataProdukFree.get(position).getKode_br(), dataProdukFree.get(position).getQty(), KategoriFreeProduk.BEBAS));
                                    adapt.notifyDataSetChanged();
                                    jumlahFreeProdukDipilih = jumlahFreeProdukDipilih + 1;
                                    arrdata.add(new DiskonFreeProduk(dataProdukFree.get(position).getNama(), dataProdukFree.get(position).getKode_br(), 1));
                                    model.getDiskon().setDiskonFreeProduks(arrdata);
                                    dialog.dismiss();
                                }



//                            Toast.makeText(TambahBarangActivity.this, dataProdukFree.get(position).getNama(), Toast.LENGTH_SHORT).show();

                            }
                        });

                        view.setLayoutManager(new LinearLayoutManager(TambahBarangActivity.this));
                        view.setAdapter(dapt);


                        loadinggg.setVisibility(View.GONE);
                        imagenovalue.setVisibility(View.GONE);
                        view.setVisibility(View.VISIBLE);
                    } else {
                        view.setVisibility(View.GONE);
                        loadinggg.setVisibility(View.GONE);
                        imagenovalue.setVisibility(View.VISIBLE);
                    }
                    queue.getCache().clear();

//                    Toast.makeText(TambahBarangActivity.this, "sukses", Toast.LENGTH_SHORT).show();
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

    private void loadProductFreeSearch(RecyclerView view, AlertDialog dialog, AdapterProdukFreePreview adapt, ModelTambahProduk model, LinearLayout imagenovalue, ShimmerLayout loadinggg, String keyword){
        view.setVisibility(View.GONE);
        imagenovalue.setVisibility(View.GONE);
        loadinggg.setVisibility(View.VISIBLE);
        loadinggg.startShimmerAnimation();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, Env.BASE_URL + "product/free/" + keyword + "?apikey=" + Env.API_KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataProdukFree = new ArrayList<>();
                try {

                    JSONArray res = new JSONArray(response);

                    if(res.length() != 0) {
                        JSONObject produk;
                        String kategori;
                        for(int i = 0; i < res.length(); i++){
                            produk = res.getJSONObject(i);

                            dataProdukFree.add(new ModelProdukFree(produk.getString("nama_br"), url + "/uploads/products/" + produk.getString("gambar"), produk.getString("kode_br"), 1, KategoriFreeProduk.BEBAS));
                        }

                        AdapterProdukFree dapt = new AdapterProdukFree(dataProdukFree, new RecyclerViewListener() {
                            @Override
                            public void onClick(View view, int position) {
                                boolean isError = false;
                                for(int i = 0; i < dataProdukFreePreview.size(); i++){
                                    if(dataProdukFreePreview.get(i).getKode_br().equals(dataProdukFree.get(position).getKode_br())){
                                        isError = true;
                                    }
                                }

                                if(isError){
                                    setupDialog(CustomDialog.WARNING);
                                    mDialog.setJudul("Informasi");
                                    mDialog.setDeskripsi("Data sudah dimasukkan");
                                    mDialog.setListenerOK(v -> {
                                        mDialog.dismiss();
                                    });
                                    mDialog.show();
                                } else {
                                    dataProdukFreePreview.add(new ModelProdukFree(dataProdukFree.get(position).getNama(), dataProdukFree.get(position).getPath_gambar(), dataProdukFree.get(position).getKode_br(), dataProdukFree.get(position).getQty(), KategoriFreeProduk.BEBAS));
                                    adapt.notifyDataSetChanged();
                                    jumlahFreeProdukDipilih = jumlahFreeProdukDipilih + 1;
                                    arrdata.add(new DiskonFreeProduk(dataProdukFree.get(position).getNama(), dataProdukFree.get(position).getKode_br(), 1));
                                    model.getDiskon().setDiskonFreeProduks(arrdata);
                                    dialog.dismiss();
                                }

//                            Toast.makeText(TambahBarangActivity.this, dataProdukFree.get(position).getNama(), Toast.LENGTH_SHORT).show();

                            }
                        });

                        view.setLayoutManager(new LinearLayoutManager(TambahBarangActivity.this));
                        view.setAdapter(dapt);

                        loadinggg.setVisibility(View.GONE);
                        imagenovalue.setVisibility(View.GONE);
                        view.setVisibility(View.VISIBLE);
                    } else {
                        view.setVisibility(View.GONE);
                        loadinggg.setVisibility(View.GONE);
                        imagenovalue.setVisibility(View.VISIBLE);
                    }
                    queue.getCache().clear();

//                    Toast.makeText(TambahBarangActivity.this, "sukses", Toast.LENGTH_SHORT).show();
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

    ImageView back_to_main_transaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_barang);

        refreshLayout = findViewById(R.id.refreshLayout);
        listbarang = findViewById(R.id.list_barang);
        field_kode_product = findViewById(R.id.field_kode_product);
        scan_produk = findViewById(R.id.scan_produk);
        image_no_value = findViewById(R.id.image_no_value);
        loading = findViewById(R.id.loading);
        loading.startShimmerAnimation();
        back_to_main_transaksi = findViewById(R.id.back_to_main_transaksi);
        field_kode_product.addTextChangedListener(this);

        if(getIntent().getStringExtra("kode") == null){
            loadProduct();
        } else {
            field_kode_product.setText(getIntent().getStringExtra("kode"));
            loadProductSearch(field_kode_product.getText().toString());
        }

        scan_produk.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(TambahBarangActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(TambahBarangActivity.this, android.Manifest.permission.CAMERA)) {
                    scanBarcode();
                } else {
                    ActivityCompat.requestPermissions(TambahBarangActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                }
            } else {
                scanBarcode();
            }
        });

        back_to_main_transaksi.setOnClickListener(view -> {
            Intent inten = new Intent();
            inten.putExtra("status", "back");
            setResult(RESULT_CANCELED, inten);
            finish();
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(field_kode_product.getText().toString().equals("")){
                    loadProduct();
                } else {
                    loadProductSearch(field_kode_product.getText().toString());
                }
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void scanBarcodeFree() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan kode barang");
        options.setBeepEnabled(true);
        options.setCaptureActivity(ScanBarcodeActivity.class);
        options.setOrientationLocked(true);
        barLauncherFree.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncherFree = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            fielddd.setText(result.getContents());
        }
    });

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
            field_kode_product.setText(result.getContents());
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

    @Override
    public void onBackPressed() {
        Intent inten = new Intent();
        inten.putExtra("status", "back");
        setResult(RESULT_CANCELED, inten);
        finish();
        super.onBackPressed();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }



    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(!editable.toString().isEmpty()){
            loadProductSearch(editable.toString());
        } else {
            loadProduct();
        }
    }
}

class AdapterTambahProduk extends RecyclerView.Adapter<AdapterTambahProduk.RecyclerViewViewHolder>{
    private ArrayList<ModelTambahProduk> dataList;
    RecyclerViewListener listener;
    private Context context;

    public AdapterTambahProduk(ArrayList<ModelTambahProduk> dataList, RecyclerViewListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layout = LayoutInflater.from(parent.getContext());
        View view = layout.inflate(R.layout.content_tambah_barang, parent, false);
        return new AdapterTambahProduk.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
        holder.nama.setText(dataList.get(position).getNama());
        if(dataList.get(position).getDiskon() != null){
            if(dataList.get(position).getDiskon().getKategori() == KategoriDiskon.PERSEN){
                holder.harga.setText(Env.formatRupiah(dataList.get(position).getHarga_asli() - ((dataList.get(position).getHarga_asli() / 100) * dataList.get(position).getDiskon().getNominal())));
            } else {
                holder.harga.setText(Env.formatRupiah(dataList.get(position).getHarga_asli() - dataList.get(position).getDiskon().getNominal()));
            }
        } else {
            holder.harga.setText(Env.formatRupiah(dataList.get(position).getHarga_asli()));
        }

        holder.icon_sampah.setVisibility(View.GONE);
        Glide.with(context).load(dataList.get(position).getGambar()).centerCrop().placeholder(R.drawable.tshirt).into(holder.img_tambah_barang);
        if(dataList.get(position).getDiskon() != null){
            if(dataList.get(position).getDiskon().getKategori() == KategoriDiskon.NOMINAL || dataList.get(position).getDiskon().getKategori() == KategoriDiskon.PERSEN){
                holder.potongan_harga.setText(Env.formatRupiah(dataList.get(position).getHarga_asli()));
                holder.potongan_harga.setPaintFlags(holder.potongan_harga.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.potongan_harga.setText("Buy " + dataList.get(position).getDiskon().getBuy() + " Free " + dataList.get(position).getDiskon().getFree());
            }

        } else {
            holder.potongan_harga.setText("");
        }
        holder.layout_harga_total.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nama, harga, potongan_harga;
        LinearLayout layout_harga_total;
        ImageView img_tambah_barang, icon_sampah;
        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.name_product);
            harga = itemView.findViewById(R.id.harga_pcs);
            potongan_harga = itemView.findViewById(R.id.potongan_harga);
            layout_harga_total = itemView.findViewById(R.id.layout_harga_total);
            img_tambah_barang = itemView.findViewById(R.id.img_tambah_barang);
            icon_sampah = itemView.findViewById(R.id.icon_sampah);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
}

interface ClickedMinPlus {
    void clickMin(View view, int position);
    void clickPlus(View view, int position);
}


class AdapterProdukFreePreview extends RecyclerView.Adapter<AdapterProdukFreePreview.RecyclerViewViewHolder>{
    private ArrayList<ModelProdukFree> dataList;
    ClickedMinPlus listener;
    private Context context;

    public void clearData(){
        dataList.clear();
        notifyDataSetChanged();
    }

    public AdapterProdukFreePreview(ArrayList<ModelProdukFree> dataList, ClickedMinPlus listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layout = LayoutInflater.from(parent.getContext());
        View view = layout.inflate(R.layout.content_produk_free, parent, false);
        return new AdapterProdukFreePreview.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
        holder.nama.setText(dataList.get(position).getNama());
        if(dataList.get(position).getKategori() == KategoriFreeProduk.BEBAS){
            holder.qty.setText(String.valueOf(dataList.get(position).getQty()));
            holder.min_jumlah.setVisibility(View.VISIBLE);
            holder.add_jumlah.setVisibility(View.VISIBLE);
        } else {
            holder.qty.setText(String.valueOf(dataList.get(position).getQty()) + "x");
            holder.min_jumlah.setVisibility(View.GONE);
            holder.add_jumlah.setVisibility(View.GONE);
        }

        Glide.with(context).load(dataList.get(position).getPath_gambar()).centerCrop().placeholder(R.drawable.tshirt).into(holder.img_tambah_barang);
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        TextView nama, qty;
        ImageView img_tambah_barang, min_jumlah, add_jumlah;
        FlexboxLayout plus_minus_values;
        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.name_product);
            qty = itemView.findViewById(R.id.jumlah_pcs);
            img_tambah_barang = itemView.findViewById(R.id.img_free_produk);
            plus_minus_values = itemView.findViewById(R.id.plus_minus_values);

            min_jumlah = itemView.findViewById(R.id.min_jumlah);
            add_jumlah = itemView.findViewById(R.id.add_jumlah);

            min_jumlah.setOnClickListener(view -> {
                listener.clickMin(view, getAdapterPosition());
                notifyDataSetChanged();
            });
            add_jumlah.setOnClickListener(view -> {
                listener.clickPlus(add_jumlah, getAdapterPosition());
                notifyDataSetChanged();
            });
        }
    }
}

class AdapterProdukFree extends RecyclerView.Adapter<AdapterProdukFree.RecyclerViewViewHolder>{
    private ArrayList<ModelProdukFree> dataList;
    RecyclerViewListener listener;
    private Context context;

    public AdapterProdukFree(ArrayList<ModelProdukFree> dataList, RecyclerViewListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layout = LayoutInflater.from(parent.getContext());
        View view = layout.inflate(R.layout.content_produk_free, parent, false);
        return new AdapterProdukFree.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
        holder.nama.setText(dataList.get(position).getNama());
        holder.qty.setText(String.valueOf(dataList.get(position).getQty()));
        holder.plus_minus_values.setVisibility(View.GONE);
        Glide.with(context).load(dataList.get(position).getPath_gambar()).centerCrop().placeholder(R.drawable.tshirt).into(holder.img_tambah_barang);
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nama, qty;
        ImageView img_tambah_barang;
        FlexboxLayout plus_minus_values;
        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.name_product);
            qty = itemView.findViewById(R.id.jumlah_pcs);
            img_tambah_barang = itemView.findViewById(R.id.img_free_produk);
            plus_minus_values = itemView.findViewById(R.id.plus_minus_values);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
}

class ModelProdukFree {
    String nama, path_gambar, kode_br;
    KategoriFreeProduk kategori;
    int qty;

    public ModelProdukFree(String nama, String path_gambar, String kode_br, int qty, KategoriFreeProduk kategori) {
        this.nama = nama;
        this.path_gambar = path_gambar;
        this.kode_br = kode_br;
        this.qty = qty;
        this.kategori = kategori;
    }

    public KategoriFreeProduk getKategori() {
        return kategori;
    }

    public void setKategori(KategoriFreeProduk kategori) {
        this.kategori = kategori;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPath_gambar() {
        return path_gambar;
    }

    public void setPath_gambar(String path_gambar) {
        this.path_gambar = path_gambar;
    }

    public String getKode_br() {
        return kode_br;
    }

    public void setKode_br(String kode_br) {
        this.kode_br = kode_br;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}

class ModelTambahProduk {
    String nama, gambar, kode_br;
    int harga_asli, qty, stok;

    public JSONObject getAllData(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("nama", getNama());
            obj.put("gambar", getGambar());
            obj.put("kode_br", getKode_br());
            obj.put("harga", getHarga_asli());
            obj.put("stok", getStok());
            obj.put("qty", getQty());
            obj.put("diskon", getDiskon().getAllData());
        } catch (Exception e){

        }

        return obj;
    }

    ModelBarangDiskon diskon;

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public ModelTambahProduk(String nama, String gambar, String kode_br, int harga_asli, int qty, int stok) {
        this.nama = nama;
        this.gambar = gambar;
        this.kode_br = kode_br;
        this.harga_asli = harga_asli;
        this.qty = qty;
        this.stok = stok;
    }

    public ModelTambahProduk(String nama, String gambar, String kode_br, int harga_asli, int qty, int stok, ModelBarangDiskon diskon) {
        this.nama = nama;
        this.gambar = gambar;
        this.kode_br = kode_br;
        this.harga_asli = harga_asli;
        this.diskon = diskon;
        this.qty = qty;
        this.stok = stok;
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

    public String getKode_br() {
        return kode_br;
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

    public ModelBarangDiskon getDiskon() {
        return diskon;
    }

    public void setDiskon(ModelBarangDiskon diskon) {
        this.diskon = diskon;
    }
}

class ModelBarangDiskon {
    KategoriDiskon kategori;
    KategoriFreeProduk free_produk;
    int buy, free, nominal;

    ArrayList<DiskonFreeProduk> diskonFreeProduks;

    public JSONObject getAllData(){
        JSONObject obj = new JSONObject();
        try {
            if(getKategori() == KategoriDiskon.NOMINAL){
                obj.put("jenis_diskon", getKategori() == KategoriDiskon.NOMINAL ? "nominal" : "");
                obj.put("nominal", getNominal());
            } else if(getKategori() == KategoriDiskon.PERSEN){
                obj.put("jenis_diskon", getKategori() == KategoriDiskon.PERSEN ? "persen" : "");
                obj.put("nominal", getNominal());
            } else if(getKategori() == KategoriDiskon.FREE_PRODUK){
                obj.put("jenis_diskon", getKategori() == KategoriDiskon.FREE_PRODUK ? "free" : "");
                if(getFree_produk() == KategoriFreeProduk.BEBAS){
                    obj.put("free_produk", "bebas");
                } else {
                    obj.put("free_produk", "sama");
                }

                JSONArray arr = new JSONArray();
                for (int i = 0; i < getDiskonFreeProduks().size(); i++){
                    arr.put(getDiskonFreeProduks().get(i).getAllData());
                }

                obj.put("detail_free_produk", arr);
                obj.put("buy", getBuy());
                obj.put("free", getFree());

            }
        } catch (Exception e){

        }
        return obj;
    }

    public ArrayList<DiskonFreeProduk> getDiskonFreeProduks() {
        return diskonFreeProduks;
    }

    public void setDiskonFreeProduks(ArrayList<DiskonFreeProduk> diskonFreeProduks) {
        this.diskonFreeProduks = diskonFreeProduks;
    }

    public ModelBarangDiskon(KategoriDiskon kategori) {
        this.kategori = kategori;
    }

    public KategoriDiskon getKategori() {
        return kategori;
    }

    public KategoriFreeProduk getFree_produk() {
        return free_produk;
    }

    public void setFree_produk(KategoriFreeProduk free_produk) {
        this.free_produk = free_produk;
    }

    public void setKategori(KategoriDiskon kategori) {
        this.kategori = kategori;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public int getBuy() {
        return buy;
    }

    public void setBuy(int buy) {
        this.buy = buy;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }
}


