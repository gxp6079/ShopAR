package com.blade.ShopAR;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

//import com.google.gson.stream.JsonReader;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonToken;
import com.google.gson.*;

public class WegmansFactory extends Thread {
    private String upc;
    public WegmansData data;
    private Done callBack;

    public WegmansFactory(String upc, Done callBack) {
        this.upc = upc;
        this.data = new WegmansData();
        this.callBack = callBack;
    }

    @Override
    public void run() {
        this.data = this.upcToData(this.upc);
        this.callBack.onDone(this.data);
    }
    
    private static enum Request_Type {barcode, product, price, meal, recipe, stores}

    private static final String URL_STUB = "https://api.wegmans.io/";
    private static final String KEY = "subscription-key=2d6e9a8181bf41c09a41bc6b6ec87c4e";

    private static final String BARCODE_URL = URL_STUB + "products/barcodes/%s?api-version=2018-10-18&" + KEY;
    private static final String PRODUCT_URL = URL_STUB + "products/%s?api-version=2018-10-180&" + KEY;
    private static final String PRICES_URL = URL_STUB + "products/%s/prices?api-version=2018-10-18&" + KEY;


    public WegmansData upcToData(String barcode) {
        WegmansData data = new WegmansData();
        try {
            barcode = barcode.substring(1, barcode.length() - 1);
            System.out.println("UPC: " + barcode);
            String barcodeURL = makeURL(barcode, Request_Type.barcode);
            System.out.println(barcodeURL);
            Map<String, Object> barcodeInfo = getJSONResponse(barcodeURL);
            data.setUPC(barcode);
            String sku = barcodeInfo.get("sku").toString();
            sku = sku.substring(0, sku.length() - 2);
            data.setSKU(sku);

//            String productURL = makeURL(sku, Request_Type.product);
//            Map<String, Object> productInfo = getJSONResponse(productURL);

            String priceURL = makeURL(sku, Request_Type.price);

            Map<String, Object> priceInfo = getJSONResponse(priceURL);
            Double price = (Double) ((ArrayList<LinkedTreeMap<String, Object>>) priceInfo.get("stores")).get(0).get("price");

            data.setPrice(price.toString());
        } catch (IOException e) {
            return null;
        }

        return data;
    }


    public static String makeURL(String input, Request_Type r) {
        String url = null;
        switch (r) {
            case barcode:
                url = BARCODE_URL;    //Uses Barcode
                break;
            case product:
                url = PRODUCT_URL;    //Uses SKU
                break;
            case price:
                url = PRICES_URL;
                break;
        }
        return String.format(url, input);
    }

    // gets the data from a url and returns it in a string
    public Map<String, Object> getJSONResponse(String url) throws IOException {
        URL requestURL = new URL(url);
        HttpURLConnection con = (HttpURLConnection) requestURL.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");

        //Initialized Buffered Reader and JSONObject
        Map<String, Object> jsonResponses;
        BufferedReader bReader = null;
        System.out.println(con.getResponseCode());

        String encoding = con.getHeaderField("Content-Encoding");
        boolean gzipped = encoding != null && encoding.toLowerCase().contains("gzip");


        if (gzipped) {
            try {                       //GZip data
                InputStream in = con.getInputStream();
                GZIPInputStream gzp = new GZIPInputStream(in);
                bReader = new BufferedReader(new InputStreamReader(gzp));
            } catch (Exception e) {     // Buffered Reader error
                e.printStackTrace();
                return null;
            } finally {

                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;    //Goes through the stream and builds a string
                while ((inputStr = bReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);

                Gson gson = new Gson();
                jsonResponses = gson.fromJson(responseStrBuilder.toString(), Map.class);

                bReader.close();
            }
        } else {
            try {
                bReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {

                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;    //Goes through the stream and builds a string
                while ((inputStr = bReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);

                Gson gson = new Gson();
                jsonResponses = gson.fromJson(responseStrBuilder.toString(), Map.class);

                bReader.close();
            }
        }

        con.disconnect();

        return jsonResponses;
    }
}

class WegmansData {

    private String UPC;
    private String SKU;
    private String Price;

    public WegmansData() {
        this.UPC = null;
        this.SKU = null;
        this.Price = null;
    }

    public WegmansData(String upc, String sku, String price) {
        this.UPC = upc;
        this.SKU = sku;
        this.Price = price;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public void setPrice(String price) {
        this.Price = price;
    }

    public void setUPC(String UPC) {
        this.UPC = UPC;
    }

    public String getSKU() {
        return this.SKU;
    }

    public String getUPC() {
        return this.UPC;
    }

    public String getPrice() {
        return this.Price;
    }
}
