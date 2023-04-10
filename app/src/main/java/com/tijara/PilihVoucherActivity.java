package com.tijara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class AdapterVoucher extends RecyclerView.Adapter<AdapterVoucher.ViewVoucher>{
    private ArrayList<modalVoucher> datalist;

    public AdapterVoucher(ArrayList<modalVoucher> datalist, Context applicationContext){
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public ViewVoucher onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.voucher_nominal, parent, false);
        return new ViewVoucher(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewVoucher holder, int position) {
        holder.txtNominal.setText(datalist.get(position).getVoucher_nominal());
//        holder.txtPercentase.setText(datalist.get(position).getVoucher_persentase());
    }

    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() :0;
    }

    public class ViewVoucher extends RecyclerView.ViewHolder {
        private TextView txtNominal, txtPercentase;

        public ViewVoucher(@NonNull View itemView) {
            super(itemView);
            txtNominal = itemView.findViewById(R.id.jumlah_nominal);
//            txtPercentase = itemView.findViewById(R.id.harga_pcs);
        }
    }
}

public class PilihVoucherActivity extends AppCompatActivity {
    ArrayList<modalVoucher> dataModels;
    RecyclerView voucher_nominal, voucher_persentase;
    private static AdapterVoucher adapterVoucher;

    ImageView backTOMainTransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pilih_voucher);

        dataModels = new ArrayList<>();

        dataModels.add(new modalVoucher("10%", "Rp.20.0000"));
        dataModels.add(new modalVoucher("10%", "Rp.20.0000"));
        dataModels.add(new modalVoucher("", "Rp.20.0000"));
        adapterVoucher = new AdapterVoucher(dataModels, getApplicationContext());

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
}

class modalVoucher {
    String voucher_persentase;
    String voucher_nominal;

    public modalVoucher(String voucher_persentase, String voucher_nominal) {
        this.voucher_persentase = voucher_persentase;
        this.voucher_nominal = voucher_nominal;
    }

    public String getVoucher_persentase() {
        return voucher_persentase;
    }

    public void setVoucher_persentase(String voucher_persentase) {
        this.voucher_persentase = voucher_persentase;
    }

    public String getVoucher_nominal() {
        return voucher_nominal;
    }

    public void setVoucher_nominal(String voucher_nominal) {
        this.voucher_nominal = voucher_nominal;
    }
}