package com.tijara.keranjang;

import org.json.JSONObject;

public class ModelKeranjang {
    String nama, pathImage, kode_br;
    int harga, subtotal, qty;
    ModelDiskon diskon;

    public JSONObject getAllData(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("nama", getNama());
            obj.put("gambar", getPathImage());
            obj.put("kode_br", getKode_br());
            obj.put("harga", getHarga());
            obj.put("qty", getQty());
            obj.put("subtotal", getSubtotal());

            if(getDiskon() == null){
                obj.put("diskon", "null");
            } else {
                obj.put("diskon", getDiskon().getAllData());
            }



        } catch (Exception e){
            System.out.println(e.toString());
        }

        return obj;
    }

    public ModelKeranjang(String kode_br, String nama, String pathImage, int harga, int subtotal, int qty) {
        this.kode_br = kode_br;
        this.nama = nama;
        this.pathImage = pathImage;
        this.harga = harga;
        this.subtotal = subtotal;
        this.qty = qty;
    }

    public ModelKeranjang(String kode_br, String nama, String pathImage, int harga, int subtotal, int qty, ModelDiskon diskon) {
        this.kode_br = kode_br;
        this.nama = nama;
        this.pathImage = pathImage;
        this.harga = harga;
        this.subtotal = subtotal;
        this.qty = qty;
        this.diskon = diskon;
    }

    public String getKode_br() {
        return kode_br;
    }

    public void setKode_br(String kode_br) {
        this.kode_br = kode_br;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public ModelDiskon getDiskon() {
        return diskon;
    }

    public void setDiskon(ModelDiskon diskon) {
        this.diskon = diskon;
    }
}
