package com.tijara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class PilihBarangActivity extends AppCompatActivity implements RecyclerViewListener, TextWatcher{

    RecyclerView listbarang;
    ArrayList<ModelBarang> datalist;
    EditText field_kode_product;

    private void loadProduct(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, "http://192.168.100.63:8000/api/product/jual?apikey=" + Env.API_KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                datalist = new ArrayList<>();
                try {
                    JSONArray list_barang = new JSONArray(response);
                    JSONObject mapping_list;
                    for (int i = 0; i < list_barang.length(); i++){
                        mapping_list = list_barang.getJSONObject(i);
                        ModelBarang barang = new ModelBarang(mapping_list.getString("nama_br"), mapping_list.getString("gambar"), mapping_list.getString("kode_br"), Integer.valueOf(mapping_list.getString("harga")));
                        Toast.makeText(PilihBarangActivity.this, mapping_list.getString("diskon"), Toast.LENGTH_SHORT).show();
                        Log.d("tes error", mapping_list.getString("diskon"));
                        try {
                            JSONObject mapping_diskon = new JSONObject(mapping_list.getString("diskon"));
                            if(mapping_diskon.getString("kategori").equals("nominal") || mapping_diskon.getString("kategori").equals("persen")){
                                barang.setDiskon_nomina(new DiskonNominal(mapping_diskon.getString("kategori"), Integer.valueOf(mapping_diskon.getString("nominal"))));
//                                Toast.makeText(PilihBarangActivity.this, mapping_list.getString("nominal"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e){

                        }
                        datalist.add(barang);
                    }

                    BarangAdapter adapt = new BarangAdapter(datalist, PilihBarangActivity.this);
                    listbarang.setLayoutManager(new LinearLayoutManager(PilihBarangActivity.this));
                    listbarang.setAdapter(adapt);

                    queue.getCache().clear();
                } catch (Exception e){
                    Toast.makeText(PilihBarangActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.VolleyError error) {
                Toast.makeText(PilihBarangActivity.this, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    private void loadProductSearch(String keyword){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, "http://192.168.100.63:8000/api/product/jual/" + keyword + "?apikey=" + Env.API_KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                datalist = new ArrayList<>();
                try {
                    JSONArray list_barang = new JSONArray(response);
                    JSONObject mapping_list;
                    for (int i = 0; i < list_barang.length(); i++){
                        mapping_list = list_barang.getJSONObject(i);
                        ModelBarang barang = new ModelBarang(mapping_list.getString("nama_br"), mapping_list.getString("gambar"), mapping_list.getString("kode_br"), Integer.valueOf(mapping_list.getString("harga")));
                        Toast.makeText(PilihBarangActivity.this, mapping_list.getString("diskon"), Toast.LENGTH_SHORT).show();
                        Log.d("tes error", mapping_list.getString("diskon"));
                        try {
                            JSONObject mapping_diskon = new JSONObject(mapping_list.getString("diskon"));
                            if(mapping_diskon.getString("kategori").equals("nominal") || mapping_diskon.getString("kategori").equals("persen")){
                                barang.setDiskon_nomina(new DiskonNominal(mapping_diskon.getString("kategori"), Integer.valueOf(mapping_diskon.getString("nominal"))));
//                                Toast.makeText(PilihBarangActivity.this, mapping_list.getString("nominal"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e){
                            Toast.makeText(PilihBarangActivity.this, "Error JSON " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        datalist.add(barang);
                    }

                    BarangAdapter adapt = new BarangAdapter(datalist, PilihBarangActivity.this);
                    listbarang.setLayoutManager(new LinearLayoutManager(PilihBarangActivity.this));
                    listbarang.setAdapter(adapt);

                    queue.getCache().clear();
                } catch (Exception e){
                    Toast.makeText(PilihBarangActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.VolleyError error) {
                Toast.makeText(PilihBarangActivity.this, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_barang);
        listbarang = findViewById(R.id.list_barang);
        field_kode_product = findViewById(R.id.field_kode_product);
        field_kode_product.addTextChangedListener(PilihBarangActivity.this);
        loadProduct();
    }

    @Override
    public void onClick(View view, int position) {
        Intent inten = new Intent();
        JSONObject data = new JSONObject();
        try {
            data.put("nama_br", datalist.get(position).getNama());
            data.put("kode_br", datalist.get(position).getKode_br());
        } catch (Exception e){
            Toast.makeText(this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
        }

        inten.putExtra("data", data.toString());
        setResult(RESULT_OK, inten);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        loadProductSearch(editable.toString());
    }
}

class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.RecyclerViewViewHolder>{
    private ArrayList<ModelBarang> dataList;
    RecyclerViewListener listener;
    private Context context;

    public BarangAdapter(ArrayList<ModelBarang> dataList, RecyclerViewListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layout = LayoutInflater.from(parent.getContext());
        View view = layout.inflate(R.layout.content_tambah_barang, parent, false);
        return new BarangAdapter.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
        holder.nama.setText(dataList.get(position).getNama());
        holder.harga.setText(Env.formatRupiah(dataList.get(position).getHarga_asli()));
        holder.icon_sampah.setVisibility(View.GONE);
        Glide.with(context).load(dataList.get(position).getGambar()).centerCrop().placeholder(R.drawable.tshirt).into(holder.img_tambah_barang);
        if(dataList.get(position).getDiskon_nomina() != null){
            holder.potongan_harga.setText(String.valueOf(dataList.get(position).getDiskon_nomina().nominal));
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

class ModelBarang {
    String nama, gambar, kode_br;
    int harga_asli;

    DiskonNominal diskon_nomina;

    public ModelBarang(String nama, String gambar, String kode_br, int harga_asli) {
        this.nama = nama;
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

class DiskonNominal {
    String jenis_diskon;
    int nominal;

    public DiskonNominal(String jenis_diskon, int nominal) {
        this.jenis_diskon = jenis_diskon;
        this.nominal = nominal;
    }
}
