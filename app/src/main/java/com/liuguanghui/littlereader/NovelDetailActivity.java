package com.liuguanghui.littlereader;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lcodecore.extextview.ExpandTextView;
import com.liuguanghui.littlereader.dao.NovelInfoVODao;
import com.liuguanghui.littlereader.pojo.NovelVO;
import com.liuguanghui.littlereader.util.HttpClientUtil;
import com.liuguanghui.littlereader.util.JsonResult;

import java.io.IOException;
import java.util.Properties;

public class NovelDetailActivity extends AppCompatActivity implements View.OnClickListener {



    private LinearLayout ll_book_view;
    private ProgressBar pg_book_bar;
    private ImageView iv_book_image;
    private TextView tv_book_name;
    private TextView tv_book_author;
    private TextView tv_book_type;
    private ExpandTextView tv_book_desc;
    private Button bt_book_addStore;

    private NovelVO novelVO;
    private NovelInfoVODao dao ;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what){
                case 1:
                    novelVO = (NovelVO) msg.obj;
                    if(novelVO==null){
                        break;
                    }
                    pg_book_bar.setVisibility(View.GONE);
                    ll_book_view.setVisibility(View.VISIBLE);
                    tv_book_author.setText(novelVO.getAuthor());
                    tv_book_desc.setText(novelVO.getIntroduction());
                    tv_book_type.setText(novelVO.getTname());
                    tv_book_name.setText(novelVO.getTitle());
                    break;
                case 2:
                    novelVO = (NovelVO) msg.obj;
                    if(novelVO==null){
                        break;
                    }
                    pg_book_bar.setVisibility(View.GONE);
                    ll_book_view.setVisibility(View.VISIBLE);
                    tv_book_author.setText(novelVO.getAuthor());
                    tv_book_desc.setText(novelVO.getIntroduction());
                    tv_book_type.setText(novelVO.getTname());
                    tv_book_name.setText(novelVO.getTitle());
                    bt_book_addStore.setText("移除书架");
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
        setContentView(R.layout.activity_novel_detail);
        /* 显示App icon左侧的back键 */
        //ActionBar actionBar = getActionBar();
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
       /* private ImageView iv_book_image;
        private TextView ;
        private TextView ;
        private TextView ;
        private ExpandTextView ;*/

        ll_book_view = findViewById(R.id.ll_book_view);
        pg_book_bar = findViewById(R.id.pg_book_bar);
        iv_book_image = findViewById(R.id.iv_book_image);
        tv_book_name = findViewById(R.id.tv_book_name);
        tv_book_author = findViewById(R.id.tv_book_author);
        tv_book_type = findViewById(R.id.tv_book_type);
        tv_book_desc = findViewById(R.id.tv_book_desc);
        bt_book_addStore = findViewById(R.id.bt_book_addStore);
        dao = new NovelInfoVODao(NovelDetailActivity.this);

        // 得到intent 对象
        Intent intent = getIntent();
        String netId = intent.getStringExtra("netId");
        String novelId = intent.getStringExtra("novelId");


        bt_book_addStore.setOnClickListener(this);

        new Thread(){
            @Override
            public void run() {
                try {
                    NovelVO localNovelVO = dao.getNovelVO(Long.parseLong(netId),Long.parseLong(novelId));
                    Message message = Message.obtain();
                    if(localNovelVO!=null){
                        if(localNovelVO.getIntroduction()!=null&&localNovelVO.getImgpath()!=null){

                        }else{
                            NovelVO  vo =   NovelDetailActivity.this.requestNovelVo(netId,novelId);
                            localNovelVO.setIntroduction(vo.getIntroduction());
                            localNovelVO.setImgpath(vo.getImgpath());
                            dao.update(localNovelVO);
                        }
                        message.what = 2;//标识
                        message.obj = localNovelVO;
                    }else{
                        NovelVO  vo =   NovelDetailActivity.this.requestNovelVo(netId,novelId);
                        message.what = 1;//标识
                        message.obj = vo;
                    }
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 联网请求得到jsonString
     * @return
     * @throws Exception
     */
    private   NovelVO requestNovelVo(String netId,String novelId) throws Exception {
        String result = null;
        Properties properties = new Properties();
        String path = "";
        NovelVO novelVO = null;
        try {
            properties.load(getAssets().open("my.properties"));
            path = properties.getProperty("novel_info_url");
        } catch (IOException e) {
            e.printStackTrace();
        }

        result = HttpClientUtil.httpPost(path+"/"+netId+"/"+novelId,null);
        if(result !=null && !"".equals(result)){
            JsonResult jsonResult =   JsonResult.formatToPojo(result,NovelVO.class);
            if(jsonResult.getStatus() == 200){
                novelVO = (NovelVO) jsonResult.getData();
            }
        }
        return novelVO;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_book_addStore:
              String btName =   bt_book_addStore.getText().toString();
                if("加入书架".equals(btName)){
                    novelVO.setReadDate(SystemClock.currentThreadTimeMillis());
                    bt_book_addStore.setText("移除书架");
                    dao.add(novelVO);
                }else{
                    bt_book_addStore.setText("加入书架");
                    dao.deleteById(novelVO);
                }
                break;
            default:
                break;
        }


    }
}
