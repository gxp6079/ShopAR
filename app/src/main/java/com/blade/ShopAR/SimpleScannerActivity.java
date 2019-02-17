package com.blade.ShopAR;

import android.app.ActionBar;
import android.app.Activity;
import android.drm.DrmStore;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.view.InputEvent;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.KeyEvent.KEYCODE_MENU;
import static com.google.android.gms.plus.PlusOneDummyView.TAG;

public class SimpleScannerActivity extends Activity implements ZXingScannerView.ResultHandler, KeyEvent.Callback {

    private TextView barcodeText;

    private ZXingScannerView mScannerView;

    private ShoppingCart shoppingCart;

    private String currentBarcode = null;

    public void onCreate(Bundle state) {

        super.onCreate(state);
        //List<BarcodeFormat> formats = new ArrayList<>();
        //formats.add(BarcodeFormat.CODE_39);
        //mScannerView.setFormats(formats);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        mScannerView.setAutoFocus(true);
        barcodeText = findViewById(R.id.main_text);
        setContentView(mScannerView);// Set the scanner view as the content view

        this.shoppingCart = new ShoppingCart();

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v(TAG, rawResult.getText()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        this.currentBarcode = rawResult.getText();
        // If you would like to resume scanning, call this method below:

    }

    public boolean onKeyUp (int keyCode, KeyEvent event){
        switch (keyCode){
            case KEYCODE_MENU:
                if(this.currentBarcode != null) {
                    shoppingCart.addToCart(WegmansFactory.upcToData(this.currentBarcode));
                    this.currentBarcode = null;
                    Log.v(TAG, "Total: " + shoppingCart.getTotal());
                }
                mScannerView.resumeCameraPreview(this);

                return true;
            case KEYCODE_ENTER:
                mScannerView.resumeCameraPreview(this);
            default:
                return super.onKeyUp(keyCode, event);
        }
    }
}
