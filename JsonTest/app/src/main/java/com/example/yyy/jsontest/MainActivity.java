package com.example.yyy.jsontest;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    //OkHttpClient client = new OkHttpClient();  //新建客户端
    JSONObject obj;
    JSONArray jsonArray = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //api访问
        /**
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("search",FatSecretSearch.searchFood("rice",0).toString());
                try {
                    jsonArray = new JSONArray(FatSecretSearch.searchFood("rice",0).toString());
                    for (int i = 0; i < 1;i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        String food_description = jsonObject.optString("food_description");
                        //int age = jsonObject.optInt("age");
                        Log.e("eee",food_description);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        */
    }


}


