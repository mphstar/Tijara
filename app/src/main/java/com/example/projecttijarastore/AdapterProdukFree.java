package com.example.projecttijarastore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterProdukFree extends RecyclerView.Adapter<AdapterProdukFree.ViewHolder> {

    private ArrayList<modelProdukFree> datalist;

    public AdapterProdukFree(ArrayList<modelProdukFree> datalist){
        this.datalist = datalist;
    }

    public void filterList(ArrayList<modelProdukFree> filterdNames, LinearLayout linearLayout, RecyclerView materi) {
        this.datalist = filterdNames;;
        if (datalist.isEmpty()){
            materi.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
        System.out.println(datalist);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.content_produk_free, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtNamaProduk.setText(datalist.get(position).getName_product());
        holder.txtValues.setText(datalist.get(position).getValues());

    }

    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() :0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNamaProduk, txtValues;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNamaProduk = itemView.findViewById(R.id.name_product);
            txtValues = itemView.findViewById(R.id.jumlah_pcs);
        }
    }
}
