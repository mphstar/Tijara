package com.tijara;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityDetailRetur extends AppCompatActivity {
    String[] jenis_kembalian = {"Tunai", "Produk"};
    int selectedJenis = 0;
    TextView field_jenis_kembalian, btn_pilih_produk, field_produk;
    EditText field_jumlah_barang_retur;

    FlexboxLayout layout_bayar_kurang, layout_kembalian_tunai, layout_bayar_tunai;
    int qty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rincian_barang_return);
        field_jenis_kembalian = findViewById(R.id.field_jenis_kembalian);
        field_jumlah_barang_retur = findViewById(R.id.field_jumlah_barang_retur);
        layout_bayar_kurang = findViewById(R.id.layout_bayar_kurang);
        layout_kembalian_tunai = findViewById(R.id.layout_kembalian_tunai);
        layout_bayar_tunai = findViewById(R.id.layout_bayar_tunai);
        btn_pilih_produk = findViewById(R.id.btn_pilih_produk);
        field_produk = findViewById(R.id.field_produk);

        String mystring = getIntent().getStringExtra("barang");
        try {
            JSONObject ob = new JSONObject(mystring);
            qty = Integer.valueOf(ob.getString("qty"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        field_jenis_kembalian.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDetailRetur.this);
            builder.setTitle("Jenis Pengembalian");
            builder.setSingleChoiceItems(jenis_kembalian, selectedJenis, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    selectedJenis = i;
                    dialogInterface.dismiss();
                    field_jenis_kembalian.setText(jenis_kembalian[i].toString());
                    if(selectedJenis == 0){
                        layout_bayar_kurang.setVisibility(View.GONE);
                        layout_kembalian_tunai.setVisibility(View.GONE);
                        layout_bayar_tunai.setVisibility(View.VISIBLE);
                    } else {
                        layout_bayar_kurang.setVisibility(View.GONE);
                        layout_kembalian_tunai.setVisibility(View.GONE);
                        layout_bayar_tunai.setVisibility(View.GONE);
                    }
                }
            });
            builder.show();
        });

        btn_pilih_produk.setOnClickListener(view -> {
            Intent inten = new Intent(ActivityDetailRetur.this, PilihBarangActivity.class);
            startActivityForResult(inten, 1);
        });


        field_jumlah_barang_retur.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    int number = Integer.parseInt(charSequence.toString());

                    // Periksa apakah angka berada dalam rentang yang diinginkan
                    if (number < 1 || number > qty) {
                        // Angka di luar rentang, hapus teks terakhir
                        field_jumlah_barang_retur.setText(charSequence.subSequence(0, charSequence.length() - 1));
                        field_jumlah_barang_retur.setSelection(field_jumlah_barang_retur.getText().length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String getdata = data.getStringExtra("data");
                try {
                    JSONObject obj = new JSONObject(getdata);
                    field_produk.setText(obj.getString("nama_br"));
                    Toast.makeText(this, obj.getString("nama_br"), Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Toast.makeText(this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
