package com.tijara;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Env {
    static String BASE_URL = "http://10.188.50.240:8000/api/";
    static  String API_KEY = "DWuqUHWDUhDQUDadaq";

    static String formatRupiah(int value){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');

        DecimalFormat formatRupiah = new DecimalFormat("###,###", symbols);
        return  formatRupiah.format(value);
    }
}
