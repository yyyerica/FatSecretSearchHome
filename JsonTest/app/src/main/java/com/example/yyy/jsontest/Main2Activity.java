package com.example.yyy.jsontest;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main2Activity extends AppCompatActivity {
    Bundle bundle;
    Bitmap bitmap;
    ImageView drawImage;
    Button button;
    ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        在manifests声明
//         <activity android:name=".Main2Activity"
//        android:theme="@style/Theme.AppCompat.NoActionBar"/>

        setContentView(R.layout.activity_main2);

        drawImage = (ImageView)findViewById(R.id.drawpicImageView);
        ImageView imageView = (ImageView) findViewById(R.id.imageView1);
        bundle = getIntent().getExtras();
        button = (Button)findViewById(R.id.postButton);
        mProgressBar = (ProgressBar)findViewById(R.id.processBar);

        if (bundle != null) {
            //album
            String path = (String) bundle.get("path");
            int degree = (int) bundle.get("degree");
            if (path != null) {
                if (degree != 0) {
                    // 旋转图片
                    Matrix m = new Matrix();
                    m.postRotate(degree);
                    bitmap = BitmapFactory.decodeFile(path);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                            bitmap.getHeight(), m, true);
                } else  {
                    bitmap = BitmapFactory.decodeFile(path);
                }
                imageView.setImageBitmap(bitmap);

            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new GetNetWork().execute();   ////向服务器发送图片
                }
            });
        }
    }
    /*
     * 设置控件所在的位置XY，并且不改变宽高，
     * XY为绝对位置
     */
    public static void setLayout(View view, int x, int y)
    {
        ViewGroup.MarginLayoutParams margin=new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x,y, x+margin.width, y+margin.height);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }

    void showprocess(int value) {
        Toast.makeText(this,"update",Toast.LENGTH_LONG);
    }


    //内部类
    //向服务器发送图片  http://blog.csdn.net/guolin_blog/article/details/11711405
    class GetNetWork extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //开始前的准备工作
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
//            Socket socket;
//            try {
//                socket = new Socket("192.168.0.131",9999);
//                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                //读取图片到ByteArrayOutputStream
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                byte[] bytes = baos.toByteArray();
//                out.write(bytes);
//
//                System.out.println("bytes--->"+bytes);
//                out.close();
//                socket.close();
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "ok";

        }

        /**
         * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
         * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressBar.setVisibility(View.GONE);
            //mImageView.setImageBitmap(bitmap);
            Log.e("result",result);
            setLayout(drawImage,180,150);
            drawImage.setVisibility(View.VISIBLE);
        }


        /**
         * 这里的Intege参数对应AsyncTask中的第二个参数
         * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
         * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            int vlaue = values[0];
            //progressBar.setProgress(vlaue);
            showprocess(vlaue);
        }
    }
}

