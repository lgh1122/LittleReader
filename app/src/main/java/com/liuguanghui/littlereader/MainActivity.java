package com.liuguanghui.littlereader;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
                content_text_view.setVisibility(View.GONE);
            }
        });
        content_refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //content_text_view.setVisibility(View.INVISIBLE);
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
                                        Toast.makeText(MainActivity.this,"书籍详情",Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2:
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
                Toast.makeText(MainActivity.this, "设置", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }




}
