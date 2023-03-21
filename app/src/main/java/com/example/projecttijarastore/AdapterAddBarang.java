package com.example.projecttijarastore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;


public class AdapterAddBarang extends RecyclerView.Adapter<AdapterAddBarang.MahasiswaViewHolder>{

    private String nameProduct, priceProduct, Values;
    private ArrayList<ModelAddBarang> datalist;

    public AdapterAddBarang(ArrayList<ModelAddBarang> datalist, Context applicationContext){
        this.datalist = datalist;
    }

    public void filterList(ArrayList<ModelAddBarang> filterdNames, LinearLayout linearLayout, RecyclerView materi) {
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
    public MahasiswaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.content_tambah_barang, parent, false);
        return new MahasiswaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MahasiswaViewHolder holder, int position) {
        String name_product = datalist.get(position).getNama_produk();
        String price_product = datalist.get(position).getHarga();
        String value = datalist.get(position).getValue();
        holder.txtNamaProduk.setText(datalist.get(position).getNama_produk());
        holder.txtHarga.setText(datalist.get(position).getHarga());
        holder.potonganHarga.setText(datalist.get(position).getPotongan_harga());
        holder.value.setText(datalist.get(position).getValue());
        holder.txtSubHarga.setText(datalist.get(position).getSub_harga());
        holder.img.setImageResource(datalist.get(position).getImgs());
        holder.trush.setImageResource(datalist.get(position).getTrush());
        holder.value.setVisibility(View.GONE);
        holder.trush.setVisibility(View.GONE);
        holder.txtSubHarga.setVisibility(View.GONE);
//        holder.tambah_produk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext(), R.style.dialog);
//                View dialogView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.content_dialog_transaksi, null);
//                builder.setView(dialogView);
//                builder.setCancelable(true);
//                builder.show();
//            }
//        });
        holder.to_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameProduct = name_product;
                priceProduct = price_product;
                Values = value;
                showAlertDialog(view);
            }
        });
    }

    private void showAlertDialog(View view){

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getRootView().getContext(), R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_product);
        bottomSheetDialog.show();

        TextView kode_produk, nama_produk, harga_produk, diskon_produk;
        FlexboxLayout values;
        values = bottomSheetDialog.findViewById(R.id.info_pesanan);
        nama_produk = bottomSheetDialog.findViewById(R.id.nama_produk);
        harga_produk = bottomSheetDialog.findViewById(R.id.harga_produk);
        if (Values == "2"){
            values.setVisibility(View.VISIBLE);
            System.out.println("gratis produk");
        }else if (Values == "1") {
            values.setVisibility(View.GONE);
            System.out.println("no gratis");
        }
        nama_produk.setText(nameProduct);
        harga_produk.setText(priceProduct);

        TextView tambah_barang;
        tambah_barang = bottomSheetDialog.findViewById(R.id.tambah_produk);
        tambah_barang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext(), R.style.dialog);
                View dialogView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.content_dialog_transaksi, null);
                builder.setView(dialogView);
                builder.setCancelable(true);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (datalist != null) ? datalist.size() :0;
    }


    public class MahasiswaViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNamaProduk, txtHarga, potonganHarga, value, txtSubHarga;
        private ImageView img, trush;
        private LinearLayout to_detail;
        private TextView tambah_produk;

        public MahasiswaViewHolder(@NonNull View itemView) {
            super(itemView);
            tambah_produk = itemView.findViewById(R.id.tambah_produk);
            to_detail = itemView.findViewById(R.id.to_detail);
            txtNamaProduk = itemView.findViewById(R.id.name_product);
            txtHarga = itemView.findViewById(R.id.harga_pcs);
            potonganHarga = itemView.findViewById(R.id.potongan_harga);
            value = itemView.findViewById(R.id.value);
            txtSubHarga = itemView.findViewById(R.id.harga_total);
            img = itemView.findViewById(R.id.img);
            trush = itemView.findViewById(R.id.icon_sampah);
        }
    }
//
//    private ArrayList<ModelTransaksi> dataSet;
//    Context mContext;
//
//    // View lookup cache
//    private static class ViewHolder {
//        TextView txtNamaProduk;
//        TextView txtHarga;
//        TextView potonganHarga;
//        TextView value;
//        TextView txtSubHarga;
//        ImageView img;
//        ImageView trush;
//    }
//
//    public AdapterTransaksi(ArrayList<ModelTransaksi> data, Context context) {
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
//            convertView = inflater.inflate(R.layout.activity_content, parent, false);
//            viewHolder.txtNamaProduk = (TextView) convertView.findViewById(R.id.name_product);
//            viewHolder.txtHarga = (TextView) convertView.findViewById(R.id.harga_pcs);
//            viewHolder.potonganHarga = (TextView) convertView.findViewById(R.id.potongan_harga);
//            viewHolder.value = (TextView) convertView.findViewById(R.id.value);
//            viewHolder.txtSubHarga = (TextView) convertView.findViewById(R.id.harga_total);
//            viewHolder.trush = (ImageView) convertView.findViewById(R.id.icon_sampah);
//            viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
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
//        viewHolder.img.setImageResource(model.getImgs());
//        viewHolder.trush.setImageResource(model.getImgs());
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
//        viewHolder.img.setTag(position);
//        // Return the completed view to render on screen
//        return convertView;
//    }
}
