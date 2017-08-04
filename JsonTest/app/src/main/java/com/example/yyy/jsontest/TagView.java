package com.example.yyy.jsontest;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.MotionEvent;

import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by YYY on 2017/7/27.
 */

public class TagView extends FrameLayout {

    private TextView FoodNameText;
    Context context;
    String[] items = new String[]{"foodname1","foodname2","foodname3"};
    AlertDialog dialog;

    public TagView(final Context context, Food food) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_tagview, this);
        FoodNameText = (TextView) findViewById(R.id.foodname_text);

        FoodNameText.setText(food.getName());
    }

//    public void setImageView(int id) {
//        imageView.setImageResource(id);
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                break;
//            case MotionEvent.ACTION_MOVE:
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.e("TagView","tabtabtabtab");
//                showSingleChoiceDialog();
//                break;
//        }
//        return true;
//    }


//    //声明一个AlertDialog构造器
//    private AlertDialog.Builder builder;
//    final ChoiceOnClickListener choiceListener =
//            new ChoiceOnClickListener();
//    private void showSingleChoiceDialog() {
//        builder=new AlertDialog.Builder(context);
//        builder.setIcon(R.mipmap.ic_launcher);
//        builder.setTitle("lalala")
//                .setPositiveButton("删除",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialoginterface, int i) {
//                                setVisibility(GONE);
//                            }
//                        }).show();
//
//        /**
//         * 设置内容区域为单选列表项
//         */
//
//        builder.setSingleChoiceItems(items, 0, choiceListener);
//
//        builder.setCancelable(true);
//        dialog = builder.create();
//        dialog.show();

//    }


//    private class ChoiceOnClickListener implements DialogInterface.OnClickListener {
//
//        private int which = 0;
//        @Override
//        public void onClick(DialogInterface dialogInterface, int which) {
//            this.which = which;
//            FoodNameText.setText(items[which]);
//            dialog.dismiss();
//        }
//    }


}
