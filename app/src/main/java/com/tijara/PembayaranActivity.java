package com.tijara;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.tcp.TcpConnection;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class AdapterListProdukPembayaran extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Object> items;

    private static final int VIEW_TYPE_A = 0;
    private static final int VIEW_TYPE_B = 1;
    private static final int VIEW_TYPE_C = 2;

    public AdapterListProdukPembayaran(ArrayList<Object> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof ModelA) {
            return VIEW_TYPE_A;
        } else if (items.get(position) instanceof ModelB) {
            return VIEW_TYPE_B;
        } else if (items.get(position) instanceof ModelC) {
            return VIEW_TYPE_C;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_A:
                View viewA = inflater.inflate(R.layout.item_view_type_a, parent, false);
                viewHolder = new ViewHolderTypeA(viewA);
                break;
            case VIEW_TYPE_B:
                View viewB = inflater.inflate(R.layout.item_view_type_b, parent, false);
                viewHolder = new ViewHolderTypeB(viewB);
                break;
            case VIEW_TYPE_C:
                View viewC = inflater.inflate(R.layout.item_view_type_c, parent, false);
                viewHolder = new ViewHolderTypeC(viewC);
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
            case VIEW_TYPE_C:
                ViewHolderTypeC viewHolderTypeC = (ViewHolderTypeC) holder;
                configureViewHolderTypeC(viewHolderTypeC, position);
                break;
        }
    }

    private void configureViewHolderTypeA(ViewHolderTypeA holder, int position) {
        ModelA modelA = (ModelA) items.get(position);
        holder.NamaProduk.setText(modelA.getNamaProduk());
        holder.Harga.setText(allTypeData.format.format(Integer.valueOf(modelA.getHargaProduk())));
        holder.JumlahHargaProduk.setText(allTypeData.format.format(Integer.valueOf(modelA.getSubHargaProduk())));
        holder.JumlahHargaProduk2.setText(allTypeData.format.format(Integer.valueOf(modelA.getSubHargaProduk())));
        holder.nominalVoucher.setText(modelA.getNoiminalDiskon());
        holder.PotonganHarga.setText(allTypeData.format.format(Integer.valueOf(modelA.getPotonganDiskon())));
        holder.value.setText(modelA.getValueProduk());
    }

    private void configureViewHolderTypeB(ViewHolderTypeB holder, int position) {
        ModelB modelB = (ModelB) items.get(position);
        holder.NamaProduk.setText(modelB.getNamaProduk());
        holder.Harga.setText(allTypeData.format.format(Integer.valueOf(modelB.getHargaProduk())));
        holder.value.setText(modelB.getValueProduk());
        holder.totalSeluruhHargaBarang.setText(allTypeData.format.format(Integer.valueOf(modelB.getValueProduk()) * Integer.valueOf(modelB.getHargaProduk())));
    }
    private void configureViewHolderTypeC(ViewHolderTypeC holder, int position) {
        ModelC modelC = (ModelC) items.get(position);
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.listProdukFree.getContext(), LinearLayoutManager.VERTICAL, false);
        holder.NamaProduk.setText(modelC.getNamaProduk());
        holder.value.setText(modelC.getValueProduk());
        holder.Harga.setText(allTypeData.format.format(Integer.valueOf(modelC.getHargaProduk())));
        holder.totalSeluruhHargaBarang.setText(allTypeData.format.format(Integer.valueOf(modelC.getTotalHargaBarang())));
        holder.listProdukFree.setLayoutManager(layoutManager);
        holder.listProdukFree.setAdapter(new AdapterListProdukFree(modelC.getListProdukFreeDeal()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolderTypeA extends RecyclerView.ViewHolder {
        public TextView NamaProduk, Harga, PotonganHarga, value, JumlahHargaProduk, JumlahHargaProduk2, totalSeluruhHargaBarang, nominalVoucher;

        public ViewHolderTypeA(View itemView) {
            super(itemView);
            NamaProduk = itemView.findViewById(R.id.barang_pesan);
            Harga = itemView.findViewById(R.id.harga_barang_pesan);
            PotonganHarga = itemView.findViewById(R.id.total_seluruh_harga_barang);
            JumlahHargaProduk = itemView.findViewById(R.id.total_harga_barang_pesan);
            JumlahHargaProduk2 = itemView.findViewById(R.id.total_harga_barang_akhir);
            value = itemView.findViewById(R.id.jum_barang_pesan);
            nominalVoucher = itemView.findViewById(R.id.total_potongan_voucher);
            totalSeluruhHargaBarang = itemView.findViewById(R.id.total_seluruh_harga_barang);
        }
    }

    public static class ViewHolderTypeB extends RecyclerView.ViewHolder {
        public TextView NamaProduk, Harga, value, totalSeluruhHargaBarang;

        public ViewHolderTypeB(View itemView) {
            super(itemView);
            NamaProduk = itemView.findViewById(R.id.barang_pesan);
            Harga = itemView.findViewById(R.id.harga_barang_pesan);
            value = itemView.findViewById(R.id.jum_barang_pesan);
            totalSeluruhHargaBarang = itemView.findViewById(R.id.total_harga_barang_pesan);
        }
    }

    public static class ViewHolderTypeC extends RecyclerView.ViewHolder {
        public TextView NamaProduk, Harga, value, JumlahHargaProduk, JumlahHargaProduk2, totalSeluruhHargaBarang;
        public RecyclerView listProdukFree;

        public ViewHolderTypeC(View itemView) {
            super(itemView);
            NamaProduk = itemView.findViewById(R.id.barang_pesan);
            Harga = itemView.findViewById(R.id.harga_barang_pesan);
            value = itemView.findViewById(R.id.jum_barang_pesan);
            listProdukFree = itemView.findViewById(R.id.rincian_barang_free);
//            listProdukFree.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
//            JumlahHargaProduk = itemView.findViewById(R.id.total_harga_barang_pesan);
//            JumlahHargaProduk2 = itemView.findViewById(R.id.total_harga_barang_akhir);
            totalSeluruhHargaBarang = itemView.findViewById(R.id.total_harga_barang_pesan);
        }
    }
}

//class AdapterPembayaran extends RecyclerView.Adapter<AdapterPembayaran.PembayaranViewHolder>{
//    private ArrayList<ModelPembayaran> datalist;
//
//    public AdapterPembayaran(ArrayList<ModelPembayaran> datalist, Context applicationContext){
//        this.datalist = datalist;
//    }
//
//    @NonNull
//    @Override
//    public PembayaranViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View view = layoutInflater.inflate(R.layout.content_rincian_barang, parent, false);
//        return new PembayaranViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PembayaranViewHolder holder, int position) {
//        holder.NamaProduk.setText(datalist.get(position).getNamaProdukDeal());
//        holder.Harga.setText(datalist.get(position).getHargaProdukDeal());
//        holder.value.setText(datalist.get(position).getTotalProdukDeal());
////        System.out.println(PembayaranActivity.listProdukFree.length()+"freProd");
////        System.out.println(PembayaranActivity.diskonProduk.length()+"dis");
//        System.out.println("asasaaa");
//
//        if (PembayaranActivity.diskonProduk == null && PembayaranActivity.listProdukFree.length() > 0){
//
//            System.out.println("FreProd");
//            holder.KalkulasiDiskon.setVisibility(View.INVISIBLE);
//            holder.listProdukFree.setVisibility(View.VISIBLE);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(holder.listProdukFree.getContext(), LinearLayoutManager.HORIZONTAL, false);
//            holder.listProdukFree.setLayoutManager(layoutManager);
//            holder.listProdukFree.setAdapter(new AdapterListProdukFree(PembayaranActivity.arrayList));
//        }else if (PembayaranActivity.listProdukFree == null && PembayaranActivity.diskonProduk.length() > 0){
//
//            System.out.println("dis");
//            holder.KalkulasiDiskon.setVisibility(View.VISIBLE);
//            holder.listProdukFree.setVisibility(View.INVISIBLE);
////                holder.ListProdukFree.set(datalist.get(position).getListProdukFreeDeal());
//        }
////        else {
////            holder.ListProdukFree.setVisibility(View.GONE);
////            holder.PotonganHarga.setText(datalist.get(position).getPotonganHargaProdukDeal());
////            holder.JumlahHargaProduk.setText(datalist.get(position).getJumlahKalkulasiHarga());
////            holder.JumlahHargaProduk2.setText(datalist.get(position).getJumlahKalkulasiHarga());
////            holder.totalSeluruhHargaBarang.setText(datalist.get(position).getJumlahHargaAkhirBarang());
////        }
//        //
//    }
//
//    @Override
//    public int getItemCount() {
////        return (datalist != null) ? datalist.size() :0;
//        return datalist.size();
//    }
//
//    public class PembayaranViewHolder extends RecyclerView.ViewHolder {
//        private TextView NamaProduk, Harga, PotonganHarga, value, JumlahHargaProduk, JumlahHargaProduk2, totalSeluruhHargaBarang;
//        private RecyclerView listProdukFree;
//        private LinearLayout KalkulasiDiskon;
//
//        public PembayaranViewHolder(@NonNull View itemView) {
//            super(itemView);
//            NamaProduk = itemView.findViewById(R.id.barang_pesan);
//            Harga = itemView.findViewById(R.id.harga_barang_pesan);
//            PotonganHarga = itemView.findViewById(R.id.total_potongan_voucher);
//            value = itemView.findViewById(R.id.jum_barang_pesan);
//            KalkulasiDiskon = itemView.findViewById(R.id.kalkulasi_diskon);
//            listProdukFree = itemView.findViewById(R.id.rincian_barang_free);
//            JumlahHargaProduk = itemView.findViewById(R.id.total_harga_barang_pesan);
//            JumlahHargaProduk2 = itemView.findViewById(R.id.total_harga_barang_akhir);
//            totalSeluruhHargaBarang = itemView.findViewById(R.id.total_seluruh_harga_barang);
//        }
//    }
//
////    private ArrayList<ModelTransaksi> dataSet;
////    Context mContext;
////
////    // View lookup cache
////    private static class ViewHolder {
////
////        TextView txtNamaProduk, txtHarga, potonganHarga, value, txtSubHarga, hargaTotalProduk, totalVoucher;
////    }
////
////    public AdapterPembayaran(ArrayList<ModelTransaksi> data, Context context) {
////        super(context, R.layout.activity_content, data);
////        this.dataSet = data;
////        this.mContext=context;
////
////    }
////
////    private int lastPosition = -1;
////
////    @Override
////    public View getView(int position, View convertView, ViewGroup parent) {
////        // Get the data item for this position
////        ModelTransaksi model = getItem(position);
////        // Check if an existing view is being reused, otherwise inflate the view
////        ViewHolder viewHolder; // view lookup cache stored in tag
////
////        final View result;
////
////        if (convertView == null) {
////
////            viewHolder = new ViewHolder();
////            LayoutInflater inflater = LayoutInflater.from(getContext());
////            convertView = inflater.inflate(R.layout.detail_rincian_barang, parent, false);
////            viewHolder.txtNamaProduk = (TextView) convertView.findViewById(R.id.name_product);
////            viewHolder.txtHarga = (TextView) convertView.findViewById(R.id.harga_pcs);
////            viewHolder.potonganHarga = (TextView) convertView.findViewById(R.id.potongan_harga);
////            viewHolder.value = (TextView) convertView.findViewById(R.id.value);
////            viewHolder.txtSubHarga = (TextView) convertView.findViewById(R.id.harga_total);
////            viewHolder.hargaTotalProduk = (TextView) convertView.findViewById(R.id.total_harga_barang_akhir);
////            viewHolder.totalVoucher = (TextView) convertView.findViewById(R.id.total_potongan_voucher);
////
////            result=convertView;
////
////            convertView.setTag(viewHolder);
////        } else {
////            viewHolder = (ViewHolder) convertView.getTag();
////            result=convertView;
////        }
////
//////        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//////        result.startAnimation(animation);
//////        lastPosition = position;
////
////        viewHolder.txtNamaProduk.setText(model.getNama_produk());
////        viewHolder.txtHarga.setText(model.getHarga());
////        viewHolder.potonganHarga.setText(model.getHarga());
////        viewHolder.value.setText(model.getHarga());
////        viewHolder.txtSubHarga.setText(model.getSub_harga());
////        viewHolder.hargaTotalProduk.setText(model.getjumlahBarangPilih());
////        viewHolder.totalVoucher.setText(model.getTotal_harga_barang());
//////        viewHolder.img.setOnClickListener(new View.OnClickListener() {
//////            @Override
//////            public void onClick(View view) {
//////                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext(), R.style.dialog);
//////                View diaView1 = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.activity_dialog, null);
//////                TextView textLorem;
//////                ImageView ImageProfile;
////////                textLorem = (TextView) diaView1.findViewById(R.id.text_lorem);
//////                ImageProfile = (ImageView) diaView1.findViewById(R.id.pop_up_image);
////////                textLorem.setText(model.getMessage());
//////                ImageProfile.setImageResource(model.getImgs());
//////                builder.setView(diaView1);
//////                builder.setCancelable(true);
//////                builder.show();
//////            }
//////        });
//////        viewHolder.img.setTag(position);
////        // Return the completed view to render on screen
////        return convertView;
////    }
//}

class AdapterListProdukFree extends RecyclerView.Adapter<AdapterListProdukFree.PembayaranViewHolder>{
    private ArrayList<produkfree> datalist;

    public AdapterListProdukFree(ArrayList<produkfree> datalist){
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public PembayaranViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.content_rincian_barang_free, parent, false);
        return new PembayaranViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PembayaranViewHolder holder, int position) {
        produkfree produkfree = datalist.get(position);
        holder.NamaProduk.setText(produkfree.getNamaProduk());
        holder.value.setText(produkfree.getValueProduk());
//
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class PembayaranViewHolder extends RecyclerView.ViewHolder {
        private TextView NamaProduk, value;

        public PembayaranViewHolder(@NonNull View itemView) {
            super(itemView);
            NamaProduk = itemView.findViewById(R.id.nama_barang_free);
            value = itemView.findViewById(R.id.jum_barang_free);
        }
    }
}



public class PembayaranActivity extends AppCompatActivity {
    ArrayList<Object> dataModels;
    ArrayList<ModelPembayaran> dataModels2;
    static int a = 0, hasil_hasil, kembalian, total_bayar;
    static String voucher = null;
    static TextView totalAkhirPalingAkhir, kurangBayar, keteranganBayar, teks_tunai, teks_qris, nominal_didapat;
    static EditText field_total_bayar;
    static ArrayList<produkfree> arrayList;
    static RecyclerView materi, materi2;
    static JSONObject jsonObject, jsonObject2;
    static JSONArray listProdukFree;
    static String diskonProduk, jenisPembayaran = "Cash";
    static LinearLayout pembayaran_akhir;
    private static AdapterListProdukFree adapterListProdukFree;
    ImageView button_voucher, backTOMainTransaksi, button_tunai, button_qris, icon_tunai, icon_qris;
    static RelativeLayout loadingStruk;

    static void eksekusi_field_total_akhir(String jumlah){

//        PembayaranActivity.field_total_bayar.setText(allTypeData.format.format(jumlah));

        allTypeData.totalAkhirPalingakhir = kirimValues.total;
        hasil_hasil = a - Integer.parseInt(jumlah);
        System.out.println(jumlah);
        System.out.println(hasil_hasil);
        System.out.println(PembayaranActivity.a);
        if (allTypeData.jenisProduk == 1){
            if (Integer.valueOf(jumlah) > PembayaranActivity.a){
                kembalian = Math.abs(hasil_hasil);
                System.out.println(kembalian);
                PembayaranActivity.keteranganBayar.setText("Kembalian :");
                PembayaranActivity.kurangBayar.setTextColor(Color.parseColor("#00F30C"));
                PembayaranActivity.kurangBayar.setText(allTypeData.format.format(kembalian));
            } else if (Integer.valueOf(jumlah) <= PembayaranActivity.a) {
                PembayaranActivity.keteranganBayar.setText("Kurang Bayar : ");
                PembayaranActivity.kurangBayar.setTextColor(Color.parseColor("#FF0000"));
                PembayaranActivity.kurangBayar.setText(allTypeData.format.format(hasil_hasil));
            } else if (hasil_hasil == 0){
                PembayaranActivity.keteranganBayar.setText("Kurang Bayar : ");
                PembayaranActivity.kurangBayar.setTextColor(Color.parseColor("#FF0000"));
                PembayaranActivity.kurangBayar.setText(allTypeData.format.format(hasil_hasil));
            }
        }else {
            PembayaranActivity.totalAkhirPalingAkhir.setText(allTypeData.format.format(PembayaranActivity.a));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String getdata = data.getStringExtra("data_voucher");
                try {
                    JSONObject obj = new JSONObject(getdata);
                    if (obj.getString("jenis_voucher").equals("nominal")){
                        nominal_didapat.setText("Voucher : "+Env.formatRupiah(Integer.valueOf(obj.getString("nominal_voucher"))));
                        nominal_didapat.setTextColor(Color.parseColor("#FFB015"));
                        voucher = obj.getString("nominal_voucher");
                    }else if (obj.getString("jenis_voucher").equals("persen")){
                        nominal_didapat.setText("Voucher : "+obj.getString("nominal_voucher")+" %");
                        nominal_didapat.setTextColor(Color.parseColor("#FFB015"));
                        voucher = obj.getString("nominal_voucher");
                    }
                } catch (Exception e){
                }

            }
        }
    }

    private static class CurrencyTextWatcher implements TextWatcher {

        private final EditText editText;
        private String previousValue = "";

        public CurrencyTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            previousValue = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if (!s.toString().equals(previousValue)) {
//                editText.removeTextChangedListener(this);
//
//                String cleanString = s.toString().replaceAll("[Rp,. ]", "");
//                double parsed = Double.parseDouble(cleanString);
//                String formatted = "Rp " + new DecimalFormat("###,###,###,###.##").format(parsed);
//
//                editText.setText(formatted);
//                editText.setSelection(formatted.length());
//
//                editText.addTextChangedListener(this);
//            }
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s.toString().length() != 0){
                allTypeData.jenisProduk = 1;
                PembayaranActivity.total_bayar = Integer.valueOf(s.toString());
                PembayaranActivity.eksekusi_field_total_akhir(s.toString());
            }else if (s.toString().length() == 0){
                allTypeData.jenisProduk = 2;
                PembayaranActivity.eksekusi_field_total_akhir(String.valueOf(0));
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pembayaran);

        dataModels = new ArrayList<>();

        nominal_didapat = findViewById(R.id.nominal_didapat);
        button_voucher= findViewById(R.id.pilih_voucher);
        backTOMainTransaksi = findViewById(R.id.back_to_view_transaksi);
        teks_tunai = findViewById(R.id.teksTunai);
        teks_qris = findViewById(R.id.teksQris);
        icon_tunai = findViewById(R.id.iconTunai);
        icon_qris = findViewById(R.id.iconQris);
        loadingStruk = findViewById(R.id.loadingPrint);
        materi = findViewById(R.id.rincian_barang);
        totalAkhirPalingAkhir = findViewById(R.id.total_akhir_palihAkhir);
        kurangBayar = findViewById(R.id.kurang_bayar);
        button_tunai = findViewById(R.id.buttonTunai);
        button_qris = findViewById(R.id.buttonQris);
        keteranganBayar = findViewById(R.id.keterangan_bayar);
        pembayaran_akhir = findViewById(R.id.pembayaran_paling_akhir);
        field_total_bayar = findViewById(R.id.field_total_bayar);

        try {
            for (int i = 0; i < Transaksi.jsonArray.length(); i++){

                jsonObject = Transaksi.jsonArray.getJSONObject(i);

                if (jsonObject.getString("jenisDiskon").equals("Free_produk")){

                    String namaProduk = jsonObject.getString("namaProduk");
                    String hargaProduk = jsonObject.getString("hargaProduk");
                    String jumlahPesanan = jsonObject.getString("jumlahPesanan");
                    String totalHargaProduk = jsonObject.getString("diskonProduk");
                    listProdukFree = jsonObject.getJSONArray("dataProdukFree");
                    System.out.println(listProdukFree);

                    arrayList = new ArrayList<>();
                    JSONObject jsonObject1 = new JSONObject();
                    try {
//                        JSONArray jsonArray = new JSONArray(listProdukFree);
                        for (int j = 0; j < listProdukFree.length(); j++) {
                            JSONObject jsonObject = listProdukFree.getJSONObject(j);
                            String name = jsonObject.getString("nama");
                            String value = jsonObject.getString("value");

                            arrayList.add(new produkfree(name, value));
                        }
                        System.out.println(arrayList.size()+"sjkd");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("aaa");
                    dataModels.add(new ModelC(namaProduk, jumlahPesanan, hargaProduk, totalHargaProduk, arrayList));
                }else if (jsonObject.getString("jenisDiskon").equals("Non_free_produk") && jsonObject.getString("nominalDiskon") == "0"){

                    String namaProduk = jsonObject.getString("namaProduk");
                    String hargaProduk = jsonObject.getString("hargaProduk");
                    String jumlahPesanan = jsonObject.getString("jumlahPesanan");
//                    String subHargaProduk = jsonObject.getString("subHarga");
//                    String nominalDiskon = jsonObject.getString("nominalDiskon");
                    diskonProduk = jsonObject.getString("diskonProduk");

                    System.out.println("bb2");
                    dataModels.add(new ModelB(namaProduk, jumlahPesanan, hargaProduk));
                }else {

                    String namaProduk = jsonObject.getString("namaProduk");
                    String hargaProduk = jsonObject.getString("hargaProduk");
                    String jumlahPesanan = jsonObject.getString("jumlahPesanan");
                    String subHargaProduk = jsonObject.getString("subHarga");
                    String nominalDiskon = jsonObject.getString("nominalDiskon");
                    diskonProduk = jsonObject.getString("diskonProduk");

                    System.out.println("bbb");
                    dataModels.add(new ModelA(namaProduk, jumlahPesanan, hargaProduk, diskonProduk, subHargaProduk, nominalDiskon));
                }
//
            }

            System.out.println("ccc");
            AdapterListProdukPembayaran adapter = new AdapterListProdukPembayaran(dataModels);
            RecyclerView recyclerView = findViewById(R.id.rincian_barang);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            try {


                for (int i = 0; i < Transaksi.jsonArray.length(); i++) {
                    JSONObject jsonObject = Transaksi.jsonArray.getJSONObject(i);
                    String diskon = jsonObject.getString("diskonProduk");
                    a = Integer.valueOf(diskon) + a;
                    System.out.println(a);
                }

                PembayaranActivity.kurangBayar.setText(allTypeData.format.format(a - 0));
                PembayaranActivity.totalAkhirPalingAkhir.setText(allTypeData.format.format(a));
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            adapterPembayaran = new AdapterPembayaran(dataModels, getApplicationContext());
//            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PembayaranActivity.this);
//            materi.setLayoutManager(layoutManager);
//            materi.setAdapter(adapterPembayaran);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//
//        dataModels = new ArrayList<>();
//
//        dataModels.add(new ModelTransaksi("Dress Panjang Kondangan K..", "Rp.150.000", "Rp.150.000", "Rp.150.000", "0"," ","1", R.drawable.shape_white, R.drawable.shape_white));
//        dataModels.add(new ModelTransaksi("Dress Casual Pink", "Rp.210.000", "Rp.210.000", "Rp.150.000", "0"," ","2", R.drawable.shape_white, R.drawable.shape_white));
//        dataModels.add(new ModelTransaksi("Celana Chinos Buat Perang ...", "Rp.70.000", "Rp.70.000", "Rp.150.000", "0"," ","1", R.drawable.shape_white, R.drawable.shape_white));
//        for (int i = 0; i <10; i++){
//            dataModels.add(new ModelTransaksi("Dress Casual Pink", "Rp.210.000", "Rp.210.000", "Rp.150.000",  "0"," ","3", R.drawable.shape_white, R.drawable.shape_white));
//        }
//        adapterPembayaran = new AdapterTransaksi(dataModels, getApplicationContext());
//
//        materi = findViewById(R.id.list_barang);
//
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TambahBarangActivity.this);
//        materi.setLayoutManager(layoutManager);
//        materi.setAdapter(adapterTransaksi);
//        backTOMainTransaksi = findViewById(R.id.back_to_view_transaksi);
//        backTOMainTransaksi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
        pembayaran_akhir.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(PembayaranActivity.this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(PembayaranActivity.this, new String[]{Manifest.permission.BLUETOOTH}, 1);
            } else {

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            EscPosPrinter printer = new EscPosPrinter(new TcpConnection("192.168.0.45", 9100), 200, 80f, 45                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                );
                            printer.printFormattedText(
                                    "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, getApplicationContext().getResources().getDrawableForDensity(R.drawable.add, 200)) + "</img>\n" +

                                            "[L]\n" +
                                            "[C]<u><font size='big'>ORDER Mphstarr</font></u>\n" +
                                            "[L]\n" +
                                            "[C]================================================\n" +
                                            "[L]\n" +
                                            "[L]<b>BEAUTIFUL SHIRT</b>[R]9.99e\n" +
                                            "[L]  + Size : S\n" +
                                            "[L]\n" +
                                            "[L]<b>AWESOME HAT</b>[R]24.99e\n" +
                                            "[L]  + Size : 57/58\n" +
                                            "[L]\n" +
                                            "[C]================================================\n" +
                                            "[R]TOTAL PRICE :[R]34.98e\n" +
                                            "[R]TAX :[R]4.23e\n" +
                                            "[L]\n" +
                                            "[C]================================================\n" +
                                            "[L]\n" +
                                            "[L]<font size='tall'>Customer :</font> [R]<b>Tes</b>\n" +
                                            "[L]Raymond DUPONT\n" +
                                            "[L]5 rue des girafes\n" +
                                            "[L]31547 PERPETES\n" +
                                            "[L]Tel : +33801201456\n" +
                                            "[L]\n" +
                                            "[C]<barcode type='ean13' height='10'>831254784551</barcode>\n" +
                                            "[L]<qrcode size='20'>http://www.developpeur-web.dantsu.com/</qrcode>\n"
                            );
                            Toast.makeText(PembayaranActivity.this, "sukses", Toast.LENGTH_SHORT).show();


                        } catch (Exception e){
                        }
                    }
                }).start();
            }

            PembayaranActivity.loadingStruk.setVisibility(View.VISIBLE);
            // Post API
            String url = Env.BASE_URL+"barang_uplode";
            try {
//                    JSONObject requestBody = new JSONObject();
//                    requestBody.put("kodeTr", Transaksi.jsonArray);
//                    System.out.println(requestBody);

//                    Map<String, String> params = new HashMap<>();

                StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PembayaranActivity.loadingStruk.setVisibility(View.GONE);
                        Toast.makeText(PembayaranActivity.this, response, Toast.LENGTH_SHORT).show();
                        Log.d("tes", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        PembayaranActivity.loadingStruk.setVisibility(View.GONE);
                        Toast.makeText(PembayaranActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> mapp = new HashMap<>();
                        mapp.put("total", String.valueOf(PembayaranActivity.a));
                        mapp.put("total_bayar", String.valueOf(PembayaranActivity.total_bayar));
                        mapp.put("kembalian", String.valueOf(Math.abs(hasil_hasil)));
                        mapp.put("data_list", Transaksi.jsonArray.toString());
                        mapp.put("jenis_pembayaran", PembayaranActivity.jenisPembayaran);
//                            mapp.put("nama_kasir", Home.name);
                        mapp.put("nama_kasir", "fathur");

                        System.out.println(mapp.toString() + " pp");

                        return  mapp;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(req);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // End Post API
        });


        field_total_bayar.addTextChangedListener(new CurrencyTextWatcher(field_total_bayar));

        button_qris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PembayaranActivity.jenisPembayaran = "QRIS";
                field_total_bayar.setVisibility(View.GONE);
                keteranganBayar.setText("Pembayaran : ");
                kurangBayar.setText("LUNAS");
                kurangBayar.setTextColor(Color.parseColor("#00F30C"));
                icon_qris.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                teks_qris.setTextColor(getResources().getColorStateList(R.color.white));
                icon_tunai.setBackgroundTintList(getResources().getColorStateList(R.color.colorBlack));
                teks_tunai.setTextColor(getResources().getColorStateList(R.color.colorBlack));
                button_qris.setBackgroundTintList(getResources().getColorStateList(R.color.colorBlack));
                button_tunai.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
            }
        });

        button_tunai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PembayaranActivity.jenisPembayaran = "Cash";
                field_total_bayar.setVisibility(View.VISIBLE);
                kurangBayar.setTextColor(Color.parseColor("#FF0000"));
                kurangBayar.setText(allTypeData.format.format(a));
                keteranganBayar.setText("Kurang Bayar : ");
                icon_tunai.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                teks_tunai.setTextColor(getResources().getColorStateList(R.color.white));
                icon_qris.setBackgroundTintList(getResources().getColorStateList(R.color.colorBlack));
                teks_qris.setTextColor(getResources().getColorStateList(R.color.colorBlack));
                button_qris.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                button_tunai.setBackgroundTintList(getResources().getColorStateList(R.color.colorBlack));
            }
        });

        backTOMainTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten = new Intent(PembayaranActivity.this, PilihVoucherActivity.class);
                startActivityForResult(inten, 1);
            }
        });
    }
}

class ModelPembayaran{

    String namaProdukDeal;
    String potonganHargaProdukDeal;
    String totalProdukDeal;
    String hargaProdukDeal;
    int jumlahKalkulasiHarga, jumlahHargaAkhirBarang;
    JSONArray listProdukFreeDeal;

    public ModelPembayaran(String namaProdukDeal, @Nullable String potonganHargaProdukDeal, String totalProdukDeal, String hargaProdukDeal, @Nullable JSONArray listProdukFreeDeal) {
        this.namaProdukDeal = namaProdukDeal;
        this.potonganHargaProdukDeal = potonganHargaProdukDeal;
        this.totalProdukDeal = totalProdukDeal;
        this.hargaProdukDeal = hargaProdukDeal;
        this.listProdukFreeDeal = listProdukFreeDeal;
    }

    public void setJumlahKalkulasiHarga(int jumlahKalkulasiHarga) {
        this.jumlahKalkulasiHarga = jumlahKalkulasiHarga;
    }

    public void setJumlahHargaAkhirBarang(int jumlahHargaAkhirBarang) {
        this.jumlahHargaAkhirBarang = jumlahHargaAkhirBarang;
    }

    public String getNamaProdukDeal() {
        return namaProdukDeal;
    }

    public int getJumlahHargaAkhirBarang() {
//        jumlahHargaAkhirBarang = jumlahKalkulasiHarga - potonganHargaProdukDeal;
        setJumlahHargaAkhirBarang(300000);
        return jumlahHargaAkhirBarang;
    }

    public int getJumlahKalkulasiHarga() {
//        jumlahKalkulasiHarga = hargaProdukDeal + totalProdukDeal;
        setJumlahKalkulasiHarga(25000);
        return jumlahKalkulasiHarga;
    }

    public String getPotonganHargaProdukDeal() {
        return potonganHargaProdukDeal;
    }

    public String getTotalProdukDeal() {
        return totalProdukDeal;
    }

    public String getHargaProdukDeal() {
        return hargaProdukDeal;
    }

    public JSONArray getListProdukFreeDeal() {
        return listProdukFreeDeal;
    }
}

class produkfree {

    String namaProduk;
    String valueProduk;

    public produkfree(String namaProduk, String valueProduk) {
        this.namaProduk = namaProduk;
        this.valueProduk = valueProduk;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public String getValueProduk() {
        return valueProduk;
    }
}

class ModelA{

    String namaProduk;
    String valueProduk;
    String hargaProduk;
    String potonganDiskon;
    String subHargaProduk;
    String noiminalDiskon;

    public ModelA(String namaProduk, String valueProduk, String hargaProduk, String potonganDiskon, String subHargaProduk, String noiminalDiskon) {
        this.namaProduk = namaProduk;
        this.valueProduk = valueProduk;
        this.hargaProduk = hargaProduk;
        this.potonganDiskon = potonganDiskon;
        this.subHargaProduk = subHargaProduk;
        this.noiminalDiskon = noiminalDiskon;
    }

    public String getNoiminalDiskon() {
        return noiminalDiskon;
    }
    public String getSubHargaProduk() {
        return subHargaProduk;
    }
    public String getHargaProduk() {
        return hargaProduk;
    }
    public String getPotonganDiskon() {
        return potonganDiskon;
    }
    public String getNamaProduk() {
        return namaProduk;
    }
    public String getValueProduk() {
        return valueProduk;
    }
}

class ModelB{

    String namaProduk;
    String valueProduk;
    String hargaProduk;

    public ModelB(String namaProduk, String valueProduk, String hargaProduk) {
        this.namaProduk = namaProduk;
        this.valueProduk = valueProduk;
        this.hargaProduk = hargaProduk;
    }

    public String getNamaProduk() {
        return namaProduk;
    }
    public String getHargaProduk() {
        return hargaProduk;
    }
    public String getValueProduk() {
        return valueProduk;
    }
}

class ModelC{

    String namaProduk;
    String valueProduk;
    String hargaProduk;
    String totalHargaBarang;
    ArrayList<produkfree> listProdukFreeDeal;

    public ModelC(String namaProduk, String valueProduk, String hargaProduk, String totalHargaBarang, ArrayList<produkfree> listProdukFreeDeal) {
        this.namaProduk = namaProduk;
        this.valueProduk = valueProduk;
        this.hargaProduk = hargaProduk;
        this.totalHargaBarang = totalHargaBarang;
        this.listProdukFreeDeal = listProdukFreeDeal;
    }

    public String getTotalHargaBarang() {
        return totalHargaBarang;
    }
    public String getNamaProduk() {
        return namaProduk;
    }
    public String getHargaProduk() {
        return hargaProduk;
    }
    public ArrayList<produkfree> getListProdukFreeDeal() {
        return listProdukFreeDeal;
    }
    public String getValueProduk() {
        return valueProduk;
    }
}