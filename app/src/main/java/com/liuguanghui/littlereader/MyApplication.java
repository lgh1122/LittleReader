package com.liuguanghui.littlereader;

import android.app.Application;
import android.content.Context;

/**
 * Created by liuguanghui on 2018/5/27.
 */

public class MyApplication  extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }
    /**
     * 获取全局上下文*/
    public static Context getAppContext() {
        return context;
    }
}