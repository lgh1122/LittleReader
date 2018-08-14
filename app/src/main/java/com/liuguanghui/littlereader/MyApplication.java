package com.liuguanghui.littlereader;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.allen.library.RxHttpUtils;
import com.liuguanghui.littlereader.util.Constant;

/**
 * Created by liuguanghui on 2018/5/27.
 */

public class MyApplication  extends Application {
    //private static RequestQueue sRequestQueue;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        RxHttpUtils.init(this);
        /** * 全局请求的统一配置 */
        RxHttpUtils .getInstance()
                //开启全局配置
                .config()
                //全局的BaseUrl,必须配置(baseurl以 / 结尾)
                 .setBaseUrl(Constant.BASE_URL)
                // 开启缓存策略
                .setCache()
                //全局的请求头信息
               //.setHeaders(headerMaps)
                // 全局持久话cookie,保存本地每次都会携带在header中
                .setCookie(false)
                //全局ssl证书认证，支持三种方式
                // 1、信任所有证书,不安全有风险
                .setSslSocketFactory()
                //2、使用预埋证书，校验服务端证书（自签名证书）
                //.setSslSocketFactory(getAssets().open("your.cer"))
                // 3、使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
                //.setSslSocketFactory(getAssets().open("your.bks"), "123456", getAssets().open("your.cer"))
                // 全局超时配置
                .setReadTimeout(10)
                //全局超时配置
                .setWriteTimeout(10)
                //全局超时配置
                .setConnectTimeout(10)
                //全局是否打开请求log日志
                .setLog(true);



    }
    /**
     * 获取全局上下文*/
    public static Context getAppContext() {
        return context;
    }

    public static Resources getAppResources() {
        return context.getResources();
    }




}
