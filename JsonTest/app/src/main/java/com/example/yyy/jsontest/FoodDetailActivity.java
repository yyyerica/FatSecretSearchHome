package com.example.yyy.jsontest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FoodDetailActivity extends AppCompatActivity {
    ListView listView;

    ArrayList<Food> foodArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        Intent intent = getIntent();
        int tp = intent.getIntExtra("tp",0);
        listView = (ListView)findViewById(R.id.detailListView);

        switch(tp) { //foodArrayList 从数据库取出
            case 0 ://没有食物
                break;
            case 1://早
                break;
            case 2://中
                break;
            case 3://晚
                break;
            case 4://加餐
                break;
        }

        //listView.setAdapter(new listAdapter(FoodDetailActivity.this));
    }

    public class listAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public listAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return foodArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return foodArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = mInflater.inflate(R.layout.searchfood_item,null);
                viewHolder.nametext = (TextView)view.findViewById(R.id.foodname_text);
                viewHolder.destext = (TextView)view.findViewById(R.id.fooddes_text);
                view.setTag(viewHolder);
            } else  viewHolder = (ViewHolder)view.getTag();

            viewHolder.nametext.setText(foodArrayList.get(i).getName());
            viewHolder.destext.setText(foodArrayList.get(i).getdescription());

            return view;
        }
    }

    public class ViewHolder {
        TextView nametext,destext;
    }

}
