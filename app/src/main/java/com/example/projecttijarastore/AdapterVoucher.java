package com.example.projecttijarastore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterVoucher extends RecyclerView.Adapter<AdapterVoucher.ViewVoucher>{
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
