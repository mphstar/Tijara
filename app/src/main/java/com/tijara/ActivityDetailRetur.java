package com.tijara;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.flexbox.FlexboxLayout;
import com.tijara.keranjang.DataKeranjang;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ActivityDetailRetur extends AppCompatActivity {
    String[] jenis_kembalian = {"Tunai", "Produk"};
    int selectedJenis = 0;
    TextView field_jenis_kembalian, btn_pilih_produk, field_produk, txt_info_jumlah, field_kembalian_tunai, txt_info_kurang_bayar;
    EditText field_jumlah_barang_retur, field_bayar_kurang, field_bayar_tunai;

    FlexboxLayout layout_bayar_kurang, layout_kembalian_tunai, layout_bayar_tunai;
    LinearLayout layout_pilih_produk, btn_submit;
    int qty;
    JSONObject myData;
    JSONObject productReturSelected;

    String opsiReturProduk = "";
    int hitungTunaiDariQty = 1;

    CustomDialogSetup mDialog;

    private void setupDialog(CustomDialog type){
        mDialog = new CustomDialogSetup(this, R.style.dialog, type);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rincian_barang_return);
        field_jenis_kembalian = findViewById(R.id.field_jenis_kembalian);
        field_jumlah_barang_retur = findViewById(R.id.field_jumlah_barang_retur);
        layout_bayar_kurang = findViewById(R.id.layout_bayar_kurang);
        layout_kembalian_tunai = findViewById(R.id.layout_kembalian_tunai);
        layout_bayar_tunai = findViewById(R.id.layout_bayar_tunai);
        btn_pilih_produk = findViewById(R.id.btn_pilih_produk);
        field_produk = findViewById(R.id.field_produk);
        layout_pilih_produk = findViewById(R.id.layout_pilih_produk);
        txt_info_jumlah = findViewById(R.id.txt_info_jumlah);
        field_kembalian_tunai = findViewById(R.id.field_kembalian_tunai);
        field_bayar_kurang = findViewById(R.id.field_bayar_kurang);
        txt_info_kurang_bayar = findViewById(R.id.txt_info_kurang_bayar);
        field_bayar_tunai = findViewById(R.id.field_bayar_tunai);
        btn_submit = findViewById(R.id.btn_submit);

        String mystring = getIntent().getStringExtra("barang");
        try {
            myData = new JSONObject(mystring);
            qty = Integer.valueOf(myData.getString("qty"));
            txt_info_jumlah.setText("Maksimal input jumlah produk retur: " + qty);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        field_jenis_kembalian.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDetailRetur.this);
            builder.setTitle("Jenis Pengembalian");
            builder.setSingleChoiceItems(jenis_kembalian, selectedJenis, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    selectedJenis = i;
                    dialogInterface.dismiss();
                    field_jenis_kembalian.setText(jenis_kembalian[i].toString());
                    if(selectedJenis == 0){
                        layout_pilih_produk.setVisibility(View.GONE);
                        layout_bayar_kurang.setVisibility(View.GONE);
                        layout_kembalian_tunai.setVisibility(View.GONE);
                        layout_bayar_tunai.setVisibility(View.VISIBLE);
                    } else {
                        layout_pilih_produk.setVisibility(View.VISIBLE);
                        layout_bayar_kurang.setVisibility(View.GONE);
                        layout_kembalian_tunai.setVisibility(View.GONE);
                        layout_bayar_tunai.setVisibility(View.GONE);
                    }
                }
            });
            builder.show();
        });

        btn_pilih_produk.setOnClickListener(view -> {
            Intent inten = new Intent(ActivityDetailRetur.this, PilihBarangActivity.class);
            startActivityForResult(inten, 1);
        });


        field_jumlah_barang_retur.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    int number = Integer.parseInt(charSequence.toString());

                    // Periksa apakah angka berada dalam rentang yang diinginkan
                    if (number < 1 || number > qty) {
                        // Angka di luar rentang, hapus teks terakhir

                        field_jumlah_barang_retur.setText(charSequence.subSequence(0, charSequence.length() - 1));
                        field_jumlah_barang_retur.setSelection(field_jumlah_barang_retur.getText().length());

                    } else {
                        hitungTunaiDariQty = number;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if(opsiReturProduk.equals("")){
                        
                    } else {
                        if(opsiReturProduk.equals("kembalian_tunai")){
                            try {
                                field_kembalian_tunai.setText(Env.formatRupiah((Integer.valueOf(myData.getString("harga")) - Integer.valueOf(productReturSelected.getString("harga"))) * hitungTunaiDariQty));
                            } catch (Exception e) {
//                        throw new RuntimeException(e);
                            }
                            if(editable.toString().equals("")){
                                field_kembalian_tunai.setText("");
                            }
                        } else if(opsiReturProduk.equals("bayar_kurang")){
                            try {
                                txt_info_kurang_bayar.setText("Bayar kurang: " + Env.formatRupiah(((Integer.valueOf(productReturSelected.getString("harga")) - Integer.valueOf(myData.getString("harga"))) * hitungTunaiDariQty)));
                                if((Integer.valueOf(productReturSelected.getString("harga")) - Integer.valueOf(myData.getString("harga"))) * hitungTunaiDariQty > Integer.valueOf(field_bayar_kurang.getText().toString().replace(".", ""))){
                                    txt_info_kurang_bayar.setTextColor(Color.parseColor("#6B0000"));
                                    txt_info_kurang_bayar.setText("Bayar kurang: " + Env.formatRupiah(((Integer.valueOf(productReturSelected.getString("harga")) - Integer.valueOf(myData.getString("harga"))) * hitungTunaiDariQty) - Integer.valueOf(field_bayar_kurang.getText().toString().replace(".", ""))));
                                } else {
                                    txt_info_kurang_bayar.setTextColor(Color.parseColor("#176B00"));
                                    txt_info_kurang_bayar.setText("Kembalian: " + Env.formatRupiah(Integer.valueOf(field_bayar_kurang.getText().toString().replace(".", "")) - ((Integer.valueOf(productReturSelected.getString("harga")) - Integer.valueOf(myData.getString("harga"))) * hitungTunaiDariQty)));
                                }

                            } catch (Exception e) {
//                        throw new RuntimeException(e);
                            }
                            if(editable.toString().equals("")){
                                txt_info_kurang_bayar.setText("");
                                field_bayar_kurang.setText("");
                            }
                        }
                    }

                } catch (Exception e){

                }

            }
        });

        field_bayar_kurang.addTextChangedListener(new RupiahTextWatcher(field_bayar_kurang));
        field_bayar_tunai.addTextChangedListener(new RupiahBayarTunai(field_bayar_tunai));

        btn_submit.setOnClickListener(view -> {
            if(selectedJenis == 0){
                String error = "";
                if(field_jumlah_barang_retur.getText().toString().equals("")){
//                    Toast.makeText(this, "isi dulu cuy", Toast.LENGTH_SHORT).show();
                    error = "Jumlah produk retur wajib diisi";
                } else if(field_bayar_tunai.getText().toString().equals("")){
                    error = "Bayar tunai wajib diisi";
                }

                if(error.equals("")){
                    setupDialog(CustomDialog.CONFIRMATION);
                    mDialog.setJudul("Retur");
                    mDialog.setDeskripsi("Apakah anda yakin ingin melakukan retur");
                    mDialog.setListenerTidak(v -> {
                        mDialog.dismiss();
                    });
                    mDialog.setListenerOK(v -> {
                        mDialog.dismiss();
                        setupDialog(CustomDialog.LOADING);
                        mDialog.setJudul("Loading");
                        mDialog.setDeskripsi("Sedang melakukan proses retur");
                        mDialog.show();

                        submitRetur();
                    });
                    mDialog.show();


                } else {
                    setupDialog(CustomDialog.WARNING);
                    mDialog.setJudul("Informasi");
                    mDialog.setDeskripsi(error);
                    mDialog.setListenerOK(v -> {
                        mDialog.dismiss();
                    });
                    mDialog.show();

                }
            } else {
                if(field_jumlah_barang_retur.getText().toString().equals("")){
                    setupDialog(CustomDialog.WARNING);
                    mDialog.setJudul("Informasi");
                    mDialog.setDeskripsi("Jumlah produk retur wajib diisi");
                    mDialog.setListenerOK(v -> {
                        mDialog.dismiss();
                    });
                    mDialog.show();

                } else {
                    if(field_produk.getText().toString().equals("")){
                        setupDialog(CustomDialog.WARNING);
                        mDialog.setJudul("Informasi");
                        mDialog.setDeskripsi("Pilih produk terlebih dahulu");
                        mDialog.setListenerOK(v -> {
                            mDialog.dismiss();
                        });
                        mDialog.show();
                    } else {
                        if(opsiReturProduk.equals("pas")){
                            setupDialog(CustomDialog.CONFIRMATION);
                            mDialog.setJudul("Retur");
                            mDialog.setDeskripsi("Apakah anda yakin ingin melakukan retur");
                            mDialog.setListenerTidak(v -> {
                                mDialog.dismiss();
                            });
                            mDialog.setListenerOK(v -> {
                                mDialog.dismiss();
                                setupDialog(CustomDialog.LOADING);
                                mDialog.setJudul("Loading");
                                mDialog.setDeskripsi("Sedang melakukan proses retur");
                                mDialog.show();

                                submitRetur();
                            });
                            mDialog.show();
                        } else if(opsiReturProduk.equals("bayar_kurang")){
                            if(field_bayar_kurang.getText().toString().equals("")){
                                setupDialog(CustomDialog.WARNING);
                                mDialog.setJudul("Informasi");
                                mDialog.setDeskripsi("Bayar kurang wajib diisi");
                                mDialog.setListenerOK(v -> {
                                    mDialog.dismiss();
                                });
                                mDialog.show();

                            } else if(txt_info_kurang_bayar.getText().toString().contains("Bayar")){
                                try {
                                    setupDialog(CustomDialog.WARNING);
                                    mDialog.setJudul("Informasi");
                                    mDialog.setDeskripsi("Bayar kurang harus lebih dari " + Env.formatRupiah((Integer.valueOf(productReturSelected.getString("harga")) - Integer.valueOf(myData.getString("harga"))) * hitungTunaiDariQty));
                                    mDialog.setListenerOK(v -> {
                                        mDialog.dismiss();
                                    });
                                    mDialog.show();

                                } catch (JSONException e) {
//                                    throw new RuntimeException(e);
                                }
                            }else {
                                setupDialog(CustomDialog.CONFIRMATION);
                                mDialog.setJudul("Retur");
                                mDialog.setDeskripsi("Apakah anda yakin ingin melakukan retur");
                                mDialog.setListenerTidak(v -> {
                                    mDialog.dismiss();
                                });
                                mDialog.setListenerOK(v -> {
                                    mDialog.dismiss();
                                    setupDialog(CustomDialog.LOADING);
                                    mDialog.setJudul("Loading");
                                    mDialog.setDeskripsi("Sedang melakukan proses retur");
                                    mDialog.show();

                                    submitRetur();
                                });
                                mDialog.show();
                            }
                        } else if(opsiReturProduk.equals("kembalian_tunai")){
                            setupDialog(CustomDialog.CONFIRMATION);
                            mDialog.setJudul("Retur");
                            mDialog.setDeskripsi("Apakah anda yakin ingin melakukan retur");
                            mDialog.setListenerTidak(v -> {
                                mDialog.dismiss();
                            });
                            mDialog.setListenerOK(v -> {
                                mDialog.dismiss();
                                setupDialog(CustomDialog.LOADING);
                                mDialog.setJudul("Loading");
                                mDialog.setDeskripsi("Sedang melakukan proses retur");
                                mDialog.show();

                                submitRetur();
                            });
                            mDialog.show();
                        }
                    }
                }

            }
        });
    }

    private void submitRetur(){
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject input = new JSONObject();
        try {
//            Toast.makeText(ActivityDetailRetur.this, myData.getString("nama"), Toast.LENGTH_SHORT).show();
            input.put("nama_pegawai", "Mphstar");
            input.put("kode_br", myData.getString("kode"));
            input.put("kode_tr", myData.getString("kode_tr"));
            input.put("qty", field_jumlah_barang_retur.getText().toString());
            input.put("jenis_pengembalian", field_jenis_kembalian.getText().toString().toLowerCase());
            if(opsiReturProduk.equals("bayar_kurang")){
                input.put("bayar_kurang", field_bayar_kurang.getText().toString().replace(".", ""));
            } else if(opsiReturProduk.equals("kembalian_tunai")){
                input.put("kembalian_tunai", field_kembalian_tunai.getText().toString().replace(".", ""));
            }
            if(selectedJenis == 0){
                input.put("bayar_tunai", field_bayar_tunai.getText().toString().replace(".", ""));
            } else {
                input.put("kode_br_keluar", productReturSelected.getString("kode_br"));
            }
        } catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Env.BASE_URL + "submit_retur_customer?apikey=" + Env.API_KEY, input, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mDialog.dismiss();
                try {
                    JSONObject res = response;
                    if(res.getString("status").equals("success")){
                        setupDialog(CustomDialog.SUCCESS);
                        mDialog.setJudul("Berhasil");
                        mDialog.setDeskripsi(res.getString("message"));
                        mDialog.setCancelable(false);
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.setListenerOK(v -> {
                            mDialog.dismiss();
                            Intent intent = new Intent(ActivityDetailRetur.this, Home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        });
                        mDialog.show();

                    } else if(res.getString("status").equals("error")) {
                        setupDialog(CustomDialog.ERROR);
                        mDialog.setJudul("Gagal");
                        mDialog.setDeskripsi(res.getString("message"));
                        mDialog.setListenerOK(v -> {
                            mDialog.dismiss();
                        });
                        mDialog.show();
                    }
                    queue.getCache().clear();
                } catch (Exception e){
                    setupDialog(CustomDialog.ERROR);
                    mDialog.setJudul("Gagal");
                    mDialog.setDeskripsi(e.toString());
                    mDialog.setListenerOK(v -> {
                        mDialog.dismiss();
                    });
                    mDialog.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.VolleyError error) {
                Toast.makeText(ActivityDetailRetur.this, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("error", error.toString());
                mDialog.dismiss();
            }
        });

        queue.add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String getdata = data.getStringExtra("data");
                try {
                    productReturSelected = new JSONObject(getdata);
                    if(field_jumlah_barang_retur.getText().toString().equals("")){
                        hitungTunaiDariQty = 1;
                    } else {
                        hitungTunaiDariQty = Integer.valueOf(field_jumlah_barang_retur.getText().toString());
                    }
//                    Toast.makeText(this, productReturSelected.getString("harga"), Toast.LENGTH_SHORT).show();
                    field_produk.setText(productReturSelected.getString("nama_br"));
                    if(Integer.valueOf(myData.getString("harga")) < Integer.valueOf(productReturSelected.getString("harga"))){
                        opsiReturProduk = "bayar_kurang";
                        layout_bayar_kurang.setVisibility(View.VISIBLE);
                        layout_kembalian_tunai.setVisibility(View.GONE);
                        txt_info_kurang_bayar.setTextColor(Color.parseColor("#6B0000"));
                        txt_info_kurang_bayar.setText("Bayar kurang: " + Env.formatRupiah((Integer.valueOf(productReturSelected.getString("harga")) - Integer.valueOf(myData.getString("harga"))) * hitungTunaiDariQty));
                    } else if(Integer.valueOf(myData.getString("harga")) > Integer.valueOf(productReturSelected.getString("harga"))){
                        opsiReturProduk = "kembalian_tunai";
                        layout_bayar_kurang.setVisibility(View.GONE);
                        layout_kembalian_tunai.setVisibility(View.VISIBLE);

                        field_kembalian_tunai.setText(Env.formatRupiah((Integer.valueOf(myData.getString("harga")) - Integer.valueOf(productReturSelected.getString("harga"))) * hitungTunaiDariQty ));
                    } else {
                        opsiReturProduk = "pas";
                        layout_bayar_kurang.setVisibility(View.GONE);
                        layout_kembalian_tunai.setVisibility(View.GONE);
                    }
                } catch (Exception e){
                    Toast.makeText(this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    class RupiahBayarTunai implements TextWatcher {

        private EditText editText;

        public RupiahBayarTunai(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            editText.removeTextChangedListener(this);

            String originalString = s.toString();

            // Menghapus tanda titik dan koma
            String cleanString = originalString.replaceAll("[Rp,.]", "");

            try {
                // Parsing angka
                int parsed = Integer.valueOf(cleanString);

                // Format angka ke Rupiah
                String formattedString = Env.formatRupiah(parsed);

                // Setel kembali teks pada EditText
                editText.setText(formattedString);
                editText.setSelection(formattedString.length());
            } catch (NumberFormatException e) {
                // Tangani jika terjadi kesalahan parsing
            }

            editText.addTextChangedListener(this);
        }
    }

    class RupiahTextWatcher implements TextWatcher {

        private EditText editText;

        public RupiahTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            editText.removeTextChangedListener(this);

            String originalString = s.toString();

            // Menghapus tanda titik dan koma
            String cleanString = originalString.replaceAll("[Rp,.]", "");

            try {
                // Parsing angka
                int parsed = Integer.valueOf(cleanString);
                if(parsed < (Integer.valueOf(Integer.valueOf(productReturSelected.getString("harga")) - Integer.valueOf(myData.getString("harga"))) * hitungTunaiDariQty)){
                    txt_info_kurang_bayar.setTextColor(Color.parseColor("#6B0000"));
                    txt_info_kurang_bayar.setText("Bayar kurang: " + Env.formatRupiah(((Integer.valueOf(Integer.valueOf(productReturSelected.getString("harga")) - Integer.valueOf(myData.getString("harga")))) * hitungTunaiDariQty )  - parsed));
                } else {
                    txt_info_kurang_bayar.setTextColor(Color.parseColor("#176B00"));
                    txt_info_kurang_bayar.setText("Kembalian: " + Env.formatRupiah(parsed - (hitungTunaiDariQty * (Integer.valueOf(Integer.valueOf(productReturSelected.getString("harga")) - Integer.valueOf(myData.getString("harga")))))));
                }

                // Format angka ke Rupiah
                String formattedString = Env.formatRupiah(parsed);

                // Setel kembali teks pada EditText
                editText.setText(formattedString);
                editText.setSelection(formattedString.length());
            } catch (NumberFormatException e) {
                // Tangani jika terjadi kesalahan parsing
            } catch (JSONException e) {
            }

            editText.addTextChangedListener(this);
        }
    }
}
