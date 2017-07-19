package com.example.yyy.jsontest;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;


public class DietFragment extends Fragment {

    private RoundProgressBar mRoundProgressBar1,mRoundProgressBar2,mRoundProgressBar3;
    private int progress1 = 0 , progress2 = 0, progress3 = 0;

    final int PHOTO_REQUEST_GALLERY = 23;
    final int CAMERA_REQUEST = 343;

    LinearLayout ll;

    int carbohydrate = 0,fat = 0,protein = 0;

    public DietFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ll = (LinearLayout)inflater.inflate(R.layout.fragment_diet, container, false);
        initFAB(); //浮动按钮

        initProgressRing(53,23,42);//圆环
        //new GetNetWork().execute();

        // Inflate the layout for this fragment
        return ll;
    }


    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            //ImageView imageView = (ImageView) getActivity().findViewById(R.id.imgView);
            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        } else if(requestCode == CAMERA_REQUEST) {

            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            //img_show.setImageBitmap(bitmap);

        }
    }

    //浮动按钮
    void initFAB() {
        //浮动按钮 https://github.com/oguzbilgener/CircularFloatingActionMenu
        Drawable fabicon = getResources().getDrawable(R.drawable.addfab);
        ImageView icon = new ImageView(getContext()); // Create an icon
        icon.setImageDrawable(fabicon);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(getActivity())
                .setContentView(icon)
                .setPosition(3) //位置
                .build();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());

        //子按钮大小
        FrameLayout.LayoutParams Params =  new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        Params.height = 170;
        Params.width = 170;

        // repeat many times:
        ImageView itemIcon = new ImageView(getContext());
        Drawable fabicon1 = getResources().getDrawable(R.drawable.featheritem);
        itemIcon.setImageDrawable(fabicon1);
        SubActionButton button1 = itemBuilder.setContentView(itemIcon)
                .setLayoutParams(Params)
                .build();
        button1.setPadding(10,10,10,10);

        ImageView itemIcon2 = new ImageView(getContext());
        Drawable fabicon2 = getResources().getDrawable(R.drawable.picitem);
        itemIcon2.setImageDrawable(fabicon2);
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2)
                .setLayoutParams(Params)
                .build();
        button2.setPadding(10,10,10,10);

        ImageView itemIcon3 = new ImageView(getContext());
        Drawable fabicon3 = getResources().getDrawable(R.drawable.cameraitem);
        itemIcon3.setImageDrawable(fabicon3);
        SubActionButton button3 = itemBuilder.setContentView(itemIcon3)
                .setLayoutParams(Params)
                .build();
        button3.setPadding(10,10,10,10);

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .setStartAngle(120)
                .setEndAngle(240)
                .attachTo(actionButton)
                .build();

        //跳转页面
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),EditFoodActivity.class);
                startActivity(intent);
            }
        });

        //photo
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 激活系统图库，选择一张图片
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
            }
        });

        //camera
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(it,CAMERA_REQUEST);
            }
        });
    }

    //圆环
    void initProgressRing(int carbohydrate1,int fat1,int protein1) {
        mRoundProgressBar1 = (RoundProgressBar) ll.findViewById(R.id.roundProgressBar1);
        mRoundProgressBar2 = (RoundProgressBar) ll.findViewById(R.id.roundProgressBar2);
        mRoundProgressBar3 = (RoundProgressBar) ll.findViewById(R.id.roundProgressBar3);

        carbohydrate = carbohydrate1;
        fat = fat1;
        protein = protein1;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progress1 < carbohydrate) {
                    progress1 += 1;

                    mRoundProgressBar1.setProgress(progress1);

                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progress2 < fat) {
                    progress2 += 1;
                    mRoundProgressBar2.setProgress(progress2);

                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progress3 < protein) {
                    progress3 += 1;

                    mRoundProgressBar3.setProgress(progress3);

                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

//    public class GetNetWork extends AsyncTask<Void,Void,Void>
//    {
//        @Override
//        protected Void doInBackground(Void... params) {  //延时操作，比如sleep,也可以在里面写耗时操作
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) { //延时的同时，可以进行别的操作，比如圆形缓存条的转动
//            super.onPostExecute(aVoid);
//
//        }
//    }

}
