package com.example.yyy.jsontest;

import android.content.Intent;
import android.database.Cursor;

import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;

import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.yyy.jsontest.ProgressBar.RoundProgressBar;
import com.example.yyy.jsontest.ProgressBar.RoundProgressBar2;
import com.example.yyy.jsontest.ProgressBar.RoundProgressBar3;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.io.File;
import java.io.IOException;


public class DietFragment extends Fragment {

    private RoundProgressBar mRoundProgressBar1;
    private RoundProgressBar2 mRoundProgressBar2;
    private RoundProgressBar3 mRoundProgressBar3;
    private int progress1 = 0 , progress2 = 0, progress3 = 0;

    final int PHOTO_REQUEST_GALLERY = 23;
    final int CAMERA_REQUEST = 343;
    public static final String PICTURE_FILE="temp.jpg";

    RelativeLayout ll;

    int carbohydrate = 0,fat = 0,protein = 0;

    Button detailBu;

    private int screenWidth = 0;
    private int screenHeight = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ll = (RelativeLayout)inflater.inflate(R.layout.fragment_diet, container, false);
        init();

        initProgressRing(53,23,42);//圆环

        initFAB(); //浮动按钮

        return ll;
    }

    void init() {
        detailBu = (Button)ll.findViewById(R.id.detailsButton);
        detailBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),FoodDetailActivity.class);
                startActivity(intent);
            }
        });

//        Button addbu = (Button)ll.findViewById(R.id.AdddDelButton);
//        addbu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(),AddDelFoodActivity.class);
//                startActivity(intent);
//            }
//        });

        // 获取屏幕大小
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
    }

    //浮动按钮
    void initFAB() {
        //浮动按钮 https://github.com/oguzbilgener/CircularFloatingActionMenu
        Drawable fabicon = getResources().getDrawable(R.drawable.addfab);
        ImageView icon = new ImageView(getContext()); // Create an icon
        icon.setImageDrawable(fabicon);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(getActivity())
                .setContentView(icon)
                .setPosition(4) //位置
                .build();

        //((FloatingActionButton.LayoutParams) actionButton.getLayoutParams()).setMargins(0, 400, 30, 0);//偏移

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
//                .setStartAngle(120)
//                .setEndAngle(240)
                .attachTo(actionButton)
                .build();

        //跳转页面
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SearchFoodActivity.class);
                startActivity(intent);
            }
        });

        //photo
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");//相片类型
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
            }
        });

        //camera
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                    Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),PICTURE_FILE));
                    getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    // 这样每次调用相机拍照都会在sd卡根目录生成名为temp.jpg的图片，每次拍照都会覆盖旧的文件。这样的话就不能通过onActivityResult方法的intent参数获取照片数据，可以直接读取文件
                    // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
                    startActivityForResult(getImageByCamera, CAMERA_REQUEST);
                }
                else {
                    Toast.makeText(getActivity(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
                }
            }
        });

        //actionButton.setOnTouchListener(onDragTouchListener);//拖动
    }

//    View.OnTouchListener onDragTouchListener = new View.OnTouchListener() {
//        private float startX = 0;
//        private float startY = 0;
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN: {
//                    startX = event.getRawX();
//                    startY = event.getRawY();
//                    break;
//                }
//                case MotionEvent.ACTION_MOVE: {
//
//                    // 计算偏移量
//                    int dx = (int) (event.getRawX() - startX);
//                    int dy = (int) (event.getRawY() - startY);
//
//                    // 计算控件的区域
//                    int left = v.getLeft() + dx;
//                    int right = v.getRight() + dx;
//                    int top = v.getTop() + dy;
//                    int bottom = v.getBottom() + dy;
//
//                    // 超出屏幕检测
//                    if (left < 0) {
//                        left = 0;
//                        right = v.getWidth();
//                    }
//
//                    if (right > screenWidth) {
//                        right = screenWidth;
//                        left = screenWidth - v.getWidth();
//                    }
//
//                    if (top < 0) {
//                        top = 0;
//                        bottom = v.getHeight();
//                    }
//
//                    if (bottom > screenHeight) {
//                        bottom = screenHeight;
//                        top = screenHeight - v.getHeight();
//                    }
//
//                    v.layout(left, top, right, bottom);
//
//                    startX = event.getRawX();
//                    startY = event.getRawY();
//
//                    break;
//                }
//            }
//            return false;
//        }
//    };

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            //打开相册并选择照片，这个方式选择单张
// 获取返回的数据，这里是android自定义的Uri地址
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
// 获取选择照片的数据视图
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
// 从数据视图中获取已选择图片的路径
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            manageimage(picturePath);
        } else if(requestCode == CAMERA_REQUEST) {
            File f = new File(Environment.getExternalStorageDirectory()
                    + "/" + PICTURE_FILE);
            String filePath = f.getPath();
            manageimage(filePath);

        }
    }

    //调正照片角度
    void manageimage(String filePath) {
        //根据图片的filepath获取到一个ExifInterface的对象
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }
        //获取图片的方向角度：
        int degree=0;
        if (exif != null) {
            // 读取图片中相机方向信息
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            // 计算旋转角度
            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
                    break;
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString("path",filePath);
        bundle.putInt("degree",degree);
        Intent intent = new Intent(getActivity(),ShowPicActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    //圆环
    void initProgressRing(int carbohydrate1,int fat1,int protein1) {
        mRoundProgressBar1 = (RoundProgressBar) ll.findViewById(R.id.roundProgressBar1);
        mRoundProgressBar2 = (RoundProgressBar2) ll.findViewById(R.id.roundProgressBar2);
        mRoundProgressBar3 = (RoundProgressBar3) ll.findViewById(R.id.roundProgressBar3);

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
