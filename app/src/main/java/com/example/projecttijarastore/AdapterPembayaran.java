package com.example.projecttijarastore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class AdapterPembayaran extends RecyclerView.Adapter<AdapterPembayaran.MahasiswaViewHolder>{
    private ArrayList<ModelTransaksi> datalist;

    public AdapterPembayaran(ArrayList<ModelTransaksi> datalist, Context applicationContext){
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public MahasiswaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.detail_rincian_barang, parent, false);
        return new MahasiswaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MahasiswaViewHolder holder, int position) {
        holder.txtNamaProduk.setText(datalist.get(position).getNama_produk());
        holder.txtHarga.setText(datalist.get(position).getHarga());
        holder.potonganHarga.setText(datalist.get(position).getPotongan_harga());
        holder.value.setText(datalist.get(position).getValue());
        holder.txtSubHarga.setText(datalist.get(position).getSub_harga());
        holder.hargaTotalProduk.setText(datalist.get(position).getjumlahBarangPilih());
        holder.totalVoucher.setText(datalist.get(position).getTotal_harga_barang());
    }

    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() :0;
    }

    public class MahasiswaViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNamaProduk, txtHarga, potonganHarga, value, txtSubHarga, hargaTotalProduk, totalVoucher;

        public MahasiswaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNamaProduk = itemView.findViewById(R.id.barang_pesan);
            txtHarga = itemView.findViewById(R.id.harga_barang_pesan);
            potonganHarga = itemView.findViewById(R.id.total_harga_barang_pesan);
            value = itemView.findViewById(R.id.jum_barang_pesan);
            txtSubHarga = itemView.findViewById(R.id.total_seluruh_harga_barang);
            hargaTotalProduk = itemView.findViewById(R.id.total_harga_barang_akhir);
            totalVoucher = itemView.findViewById(R.id.total_potongan_voucher);
        }
    }

//    private ArrayList<ModelTransaksi> dataSet;
//    Context mContext;
//
//    // View lookup cache
//    private static class ViewHolder {
//
//        TextView txtNamaProduk, txtHarga, potonganHarga, value, txtSubHarga, hargaTotalProduk, totalVoucher;
//    }
//
//    public AdapterPembayaran(ArrayList<ModelTransaksi> data, Context context) {
//        super(context, R.layout.activity_content, data);
//        this.dataSet = data;
//        this.mContext=context;
//
//    }
//
//    private int lastPosition = -1;
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // Get the data item for this position
//        ModelTransaksi model = getItem(position);
//        // Check if an existing view is being reused, otherwise inflate the view
//        ViewHolder viewHolder; // view lookup cache stored in tag
//
//        final View result;
//
//        if (convertView == null) {
//
//            viewHolder = new ViewHolder();
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(R.layout.detail_rincian_barang, parent, false);
//            viewHolder.txtNamaProduk = (TextView) convertView.findViewById(R.id.name_product);
//            viewHolder.txtHarga = (TextView) convertView.findViewById(R.id.harga_pcs);
//            viewHolder.potonganHarga = (TextView) convertView.findViewById(R.id.potongan_harga);
//            viewHolder.value = (TextView) convertView.findViewById(R.id.value);
//            viewHolder.txtSubHarga = (TextView) convertView.findViewById(R.id.harga_total);
//            viewHolder.hargaTotalProduk = (TextView) convertView.findViewById(R.id.total_harga_barang_akhir);
//            viewHolder.totalVoucher = (TextView) convertView.findViewById(R.id.total_potongan_voucher);
//
//            result=convertView;
//
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//            result=convertView;
//        }
//
////        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
////        result.startAnimation(animation);
////        lastPosition = position;
//
//        viewHolder.txtNamaProduk.setText(model.getNama_produk());
//        viewHolder.txtHarga.setText(model.getHarga());
//        viewHolder.potonganHarga.setText(model.getHarga());
//        viewHolder.value.setText(model.getHarga());
//        viewHolder.txtSubHarga.setText(model.getSub_harga());
//        viewHolder.hargaTotalProduk.setText(model.getjumlahBarangPilih());
//        viewHolder.totalVoucher.setText(model.getTotal_harga_barang());
////        viewHolder.img.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext(), R.style.dialog);
////                View diaView1 = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.activity_dialog, null);
////                TextView textLorem;
////                ImageView ImageProfile;
//////                textLorem = (TextView) diaView1.findViewById(R.id.text_lorem);
////                ImageProfile = (ImageView) diaView1.findViewById(R.id.pop_up_image);
//////                textLorem.setText(model.getMessage());
////                ImageProfile.setImageResource(model.getImgs());
////                builder.setView(diaView1);
////                builder.setCancelable(true);
////                builder.show();
////            }
////        });
////        viewHolder.img.setTag(position);
//        // Return the completed view to render on screen
//        return convertView;
//    }
}
