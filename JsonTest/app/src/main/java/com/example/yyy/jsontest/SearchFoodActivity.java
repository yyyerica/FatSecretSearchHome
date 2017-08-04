package com.example.yyy.jsontest;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchFoodActivity extends AppCompatActivity {

    EditText searchEdit;
    Button cancelButton;
    TextView nulltext;
    ListView listView;
    ProgressBar mProgressBar;
    org.json.JSONArray jsonArray = null;
    String content;
    final static int REQUESTCODE = 34;

    //ArrayList<HashMap<String, String>> mylist;
    ArrayList<Food> foodlist;//查找的列表

    final FragmentManager fm = this.getFragmentManager();

    //从bottomfragment返回之后得到food对象
    //1.从dietfragment的fab进来的  执行 发送，存储cal 和 calo 和 choose（时段）
    //2.从adddelfoodactivity的fab进来的  返回一个Food对象
    Food food = null;//返回的food
    int tp = 0;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);

        init();
    }

    void init() {
        intent = getIntent();
        mProgressBar = (ProgressBar) findViewById(R.id.processBar);
        searchEdit = (EditText) findViewById(R.id.searchEditText);
        searchEdit.clearFocus();

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                //这个方法被调用，说明在s字符串中，从start位置开始的count个字符即将被长度为after的新文本所取代。在这个方法里面改变s，会报错。
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                //这个方法被调用，说明在s字符串中，从start位置开始的count个字符刚刚取代了长度为before的旧文本。在这个方法里面改变s，会报错。
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //这个方法被调用，那么说明s字符串的某个地方已经被改变。
                new GetNetWork().execute();
            }
        });

        cancelButton = (Button)findViewById(R.id.cancel_action);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //searchButton = (Button) findViewById(R.id.searchButton);
        nulltext = (TextView) findViewById(R.id.nulltext);
        listView = (ListView) findViewById(R.id.searchListView);

        //mylist = new ArrayList<HashMap<String, String>>();
        foodlist = new ArrayList<>();

//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new GetNetWork().execute();
//            }
//        });
    }

    public int getNumber(String text) {
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(text);
        int after=Integer.valueOf(m.replaceAll("").trim());
        return after;
    }

    public Food getFood(long id) {
        Food food;
        food = Food.JsontoFood(FatSecretGet.getFood(id));
        return food;
    }

    //内部类
    //向服务器发送图片  http://blog.csdn.net/guolin_blog/article/details/11711405
    class GetNetWork extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //开始前的准备工作
            nulltext.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            //mylist.clear();
            foodlist.clear();
            content = searchEdit.getText().toString();

        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                //try {
                    jsonArray = new JSONArray(FatSecretSearch.searchFood(content,0).toString());
//                } catch (Exception e) {
//                    Toast.makeText(SearchFoodActivity.this,"请输入正确搜索信息！",Toast.LENGTH_LONG);
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
         * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressBar.setVisibility(View.GONE);
            if(jsonArray != null && jsonArray.length()>0) {
                nulltext.setVisibility(View.GONE);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
//                    String food_description = jsonObject.optString("food_description");
//                    String food_name = jsonObject.optString("food_name");
//
////                    map.put("FoodName", food_name);
////                    map.put("Description", food_description);
////                    mylist.add(map);
//                    String[] des1 = food_description.split("-");
//                    String[] des2 = des1[1].split("\\|");
//                    Food food = new Food(food_name,getNumber(des2[0]),getNumber(des2[1]),getNumber(des2[2]),getNumber(des2[3]),food_description,100);
                    Food food = Food.JsontoFood(jsonObject);
                    foodlist.add(food);
                }
                //生成适配器，数组===》ListItem
//                SimpleAdapter mSchedule = new SimpleAdapter(SearchFoodActivity.this, //没什么解释
//                        foodlist,//数据来源
//                        R.layout.searchfood_item,//ListItem的XML实现
//                        new String[]{"FoodName", "Description"},//动态数组与ListItem对应的子项
//                        new int[]{R.id.foodname_text, R.id.fooddes_text}//ListItem的XML文件里面的两个TextView ID
//                );
                //添加并且显示
                listView.setAdapter(new listAdapter(SearchFoodActivity.this));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                        HashMap<String, String> listItem = (HashMap<String, String>) listView.getItemAtPosition(position);
//                        String description = listItem.get("Description");
//                        String name = listItem.get("FoodName");
//                        Bundle args = new Bundle();
//                        args.putString("FoodName", name);
//                        args.putString("Description", description);

                        Food food = (Food) listView.getItemAtPosition(position);
                        Bundle args = new Bundle();
                        args.putSerializable("foodObject",food);

                        BottomDialogFragment f = new BottomDialogFragment();
                        f.setArguments(args);
                        f.show(fm, BottomDialogFragment.class.getName());
                    }
                });
            } else nulltext.setVisibility(View.VISIBLE);
        }
    }

    public void returnfood(Food food,int tp) {
        this.food = food;
        this.tp = tp;
        intent.putExtra("returnfood",food);
        intent.putExtra("returntp",tp);
        setResult(REQUESTCODE, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
        finish();//此处一定要调用finish()方法
        //执行存储或发送工作
    }

    public class listAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public listAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return foodlist.size();
        }

        @Override
        public Object getItem(int i) {
            return foodlist.get(i);
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

            viewHolder.nametext.setText(foodlist.get(i).getName());
            viewHolder.destext.setText(foodlist.get(i).getdescription());

            return view;
        }
    }

    public class ViewHolder {
        TextView nametext,destext;
    }


}
