package com.example.yyy.jsontest;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class FatSecretGet {
    /**
     * FatSecret Authentication
     * http://platform.fatsecret.com/api/default.aspx?screen=rapiauth
     * Reference
     * https://github.com/ethan-james/cookbox/blob/master/src/com/vitaminc4/cookbox/FatSecret.java
     */
    final static private String APP_METHOD = "GET";
    final static private String APP_KEY = "7e3cdfadb0404f4bad1ca1b3194ae456";
    static private String APP_SECRET = "10f660a97a2941fa902dbd77a7582bbf";
    final static private String APP_URL = "http://platform.fatsecret.com/rest/server.api";
    private static Context context;

    public static JSONObject getFood(Long f) {
        List<String> params = new ArrayList<String>(Arrays.asList(generateOauthParams()));
        String[] template = new String[1];
//        params.add("method=foods.search");
//        params.add("search_expression=" + Uri.encode(f));
//        params.add("oauth_signature=" + sign(APP_METHOD, APP_URL, params.toArray(template)));
        params.add("method=food.get");
        params.add("food_id=" + f);
        params.add("oauth_signature=" + sign(APP_METHOD, APP_URL, params.toArray(template)));

        JSONObject food = null;
        try {
            URL url = new URL(APP_URL + "?" + paramify(params.toArray(template)));
            URLConnection api = url.openConnection();
            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(api.getInputStream()));
            while ((line = reader.readLine()) != null)
                builder.append(line);
            JSONObject foodGet = new JSONObject(builder.toString());
            food = foodGet.getJSONObject("food");
        } catch (Exception e) {
            Log.w("Fit", e.toString());
            e.printStackTrace();
        }
        return food;
    }

    public static String[] generateOauthParams() {
        String[] a = {
                "oauth_consumer_key=" + APP_KEY,
                "oauth_signature_method=HMAC-SHA1",
                "oauth_timestamp=" + new Long(System.currentTimeMillis() / 1000).toString(),
                "oauth_nonce=" + nonce(),
                "oauth_version=1.0",
                "format=json"
        };
        return a;
    }

    public static String sign(String method, String uri, String[] params) {
        String[] p = {method, Uri.encode(uri), Uri.encode(paramify(params))};
        String s = join(p, "&");
        APP_SECRET+="&";
        SecretKey sk = new SecretKeySpec(APP_SECRET.getBytes(), "HmacSHA1");
        APP_SECRET = APP_SECRET.substring(0, APP_SECRET.length()-1);
        try {
            Mac m = Mac.getInstance("HmacSHA1");
            m.init(sk);
            String signature = Uri.encode(new String(Base64.encode(m.doFinal(s.getBytes()), Base64.DEFAULT)).trim());
            // Log.w("Cookbox", "signature: " + signature);
            return signature;
        } catch (java.security.NoSuchAlgorithmException e) {
            Log.w("Cookbox", e.getMessage());
            return null;
        } catch (java.security.InvalidKeyException e) {
            Log.w("Cookbox", e.getMessage());
            return null;
        }
    }

    public static String paramify(String[] params) {
        String[] p = Arrays.copyOf(params, params.length);
        Arrays.sort(p);
        return join(p, "&");
    }

    public static String join(String[] array, String separator) {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) b.append(separator);
            b.append(array[i]);
        }
        return b.toString();
    }

    public static String nonce() {
        Random r = new Random();
        StringBuffer n = new StringBuffer();
        for (int i = 0; i < r.nextInt(8) + 2; i++) n.append(r.nextInt(26) + 'a');
        return n.toString();
    }
}