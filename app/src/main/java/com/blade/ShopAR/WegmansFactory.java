package com.blade.ShopAR;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

public class WegmansFactory {
    public static void main(String[] args) throws IOException{
        String url = makeBarcodeURL("3400004025");
        String s = getInfo(url);
        String s1 = getInfo(urlStub + "/products/36794?api-version=2018-10-18" + "&subscription-key=2d6e9a8181bf41c09a41bc6b6ec87c4e");
        String s2 = getInfo(urlStub + "/products/665368?api-version=2018-10-18" + "&" + key);
        String s3 = getInfo(makeProductURL("/products/484208/prices?api-version=2018-10-18"));
        String m = "/products/484208?api-version=2018-10-18";


        System.out.println(s3.replace(',', '\n'));
        //System.out.println(s1.replace(',', '\n'));
    }

    // "/products/484208?api-version=2018-10-18"
    // "https://api.wegmans.io/meals/recipes/22187?api-version=2018-10-18&subscription-key=2d6e9a8181bf41c09a41bc6b6ec87c4e"

    private static enum url {barcode, product, price, meal, recipei, stores}
    private static final String urlStub = "https://api.wegmans.io/";
    private static final String key = "subscription-key=2d6e9a8181bf41c09a41bc6b6ec87c4e";

    // takes in barcode and makes valid url to search with
    public static String makeBarcodeURL(String code) {
        return (urlStub + "products/barcodes/" + code + "?api-version=2018-10-18&" + key);
    }

    // takes in partial url that you get from a barcode search
    // returns full url that you can search with
    public static String makeProductURL(String given) {
        return urlStub + given + "&" + key;
    }

    //takes in url for a product
    // returns url to get prices for a product
    public static String makePriceURL(String given) {
        given.replace("?", "/prices?");
        return given;
    }

    // gets the data from a url and returns it in a string
    public static String getInfo(String url) throws IOException {
        URL requestURL = new URL(url);
        HttpURLConnection con = (HttpURLConnection) requestURL.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");


        InputStream response = null;
        BufferedReader bReader = null;
        String inputLine;
        String content = "";

        try {
            response = con.getInputStream();
            bReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(response)));

            while ((inputLine = bReader.readLine()) != null) {
                content += inputLine;
            }
        } catch (ZipException z) {
            response = con.getInputStream();
            bReader = new BufferedReader(new InputStreamReader(response));

            while ((inputLine = bReader.readLine()) != null) {
                content += inputLine;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bReader.close();
            response.close();
        }

        con.disconnect();

        return content;
    }

}
