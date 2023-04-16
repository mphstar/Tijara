package com.tijara;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

class AdapterVoucher extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Object> items;

    private static final int VIEW_TYPE_A = 0;
    private static final int VIEW_TYPE_B = 1;

    public AdapterVoucher(ArrayList<Object> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof ModelVoucherNominal) {
            return VIEW_TYPE_A;
        } else if (items.get(position) instanceof ModelVoucherPersentase) {
            return VIEW_TYPE_B;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_A:
                View viewA = inflater.inflate(R.layout.voucher_nominal, parent, false);
                viewHolder = new ViewHolderTypeA(viewA);
                break;
            case VIEW_TYPE_B:
                View viewB = inflater.inflate(R.layout.voucher_persentase, parent, false);
                viewHolder = new ViewHolderTypeB(viewB);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_A:
                ViewHolderTypeA viewHolderTypeA = (ViewHolderTypeA) holder;
                configureViewHolderTypeA(viewHolderTypeA, position);
                break;
            case VIEW_TYPE_B:
                ViewHolderTypeB viewHolderTypeB = (ViewHolderTypeB) holder;
                configureViewHolderTypeB(viewHolderTypeB, position);
                break;

        }
    }

    private void configureViewHolderTypeA(ViewHolderTypeA holder, int position) {
        ModelVoucherNominal modelA = (ModelVoucherNominal) items.get(position);
        holder.VoucherNominal.setText(modelA.getVoucher_nominal());
    }

    private void configureViewHolderTypeB(ViewHolderTypeB holder, int position) {
        ModelVoucherPersentase modelB = (ModelVoucherPersentase) items.get(position);
        holder.VoucherPersentase.setText(modelB.getVoucher_persentase());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolderTypeA extends RecyclerView.ViewHolder {
        public TextView VoucherNominal;

        public ViewHolderTypeA(View itemView) {
            super(itemView);
            VoucherNominal = itemView.findViewById(R.id.jumlah_nominal);
        }
    }

    public static class ViewHolderTypeB extends RecyclerView.ViewHolder {
        public TextView VoucherPersentase;

        public ViewHolderTypeB(View itemView) {
            super(itemView);
            VoucherPersentase = itemView.findViewById(R.id.jumlah_persentase);
        }
    }
}

public class PilihVoucherActivity extends AppCompatActivity {
    ArrayList<Object> dataModels;
    RecyclerView voucher_nominal, voucher_persentase;
    private static AdapterVoucher adapterVoucher;

    ImageView backTOMainTransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pilih_voucher);

        dataModels = new ArrayList<>();

        dataModels.add(new ModelVoucherNominal("Rp.20.0000"));
        dataModels.add(new ModelVoucherNominal("Rp.20.0000"));
        dataModels.add(new ModelVoucherPersentase("10%"));
        adapterVoucher = new AdapterVoucher(dataModels);

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

class ModelVoucherPersentase {
    String voucher_persentase;

    public ModelVoucherPersentase(String voucher_persentase) {
        this.voucher_persentase = voucher_persentase;
    }

    public String getVoucher_persentase() {
        return voucher_persentase;
    }
}

class ModelVoucherNominal {
    String voucher_nominal;

    public ModelVoucherNominal(String voucher_nominal) {
        this.voucher_nominal = voucher_nominal;
    }

    public String getVoucher_nominal() {
        return voucher_nominal;
    }
}