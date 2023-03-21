package com.example.projecttijarastore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.google.zxing.Result;
import com.journeyapps.barcodescanner.CaptureActivity;

public class ScanBarcodeActivity extends CaptureActivity {
//    private ZXingScannerView Scanner;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Scanner = new ZXingScannerView(this);
//        setContentView(Scanner);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Scanner.setResultHandler(this);
//        Scanner.startCamera();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Scanner.stopCamera();
//    }
//
//    @Override
//    public void handleResult(Result result) {
//        // KODE YANG DIKIRIM KE ACTIVITY LAIN
//        Intent intent = new Intent();
//        intent.putExtra("result", result.getText());
//        setResult(RESULT_OK, intent);
//        finish();
//    }
}