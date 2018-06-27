package com.liuguanghui.littlereader.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.liuguanghui.littlereader.R;
import com.liuguanghui.littlereader.view.NovelSettingsActivity;
import com.qiangxi.checkupdatelibrary.Q;
import com.qiangxi.checkupdatelibrary.bean.CheckUpdateInfo;
import com.qiangxi.checkupdatelibrary.callback.CheckUpdateCallback;
import com.qiangxi.checkupdatelibrary.dialog.ForceUpdateDialog;
import com.qiangxi.checkupdatelibrary.dialog.UpdateDialog;

/**
 * Created by liuguanghui on 2018/6/26.
 */

public class UpdateVersionUtil {


    private Context context;
    private CheckUpdateInfo mCheckUpdateInfo;

    private String checkVersionUrl;


    /**
     * 强制更新
     */
    private ForceUpdateDialog mForceUpdateDialog = null;
    /**
     * 非强制更新
     */
    private UpdateDialog mUpdateDialog = null;

    public UpdateVersionUtil(Context context ,String checkVersionUrl ){
        this.context = context;
        this.checkVersionUrl = checkVersionUrl;
    }

    public  void initData() {
         //="http://10.3.29.67:9085/version/find";
        //被注释的代码用来进行检查更新,这里为了方便,模拟一些假数据
        Log.i("VersionCode" , getVersionCode()+"");
        Q.checkUpdate("post", getVersionCode(), checkVersionUrl ,null, new CheckUpdateCallback() {
            @Override
            public void onCheckUpdateSuccess(String result, boolean hasUpdate) {
                //result:服务端返回的json
                mCheckUpdateInfo = new Gson().fromJson(result, CheckUpdateInfo.class);
                //有更新,显示dialog等
                if (hasUpdate) {
                    //强制更新,这里用0表示强制更新,实际情况中可与服务端商定什么数字代表强制更新即可
                    if (mCheckUpdateInfo.getIsForceUpdate() == 0) {
                        //show ForceUpdateDialog
                        forceUpdateDialogClick( );
                    }
                    //非强制更新
                    else {
                        //show UpdateDialog
                        UpdateDialogClick();
                    }
                } else {
                    //无更新,提示已是最新版等
                    Toast.makeText(context, "已是最新版本", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCheckUpdateFailure(String failureMessage, int errorCode) {
                Toast.makeText(context, "无法连接服务端获取更新", Toast.LENGTH_SHORT).show();
                //failureMessage:一般为try{}catch(){}捕获的异常信息
                //errorCode:暂时没什么用
            }
        });

    }

    /**
     * 获取当前应用版本号
     */
    private int getVersionCode() {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public void forceUpdateDialogClick( ) {

        if (mCheckUpdateInfo.getIsForceUpdate() == 0) {
            mForceUpdateDialog = new ForceUpdateDialog(context);
            mForceUpdateDialog.setAppSize(mCheckUpdateInfo.getNewAppSize())
                    .setDownloadUrl(mCheckUpdateInfo.getNewAppUrl())
                    .setTitle(mCheckUpdateInfo.getAppName() + "有更新啦")
                    .setReleaseTime(mCheckUpdateInfo.getNewAppReleaseTime())
                    .setVersionName(mCheckUpdateInfo.getNewAppVersionName())
                    .setUpdateDesc(mCheckUpdateInfo.getNewAppUpdateDesc())
                    .setFileName("都邦水印相机.apk")
                    .setFilePath(Environment.getExternalStorageDirectory().getPath() + "/checkupdatelib").show();
        }
    }



    public void UpdateDialogClick( ) {
        if (mCheckUpdateInfo.getIsForceUpdate() == 1) {
            mUpdateDialog = new UpdateDialog(context);
            mUpdateDialog.setAppSize(mCheckUpdateInfo.getNewAppSize())
                    .setDownloadUrl(mCheckUpdateInfo.getNewAppUrl())
                    .setTitle(mCheckUpdateInfo.getAppName() + "有更新啦")
                    .setReleaseTime(mCheckUpdateInfo.getNewAppReleaseTime())
                    .setVersionName(mCheckUpdateInfo.getNewAppVersionName())
                    .setUpdateDesc(mCheckUpdateInfo.getNewAppUpdateDesc())
                    .setFileName(mCheckUpdateInfo.getAppName()+".apk")
                    .setFilePath(Environment.getExternalStorageDirectory().getPath() + "/checkupdatelib")
                    //该方法需设为true,才会在通知栏显示下载进度,默认为false,即不显示
                    //该方法只会控制下载进度的展示,当下载完成或下载失败时展示的通知不受该方法影响
                    //即不管该方法是置为false还是true,当下载完成或下载失败时都会在通知栏展示一个通知
                    .setShowProgress(true)
                    .setIconResId(R.mipmap.ic_launcher)
                    .setAppName(mCheckUpdateInfo.getAppName()).show();
        }
    }


    public void permissionsResultDown(){
        if (mCheckUpdateInfo.getIsForceUpdate() == 1) {
            mUpdateDialog.download();
        }else if(mCheckUpdateInfo.getIsForceUpdate() == 0){
            //进行强制下载操作
            mForceUpdateDialog.download();
        }
    }
}
