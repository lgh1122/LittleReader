package com.liuguanghui.littlereader;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.liuguanghui.littlereader.adapter.SearchListAdapter;
import com.liuguanghui.littlereader.dao.NovelInfoVODao;
import com.liuguanghui.littlereader.pojo.NovelVO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class SearchActivity  extends Activity {
    protected static final int WHAT_REQUEST_SUCCESS = 1;
    protected static final int WHAT_REQUEST_ERROR = 2;
    private static final int WHAT_REQUEST_NONE = 3 ;
    private ListView search_list_view;
    private List<NovelVO> data;
    private SearchListAdapter adapter;
    private NovelInfoVODao dao ;
    private LinearLayout ll_search_loading;
    private EditText search_text;
    private ImageView search_back;
    private ImageView search_search;

    private Handler handler = new SearchHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        dao = new NovelInfoVODao(SearchActivity.this);
        //创建分线程请求服务器动态加载数据并显示
        search_list_view= findViewById(R.id.search_list_view);
        ll_search_loading = (LinearLayout) findViewById(R.id.ll_search_loading);
        search_text = findViewById(R.id.search_text);
        search_search =   (ImageView) findViewById(R.id.search_search);
        search_back =   (ImageView) findViewById(R.id.search_back);


        search_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text = search_text.getText().toString();
                if(text!=null && !"".equals(text)){
                    //1. 主线程, 显示提示视图
                    ll_search_loading.setVisibility(View.VISIBLE);
                    //分线程，用于查询书籍书籍从服务器
                    Thread thread = new SearchThread(text);
                    thread.start();
                }
                //设置背景图片
                /* iv_simple_icon.setBackgroundResource(android.R.drawable.alert_light_frame);
                //设置前景图片
                iv_simple_icon.setImageResource(android.R.drawable.ic_media_pause);*/
            }
        });


        search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
        search_list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final NovelVO info = data.get(position);

              NovelVO sqlNovel =   dao.getNovelVO(info.getNetid(),info.getId());
                final String[] items = {sqlNovel!=null ?"移除书架":"加入书架", "书籍详情"};
                new AlertDialog.Builder(SearchActivity.this)
                        .setTitle(info.getTitle())
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        Toast.makeText(SearchActivity.this,items[which],Toast.LENGTH_SHORT).show();
                                        if("加入书架".equals(items[which])){
                                            info.setReadDate(SystemClock.currentThreadTimeMillis());
                                            dao.add(info);
                                        }else{
                                            dao.deleteById(info);
                                        }
                                        break;
                                    case 1:
                                        Toast.makeText(SearchActivity.this,"书籍详情",Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                        .show();

                return true;
            }
        });



        // 获取书籍列表数据



    }

    /**
     * 联网请求得到jsonString
     * @return
     * @throws Exception
     */
    private String requestJson(String title) throws Exception {
        String result = null;
        Properties properties = new Properties();
        String path = "";
        try {
            properties.load(getAssets().open("my.properties"));
            path = properties.getProperty("search_url");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //1. 得到连接对象
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 4). 设置请求方式,连接超时, 读取数据超时
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        // 5). 连接服务器
        connection.connect();
        //得到输出流, 写请求体:name=Tom1&age=11
        OutputStream os = connection.getOutputStream();
        String data = "title="+title;
        os.write(data.getBytes("utf-8"));
        //发请求并读取服务器返回的数据
        int responseCode = connection.getResponseCode();
        if(responseCode==200) {
            InputStream is = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            connection.disconnect();
            result = baos.toString();
        } else {
            //也可以抛出运行时异常
        }
        return result;
    }



    private class SearchHandler extends Handler {

            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case WHAT_REQUEST_SUCCESS:
                        ll_search_loading.setVisibility(View.GONE);
                        //显示列表
                        adapter  = new SearchListAdapter(SearchActivity.this, data);
                        search_list_view.setAdapter(adapter);
                        break;
                    case WHAT_REQUEST_ERROR:
                        ll_search_loading.setVisibility(View.GONE);
                        Toast.makeText(SearchActivity.this, "加载数据失败", Toast.LENGTH_SHORT).show();
                        break;
                    case WHAT_REQUEST_NONE:
                        ll_search_loading.setVisibility(View.GONE);
                        Toast.makeText(SearchActivity.this, "未找到书籍，请检查查询条件", Toast.LENGTH_LONG).show();
                        break;

                    default:
                        break;
                }
            }
    }

    /**
     * 分线程，用于查询书籍书籍从服务器
     */
    private class SearchThread extends  Thread{
        private String text ;
        public SearchThread(String text) {
            super();
            this.text = text;
        }

        public void run() {
            //联网请求得到jsonString
            try {
                String jsonString = requestJson(text);
                //解析成List<ShopInfo>
                // EUDataGridResult data = new Gson().fromJson(jsonString,);
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");
                if(jsonArray !=null && jsonArray.length() > 0){
                    GsonBuilder builder = new GsonBuilder();
                    // Register an adapter to manage the date types as long values
                    builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                        @Override
                        public Date deserialize(JsonElement json, Type typeOfT,
                                                JsonDeserializationContext context) throws JsonParseException {
                            return new Date(json.getAsJsonPrimitive().getAsLong());
                        }
                    });

                    Gson gson = builder.create();
                    List<NovelVO> list =gson.fromJson(jsonArray.toString(), new TypeToken< List<NovelVO> >(){}.getType());
                    //data = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<NovelVO>>(){}.getType());
                    data = list;
                    //3. 主线程, 更新界面
                    handler.sendEmptyMessage(WHAT_REQUEST_SUCCESS);//发请求成功的消息
                }else{
                    handler.sendEmptyMessage(WHAT_REQUEST_NONE);//发送请求失败的消息
                }
            } catch (Exception e) {
                e.printStackTrace();
                handler.sendEmptyMessage(WHAT_REQUEST_ERROR);//发送请求失败的消息
            }
        }
    }
}
