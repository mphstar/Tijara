package com.tijara;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class ActivityRetur extends AppCompatActivity implements RecyclerViewListener {

    EditText field_search;
    ArrayList<DetailBarangTransaksi> listData;
    RecyclerView recView;
    LinearLayout layout_no_value;
    LinearLayout loading;
    FrameLayout btn_scan;
    SwipeRefreshLayout refreshLayout;
    View nokoneksi;
    Button coba_lagi;
    CustomDialogSetup mDialog;

    private void setupDialog(CustomDialog type){
        mDialog = new CustomDialogSetup(this, R.style.dialog, type);
    }

    private void loadDetailTransaksi(String keyword){
        nokoneksi.setVisibility(View.GONE);
        layout_no_value.setVisibility(View.GONE);
        recView.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, Env.BASE_URL + "detail_transaksi/" + URLEncoder.encode(keyword) + "?apikey=" + Env.API_KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    if(res.getString("status").equals("success")){
                        listData = new ArrayList<>();
                        JSONObject data = new JSONObject(res.getString("data"));
                        JSONArray detail = new JSONArray(data.getString("detail_transaksi"));
                        JSONObject mapping_detail;
                        for (int i = 0; i < detail.length(); i++){
                            mapping_detail = detail.getJSONObject(i);

                            JSONObject barang = new JSONObject(mapping_detail.getString("barang"));
                            int hitungDiskon = Integer.valueOf(barang.getString("harga"));
                            if(mapping_detail.getString("detail_diskon_transaksi").equals("null")){
                                hitungDiskon = Integer.valueOf(barang.getString("harga"));
                            } else {
                                JSONObject mapdiskon = new JSONObject(mapping_detail.getString("detail_diskon_transaksi"));
                                if(mapdiskon.getString("kategori").equals("nominal")){
                                    hitungDiskon -= Integer.valueOf(mapdiskon.getString("nominal"));
                                } else if(mapdiskon.getString("kategori").equals("persen")){
                                    hitungDiskon = hitungDiskon - (hitungDiskon / Integer.valueOf(mapdiskon.getString("nominal")));
                                }
                            }

                            listData.add(new DetailBarangTransaksi(barang.getString("nama_br"), mapping_detail.getString("kode_br"), Integer.valueOf(mapping_detail.getString("QTY")), hitungDiskon, mapping_detail.getString("kode_tr")));
                        }

                        DetailBarangAdapter adapt = new DetailBarangAdapter(listData, ActivityRetur.this);
                        recView.setLayoutManager(new LinearLayoutManager(ActivityRetur.this));
                        recView.setAdapter(adapt);

                        loading.setVisibility(View.GONE);
                        layout_no_value.setVisibility(View.GONE);
                        recView.setVisibility(View.VISIBLE);

                    } else if(res.getString("status").equals("error")) {
                        recView.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                        layout_no_value.setVisibility(View.VISIBLE);
//                        Toast.makeText(ActivityRetur.this, res.getString("message"), Toast.LENGTH_LONG).show();
                    }

                    queue.getCache().clear();
                } catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.VolleyError error) {
                loading.setVisibility(View.GONE);
                layout_no_value.setVisibility(View.GONE);
                recView.setVisibility(View.GONE);
                nokoneksi.setVisibility(View.VISIBLE);
//                Toast.makeText(ActivityRetur.this, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retur);
        recView = findViewById(R.id.detail_barang_pesanan);
        layout_no_value = findViewById(R.id.image_no_value);
        field_search = findViewById(R.id.field_search);
        loading = findViewById(R.id.loading);
        btn_scan = findViewById(R.id.btn_scan);
        refreshLayout = findViewById(R.id.refreshLayout);
        nokoneksi = findViewById(R.id.nokoneksi);
        coba_lagi = nokoneksi.findViewById(R.id.coba_lagi);
        coba_lagi.setOnClickListener(v -> {
            loadDetailTransaksi(field_search.getText().toString());
        });
        btn_scan.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(ActivityRetur.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityRetur.this, android.Manifest.permission.CAMERA)) {
                    scanBarcode();
                } else {
                    ActivityCompat.requestPermissions(ActivityRetur.this, new String[]{Manifest.permission.CAMERA}, 0);
                }
            } else {
                scanBarcode();
            }
        });

        field_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() != 0){
                    layout_no_value.setVisibility(View.GONE);
                    recView.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);

                    loadDetailTransaksi(editable.toString());
                } else {
                    layout_no_value.setVisibility(View.GONE);
                    recView.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);
                    nokoneksi.setVisibility(View.GONE);
                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadDetailTransaksi(field_search.getText().toString());

                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void scanBarcode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan kode transaksi");
        options.setBeepEnabled(true);
        options.setCaptureActivity(ScanBarcodeActivity.class);
        options.setOrientationLocked(true);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            field_search.setText(result.getContents());
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
    public void onClick(View view, int position) {
        Intent inten = new Intent(ActivityRetur.this, ActivityDetailRetur.class);
        inten.putExtra("barang", listData.get(position).getAllData().toString());
        startActivity(inten);
    }
}

class DetailBarangAdapter extends RecyclerView.Adapter<DetailBarangAdapter.RecyclerViewViewHolder>{
    private ArrayList<DetailBarangTransaksi> dataList;
    RecyclerViewListener listener;
    private Context context;

    public DetailBarangAdapter(ArrayList<DetailBarangTransaksi> dataList, RecyclerViewListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layout = LayoutInflater.from(parent.getContext());
        View view = layout.inflate(R.layout.activity_content_list_return, parent, false);
        return new DetailBarangAdapter.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
        holder.nama.setText(dataList.get(position).getNama());
        holder.detail.setText(dataList.get(position).getQty() + " X " + Env.formatRupiah(dataList.get(position).getHarga()));
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nama, detail;
        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.txt_nama_product);
            detail = itemView.findViewById(R.id.txt_detail_barang);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
}

class DetailBarangTransaksi {
    String nama, kode_br, kode_tr;
    int qty, harga;

    public DetailBarangTransaksi(String nama, String kode_br, int qty, int harga, String kode_tr) {
        this.nama = nama;
        this.kode_br = kode_br;
        this.qty = qty;
        this.harga = harga;
        this.kode_tr = kode_tr;
    }

    public String getKode_tr() {
        return kode_tr;
    }

    public void setKode_tr(String kode_tr) {
        this.kode_tr = kode_tr;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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

    public  JSONObject getAllData(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("nama", getNama());
            obj.put("kode", getKode_br());
            obj.put("kode_tr", getKode_tr());
            obj.put("qty", String.valueOf(getQty()));
            obj.put("harga", String.valueOf(getHarga()));

        } catch (Exception e){

        }

        return obj;
    }
}