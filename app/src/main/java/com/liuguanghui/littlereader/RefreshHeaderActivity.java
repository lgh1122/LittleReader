package com.liuguanghui.littlereader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class RefreshHeaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_header);

       RefreshLayout content_refreshLayout = (RefreshLayout)findViewById(R.id.content_refreshHeaderLayout);

        content_refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                refreshlayout.finishRefresh(2000);

            }
        });
        content_refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //content_text_view.setVisibility(View.INVISIBLE);
                refreshlayout.finishLoadmore(2000);
            }
        });

    }


}
