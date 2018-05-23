package com.liuguanghui.littlereader;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lcodecore.extextview.ExpandTextView;
import com.liuguanghui.littlereader.dao.NovelInfoVODao;
import com.liuguanghui.littlereader.pojo.NovelVO;
import com.liuguanghui.littlereader.util.HttpClientUtil;
import com.liuguanghui.littlereader.util.JsonResult;

import java.io.IOException;
import java.util.Properties;

public class NovelDetailActivity extends AppCompatActivity {



    private ScrollView sv_book_view;
    private ImageView iv_book_image;
    private TextView tv_book_name;
    private TextView tv_book_author;
    private TextView tv_book_type;
    private ExpandTextView tv_book_desc;

    private NovelInfoVODao dao ;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what){
                case 1:
                    NovelVO novelVO = (NovelVO) msg.obj;
                    if(novelVO==null){
                        break;
                    }
                    sv_book_view.setVisibility(View.VISIBLE);
                    tv_book_author.setText(novelVO.getAuthor());
                    tv_book_desc.setText(novelVO.getIntroduction());
                    tv_book_type.setText(novelVO.getTname());
                    tv_book_name.setText(novelVO.getTitle());
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

        sv_book_view = findViewById(R.id.sv_book_view);
        iv_book_image = findViewById(R.id.iv_book_image);
        tv_book_name = findViewById(R.id.tv_book_name);
        tv_book_author = findViewById(R.id.tv_book_author);
        tv_book_type = findViewById(R.id.tv_book_type);
        tv_book_desc = findViewById(R.id.tv_book_desc);
        dao = new NovelInfoVODao(NovelDetailActivity.this);

        // 得到intent 对象
        Intent intent = getIntent();
        String netId = intent.getStringExtra("netId");
        String novelId = intent.getStringExtra("novelId");

        new Thread(){
            @Override
            public void run() {
                try {
                    NovelVO vo =   NovelDetailActivity.this.requestNovelVo(netId,novelId);
                    Message message = Message.obtain();
                    message.what = 1;//标识
                    message.obj = vo;
                    if(vo != null&& vo.getImgpath()!=null){
                        dao.update(vo);
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


}
