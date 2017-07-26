package com.example.yyy.jsontest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.SparseArray;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseExpandableListAdapter;

import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;


public class AddDelFoodActivity extends AppCompatActivity{

    //接收的是食物名字
    //从网络获取食物信息
    //用Food类的JsontoFood方法获取食物列表Arraylist<Food>
    ArrayList<Food> Foodlist;

    ExpandableListView sp_date_list = null;     //列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_can_food);

        init();//初始化食物列表信息

        //获取控件
        sp_date_list = (ExpandableListView) findViewById(R.id.expandablelistview);
        sp_date_list.setAdapter(new MyExpandAdapter(this,Foodlist));
    }

    public void init() {//食物列表demo
        Foodlist = new ArrayList<Food>();
        Foodlist.add(new Food("apple",10,1,1,1,"AppleDes"));
        Foodlist.add(new Food("banana",20,2,2,2,"BananaDes"));
    }

    public void delete(int position) {
        Foodlist.remove(position);
        sp_date_list.setAdapter(new MyExpandAdapter(this,Foodlist));
    }

}


class MyExpandAdapter extends BaseExpandableListAdapter{

    AddDelFoodActivity context;

    SparseArray<ImageView> mIndicators = new SparseArray<>();  //用于存放Indicator的集合

    ArrayList<Food> foodlist;

    public MyExpandAdapter(AddDelFoodActivity context,ArrayList<Food> foodlist){
        this.context = context;
        this.foodlist = foodlist;
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return this.foodlist.size();
    }

    @Override
    public int getChildrenCount(int position) {
        // TODO Auto-generated method stub
        //if(position<0||position>=this.expandableListData.size())
            //return 0;
        //return child.get(position).size();
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return foodlist.get(groupPosition).getName();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return foodlist.get(groupPosition).getdescription();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        final GroupViewHolder groupViewHolder;
        if(convertView ==null) {//convert在运行中会重用，如果不为空，则表明不用重新获取
            convertView = LayoutInflater.from(context).inflate(R.layout.addcan_item, null);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.ivIndicator = (ImageView)convertView.findViewById(R.id.iv_indicator);
            groupViewHolder.ivTitle = (TextView)convertView.findViewById(R.id.foodname_text);
            groupViewHolder.ivSubTitle = (TextView)convertView.findViewById(R.id.fooddes_text);
            groupViewHolder.deleteButton = (Button)convertView.findViewById(R.id.deleteButton);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.ivTitle.setText(foodlist.get(groupPosition).getName());
        groupViewHolder.ivSubTitle.setText(foodlist.get(groupPosition).getheat()+"kCal/100g");
        groupViewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,foodlist.get(groupPosition).getName(),Toast.LENGTH_LONG).show();
                context.delete(groupPosition);
            }
        });

        //      把位置和图标添加到Map
        mIndicators.put(groupPosition, groupViewHolder.ivIndicator);
        //      根据分组状态设置Indicator
        setIndicatorState(groupPosition, isExpanded);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        //子列表控件通过界面文件设计
        if(convertView ==null){//convert在运行中会重用，如果不为空，则表明不用重新获取
            LayoutInflater layoutInflater;//使用这个来载入界面
            layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.add_child_item, null);
        }
        TextView tv = (TextView)convertView.findViewById(R.id.textChild);
        tv.setText(foodlist.get(groupPosition).getdescription());
        //获取文本控件，并设置值
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        //调用Activity里的ChildSelect方法
        //context.childSelect(groupPosition,childPosition);
        return true;
    }

    //            根据分组的展开闭合状态设置指示器
    public void setIndicatorState(int groupPosition, boolean isExpanded) {
        if (isExpanded) {
            mIndicators.get(groupPosition).setImageResource(R.mipmap.ic_launcher);
        } else {
            mIndicators.get(groupPosition).setImageResource(R.mipmap.ic_launcher_round);
        }
    }

    private class GroupViewHolder {
        ImageView ivIndicator;
        TextView ivTitle,ivSubTitle;
        Button deleteButton;
    }

}