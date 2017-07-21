package com.example.yyy.jsontest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddCanFoodActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<HashMap<String, String>> mylist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_can_food);
    }

    void init() {
        listView = (ListView)findViewById(R.id.foodlistview);

        //从数据库获取数据
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("FoodName", "demo1");
        map.put("Description", "demo2");
        mylist.add(map);
        mylist.add(map);
        mylist.add(map);

        //生成适配器，数组===》ListItem
        SimpleAdapter mSchedule = new SimpleAdapter(AddCanFoodActivity.this, //没什么解释
                mylist,//数据来源
                R.layout.searchfood_item,//ListItem的XML实现
                new String[] {"FoodName", "Description"},//动态数组与ListItem对应的子项
                new int[] {R.id.foodname_text,R.id.fooddes_text}//ListItem的XML文件里面的两个TextView ID
        );
        //添加并且显示
        listView.setAdapter(mSchedule);
    }


}
