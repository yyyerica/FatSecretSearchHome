package com.example.yyy.jsontest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.format.Time;
import android.util.SparseArray;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseExpandableListAdapter;

import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Handler;


public class AddDelFoodActivity extends AppCompatActivity{

    //接收的是食物名字
    //从网络获取食物信息
    //用Food类的JsontoFood方法获取食物列表Arraylist<Food>
    ArrayList<Food> Foodlist;
    List<Integer> tps;

    ExpandableListView sp_date_list = null;     //列表

    FloatingActionButton fab;

    Button confirmButton;
    final static int REQUESTCODE = 34;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_can_food);

        fab = (FloatingActionButton)findViewById(R.id.fab);

        confirmButton = (Button)findViewById(R.id.confirmButton);
        Foodlist = (ArrayList<Food>)getIntent().getSerializableExtra("postlist");//从ShowPicActivity传过来的
        tps = new ArrayList<>();
        for(int i = 0 ; i < Foodlist.size(); i++) {
            tps.add(-1);
        }

        //获取控件
        sp_date_list = (ExpandableListView) findViewById(R.id.expandablelistview);
        sp_date_list.setAdapter(new MyExpandAdapter(this));

        fab.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent = new Intent(AddDelFoodActivity.this,SearchFoodActivity.class);
                   startActivityForResult(intent,REQUESTCODE);
               }
           }
        );

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Service service = new Service();
                for(int i = 0 ; i < tps.size() ; i++) {
                    if(tps.get(i) == -1)
                        tps.set(i,service.gettp());
                }
                //存储和发送 tps 和 Foodlist
//
                service.postfoodArray(Foodlist, "tom", tps);
//
                //储存最后一餐
                SharedPreferences sp = getSharedPreferences("username", 0);
                //通过Editor对象以键值对<String Key,String Value>存储数据
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("sharetp", service.gettp());
                editor.putInt("sharedate",service.getDate());
                //通过.commit()方法保存数据
                editor.commit();
                startActivity(new Intent(AddDelFoodActivity.this,MainActivity.class));
            }
        });
    }

    /*
* 设置控件所在的位置YY，并且不改变宽高，
* XY为绝对位置
*/
    public static void setLayout(View view,int x,int y)
    {
        ViewGroup.MarginLayoutParams margin=new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x,y, x+margin.width, y+margin.height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case REQUESTCODE:
                Bundle b = data.getExtras(); //data为B中回传的Intent
                int returntp = b.getInt("returtp");
                Food returnfood = (Food)b.getSerializable("returnfood");
                Foodlist.add(returnfood);//这个food被自定义了时段，Foodlist里面的其他food用系统时间
                sp_date_list.setAdapter(new MyExpandAdapter(this));
                tps.add(returntp);
                break;
            default:
                break;
        }
    }

    public void delete(int position) {
        Foodlist.remove(position);
        sp_date_list.setAdapter(new MyExpandAdapter(this));
    }

    class MyExpandAdapter extends BaseExpandableListAdapter {

        AddDelFoodActivity context;

        SparseArray<ImageView> mIndicators = new SparseArray<>();  //用于存放Indicator的集合
        android.os.Handler handler;

        public MyExpandAdapter(AddDelFoodActivity context){
            this.context = context;
            handler = new android.os.Handler() { //用来刷新父子view，调用refresh()方法
                @Override
                public void handleMessage(Message msg) {
                    notifyDataSetChanged();
                    super.handleMessage(msg);
                }
            };

        }

        public void refresh() {
            handler.sendMessage(new Message());
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

        GroupViewHolder groupViewHolder;
        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
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
            groupViewHolder.ivSubTitle.setText((int)((double)Foodlist.get(groupPosition).getamount()/100 * Foodlist.get(groupPosition).getheat()) + "kCal/" + Foodlist.get(groupPosition).getamount() + "g");
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
                                 boolean isLastChild, View convertView, final ViewGroup parent) {
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
            childViewHolder.seekBar.setProgress(Foodlist.get(groupPosition).getamount());
            childViewHolder.seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
                @Override
                public void onProgressChanged(int progress, float progressFloat) {
                }

                @Override
                public void getProgressOnActionUp(int progress, float progressFloat) {
                    int amount = childViewHolder.seekBar.getProgress();
                    Foodlist.get(groupPosition).setamount(amount);
                    childViewHolder.seekBar.correctOffsetWhenContainerOnScrolling();

                    refresh();
                    sp_date_list.collapseGroup(groupPosition);
                    sp_date_list.expandGroup(groupPosition);
                    //sp_date_list.setAdapter(new MyExpandAdapter(AddDelFoodActivity.this));
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
                mIndicators.get(groupPosition).setImageResource(R.drawable.up_arrow);
            } else {
                mIndicators.get(groupPosition).setImageResource(R.drawable.down_arrow);
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




