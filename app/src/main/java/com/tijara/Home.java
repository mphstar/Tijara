package com.tijara;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class Home extends AppCompatActivity {

    FlexboxLayout to_transaksi, to_return;
    TextView nama, txt_pemasukan, txt_terjual_pria, txt_terjual_wanita, txt_terjual_anak;
    static String name;

    private void getPemasukan(){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, "http://192.168.107.64:8000/api/pemasukan_hari_ini?apikey=" + Env.API_KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONObject result = new JSONObject(obj.getString("result"));
                    // Mengatur pattern format Rupiah
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
                    symbols.setGroupingSeparator('.');

                    DecimalFormat formatRupiah = new DecimalFormat("###,###", symbols);

                    txt_pemasukan.setText("Rp. " + formatRupiah.format(Integer.valueOf(result.getString("total_penjualan_harian"))));
                    JSONArray total_terjual = new JSONArray(result.getString("total_pakaian_terjual"));
                    JSONObject json_terjual;
                    String kategori[] = {"pria", "wanita", "anak"};
                    String terjual[] = {"0", "0", "0"};

                    for (int i = 0; i< total_terjual.length(); i++){
                        json_terjual = total_terjual.getJSONObject(i);
                        for (int k = 0; k < kategori.length; k++){
                            if(kategori[k].equals(json_terjual.getString("kategori"))){
                                terjual[k] = json_terjual.getString("total_terjual");
                            }
                        }
                    }

                    Toast.makeText(Home.this, terjual[0], Toast.LENGTH_LONG).show();

                    TextView v[] = {txt_terjual_pria, txt_terjual_wanita, txt_terjual_anak};
                    for (int j = 0; j < v.length; j++){
                        v[j].setText(terjual[j]);
                    }

                } catch (Exception e){
                    Toast.makeText(Home.this, "Error JSON", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Home.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        queue.add(request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        to_transaksi = findViewById(R.id.to_transaksi);
        to_return = findViewById(R.id.to_return);
        nama = findViewById(R.id.nama_user);
        txt_pemasukan = findViewById(R.id.txt_pemasukan);
        txt_terjual_pria = findViewById(R.id.txt_terjual_pria);
        txt_terjual_wanita = findViewById(R.id.txt_terjual_wanita);
        txt_terjual_anak = findViewById(R.id.txt_terjual_anak);

        to_transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Transaksi.class);
                startActivity(intent);
                if (Transaksi.dataModels != null){
                    if (ambilValues.namaProduk != null){
                        System.out.println("adadad");
                        Transaksi.view = 2;
                    } else if (ambilValues.nama_produk == null){
                        Transaksi.dataModels = null;
                    }
                } else if (ambilValues.namaProduk == null){
                    Transaksi.dataModels = null;
                    System.out.println("bbbbbbbbbbbbb");
                }
            }
        });

        getPemasukan();

//        try {
//            JSONObject jsonObject = new JSONObject(Preferences.getLoggedInUser(getBaseContext()));
//            name = jsonObject.getString("nama");
//            System.out.println(jsonObject);
//            nama.setText("Hello "+name);
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
    }
}