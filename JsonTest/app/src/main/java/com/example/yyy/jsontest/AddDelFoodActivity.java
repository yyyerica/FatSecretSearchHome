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


import com.xw.repo.BubbleSeekBar;

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

        Foodlist = (ArrayList<Food>)getIntent().getSerializableExtra("postlist");

        //获取控件
        sp_date_list = (ExpandableListView) findViewById(R.id.expandablelistview);
        sp_date_list.setAdapter(new MyExpandAdapter(this));
    }


    public void delete(int position) {
        Foodlist.remove(position);
        sp_date_list.setAdapter(new MyExpandAdapter(this));
    }



    class MyExpandAdapter extends BaseExpandableListAdapter {

        AddDelFoodActivity context;

        SparseArray<ImageView> mIndicators = new SparseArray<>();  //用于存放Indicator的集合


        public MyExpandAdapter(AddDelFoodActivity context){
            this.context = context;
        }

        @Override
        public int getGroupCount() {
            // TODO Auto-generated method stub
            return Foodlist.size();
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
            return Foodlist.get(groupPosition).getName();
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return Foodlist.get(groupPosition).getdescription();
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
            groupViewHolder.ivTitle.setText(Foodlist.get(groupPosition).getName());
            groupViewHolder.ivSubTitle.setText(Foodlist.get(groupPosition).getheat()+"kCal/100g");
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
        public View getChildView(final int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            final ChildViewHolder childViewHolder;
            //子列表控件通过界面文件设计
            if(convertView == null) {//convert在运行中会重用，如果不为空，则表明不用重新获取
                convertView = LayoutInflater.from(context).inflate(R.layout.add_child_item, null);
                childViewHolder = new ChildViewHolder();
                childViewHolder.textChild = (TextView)convertView.findViewById(R.id.textChild);
                childViewHolder.seekBar = (BubbleSeekBar)convertView.findViewById(R.id.seekbar);
                convertView.setTag(childViewHolder);
            } else {
                childViewHolder = (ChildViewHolder) convertView.getTag();
            }
            childViewHolder.textChild.setText(Foodlist.get(groupPosition).getdescription());
            childViewHolder.seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
                @Override
                public void onProgressChanged(int progress, float progressFloat) {
                    int cal = childViewHolder.seekBar.getProgress();
                    Foodlist.get(groupPosition).setamount(cal);
                    childViewHolder.seekBar.correctOffsetWhenContainerOnScrolling();
                }

                @Override
                public void getProgressOnActionUp(int progress, float progressFloat) {

                }

                @Override
                public void getProgressOnFinally(int progress, float progressFloat) {

                }
            });
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

        private class ChildViewHolder {
            TextView textChild;
            BubbleSeekBar seekBar;
        }

    }
}


