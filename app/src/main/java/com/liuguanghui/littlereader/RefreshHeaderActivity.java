package com.liuguanghui.littlereader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class RefreshHeaderActivity extends AppCompatActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Uri fileUri;
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


    public void openCamera(View view) {
        Intent intent = new Intent(this,HelloCameraActivity.class);
        startActivity(intent);
    }


}
