package com.tijara;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
        holder.Harga.setText(modelA.getHargaProduk());
        holder.JumlahHargaProduk.setText(modelA.getSubHargaProduk());
        holder.JumlahHargaProduk2.setText(modelA.getSubHargaProduk());
        holder.nominalVoucher.setText(modelA.getNoiminalDiskon());
        holder.PotonganHarga.setText(modelA.getPotonganDiskon());
        holder.value.setText(modelA.getValueProduk());
    }

    private void configureViewHolderTypeB(ViewHolderTypeB holder, int position) {
        ModelB modelB = (ModelB) items.get(position);
        holder.NamaProduk.setText(modelB.getNamaProduk());
        holder.Harga.setText(modelB.getHargaProduk());
        holder.value.setText(modelB.getValueProduk());
    }
    private void configureViewHolderTypeC(ViewHolderTypeC holder, int position) {
        ModelC modelC = (ModelC) items.get(position);
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.listProdukFree.getContext(), LinearLayoutManager.VERTICAL, false);
        holder.NamaProduk.setText(modelC.getNamaProduk());
        holder.value.setText(modelC.getValueProduk());
        holder.Harga.setText(modelC.getHargaProduk());
        holder.totalSeluruhHargaBarang.setText(modelC.getTotalHargaBarang());
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
//            totalSeluruhHargaBarang = itemView.findViewById(R.id.total_seluruh_harga_barang);
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
    private ArrayList<ModelProdukFree> datalist;

    public AdapterListProdukFree(ArrayList<ModelProdukFree> datalist){
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
        ModelProdukFree modelProdukFree = datalist.get(position);
        holder.NamaProduk.setText(modelProdukFree.getNamaProduk());
        holder.value.setText(modelProdukFree.getValueProduk());
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
    static ArrayList<ModelProdukFree> arrayList;
    static RecyclerView materi, materi2;
    static JSONObject jsonObject, jsonObject2;
    static JSONArray listProdukFree;
    static String diskonProduk;
    private static AdapterListProdukFree adapterListProdukFree;
    ImageView button_voucher, backTOMainTransaksi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pembayaran);

        dataModels = new ArrayList<>();

        button_voucher= findViewById(R.id.pilih_voucher);
        backTOMainTransaksi = findViewById(R.id.back_to_view_transaksi);
        materi = findViewById(R.id.rincian_barang);

        try {
            for (int i = 0; i < Transaksi.jsonArray.length(); i++){

                jsonObject = Transaksi.jsonArray.getJSONObject(i);

                if (jsonObject.getString("nominalDiskon").equals("0")){

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

                            arrayList.add(new ModelProdukFree(name, value));
                        }
                        System.out.println(arrayList.size()+"sjkd");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("aaa");
                    dataModels.add(new ModelC(namaProduk, jumlahPesanan, hargaProduk, totalHargaProduk, arrayList));
                } else {

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

        backTOMainTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PembayaranActivity.this, PilihVoucherActivity.class);
                startActivity(intent);
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

class ModelProdukFree{

    String namaProduk;
    String valueProduk;

    public ModelProdukFree(String namaProduk, String valueProduk) {
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
    ArrayList<ModelProdukFree> listProdukFreeDeal;

    public ModelC(String namaProduk, String valueProduk, String hargaProduk, String totalHargaBarang, ArrayList<ModelProdukFree> listProdukFreeDeal) {
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
    public ArrayList<ModelProdukFree> getListProdukFreeDeal() {
        return listProdukFreeDeal;
    }
    public String getValueProduk() {
        return valueProduk;
    }
}