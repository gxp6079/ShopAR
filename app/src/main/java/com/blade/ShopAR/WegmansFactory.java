package com.blade.ShopAR;

import android.util.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.System.in;

public class WegmansFactory {

    public static void main(String[] args) throws IOException, JSONException {
        String url = makeURL("3400004025", Request_Type.barcode);
        JSONObject s = getJSONResponse(url);
        JSONObject s1 = getJSONResponse(URL_STUB + "/products/36794?api-version=2018-10-18" + "&subscription-key=2d6e9a8181bf41c09a41bc6b6ec87c4e");
        JSONObject s2 = getJSONResponse(URL_STUB + "/products/665368?api-version=2018-10-18" + "&" + KEY);
        JSONObject s3 = getJSONResponse(makeURL("484208", Request_Type.price));

        System.out.println(s.get("_links"));
        //System.out.println(s1.replace(',', '\n'));
    }

    // "/products/484208?api-version=2018-10-18"
    // "https://api.wegmans.io/meals/recipes/22187?api-version=2018-10-18&subscription-key=2d6e9a8181bf41c09a41bc6b6ec87c4e"

    private static enum Request_Type {barcode, product, price, meal, recipe, stores}
    private static final String URL_STUB = "https://api.wegmans.io/";
    private static final String KEY = "subscription-key=2d6e9a8181bf41c09a41bc6b6ec87c4e";

    private static final String BARCODE_URL = URL_STUB + "products/barcodes/%s?api-version=2018-10-18&" + KEY;
    private static final String PRODUCT_URL = URL_STUB + "products/%s?api-version=2018-10-180&" + KEY;
    private static final String PRICES_URL = URL_STUB + "products/%s/prices?api-version=2018-10-18" + KEY;

    public static String makeURL(String input, Request_Type r) {
        
        String url = null;
        switch(r) {
            case barcode:
                url = BARCODE_URL;    //Uses Barcode
                break;
            case product:
                url = PRODUCT_URL;    //Uses SKU
                break;
            case price:
                url = PRICES_URL;
                break;
            /*case meal:
                url = MEALS_URL;
                break;
            case recipe:
                url = RECIPES_URL;
                break;
            case stores:
                url = STORES_URL;
                break;
            Not iplemented yet, no use planned. Trash. */
        }
        return String.format(url,input);
    }

    //takes in url for a product
    // returns url to get prices for a product
    public static String makePriceURL(String given) {
        given.replace("?", "/prices?");
        return given;
    }

    // gets the data from a url and returns it in a string
    public static JSONObject getJSONResponse(String url) throws IOException, JSONException {
        URL requestURL = new URL(url);
        HttpURLConnection con = (HttpURLConnection) requestURL.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");

        //Initialized Buffered Reader and JSONObject
        JSONObject jsonResponse;
        BufferedReader bReader = null;

        try {                       //GZip data
            bReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(con.getInputStream())));
        } catch (ZipException z) {  //Basic JSON data
            bReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } catch (Exception e) {     // Buffered Reader error
            e.printStackTrace();
        } finally {

            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;    //Goes through the stream and builds a string
            while ((inputStr = bReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            // Converts a string into a JSONObject
            //JsonReader jReader = Json.createReader();
            jsonResponse = new JSONObject(responseStrBuilder.toString());

            bReader.close();
        }

        con.disconnect();

        return jsonResponse;
    }

}

class WegmansData {

    private String UPC;
    private String SKU;
    private String Price;

    public WegmansData(String upc, String sku, String price) {
        this.UPC = upc;
        this.SKU = sku;
        this.Price = price;
    }

}
