package com.example.yyy.jsontest;

import android.text.format.Time;
import android.util.Log;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by YYY on 2017/7/31.
 */

public class Service {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String jsonString;
    String url = "http://192.168.0.136:5000/";
    String urlpic = "192.168.0.124";
    int port = 5556;

    Time t;
    int year,month,day,date;

    public Service(String jsonString) {
        this.jsonString = jsonString;
    }

    public Service() {
        t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间
        this.year = t.year;
        this.month = t.month + 1;
        this.day = t.monthDay;
        this.date = t.yearDay;
    }

    public int getDate() {
        return date;
    }

    //传json(添加食物)
    public void postfoodArray(final ArrayList<Food> foodlist, final String uid, final List<Integer> tplist)
    {

        final JSONArray aofa1 = new JSONArray();
        final JSONArray aofa2 = new JSONArray();
        final JSONArray aofa3 = new JSONArray();
        final JSONArray aofa4 = new JSONArray();
        for(int i = 0 ; i < tplist.size() ; i ++) {
            switch (tplist.get(i)) {
                case 1:
                    JSONArray foodArray = new JSONArray();
                    foodArray.put(foodlist.get(i).getName());
                    foodArray.put(foodlist.get(i).getamount());
                    foodArray.put("g");
                    aofa1.put(foodArray);
                    break;
                case 2:
                    JSONArray foodArray2 = new JSONArray();
                    foodArray2.put(foodlist.get(i).getName());
                    foodArray2.put(foodlist.get(i).getamount());
                    foodArray2.put("g");
                    aofa2.put(foodArray2);
                    break;
                case 3:
                    JSONArray foodArray3 = new JSONArray();
                    foodArray3.put(foodlist.get(i).getName());
                    foodArray3.put(foodlist.get(i).getamount());
                    foodArray3.put("g");
                    aofa3.put(foodArray3);
                    break;
                case 4:
                    JSONArray foodArray4 = new JSONArray();
                    foodArray4.put(foodlist.get(i).getName());
                    foodArray4.put(foodlist.get(i).getamount());
                    foodArray4.put("g");
                    aofa4.put(foodArray4);
                    break;
            }
        }

        if(aofa1.length()>0)
            new Thread() {
                @Override
                public void run() {
                    JSONObject jsonObject1 = new JSONObject();
                    try {
                        jsonObject1.put("food", aofa1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonString = jsonObject1.toString();
                    postJson(jsonString,uid,1);
                }
            }.start();
        if(aofa2.length()>0)
            new Thread() {
                @Override
                public void run() {
                    JSONObject jsonObject2 = new JSONObject();
                    try {
                        jsonObject2.put("food", aofa2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonString = jsonObject2.toString();
                    postJson(jsonString,uid,2);
                }
            }.start();
        if(aofa3.length()>0)
            new Thread() {
                @Override
                public void run() {
                    JSONObject jsonObject3 = new JSONObject();
                    try {
                        jsonObject3.put("food", aofa3);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonString = jsonObject3.toString();
                    postJson(jsonString,uid,3);
                }
            }.start();
        if(aofa4.length()>0)
            new Thread() {
                @Override
                public void run() {
                    JSONObject jsonObject4 = new JSONObject();
                    try {
                        jsonObject4.put("food", aofa4);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonString = jsonObject4.toString();
                    postJson(jsonString,uid,4);
                }
            }.start();
    }

    //传json(添加食物)
    public void postfood(Food food, final String uid,final int tp)
    {
        //创建JsonArray方法2

        JSONArray foodArray = new JSONArray();
        foodArray.put(food.getName());
        foodArray.put(food.getamount());
        foodArray.put("g");
        JSONArray aofa = new JSONArray();
        aofa.put(foodArray);

        JSONObject jsonObject2 = new JSONObject();
        try {
            jsonObject2.put("food", aofa);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonString = jsonObject2.toString();
        new Thread() {
            @Override
            public void run() {
                postJson(jsonString,uid,tp);
            }
        }.start();
    }

    private void postJson(String json,String uid, int tp) {
        //申明给服务端传递一个json串
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        //json为String类型的json数据
        RequestBody requestBody = RequestBody.create(JSON, json);

        //创建一个请求对象
        Request request = new Request.Builder()
                .url(url+"addFoodRec?" +
                        "uid=" + uid +
                        "&year=" + year +
                        "&month=" + month +
                        "&day=" + day +
                        "&tp=" + tp)
                .post(requestBody)
                .build();
        //发送请求获取响应
        try {
            Response response = okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if(response.isSuccessful()){
                //打印服务端返回结果
                Log.e("responseBODY",response.body().string());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void postPic(File file) {
//        int length = 0;
//        byte[] sendBytes = null;
//        Socket socket = null;
//        DataOutputStream dos = null;
//        FileInputStream fis = null;
//        try {
//            try {
//                socket = new Socket();
//                socket.connect(new InetSocketAddress(urlpic, port),
//                        10 * 1000);
//                dos = new DataOutputStream(socket.getOutputStream());
//                //File file = new File("C:\\Users\\JP\\Pictures\\head.jpg");
//                fis = new FileInputStream(file);
//                int filelen = fis.available();
//                sendBytes = new byte[1024];
//                while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
//                    dos.write(sendBytes, 0, length);
//                    dos.flush();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (dos != null)
//                    dos.close();
//                if (fis != null)
//                    fis.close();
//                if (socket != null)
//                    socket.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Socket socket;
        try {
            socket = new Socket(urlpic,port);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

//            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            //读取图片到ByteArrayOutputStream
//            bitmap.compress(Bitmap.CompressFormat.PNG, 10, baos);
//            byte[] bytes = baos.toByteArray();
            byte[] bytes = getBytes(file.getPath());
            out.writeInt(bytes.length);
            out.write(bytes);
            //System.out.println("bytes--->"+bytes);
            out.close();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] getBytes(String filePath){
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public int gettp() {
//        int year = t.year;
//        int month = t.month;
//        int date = t.monthDay;
        int hour = t.hour;    // 0-23

        int a = hour+2;
        int tp = a/6; //4-9早1 10-15午2 16-21晚3 22-3加餐4

        if(tp == 0) {
            return 4;

        } else return tp;
    }
}
