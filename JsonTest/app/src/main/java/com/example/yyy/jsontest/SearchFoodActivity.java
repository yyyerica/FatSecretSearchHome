package com.example.yyy.jsontest;

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SearchFoodActivity extends AppCompatActivity {

    EditText searchEdit;
    Button searchButton;
    TextView nulltext;
    ListView listView;
    ProgressBar mProgressBar;
    org.json.JSONArray jsonArray = null;
    String content;

    ArrayList<HashMap<String, String>> mylist;

    final FragmentManager fm = this.getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);

        init();
    }

    void init() {
        mProgressBar = (ProgressBar) findViewById(R.id.processBar);
        searchEdit = (EditText) findViewById(R.id.searchEditText);
        searchButton = (Button) findViewById(R.id.searchButton);
        nulltext = (TextView) findViewById(R.id.nulltext);
        listView = (ListView) findViewById(R.id.searchListView);

        mylist = new ArrayList<HashMap<String, String>>();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetNetWork().execute();
//                mylist.clear();
//                content = searchEdit.getText().toString();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //Log.e("search",FatSecretSearch.searchFood(content,0).toString());
//                        try {
//                            jsonArray = new JSONArray(FatSecretSearch.searchFood(content,0).toString());
//                            if(jsonArray.length()>0) {
//                                Message msg = new Message();
//                                msg.what = 111;
//                                handler.sendMessage(msg);
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
            }
        });


    }

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 111) {
//                nulltext.setVisibility(View.GONE);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    HashMap<String, String> map = new HashMap<String, String>();
//                    JSONObject jsonObject = jsonArray.optJSONObject(i);
//                    String food_description = jsonObject.optString("food_description");
//                    String food_name = jsonObject.optString("food_name");
//                    map.put("FoodName", food_name);
//                    map.put("Description", food_description);
//                    mylist.add(map);
//                }
//                //生成适配器，数组===》ListItem
//                SimpleAdapter mSchedule = new SimpleAdapter(SearchFoodActivity.this, //没什么解释
//                        mylist,//数据来源
//                        R.layout.searchfood_item,//ListItem的XML实现
//                        new String[] {"FoodName", "Description"},//动态数组与ListItem对应的子项
//                        new int[] {R.id.foodname_text,R.id.fooddes_text}//ListItem的XML文件里面的两个TextView ID
//                );
//                //添加并且显示
//                listView.setAdapter(mSchedule);
//
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                        HashMap<String, String> listItem = (HashMap<String, String>)listView.getItemAtPosition(position);
//                        String description = listItem.get("Description");
//                        String name = listItem.get("FoodName");
//                        Bundle args = new Bundle();
//                        args.putString("FoodName", name);
//                        args.putString("Description", description);
//                        BottomDialogFragment f = new BottomDialogFragment();
//                        f.setArguments(args);
//                        f.show(fm, BottomDialogFragment.class.getName());
//                    }
//                });
//
//            }
//        }
//    };

    //内部类
    //向服务器发送图片  http://blog.csdn.net/guolin_blog/article/details/11711405
    class GetNetWork extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //开始前的准备工作
            nulltext.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            mylist.clear();
            content = searchEdit.getText().toString();

        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                jsonArray = new JSONArray(FatSecretSearch.searchFood(content,0).toString());
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
            if(jsonArray.length()>0) {
                nulltext.setVisibility(View.GONE);
                for (int i = 0; i < jsonArray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    String food_description = jsonObject.optString("food_description");
                    String food_name = jsonObject.optString("food_name");
                    map.put("FoodName", food_name);
                    map.put("Description", food_description);
                    mylist.add(map);
                }
                //生成适配器，数组===》ListItem
                SimpleAdapter mSchedule = new SimpleAdapter(SearchFoodActivity.this, //没什么解释
                        mylist,//数据来源
                        R.layout.searchfood_item,//ListItem的XML实现
                        new String[]{"FoodName", "Description"},//动态数组与ListItem对应的子项
                        new int[]{R.id.foodname_text, R.id.fooddes_text}//ListItem的XML文件里面的两个TextView ID
                );
                //添加并且显示
                listView.setAdapter(mSchedule);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        HashMap<String, String> listItem = (HashMap<String, String>) listView.getItemAtPosition(position);
                        String description = listItem.get("Description");
                        String name = listItem.get("FoodName");
                        Bundle args = new Bundle();
                        args.putString("FoodName", name);
                        args.putString("Description", description);
                        BottomDialogFragment f = new BottomDialogFragment();
                        f.setArguments(args);
                        f.show(fm, BottomDialogFragment.class.getName());
                    }
                });
            } else nulltext.setVisibility(View.VISIBLE);
        }
    }
}
