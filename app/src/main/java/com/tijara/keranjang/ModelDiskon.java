package com.tijara.keranjang;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ModelDiskon {
    KategoriDiskon kategori;

    KategoriFreeProduk kategori_free;
    String nominal;
    int buy, free;

    ArrayList<DiskonFreeProduk> detail_free_produk = new ArrayList<>();

    public JSONObject getAllData(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("kategori", getKategori());
            obj.put("kategori_free", getKategori_free());
            obj.put("nominal", getNominal());
            obj.put("buy", getBuy());
            obj.put("free", getFree());
            if(getKategori() == KategoriDiskon.FREE_PRODUK){
                JSONArray arr = new JSONArray();
                for (int i = 0; i < detail_free_produk.size(); i++){
                    arr.put(detail_free_produk.get(i).getAllData());
                }
                obj.put("detail_free_produk", arr);
            }
        } catch (Exception e){
            Log.d("error", e.toString());
        }

        return obj;
    }

    public KategoriFreeProduk getKategori_free() {
        return kategori_free;
    }

    public void setKategori_free(KategoriFreeProduk kategori_free) {
        this.kategori_free = kategori_free;
    }

    public ArrayList<DiskonFreeProduk> getDetail_free_produk() {
        return detail_free_produk;
    }

    public void setDetail_free_produk(ArrayList<DiskonFreeProduk> detail_free_produk) {
        this.detail_free_produk = detail_free_produk;
    }

    public ModelDiskon(KategoriDiskon kategori) {
        this.kategori = kategori;
    }

    public KategoriDiskon getKategori() {
        return kategori;
    }

    public void setKategori(KategoriDiskon kategori) {
        this.kategori = kategori;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public int getBuy() {
        return buy;
    }

    public void setBuy(int buy) {
        this.buy = buy;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }
}

