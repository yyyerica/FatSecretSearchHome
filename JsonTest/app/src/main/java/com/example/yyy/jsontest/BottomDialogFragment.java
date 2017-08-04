package com.example.yyy.jsontest;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.format.Time;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.xw.repo.BubbleSeekBar;

/**
 * Created by YYY on 2017/7/20.
 */

public class BottomDialogFragment extends DialogFragment {

    View view;
    int choose = 0;
    double calo,cal;

    Food food;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

//        foodname = getArguments().getString("FoodName");
//        description = getArguments().getString("Description");
        food = (Food)getArguments().get("foodObject");


        Dialog dialog = new Dialog(getActivity(), R.style.BottomFragmentDialog);

        // 必须在setContentView之前调用。否则运行时报错。
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.bottomfragment_view, null);
        init();

        // 底部弹出的DialogFragment装载的View
        dialog.setContentView(view);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        // 设置底部弹出显示的DialogFragment窗口属性。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = 1600; // 底部弹出的DialogFragment的高度，如果是MATCH_PARENT则铺满整个窗口
        window.setAttributes(params);

        return dialog;
    }

    void init() {
        Button addbutton = (Button) view.findViewById(R.id.addButton);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner1);//下拉按钮
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                                getActivity(), R.array.meal, R.layout.spinnerlayout);//自定义字体
        adapter.setDropDownViewResource(R.layout.spinnerlayout);//自定义字体，拉开后
        spinner.setAdapter(adapter);
        spinner.setSelection(new Service().gettp()-1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                choose = pos + 1;
                //String[] selectedMeal = getResources().getStringArray(R.array.meal);
                //Toast.makeText(getActivity(), "你点击的是:"+selectedMeal[pos], Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        TextView text1 = (TextView) view.findViewById(R.id.NameText);
        text1.setText(food.getName());
        TextView text2 = (TextView) view.findViewById(R.id.desText);
        text2.setText(food.getdescription());

        //description做处理
//        String[] des = description.split("-");
//        String[] item = des[1].split("\\|");
//        String des1 = des[1];
//        final int calint = getNumber(item[0]);
        final TextView weText = (TextView) view.findViewById(R.id.weightText);
        final TextView calText = (TextView) view.findViewById(R.id.calText);
        //calText.setText(item[0].split(":")[1]);
        final int heat = food.getheat();
        calText.setText(heat+"");//热量
        weText.setText("100克");//质量

        final BubbleSeekBar bubbleSeekBar = (BubbleSeekBar) view.findViewById(R.id.seekbar);
        bubbleSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                cal = bubbleSeekBar.getProgress();
                weText.setText(cal +"克");
                calo = heat/100*(bubbleSeekBar.getProgress());
                calText.setText(calo + "kCal");
                bubbleSeekBar.correctOffsetWhenContainerOnScrolling();
                food.setamount((int)cal);
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //储存最后一餐
                SharedPreferences sp = getActivity().getSharedPreferences("username", 0);
                //通过Editor对象以键值对<String Key,String Value>存储数据
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("sharetp", choose);
                //通过.commit()方法保存数据
                editor.commit();


                //返回int amount
                ((SearchFoodActivity)getActivity()).returnfood(food,choose);
                getDialog().dismiss();
            }
        });
    }
}