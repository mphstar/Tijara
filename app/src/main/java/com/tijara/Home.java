package com.tijara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnections;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.google.android.flexbox.FlexboxLayout;
import com.tijara.async.AsyncBluetoothEscPosPrint;
import com.tijara.async.AsyncEscPosPrint;
import com.tijara.async.AsyncEscPosPrinter;
import com.tijara.keranjang.DataKeranjang;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.function.IntFunction;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Home extends AppCompatActivity {

    FlexboxLayout to_transaksi, to_return;
    TextView nama, txt_pemasukan, txt_terjual_pria, txt_terjual_wanita, txt_terjual_anak, btn_logout, txt_device;
    LinearLayout btn_bluetooth;

    ImageView ic_menu, ic_home;
    FrameLayout container_menu, bg_transparant, ic_warning;
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private static final int REQUEST_BLUETOOTH_PERMISSION = 2;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice printerDevice;

    ArrayList<String> listBluetooth = new ArrayList<>();
    ArrayList<String> listBluetoothAddress = new ArrayList<>();
    boolean isShowMenu = false;
    static String name;
    SwipeRefreshLayout refreshLayout;

    CustomDialogSetup mDialog;

    private void setupDialog(CustomDialog type){
        mDialog = new CustomDialogSetup(this, R.style.dialog, type);
    }

    private void getPemasukan(){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, Env.BASE_URL + "pemasukan_hari_ini?apikey=" + Env.API_KEY, new Response.Listener<String>() {
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

//                    Toast.makeText(Home.this, terjual[0], Toast.LENGTH_LONG).show();

                    TextView v[] = {txt_terjual_pria, txt_terjual_wanita, txt_terjual_anak};
                    for (int j = 0; j < v.length; j++){
                        v[j].setText(terjual[j]);
                    }

                } catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setupDialog(CustomDialog.WARNING);
                mDialog.setJudul("Informasi");
                mDialog.setDeskripsi("Tidak ada koneksi");
                mDialog.setListenerOK(v -> {
                    mDialog.dismiss();
                });
                mDialog.show();
            }
        });

        queue.add(request);
    }


    BluetoothDevice printdevice;

    public interface OnBluetoothPermissionsGranted {
        void onPermissionsGranted();
    }

    public static final int PERMISSION_BLUETOOTH = 1;
    public static final int PERMISSION_BLUETOOTH_ADMIN = 2;
    public static final int PERMISSION_BLUETOOTH_CONNECT = 3;
    public static final int PERMISSION_BLUETOOTH_SCAN = 4;

    public OnBluetoothPermissionsGranted onBluetoothPermissionsGranted;

    private BluetoothConnection selectedDevice;

    /**
     * Asynchronous printing
     */
    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss");
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 302, 76f, 48);
        StringBuilder s = new StringBuilder();
        s.append("\n");
        s.append("\n");
        s.append("[C]<b><font size='big'>TIJARA STORE</font></b>\n");
        s.append(""+ manipulationAlignCenter("Alamat: Jl. Kaca Terbang No.55, Jember Utara, Garahan, Kec. Silo, Kabupaten Jember, Jawa Timur 68117") +"\n\n");
        s.append("================================================\n");
        s.append("[L]Tanggal: " + format.format(new Date()) + "[R]Kasir: " + Preferences.getLoggedInUser(Home.this) + "\n");
        s.append("================================================\n");
        s.append("[L]<b>Nama</b>[R]<b>QTY</b>[R]<b>Subtotal</b>[R]Total\n\n");
        for (int i = 0; i < DataKeranjang.dataKeranjang.size(); i++){
            s.append("[L]"+manipulationWidth(DataKeranjang.dataKeranjang.get(i).getNama())+"[R]"+DataKeranjang.dataKeranjang.get(i).getQty()+"[R]"+Env.formatRupiah(DataKeranjang.dataKeranjang.get(i).getHarga())+"[R]"+DataKeranjang.dataKeranjang.get(i).getHarga() * DataKeranjang.dataKeranjang.get(i).getQty()+"\n");
            if(DataKeranjang.dataKeranjang.get(i).getDiskon() != null){
                switch (DataKeranjang.dataKeranjang.get(i).getDiskon().getKategori()){
                    case NOMINAL:
                        s.append("[L]<b>Diskon</b>[R]<b>"+Env.formatRupiah(Integer.parseInt(DataKeranjang.dataKeranjang.get(i).getDiskon().getNominal()))+"</b>[R]<b>"+Env.formatRupiah(Integer.parseInt(DataKeranjang.dataKeranjang.get(i).getDiskon().getNominal()))+"</b>[R]<b>"+Env.formatRupiah(DataKeranjang.dataKeranjang.get(i).getQty() * Integer.parseInt(DataKeranjang.dataKeranjang.get(i).getDiskon().getNominal()))+"</b>\n");
                        break;
                    case PERSEN:
                        s.append("[L]<b>Diskon</b>[R]<b>"+Integer.parseInt(DataKeranjang.dataKeranjang.get(i).getDiskon().getNominal()) + "%" + "</b>[R]<b>"+Env.formatRupiah((DataKeranjang.dataKeranjang.get(i).getHarga() / 100 * Integer.parseInt(DataKeranjang.dataKeranjang.get(i).getDiskon().getNominal())))+"</b>[R]<b>"+Env.formatRupiah(DataKeranjang.dataKeranjang.get(i).getQty() * (DataKeranjang.dataKeranjang.get(i).getHarga() / 100 * Integer.parseInt(DataKeranjang.dataKeranjang.get(i).getDiskon().getNominal())))+"</b>\n");
                        break;
                    case FREE_PRODUK:
                        s.append("[L]<b>Diskon</b>[R]<b>Buy " + DataKeranjang.dataKeranjang.get(i).getDiskon().getBuy() + " Free " + DataKeranjang.dataKeranjang.get(i).getDiskon().getFree() + "</b>[R]\n");
                        break;
                }
            }
        }

        s.append("\n\n");
        s.append("------------------------------------------------\n");
        int total = 0;
        for (int i = 0; i < DataKeranjang.dataKeranjang.size(); i++){
            total += DataKeranjang.dataKeranjang.get(i).getSubtotal();;
        }
        s.append("[L]Total Item[R]"+DataKeranjang.dataKeranjang.size()+"[R] [R]" + Env.formatRupiah(total) + "\n");
        s.append("[L]Voucher[R]20.000\n");
        s.append("[L]Total Harga[R]460.000\n");
        s.append("[L]Tunai[R]500.000\n");
        s.append("[L]Kembalian[R]40.000\n");
        s.append("================================================\n\n\n");
        s.append("[C]<barcode type='128' width='62' text='below'>TR0012133332431</barcode>\n\n");
        s.append("[C]Kritik&Saran: mphstar@gmail.com\n");
        s.append("[C]SMS/WA: 081233764580\n\n");

        return printer.addTextToPrint(s.toString());
    }

    private String manipulationWidth(String text){
        StringBuilder sb = new StringBuilder();
        int currentIndex = 0;

        while (currentIndex < text.length()) {
            sb.append(text.substring(currentIndex, Math.min(currentIndex + 18, text.length())));
            currentIndex += 18;
            if (currentIndex < text.length()) {
                sb.append("\n");

                while (currentIndex < text.length() && text.charAt(currentIndex) == ' ') {
                    currentIndex++;
                }
            }
        }

        return sb.toString();
    }

    private String manipulationAlignCenter(String text){
        StringBuilder sb = new StringBuilder();
        int currentIndex = 0;

        sb.append("[C]");

        while (currentIndex < text.length()) {
            sb.append(text.substring(currentIndex, Math.min(currentIndex + 38, text.length())));
            currentIndex += 38;

            if (currentIndex < text.length()) {
                sb.append("\n[C]");
            }
        }

        String formattedString = sb.toString();
        return formattedString;
    }


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        loadListBluetooth();
        refreshLayout = findViewById(R.id.refreshLayout);
        ic_warning = findViewById(R.id.ic_warning);
        txt_device = findViewById(R.id.txt_device);
        to_transaksi = findViewById(R.id.to_transaksi);
        to_return = findViewById(R.id.to_return);
        nama = findViewById(R.id.nama_user);
        txt_pemasukan = findViewById(R.id.txt_pemasukan);
        txt_terjual_pria = findViewById(R.id.txt_terjual_pria);
        txt_terjual_wanita = findViewById(R.id.txt_terjual_wanita);
        txt_terjual_anak = findViewById(R.id.txt_terjual_anak);
        container_menu = findViewById(R.id.container_menu);
        btn_logout = findViewById(R.id.btn_logout);
        ic_menu = findViewById(R.id.ic_menu);
        bg_transparant = findViewById(R.id.bg_transparant);
        btn_bluetooth = findViewById(R.id.btn_bluetooth);
        ic_home = findViewById(R.id.ic_home);

        if(!Preferences.getCustomKey(this, "device_address").equals("")){
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = adapter.getRemoteDevice(Preferences.getCustomKey(this, "device_address"));
            BluetoothConnection conn = new BluetoothConnection(device);

            if(conn != null){
                selectedDevice = conn;
                txt_device.setText(selectedDevice.getDevice().getName());
                txt_device.setTextColor(Color.parseColor("#176B00"));
                ic_warning.setVisibility(View.GONE);
            } else {
                txt_device.setText("Device tidak terhubung");
                txt_device.setTextColor(Color.parseColor("#A20A0A"));
                ic_warning.setVisibility(View.VISIBLE);
            }
        }

        ic_home.setOnClickListener(view -> {
//            this.checkBluetoothPermissions(() -> {
//                new AsyncBluetoothEscPosPrint(
//                        this,
//                        new AsyncEscPosPrint.OnPrintFinished() {
//                            @Override
//                            public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
//                                Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
//                            }
//
//                            @Override
//                            public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
//                                Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");
//
//                            }
//                        }
//                )
//                        .execute(this.getAsyncEscPosPrinter(selectedDevice));
//            });
        });

        btn_bluetooth.setOnClickListener(view -> browseBluetoothDevice());

        nama.setText("Hello " + Preferences.getLoggedInUser(Home.this));

        bg_transparant.setOnClickListener(view -> {
            if(isShowMenu){
                container_menu.setVisibility(View.GONE);
                bg_transparant.setVisibility(View.GONE);
            } else {
                bg_transparant.setVisibility(View.VISIBLE);
                container_menu.setVisibility(View.VISIBLE);
            }

            isShowMenu = !isShowMenu;
        });

        ic_menu.setOnClickListener(view -> {
            if(isShowMenu){
                container_menu.setVisibility(View.GONE);
                bg_transparant.setVisibility(View.GONE);
            } else {
                bg_transparant.setVisibility(View.VISIBLE);
                container_menu.setVisibility(View.VISIBLE);
            }

            isShowMenu = !isShowMenu;
        });

        btn_logout.setOnClickListener(view -> {
            setupDialog(CustomDialog.CONFIRMATION);
            mDialog.setJudul("Keluar");
            mDialog.setDeskripsi("Apakah anda yakin ingin keluar");
            mDialog.setListenerTidak(v -> {
                mDialog.dismiss();
            });
            mDialog.setListenerOK(v -> {
                mDialog.dismiss();
                Preferences.clearLoggedInUser(Home.this);
                Intent intent = new Intent(Home.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });

            mDialog.show();

        });

        to_transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Transaksi.class);
                startActivity(intent);

            }
        });

        to_return.setOnClickListener(view -> {
            Intent inten = new Intent(Home.this, ActivityRetur.class);
            startActivity(inten);
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

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPemasukan();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void browseBluetoothDevice() {
        this.checkBluetoothPermissions(() -> {
            final BluetoothConnection[] bluetoothDevicesList = (new BluetoothConnections()).getList();
            

            if (bluetoothDevicesList != null) {
//                Toast.makeText(this, "aawawa", Toast.LENGTH_SHORT).show();
                final String[] items = new String[bluetoothDevicesList.length + 1];
                items[0] = "Default printer";
                int i = 0;
                for (BluetoothConnection device : bluetoothDevicesList) {
                    items[++i] = device.getDevice().getName();
                }

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(Home.this);
                alertDialog.setTitle("Bluetooth printer selection");
                alertDialog.setItems(
                        items,
                        (dialogInterface, i1) -> {
                            int index = i1 - 1;
                            if (index == -1) {
                                selectedDevice = null;
                            } else {
                                selectedDevice = bluetoothDevicesList[index];
                            }
//                            Button button = (Button) findViewById(R.id.button_bluetooth_browse);
//                            button.setText(items[i1]);

                            if(selectedDevice != null){
                                Preferences.setCustomKey(this, "device_name", selectedDevice.getDevice().getName());
                                Preferences.setCustomKey(this, "device_address", selectedDevice.getDevice().getAddress());

                                txt_device.setText(selectedDevice.getDevice().getName());
                                txt_device.setTextColor(Color.parseColor("#176B00"));
                                ic_warning.setVisibility(View.GONE);
                            } else {
                                Preferences.deleteCustomKey(this, "device_name");
                                Preferences.deleteCustomKey(this, "device_address");

                                txt_device.setText("Device tidak terhubung");
                                txt_device.setTextColor(Color.parseColor("#A20A0A"));
                                ic_warning.setVisibility(View.VISIBLE);
                            }
                        }
                );

                android.app.AlertDialog alert = alertDialog.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();
            } else {
                setupDialog(CustomDialog.WARNING);
                mDialog.setJudul("Informasi");
                mDialog.setDeskripsi("Bluetooth tidak aktif");
                mDialog.setListenerOK(v -> {
                    mDialog.dismiss();
                });
                mDialog.show();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case Home.PERMISSION_BLUETOOTH:
                case Home.PERMISSION_BLUETOOTH_ADMIN:
                case Home.PERMISSION_BLUETOOTH_CONNECT:
                case Home.PERMISSION_BLUETOOTH_SCAN:
                    this.checkBluetoothPermissions(this.onBluetoothPermissionsGranted);
                    break;
            }
        }
    }

    private void checkBluetoothPermissions(OnBluetoothPermissionsGranted onBluetoothPermissionsGranted) {
        this.onBluetoothPermissionsGranted = onBluetoothPermissionsGranted;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, Home.PERMISSION_BLUETOOTH);
        } else if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, Home.PERMISSION_BLUETOOTH_ADMIN);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, Home.PERMISSION_BLUETOOTH_CONNECT);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, Home.PERMISSION_BLUETOOTH_SCAN);
        } else {
            this.onBluetoothPermissionsGranted.onPermissionsGranted();
        }
    }


}