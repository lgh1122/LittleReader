package com.liuguanghui.littlereader;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.liuguanghui.littlereader.adapter.NovelListAdapter;
import com.liuguanghui.littlereader.dao.NovelInfoVODao;
import com.liuguanghui.littlereader.pojo.NovelVO;
import com.liuguanghui.littlereader.util.CommonUtil;
import com.liuguanghui.littlereader.util.ImageLoader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView content_list_view;
    private List<NovelVO> novelInfos;
    private NovelListAdapter adapter;
    private NovelInfoVODao dao ;
    private TextView content_text_view;

    private ImageLoader imageLoader;
    private  RefreshLayout refreshLayout;

    private boolean exits = false;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what){
                case 1:
                    exits = false;
                    break;

                default:
                    break;

            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        content_text_view = findViewById(R.id.content_text_view);
        RefreshLayout content_refreshLayout = (RefreshLayout)findViewById(R.id.content_refreshLayout);

        content_refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                content_text_view.setVisibility(View.VISIBLE);
                refreshlayout.finishRefresh(2000);
                novelInfos = dao.getAll();
                adapter.setAppInfos(novelInfos);
                adapter.notifyDataSetChanged();
                content_text_view.setVisibility(View.GONE);
            }
        });
        content_refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //content_text_view.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this,"RefreshLayout onLoadmore" ,Toast.LENGTH_SHORT).show();
                refreshlayout.finishLoadmore(2000);
            }
        });

        content_list_view = findViewById(R.id.content_list_view);
        dao = new NovelInfoVODao(MainActivity.this);
        // 获取书籍列表数据
        novelInfos = dao.getAll();
        imageLoader = new ImageLoader(MainActivity.this,R.mipmap.loading,R.mipmap.error);
        adapter  = new NovelListAdapter(MainActivity.this, novelInfos,imageLoader);

        // 设置数据
        content_list_view.setAdapter(adapter);

        //添加点击监听时间
        content_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NovelVO info = novelInfos.get(position);
                Toast.makeText(MainActivity.this,info.getTitle(),Toast.LENGTH_SHORT).show();;
                info.setReadDate(SystemClock.currentThreadTimeMillis());
                dao.update(info);
                CommonUtil.sortDesc(novelInfos);
                adapter.notifyDataSetChanged();
            }
        });

        content_list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final NovelVO info = novelInfos.get(position);

                final String[] items = {info.getIsTop()==1?"取消置顶":"置顶", "书籍详情", "缓存全本", "删除"};

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(info.getTitle())
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        Toast.makeText(MainActivity.this,items[which],Toast.LENGTH_SHORT).show();
                                        if("置顶".equals(items[which])){
                                            info.setIsTop(1);
                                            info.setReadDate(SystemClock.currentThreadTimeMillis());
                                            dao.update(info);
                                        }else{
                                            info.setIsTop(0);
                                            dao.update(info);
                                        }
                                        CommonUtil.sortDesc(novelInfos);
                                        adapter.notifyDataSetChanged();
                                        break;
                                    case 1:

                                TextView main_list_novelid = (TextView) view.findViewById(R.id.main_list_novelid);
                                TextView main_list_netid = (TextView) view.findViewById(R.id.main_list_netid);
                                String netId = main_list_netid.getText().toString();
                                String novelId = main_list_novelid.getText().toString();
                                        Intent intent = new Intent(MainActivity.this,NovelDetailActivity.class);
                                        intent.putExtra("netId" ,netId);
                                        intent.putExtra("novelId" ,novelId);
                                        startActivity(intent);

                                        Toast.makeText(MainActivity.this,"正在获取书籍详情..."+netId +" "+novelId,Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2:
                                        Log.e("TAG",info.toString());
                                        Toast.makeText(MainActivity.this,"缓存全本",Toast.LENGTH_SHORT).show();
                                        break;
                                    case 3:
                                        dao.deleteById(info);
                                        novelInfos.remove(info);
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
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
      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(!exits){
            Toast.makeText(MainActivity.this,"再次点击退出应用",Toast.LENGTH_SHORT).show();
            exits = true;
            handler.sendEmptyMessageDelayed(1,2000);
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    /**
     * 加载toolbar工具栏
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_search:
                Toast.makeText(MainActivity.this, "搜索", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.action_add:

                Intent intentAdd = new Intent(this,RefreshHeaderActivity.class);
                startActivity(intentAdd);
                break;
            case R.id.action_settings:
                Intent intentDetaile = new Intent(this,NovelDetailActivity.class);
                startActivity(intentDetaile);
                //Toast.makeText(MainActivity.this, "设置", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }




}
