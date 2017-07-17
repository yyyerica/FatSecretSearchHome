package com.example.yyy.jsontest;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;


public class DietFragment extends Fragment {

    final int PHOTO_REQUEST_GALLERY = 23;
    final int CAMERA_REQUEST = 343;

    public DietFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initFAB(); //浮动按钮

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diet, container, false);
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
                .build();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());

        // repeat many times:
        ImageView itemIcon = new ImageView(getContext());
        Drawable fabicon1 = getResources().getDrawable(R.drawable.featheritem);
        itemIcon.setImageDrawable(fabicon1);
        SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();

        ImageView itemIcon2 = new ImageView(getContext());
        Drawable fabicon2 = getResources().getDrawable(R.drawable.picitem);
        itemIcon2.setImageDrawable(fabicon2);
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();

        ImageView itemIcon3 = new ImageView(getContext());
        Drawable fabicon3 = getResources().getDrawable(R.drawable.cameraitem);
        itemIcon3.setImageDrawable(fabicon3);
        SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
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

}
