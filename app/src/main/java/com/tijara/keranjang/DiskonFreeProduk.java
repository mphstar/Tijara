package com.tijara.keranjang;

import android.util.Log;

import org.json.JSONObject;

public class DiskonFreeProduk {
    String nama, kode;
    int qty;

    public JSONObject getAllData(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("nama", getNama());
            obj.put("kode_br", getKode());
            obj.put("qty", getQty());
        } catch (Exception e){
            Log.d("Error", e.toString());
        }

        return  obj;
    }

    public DiskonFreeProduk(String nama, String kode, int qty) {
        this.nama = nama;
        this.kode = kode;
        this.qty = qty;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }


}
