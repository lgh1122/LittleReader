package com.liuguanghui.littlereader.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuguanghui.littlereader.R;
import com.liuguanghui.littlereader.adapter.SearchListAdapter;
import com.liuguanghui.littlereader.db.entity.NovelBean;
import com.liuguanghui.littlereader.db.entity.NovelSearchHistory;
import com.liuguanghui.littlereader.db.helper.NovelHelper;
import com.liuguanghui.littlereader.db.helper.NovelSearchHistoryHelper;
import com.liuguanghui.littlereader.util.HttpClientUtil;
import com.liuguanghui.littlereader.util.JsonResult;
import com.liuguanghui.littlereader.util.SearchResult;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SearchActivity  extends Activity {
    protected static final int WHAT_REQUEST_SUCCESS = 1;
    protected static final int WHAT_REQUEST_ERROR = 2;
    private static final int WHAT_REQUEST_NONE = 3 ;
    private ListView search_list_view;
    private ListView search_history_view;
    private TextView search_removehistory;
    private List<NovelBean> data ;
    private SearchListAdapter adapter;
    /*private NovelInfoVODao dao ;
    private SearchHistoryDao historyDao ;*/
    private NovelHelper novelHelper;
    private NovelSearchHistoryHelper novelSearchHistoryHelper;
    private LinearLayout ll_search_loading;
    private LinearLayout ll_search_load_recommend;
    private EditText search_text;
    private ImageView search_back;
    private ImageView search_search;
    private LayoutInflater mInflater;
    private TagFlowLayout  search_all_book_name;
    private RefreshLayout search_refreshLayout;
    private Handler handler = new SearchHandler();
    private int page = 1;
    private int rows = 8;
    private boolean isHasMore = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        novelHelper = NovelHelper.getsInstance();
       // dao = new NovelInfoVODao(SearchActivity.this);
        novelSearchHistoryHelper = NovelSearchHistoryHelper.getsInstance();
       // historyDao = new SearchHistoryDao(SearchActivity.this);
        //创建分线程请求服务器动态加载数据并显示
        search_list_view= findViewById(R.id.search_list_view);
        search_history_view= findViewById(R.id.search_history_view);
        ll_search_loading = (LinearLayout) findViewById(R.id.ll_search_loading);
        ll_search_load_recommend = (LinearLayout) findViewById(R.id.ll_search_load_recommend);
        search_text = findViewById(R.id.search_text);  //搜索文本输入框
        search_removehistory = findViewById(R.id.search_removehistory);  //清空历史记录
        search_search =   (ImageView) findViewById(R.id.search_search);
        search_back =   (ImageView) findViewById(R.id.search_back);
        search_all_book_name = findViewById(R.id.search_all_book_name); //大家都在搜 流动布局

        List<String> listBooks = new ArrayList<>();
        listBooks.add("道");
        listBooks.add("氪金魔主");
        listBooks.add("英雄联盟");
        listBooks.add("七杀影响的");
        listBooks.add("氪对方的族");
        listBooks.add("武道宗师");
        listBooks.add("杀毒软件");
        //大家都在搜的书籍
        search_all_book_name.setAdapter(new TagAdapter<String>(listBooks) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                mInflater = LayoutInflater.from(SearchActivity.this);
                View   convertView = mInflater.inflate(R.layout.tags_tv, null);
                TextView tv =  convertView.findViewById(R.id.tv_tag);
                tv.setText(s);
                return tv;
            }
        });
        search_all_book_name.setOnTagClickListener((view, position, parent) -> {
            /*Intent intent = new Intent(mContext, BookDetailActivity.class);
            intent.putExtra("bookid", likebooks.get(position).get_id());
            startActivity(intent);*/
            //Toast.makeText(SearchActivity.this,listBooks.get(position),Toast.LENGTH_SHORT).show();
            search_text.setText(listBooks.get(position));
            search_search.performClick();
            return true;
        });

        search_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text = search_text.getText().toString();
                if(text!=null && !"".equals(text)){
                    //1. 主线程, 显示提示视图
                    ll_search_load_recommend.setVisibility(View.GONE);
                    ll_search_loading.setVisibility(View.VISIBLE);
                    NovelSearchHistory novelSearchHistory = novelSearchHistoryHelper.findBookHistoryByKeyword(text);
                    if(novelSearchHistory!=null){
                        novelSearchHistory.setId(new Date().getTime());
                    }else{
                        novelSearchHistory = new NovelSearchHistory();
                        novelSearchHistory.setId(new Date().getTime());
                        novelSearchHistory.setKeyword(text);
                    }
                    novelSearchHistoryHelper.saveBook(novelSearchHistory);
                    isHasMore = true;
                    page= 1;
                    data = new ArrayList<>();
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

        search_refreshLayout = (RefreshLayout)findViewById(R.id.search_refreshLayout);

        search_refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(1000);
            }
        });
        search_refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //content_text_view.setVisibility(View.INVISIBLE);
                //Toast.makeText(SearchActivity.this,"RefreshLayout onLoadmore" ,Toast.LENGTH_SHORT).show();
                String text = search_text.getText().toString();
                if(isHasMore){
                    page++;
                    Thread thread = new SearchThread(text);
                    thread.start();
                    refreshlayout.finishLoadmore(1000);
                }else{
                    Toast.makeText(SearchActivity.this,"没有更多了",Toast.LENGTH_SHORT).show();
                    refreshlayout.finishLoadmore(0);
                }


            }
        });
        search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        //添加点击监听时间
        search_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NovelBean info = data.get(position);
                Intent intent = new Intent(SearchActivity.this,NovelDetailActivity.class);
                intent.putExtra("netId" ,info.getNetid()+"");
                intent.putExtra("novelId" ,info.getId()+"");
                startActivity(intent);
            }
        });

        search_list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final NovelBean info = data.get(position);

              NovelBean sqlNovel =   novelHelper.findBookById(info.getId(),info.getNetid());
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
                                            novelHelper.saveBook(info);
                                        }else{
                                            novelHelper.removeBookInRx(info);
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



        // 搜索历史列表
        //准备集合数据
        List<NovelSearchHistory> historyList = novelSearchHistoryHelper.findLimitBookHistorys();
        List<Map<String, Object>> historyData = new ArrayList<Map<String,Object>>();
        Map<String, Object> map  = null;
         for (NovelSearchHistory novelSearchHistory : historyList){
             map = new HashMap<String, Object>();
             map.put("keyword", novelSearchHistory.getKeyword());
             map.put("image", R.mipmap.search_history_mark_light);
             historyData.add(map);
         }
        String[] from = {"image","keyword"};

        int[] to = {R.id.history_image_view,R.id.history_text_view};
        //准备ArrayAdapter对象
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, historyData, R.layout.list_history_view, from, to);

        //设置Adapter显示列表
        search_history_view.setAdapter(simpleAdapter);
        search_history_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = historyData.get(position);
                String keyword = (String) map.get("keyword");
                search_text.setText(keyword);
                search_search.performClick();

            }
        });

        search_removehistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novelSearchHistoryHelper.removeAllHistory();
                historyData.clear();
                simpleAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * 联网请求得到jsonString
     * @return
     * @throws Exception
     */
    private String requestNovelJson(String q, int page, int rows) throws Exception {
        String result = null;
        Properties properties = new Properties();
        String path = "";
        try {
            properties.load(getAssets().open("my.properties"));
            path = properties.getProperty("search_url");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String,String> param = new HashMap<>();
        param.put("q",q);
        param.put("page",page+"");
        param.put("rows",rows+"");
        result = HttpClientUtil.httpPost(path,param);
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
                        search_list_view.setSelection((page-2)*rows);
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
                String jsonString = requestNovelJson(text,page,rows);
                JsonResult taotaoResult = JsonResult.formatToPojo(jsonString, SearchResult.class);
                if (taotaoResult.getStatus() == 200) {
                    SearchResult result = (SearchResult) taotaoResult.getData();
                    System.out.println();
                    if(result.getPageCount() == result.getCurPage()){
                        isHasMore = false;
                    }
                    //JSONArray jsonArray = JSONArray.fromObject(result.getItemList().toString());
                    ObjectMapper mapper = new ObjectMapper( );

                    String jsonStr = mapper.writeValueAsString(result.getItemList());
		            List<NovelBean> novelChild = mapper.reader().withType(new TypeReference<List<NovelBean>>() {})
		            .readValue(jsonStr);
                    //List novelChild = JsonResult.formatToChildList(result.getItemList(),NovelBean.class);
                    data.addAll(novelChild);
                    Log.e("DEBUG","查询的是第"+page+"页");
                    //Log.e("DEBUG",result.getItemList().toString());
                    //3. 主线程, 更新界面
                    handler.sendEmptyMessage(WHAT_REQUEST_SUCCESS);//发请求成功的消息
                    // String jsonR =
                }else{
                    handler.sendEmptyMessage(WHAT_REQUEST_NONE);//发送请求失败的消息
                }
                //解析成List<ShopInfo>
                // EUDataGridResult data = new Gson().fromJson(jsonString,);
                /*JSONObject jsonObject = new JSONObject(jsonString);
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
                    List<NovelBean> list =gson.fromJson(jsonArray.toString(), new TypeToken< List<NovelBean> >(){}.getType());
                    //data = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<NovelBean>>(){}.getType());
                    }
                    */

            } catch (Exception e) {
                e.printStackTrace();
                handler.sendEmptyMessage(WHAT_REQUEST_ERROR);//发送请求失败的消息
            }
        }
    }
}
