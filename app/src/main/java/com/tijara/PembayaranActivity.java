package com.tijara;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import android.Manifest;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.tijara.async.AsyncBluetoothEscPosPrint;
import com.tijara.async.AsyncEscPosPrint;
import com.tijara.async.AsyncEscPosPrinter;
import com.tijara.keranjang.DataKeranjang;
import com.tijara.keranjang.DiskonFreeProduk;
import com.tijara.keranjang.KategoriDiskon;
import com.tijara.keranjang.ModelKeranjang;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PembayaranActivity extends AppCompatActivity {

    public interface OnBluetoothPermissionsGranted {
        void onPermissionsGranted();
    }


    /**
     * Asynchronous printing
     */
    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection, JSONObject data) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss");
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 302, 76f, 48);
        StringBuilder s = new StringBuilder();
        s.append("\n");
        s.append("\n");
        s.append("[C]<b><font size='big'>TIJARA STORE</font></b>\n");
        s.append(""+ manipulationAlignCenter("Alamat: Jl. Kaca Terbang No.55, Jember Utara, Garahan, Kec. Silo, Kabupaten Jember, Jawa Timur 68117") +"\n\n");
        s.append("================================================\n");
        s.append("[L]Tanggal: " + format.format(new Date()) + "[R]Kasir: " + Preferences.getLoggedInUser(PembayaranActivity.this) + "\n");
        s.append("================================================\n");
        s.append("[L]<b>Nama</b>[R]<b>QTY</b>[R]<b>Subtotal</b>[R]Total\n\n");
        for (int i = 0; i < DataKeranjang.dataKeranjang.size(); i++){
            s.append("[L]"+manipulationWidth(DataKeranjang.dataKeranjang.get(i).getNama())+"[R]"+DataKeranjang.dataKeranjang.get(i).getQty()+"[R]"+Env.formatRupiah(DataKeranjang.dataKeranjang.get(i).getHarga())+"[R]"+DataKeranjang.dataKeranjang.get(i).getHarga() * DataKeranjang.dataKeranjang.get(i).getQty()+"\n");
            if(DataKeranjang.dataKeranjang.get(i).getDiskon() != null){
                switch (DataKeranjang.dataKeranjang.get(i).getDiskon().getKategori()){
                    case NOMINAL:
                        s.append("[L]<b>Diskon</b>[R]<b>"+Env.formatRupiah(Integer.parseInt(DataKeranjang.dataKeranjang.get(i).getDiskon().getNominal()))+"</b>[R]<b>"+Env.formatRupiah(Integer.parseInt(DataKeranjang.dataKeranjang.get(i).getDiskon().getNominal()))+"</b>[R]<b>"+Env.formatRupiah(DataKeranjang.dataKeranjang.get(i).getQty() * Integer.parseInt(DataKeranjang.dataKeranjang.get(i).getDiskon().getNominal()))+"</b>\n");
                        break;
                    case PERSEN:
                        s.append("[L]<b>Diskon</b>[R]<b>"+Integer.parseInt(DataKeranjang.dataKeranjang.get(i).getDiskon().getNominal()) + "%" + "</b>[R]<b>"+Env.formatRupiah((DataKeranjang.dataKeranjang.get(i).getHarga() / 100 * Integer.parseInt(DataKeranjang.dataKeranjang.get(i).getDiskon().getNominal())))+"</b>[R]<b>"+Env.formatRupiah(DataKeranjang.dataKeranjang.get(i).getQty() * (DataKeranjang.dataKeranjang.get(i).getHarga() / 100 * Integer.parseInt(DataKeranjang.dataKeranjang.get(i).getDiskon().getNominal())))+"</b>\n");
                        break;
                    case FREE_PRODUK:
                        s.append("[L]<b>Diskon</b>[R]<b>Buy " + DataKeranjang.dataKeranjang.get(i).getDiskon().getBuy() + " Free " + DataKeranjang.dataKeranjang.get(i).getDiskon().getFree() + "</b>[R]\n");
                        break;
                }
            }
        }

        s.append("\n\n");
        s.append("------------------------------------------------\n");
        int total = 0;
        for (int i = 0; i < DataKeranjang.dataKeranjang.size(); i++){
            total += DataKeranjang.dataKeranjang.get(i).getSubtotal();;
        }
        s.append("[L]Total Item[R]"+DataKeranjang.dataKeranjang.size()+"[R] [R]" + Env.formatRupiah(total) + "\n");
        try {
            if(data.getString("voucher").equals("null")){
                s.append("[L]Voucher[R]0\n");
            } else {
                if(data.getString("jenis_voucher").equals("npminal")){
                    s.append("[L]Voucher[R]"+ Env.formatRupiah(Integer.parseInt(data.getString("voucher"))) +"\n");
                } else {
                    s.append("[L]Voucher[R]"+ data.getString("voucher") +"%\n");
                }
            }

//            if(data.getString("voucher") == null){

//            } else {
//                if(data.getString("jenis_voucher").equals("nominal")){
//                    s.append("[L]Voucher[R]" + Env.formatRupiah(Integer.parseInt(data.getString("voucher"))) + "\n");
//                } else {
//                    s.append("[L]Voucher[R]" + data.getString("voucher") + "%\n");
//                }
//            }


            s.append("[L]Total Harga[R]" + Env.formatRupiah(Integer.parseInt(data.getString("total"))) + "\n");
            s.append("[L]Tunai[R]" + Env.formatRupiah(Integer.parseInt(data.getString("bayar"))) + "\n");
            s.append("[L]Kembalian[R]" + Env.formatRupiah(Integer.parseInt(data.getString("kembalian")))  + "\n");
            s.append("================================================\n\n\n");

            s.append("[C]<barcode type='128' width='62' text='below'>" + data.getString("kode_tr") + "</barcode>\n\n");
        } catch (Exception e){

        }

        s.append("[C]Kritik&Saran: mphstar@gmail.com\n");
        s.append("[C]SMS/WA: 081233764580\n\n");

        return printer.addTextToPrint(s.toString());
    }

    private String manipulationWidth(String text){
        StringBuilder sb = new StringBuilder();
        int currentIndex = 0;

        while (currentIndex < text.length()) {
            sb.append(text.substring(currentIndex, Math.min(currentIndex + 18, text.length())));
            currentIndex += 18;
            if (currentIndex < text.length()) {
                sb.append("\n");

                while (currentIndex < text.length() && text.charAt(currentIndex) == ' ') {
                    currentIndex++;
                }
            }
        }

        return sb.toString();
    }

    private String manipulationAlignCenter(String text){
        StringBuilder sb = new StringBuilder();
        int currentIndex = 0;

        sb.append("[C]");

        while (currentIndex < text.length()) {
            sb.append(text.substring(currentIndex, Math.min(currentIndex + 38, text.length())));
            currentIndex += 38;

            if (currentIndex < text.length()) {
                sb.append("\n[C]");
            }
        }

        String formattedString = sb.toString();
        return formattedString;
    }



    ArrayList<ModelKeranjang> dataList;
    RecyclerView recView;
    TextView kurang_bayar, total_akhir, txt_voucher, keterangan_bayar;
    ArrayList<ModelVoucher> voucher;
    EditText field_total_bayar;
    LinearLayout btn_transaksi;

    int total = 0;

    ConstraintLayout button_voucher;
    public OnBluetoothPermissionsGranted onBluetoothPermissionsGranted;

    private BluetoothConnection selectedDevice;
    boolean isBluetoothError = false;

    ImageView buttonQris, buttonCash, iconTunai, iconQris, back_to_view_transaksi;
    TextView teksTunai, teksQris;

    String selectedJenisPembayaran = "cash";

    CustomDialogSetup mDialog;

    private void setupDialog(CustomDialog type){
        mDialog = new CustomDialogSetup(this, R.style.dialog, type);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pembayaran);

        if(!Preferences.getCustomKey(this, "device_address").equals("")){
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if(!adapter.isEnabled()){
                isBluetoothError = true;
            } else {
                BluetoothDevice device = adapter.getRemoteDevice(Preferences.getCustomKey(this, "device_address"));
                BluetoothConnection conn = new BluetoothConnection(device);


                if(conn != null){
                    selectedDevice = conn;
                } else {
                }
            }


        }

        buttonCash = findViewById(R.id.buttonTunai);
        buttonQris = findViewById(R.id.buttonQris);
        teksTunai = findViewById(R.id.teksTunai);
        teksQris = findViewById(R.id.teksQris);
        iconTunai = findViewById(R.id.iconTunai);
        back_to_view_transaksi = findViewById(R.id.back_to_view_transaksi);
        iconQris = findViewById(R.id.iconQris);
        recView = findViewById(R.id.rincian_barang);
        kurang_bayar = findViewById(R.id.kurang_bayar);
        keterangan_bayar = findViewById(R.id.keterangan_bayar);
        total_akhir = findViewById(R.id.total_akhir);
        txt_voucher = findViewById(R.id.txt_voucher);
        button_voucher = findViewById(R.id.button_voucher);
        field_total_bayar = findViewById(R.id.field_total_bayar);
        btn_transaksi = findViewById(R.id.btn_transaksi);
        back_to_view_transaksi.setOnClickListener(view -> {
            finish();
        });

        field_total_bayar.addTextChangedListener(new RupiahBayarTunai(field_total_bayar));

        button_voucher.setOnClickListener(view -> {
            Intent inten = new Intent(PembayaranActivity.this, PilihVoucherActivity.class);
            startActivityForResult(inten, 1);
        });

        loadData();

        btn_transaksi.setOnClickListener(view -> submitTransaksi());

        buttonCash.setOnClickListener(view -> {
            selectedJenisPembayaran = "cash";
            buttonQris.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            buttonCash.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlack)));
            teksTunai.setTextColor(Color.parseColor("#ffffffff"));
            teksQris.setTextColor(Color.parseColor("#000000"));
            iconQris.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlack)));
            iconTunai.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

            field_total_bayar.setVisibility(View.VISIBLE);
            if(field_total_bayar.getText().toString().isEmpty()){
                hitungKembalian(0);
            } else {
                hitungKembalian(Integer.parseInt(field_total_bayar.getText().toString().replaceAll("[Rp,.]", "")));
            }

        });

        buttonQris.setOnClickListener(view -> {
            selectedJenisPembayaran = "qris";
            buttonQris.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlack)));
            buttonCash.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            teksTunai.setTextColor(Color.parseColor("#000000"));
            teksQris.setTextColor(Color.parseColor("#ffffffff"));
            iconQris.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            iconTunai.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlack)));

            field_total_bayar.setVisibility(View.INVISIBLE);
            keterangan_bayar.setText("Pembayaran: ");
            kurang_bayar.setText("Lunas");
            kurang_bayar.setTextColor(Color.parseColor("#176B00"));

            hitungTotal();
        });
    }

    private void submitTransaksi(){

        if(selectedJenisPembayaran.equals("qris")){

            setupDialog(CustomDialog.CONFIRMATION);
            mDialog.setJudul("Konfirmasi");
            mDialog.setDeskripsi("Apakah anda yakin ingin melakukan transaksi");
            mDialog.setListenerTidak(v -> {
                mDialog.dismiss();
            });
            mDialog.setListenerOK(v -> {
                mDialog.dismiss();
                setupDialog(CustomDialog.LOADING);
                mDialog.setJudul("Loading");
                mDialog.setDeskripsi("Sedang melakukan proses transaksi");
                mDialog.show();

                prosesTransaksi();
            });
            mDialog.show();


        } else {
            if(field_total_bayar.getText().toString().isEmpty()){
                setupDialog(CustomDialog.WARNING);
                mDialog.setJudul("Informasi");
                mDialog.setDeskripsi("Field pembayaran tidak boleh kosong");
                mDialog.setListenerOK(v -> {
                    mDialog.dismiss();
                });
                mDialog.show();

            } else {
                if(keterangan_bayar.getText().toString().contains("Kurang")){
                    setupDialog(CustomDialog.WARNING);
                    mDialog.setJudul("Informasi");
                    mDialog.setDeskripsi("Field bayar harus lebih dari total harga");
                    mDialog.setListenerOK(v -> {
                        mDialog.dismiss();
                    });
                    mDialog.show();

                } else {
                    setupDialog(CustomDialog.CONFIRMATION);
                    mDialog.setJudul("Konfirmasi");
                    mDialog.setDeskripsi("Apakah anda yakin ingin melakukan transaksi");
                    mDialog.setListenerTidak(v -> {
                        mDialog.dismiss();
                    });
                    mDialog.setListenerOK(v -> {
                        mDialog.dismiss();
                        setupDialog(CustomDialog.LOADING);
                        mDialog.setJudul("Loading");
                        mDialog.setDeskripsi("Sedang melakukan proses transaksi");
                        mDialog.show();

                        prosesTransaksi();
                    });
                    mDialog.show();
                }

            }
        }

    }


    private void checkBluetoothPermissions(OnBluetoothPermissionsGranted onBluetoothPermissionsGranted) {
        this.onBluetoothPermissionsGranted = onBluetoothPermissionsGranted;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, Home.PERMISSION_BLUETOOTH);
        } else if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, Home.PERMISSION_BLUETOOTH_ADMIN);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, Home.PERMISSION_BLUETOOTH_CONNECT);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, Home.PERMISSION_BLUETOOTH_SCAN);
        } else {
            this.onBluetoothPermissionsGranted.onPermissionsGranted();
        }
    }


    private void prosesTransaksi(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Env.BASE_URL + "transaksi", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                dataList = new ArrayList<>();
                try {
                    JSONObject res = new JSONObject(response);
                    NotificationHelper.showNotification(PembayaranActivity.this, "Tijara Store", "Transaksi Berhasil");

                    mDialog.dismiss();
                    if(res.getString("status").equals("success")){
                        JSONObject data = res.getJSONObject("data");
                        setupDialog(CustomDialog.SUCCESS);
                        mDialog.setJudul("Berhasil");
                        mDialog.setDeskripsi(res.getString("message"));
                        mDialog.setCancelable(false);
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.setListenerOK(v -> {
                            mDialog.dismiss();

                            PembayaranActivity.this.checkBluetoothPermissions(() -> {
                                new AsyncBluetoothEscPosPrint(
                                        PembayaranActivity.this,
                                        new AsyncEscPosPrint.OnPrintFinished() {
                                            @Override
                                            public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                                                setupDialog(CustomDialog.ERROR);
                                                mDialog.setJudul("Gagal");
                                                mDialog.setDeskripsi("Gagal melakukan print struk");
                                                mDialog.setListenerOK(v -> {
                                                    mDialog.dismiss();
                                                    DataKeranjang.dataKeranjang = new ArrayList<>();

                                                    Intent intent = new Intent(PembayaranActivity.this, Home.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                });
                                                mDialog.show();

                                            }

                                            @Override
                                            public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                                                setupDialog(CustomDialog.SUCCESS);
                                                mDialog.setJudul("Berhasil");
                                                mDialog.setDeskripsi("Print struk berhasil");
                                                mDialog.setCancelable(false);
                                                mDialog.setCanceledOnTouchOutside(false);
                                                mDialog.setListenerOK(v -> {
                                                    mDialog.dismiss();
                                                    DataKeranjang.dataKeranjang = new ArrayList<>();

                                                    Intent intent = new Intent(PembayaranActivity.this, Home.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                });
                                                mDialog.show();

                                            }
                                        }
                                )
                                        .execute(PembayaranActivity.this.getAsyncEscPosPrinter(selectedDevice, data));
                            });
                        });
                        mDialog.show();

                    } else {
                        setupDialog(CustomDialog.ERROR);
                        mDialog.setJudul("Gagal");
                        mDialog.setDeskripsi(res.getString("message"));
                        mDialog.setListenerOK(v -> {
                            mDialog.dismiss();
                        });
                        mDialog.show();

                    }

//                    Toast.makeText(PembayaranActivity.this, res.getString("message"), Toast.LENGTH_SHORT).show();

                    queue.getCache().clear();
                } catch (Exception e){
                    mDialog.dismiss();
                    Toast.makeText(PembayaranActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.VolleyError error) {
                mDialog.dismiss();
                Toast.makeText(PembayaranActivity.this, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("apikey", Env.API_KEY);
                data.put("nama_kasir", Preferences.getLoggedInUser(PembayaranActivity.this));

                if(selectedJenisPembayaran.equals("qris")){
                    data.put("jenis_pembayaran", "QRIS");
                    data.put("bayar", String.valueOf(total));
                    data.put("kembalian", String.valueOf(0));
                } else {
                    data.put("jenis_pembayaran", "Cash");
                    data.put("bayar", String.valueOf(field_total_bayar.getText().toString().replaceAll("[Rp,.]", "")));
                    data.put("kembalian", String.valueOf(kurang_bayar.getText().toString().replaceAll("[Rp,.]", "")));
                }

                data.put("total", String.valueOf(total));

                if(voucher != null){
                    for (int k = 0; k < voucher.size(); k++){
                        data.put("voucher", String.valueOf(voucher.get(k).getVoucher_nominal()));
                        data.put("jenis_voucher", voucher.get(k).getKategori_voucher());
                    }
                }
                JSONArray arr = new JSONArray();
                for (int i = 0; i < DataKeranjang.dataKeranjang.size(); i++){
                    arr.put(DataKeranjang.dataKeranjang.get(i).getAllData());
                }
                data.put("detail_transaksi", arr.toString());
                return data;
            }
        };

        queue.add(request);
    }

    private void hitungKembalian(int field){
        if(field < total){
            keterangan_bayar.setText("Kurang Bayar: ");
            int nilai = total - field;
            kurang_bayar.setText("Rp. " + Env.formatRupiah(nilai));
            kurang_bayar.setTextColor(Color.parseColor("#6B0000"));
        } else {
            keterangan_bayar.setText("Kembalian: ");
            int nilai = field - total;
            kurang_bayar.setText("Rp. " + Env.formatRupiah(nilai));
            kurang_bayar.setTextColor(Color.parseColor("#176B00"));
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

            if(!s.toString().isEmpty()){
                editText.removeTextChangedListener(this);

                String originalString = s.toString();

                // Menghapus tanda titik dan koma
                String cleanString = originalString.replaceAll("[Rp,.]", "");

                try {
                    // Parsing angka
                    int parsed = Integer.valueOf(cleanString);

                    hitungKembalian(parsed);

                    // Format angka ke Rupiah
                    String formattedString = Env.formatRupiah(parsed);

                    // Setel kembali teks pada EditText
                    editText.setText(formattedString);
                    editText.setSelection(formattedString.length());
                } catch (NumberFormatException e) {
                    // Tangani jika terjadi kesalahan parsing
                }

                editText.addTextChangedListener(this);
            } else {
                keterangan_bayar.setText("Kurang Bayar: ");
                kurang_bayar.setTextColor(Color.parseColor("#6B0000"));
                kurang_bayar.setText("Rp. 0");
            }


        }
    }

    private void loadData(){
        dataList = new ArrayList<>();
        for (int i = 0; i < DataKeranjang.dataKeranjang.size(); i++){
            dataList.add(DataKeranjang.dataKeranjang.get(i));
        }

        AdapterPembayaran adapt = new AdapterPembayaran(dataList);
        recView.setLayoutManager(new LinearLayoutManager(PembayaranActivity.this));
        recView.setAdapter(adapt);


        hitungTotal();
    }

    private void hitungTotal(){
        total = 0;

        for (int i = 0; i < DataKeranjang.dataKeranjang.size(); i++){
            total += DataKeranjang.dataKeranjang.get(i).getSubtotal();
        }

        if(voucher != null){
            for (int i = 0; i < voucher.size(); i++){
                if(voucher.get(i).getKategori_voucher().equals("nominal")){
                    total -= voucher.get(i).getVoucher_nominal();

                    txt_voucher.setText("Rp. " + Env.formatRupiah(voucher.get(i).getVoucher_nominal()));
                    txt_voucher.setTextColor(Color.parseColor("#176B00"));
                } else {
                    total -= total / 100 * voucher.get(i).getVoucher_nominal();

                    txt_voucher.setText(voucher.get(i).getVoucher_nominal() + "%");
                    txt_voucher.setTextColor(Color.parseColor("#176B00"));
                }
            }
        }

        total_akhir.setText("Rp. " + Env.formatRupiah(total));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String getdata = data.getStringExtra("data_voucher");
                try {
                    JSONObject obj = new JSONObject(getdata);
                    voucher = new ArrayList<>();
                    voucher.add(new ModelVoucher(obj.getInt("nominal"), obj.getString("kategori")));
                    hitungTotal();
                    if(!field_total_bayar.getText().toString().isEmpty()){
                        String cleanString = field_total_bayar.getText().toString().replaceAll("[Rp,.]", "");
                        hitungKembalian(Integer.parseInt(cleanString));
                    }
                } catch (Exception e){
                    Toast.makeText(this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}


class AdapterPembayaran extends RecyclerView.Adapter<AdapterPembayaran.RecyclerViewViewHolder>{
    private ArrayList<ModelKeranjang> dataList;
    ArrayList<DiskonFreeProduk> listFree;
    private Context context;

    public AdapterPembayaran(ArrayList<ModelKeranjang> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layout = LayoutInflater.from(parent.getContext());
        View view = layout.inflate(R.layout.content_rincian_barang, parent, false);
        return new AdapterPembayaran.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {

        int qty = dataList.get(position).getQty();
        int harga_asli = dataList.get(position).getHarga();
        int subtotal = dataList.get(position).getSubtotal();

        holder.barang_pesan.setText(dataList.get(position).getNama());

        holder.jum_barang_pesan.setText(String.valueOf(qty));




        if(dataList.get(position).getDiskon() == null){
            holder.harga_barang_pesan.setText(Env.formatRupiah(harga_asli * qty));

            holder.total_harga_barang_pesan.setText(Env.formatRupiah(dataList.get(position).getHarga()));
            holder.kalkulasi_diskon.setVisibility(View.GONE);
            holder.rincian_barang_free.setVisibility(View.GONE);
        } else {

            holder.kalkulasi_diskon.setVisibility(View.VISIBLE);
            holder.rincian_barang_free.setVisibility(View.GONE);

            if(dataList.get(position).getDiskon().getKategori() == KategoriDiskon.NOMINAL){

                int diskon = Integer.parseInt(dataList.get(position).getDiskon().getNominal());
                holder.harga_barang_pesan.setText(Env.formatRupiah((harga_asli - diskon) * qty));

                holder.total_harga_barang_pesan.setText(Env.formatRupiah(harga_asli));
                holder.potongan_diskon.setText("Rp. " + Env.formatRupiah(diskon * qty));
            } else if(dataList.get(position).getDiskon().getKategori() == KategoriDiskon.PERSEN){
                int diskon = Integer.parseInt(dataList.get(position).getDiskon().getNominal());
                holder.harga_barang_pesan.setText(Env.formatRupiah((harga_asli - (harga_asli / 100 * diskon)) * qty));

                holder.total_harga_barang_pesan.setText(Env.formatRupiah(harga_asli));
                holder.potongan_diskon.setText(diskon + "% / Produk");

            } else if(dataList.get(position).getDiskon().getKategori() == KategoriDiskon.FREE_PRODUK){
                holder.harga_barang_pesan.setText(Env.formatRupiah(harga_asli * qty));


                holder.total_harga_barang_pesan.setText(Env.formatRupiah(harga_asli));
                holder.kalkulasi_diskon.setVisibility(View.GONE);
                holder.rincian_barang_free.setVisibility(View.VISIBLE);

                holder.rincian_barang_free.setLayoutManager(new LinearLayoutManager(context));
                listFree = new ArrayList<>();
                for(int k = 0; k < dataList.get(position).getDiskon().getDetail_free_produk().size(); k++){
                    listFree.add(dataList.get(position).getDiskon().getDetail_free_produk().get(k));
                }
                AdapterFreeProduk adapt = new AdapterFreeProduk(listFree);
                holder.rincian_barang_free.setAdapter(adapt);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder  {
        TextView barang_pesan, harga_barang_pesan, jum_barang_pesan, total_harga_barang_pesan, potongan_diskon;
        LinearLayout kalkulasi_diskon;

        RecyclerView rincian_barang_free;

        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            barang_pesan = itemView.findViewById(R.id.barang_pesan);
            harga_barang_pesan = itemView.findViewById(R.id.harga_barang_pesan);
            jum_barang_pesan = itemView.findViewById(R.id.jum_barang_pesan);
            total_harga_barang_pesan = itemView.findViewById(R.id.total_harga_barang_pesan);
            kalkulasi_diskon = itemView.findViewById(R.id.kalkulasi_diskon);
            rincian_barang_free = itemView.findViewById(R.id.rincian_barang_free);
            potongan_diskon = itemView.findViewById(R.id.potongan_diskon);

        }

    }
}


class AdapterFreeProduk extends RecyclerView.Adapter<AdapterFreeProduk.RecyclerViewViewHolder>{
    private ArrayList<DiskonFreeProduk> dataList;
    private Context context;

    public AdapterFreeProduk(ArrayList<DiskonFreeProduk> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layout = LayoutInflater.from(parent.getContext());
        View view = layout.inflate(R.layout.content_rincian_barang_free, parent, false);
        return new AdapterFreeProduk.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
        holder.jum_barang_free.setText(String.valueOf(dataList.get(position).getQty()));
        holder.nama_barang_free.setText(dataList.get(position).getNama());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        TextView jum_barang_free, nama_barang_free;

        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            jum_barang_free = itemView.findViewById(R.id.jum_barang_free);
            nama_barang_free = itemView.findViewById(R.id.nama_barang_free);
        }

    }
}
