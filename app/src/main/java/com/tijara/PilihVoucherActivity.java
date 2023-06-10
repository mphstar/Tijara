package com.tijara;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.tijara.keranjang.KategoriDiskon;
import com.tijara.keranjang.ModelKeranjang;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class PilihVoucherActivity extends AppCompatActivity {

    ImageView backTOMainTransaksi;
    ArrayList<ModelVoucher> dataList;
    RecyclerView voucher;
    ShimmerLayout loading;
    View nodata;

    private void loadVoucher(){
        voucher.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        loading.startShimmerAnimation();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, Env.BASE_URL + "voucher?apikey=" + Env.API_KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataList = new ArrayList<>();
                try {
                    JSONArray list_barang = new JSONArray(response);
                    JSONObject mapping_list;
                    if(list_barang.length() == 0){
                        voucher.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                    } else {
                        for (int i = 0; i < list_barang.length(); i++){
                            mapping_list = list_barang.getJSONObject(i);
                            if(mapping_list.getString("kategori").equals("nominal")){
                                dataList.add(new ModelVoucher(mapping_list.getInt("nominal"), "nominal"));
                            } else {
                                dataList.add(new ModelVoucher(mapping_list.getInt("nominal"), "persen"));
                            }
                        }

                        AdapterVoucher adapt = new AdapterVoucher(dataList, new RecyclerViewListener() {
                            @Override
                            public void onClick(View view, int position) {
                                Intent inten = new Intent();

                                JSONObject obj = new JSONObject();
                                try {
                                    obj.put("kategori", dataList.get(position).getKategori_voucher());
                                    obj.put("nominal", dataList.get(position).getVoucher_nominal());
                                } catch (Exception e){
                                    Toast.makeText(PilihVoucherActivity.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                                inten.putExtra("data_voucher", obj.toString());
                                setResult(RESULT_OK, inten);
                                finish();
                            }
                        });

                        voucher.setLayoutManager(new LinearLayoutManager(PilihVoucherActivity.this));
                        voucher.setAdapter(adapt);

                        voucher.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);

                    }

                    queue.getCache().clear();
                } catch (Exception e){
                    Toast.makeText(PilihVoucherActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.VolleyError error) {
                Toast.makeText(PilihVoucherActivity.this, "Error " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pilih_voucher);
        voucher = findViewById(R.id.voucher);
        loading = findViewById(R.id.loading);
        nodata = findViewById(R.id.nodata);
        loading.startShimmerAnimation();
        loadVoucher();

        backTOMainTransaksi = findViewById(R.id.back_to_pembayaran);
        backTOMainTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

class ModelVoucher {
    String kategori_voucher;
    int voucher_nominal;

    public ModelVoucher(int voucher_nominal, String kategori_voucher) {
        this.voucher_nominal = voucher_nominal;
        this.kategori_voucher = kategori_voucher;
    }

    public int getVoucher_nominal() {
        return voucher_nominal;
    }

    public void setVoucher_nominal(int voucher_nominal) {
        this.voucher_nominal = voucher_nominal;
    }

    public String getKategori_voucher() {
        return kategori_voucher;
    }

    public void setKategori_voucher(String kategori_voucher) {
        this.kategori_voucher = kategori_voucher;
    }
}


class AdapterVoucher extends RecyclerView.Adapter<AdapterVoucher.RecyclerViewViewHolder>{
    private ArrayList<ModelVoucher> dataList;
    private Context context;
    RecyclerViewListener listener;


    public AdapterVoucher(ArrayList<ModelVoucher> dataList, RecyclerViewListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layout = LayoutInflater.from(parent.getContext());
        View view = layout.inflate(R.layout.voucher_nominal, parent, false);
        return new AdapterVoucher.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
        if(dataList.get(position).getKategori_voucher().equals("nominal")){
            holder.jumlah_nominal.setText("Rp. " + Env.formatRupiah(dataList.get(position).voucher_nominal));
        } else {
            holder.jumlah_nominal.setText(dataList.get(position).voucher_nominal + "%");
        }

    }


    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder {
        TextView jumlah_nominal;
        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            jumlah_nominal = itemView.findViewById(R.id.jumlah_nominal);
            itemView.setOnClickListener(view -> listener.onClick(view, getAdapterPosition()));
        }
    }
}