package com.example.yyy.jsontest;


import android.content.Intent;
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

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class ShowPicActivity extends AppCompatActivity {
    Bundle bundle;
    Bitmap bitmap;

    ImageView imageView;
    Button postbutton,affirmButton;
    ProgressBar mProgressBar;
    FrameLayout picframeLayout;

    //TagView tagView;
    ArrayList<Food> preList,postList;
    ArrayList<TagView> viewlist;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        在manifests声明
//         <activity android:name=".Main2Activity"
//        android:theme="@style/Theme.AppCompat.NoActionBar"/>

        setContentView(R.layout.activity_showpic);


        picframeLayout = (FrameLayout) findViewById(R.id.frameLayout);
        imageView = (ImageView) findViewById(R.id.imageView1);
        bundle = getIntent().getExtras();
        postbutton = (Button)findViewById(R.id.postButton);
        affirmButton = (Button)findViewById(R.id.affirmButton);
        mProgressBar = (ProgressBar)findViewById(R.id.processBar);

        if (bundle != null) {
            //album
            path = (String) bundle.get("path");
            int degree = (int) bundle.get("degree");
            if (path != null) {
                if (degree != 0) {
                    // 旋转图片
                    Matrix m = new Matrix();
                    m.postRotate(degree);
                    bitmap = BitmapFactory.decodeFile(path);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                            bitmap.getHeight(), m, true);
                } else {
                    bitmap = BitmapFactory.decodeFile(path);
                }
                imageView.setImageBitmap(bitmap);
            }

            postbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new GetNetWork().execute();   //向服务器发送图片
                }
            });

            affirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postList.clear();
                    for (int i = 0 ; i < viewlist.size() ; i++) {
                        if(viewlist.get(i).isShown()) {
                            postList.add(preList.get(i));
                        }
                    }
                    Intent intent = new Intent(ShowPicActivity.this,AddDelFoodActivity.class);
                    intent.putExtra("postlist",postList);
                    startActivity(intent);
                }
            });
        }
    }

/*
* 设置控件所在的位置YY，并且不改变宽高，
* XY为绝对位置
*/
    public static void setLayout(View view,int x,int y) {
        ViewGroup.MarginLayoutParams margin=new ViewGroup.MarginLayoutParams(view.getLayoutParams());
//        margin.setMargins(x,y, x+margin.width, y+margin.height);
        margin.setMargins(x,y, 0, 0);//cnmlgb
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }

    //内部类
    //向服务器发送图片  http://blog.csdn.net/guolin_blog/article/details/11711405
    class GetNetWork extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //开始前的准备工作
            mProgressBar.setVisibility(View.VISIBLE);
            preList = new ArrayList<>();
            postList = new ArrayList<>();
            viewlist = new ArrayList<>();
            preList.add(new Food("foodnameDemo0",100,2,3,4,"desstring0",100,34));
            preList.add(new Food("foodnameDemo1",300,2,3,4,"desstring1",100,35));
            preList.add(new Food("foodnameDemo2",500,2,3,4,"desstring2",100,36));
            //preList.add(new Food("foodnameDemo3",1,2,3,4,"desstring3"));
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
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            Service service = new Service();
            service.postPic(new File(path));

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
            postbutton.setVisibility(View.GONE);
            TagView.LayoutParams tagparams = new TagView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            for(int i = 0 ; i < preList.size(); i++) {
                TagView tagView = new TagView(ShowPicActivity.this, preList.get(i));
                viewlist.add(tagView);//用以确认键作判断

                picframeLayout.addView(tagView, tagparams);
                int[] location = new int[2];//获得当前图片的位置（左上角）
                imageView.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
                setLayout(tagView,location[0]+i*300,location[1]+i*300);
            }
            affirmButton.setVisibility(View.VISIBLE);
        }
    }
}

