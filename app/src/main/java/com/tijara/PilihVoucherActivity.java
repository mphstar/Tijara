package com.tijara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

class AddVoucher extends RecyclerView.Adapter<AddVoucher.RecyclerViewViewHolder>{
    private ArrayList<ModelVoucher> dataList;
    static RecyclerViewListener listener;
    private Context context;

    public AddVoucher(ArrayList<ModelVoucher> dataList, RecyclerViewListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layout = LayoutInflater.from(parent.getContext());
        View view = layout.inflate(R.layout.voucher_persentase, parent, false);
        return new AddVoucher.RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
        if (dataList.get(position).getJenis_voucher().equals("nominal")){
            holder.nominal.setText(Env.formatRupiah(dataList.get(position).getNominal()));
        }else if (dataList.get(position).getJenis_voucher().equals("persen")){
            holder.nominal.setText(dataList.get(position).getNominal()+" %");
        }
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    class RecyclerViewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nominal;
        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            nominal = itemView.findViewById(R.id.jumlah_nominal);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

}

public class PilihVoucherActivity extends AppCompatActivity implements RecyclerViewListener{
    ArrayList<ModelVoucher> dataModels;
    RecyclerView voucher_nominal, voucher_persentase;
    private static AddVoucher adapterVoucher;

    ImageView backTOMainTransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pilih_voucher);

        dataModels = new ArrayList<>();

        dataModels.add(new ModelVoucher(20000, "nominal"));
        dataModels.add(new ModelVoucher(10000, "nominal"));
        dataModels.add(new ModelVoucher(10, "persen"));
        adapterVoucher = new AddVoucher(dataModels, PilihVoucherActivity.this);

        voucher_nominal = findViewById(R.id.voucher_nominal);
        voucher_nominal.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PilihVoucherActivity.this);
        voucher_nominal.setLayoutManager(layoutManager);
        voucher_nominal.setAdapter(adapterVoucher);

        backTOMainTransaksi = findViewById(R.id.back_to_pembayaran);
        backTOMainTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view, int position) {
        Intent inten = new Intent();
        JSONObject data = new JSONObject();
        try {
            data.put("nominal_voucher", dataModels.get(position).getNominal());
            data.put("jenis_voucher", dataModels.get(position).getJenis_voucher());
        } catch (Exception e){
            Toast.makeText(this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
        }

        inten.putExtra("data_voucher", data.toString());
        setResult(RESULT_OK, inten);
        finish();
    }
}

class ModelVoucher {
    int nominal;
    String jenis_voucher;

    public ModelVoucher(int nominal, String jenis_voucher) {
        this.nominal = nominal;
        this.jenis_voucher = jenis_voucher;
    }

    public int getNominal() {
        return nominal;
    }

    public String getJenis_voucher() {
        return jenis_voucher;
    }
}