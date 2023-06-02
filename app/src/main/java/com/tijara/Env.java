package com.tijara;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Env {
    static String BASE_URL = "http://192.168.0.4:8000/api/";
    static  String API_KEY = "DWuqUHWDUhDQUDadaq";

    static String formatRupiah(int value){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');

        DecimalFormat formatRupiah = new DecimalFormat("###,###", symbols);
        return  formatRupiah.format(value);
    }
}
