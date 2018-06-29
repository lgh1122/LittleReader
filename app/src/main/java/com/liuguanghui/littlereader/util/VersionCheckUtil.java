package com.liuguanghui.littlereader.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.allenliu.versionchecklib.callback.OnCancelListener;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener;
import com.google.gson.Gson;
import com.liuguanghui.littlereader.R;
import com.liuguanghui.littlereader.entity.CheckUpdateInfo;

/**
 * Created by liuguanghui on 2018/6/28.
 */

public class VersionCheckUtil {

    private Context context;
    private DownloadBuilder builder;

    public VersionCheckUtil(Context context) {
        this.context = context;
    }

    public void sendRequest() {
        builder = AllenVersionChecker
                .getInstance()
                .requestVersion()
                .setRequestUrl("http://10.3.29.67:9085/version/find")
                .request(new RequestVersionListener() {
                    @Nullable
                    @Override
                    public UIData onRequestVersionSuccess(String result) {
                        CheckUpdateInfo mCheckUpdateInfo = new Gson().fromJson(result, CheckUpdateInfo.class);
                        int localVersionCode = getVersionCode();
                        if(mCheckUpdateInfo.getNewAppVersionCode()>localVersionCode){
                            UIData uiData = UIData.create();
                            if(mCheckUpdateInfo.getIsUsePreDialog() == 0 ){
                                //使用默认界面

                                uiData.setTitle(mCheckUpdateInfo.getAppName()+"  更新");
                                uiData.setDownloadUrl(mCheckUpdateInfo.getNewAppUrl());
                                StringBuffer sb = new StringBuffer();
                                sb.append("发布时间：").append(mCheckUpdateInfo.getNewAppReleaseTime()).append("\n")
                                        .append("版本：").append(mCheckUpdateInfo.getNewAppVersionName()).append("\n")
                                        .append("大小：").append(mCheckUpdateInfo.getNewAppSize()).append("\n")
                                        .append("更新内容：").append(mCheckUpdateInfo.getNewAppUpdateDesc());
                                uiData.setContent(sb.toString());
                            }else{
                                // 使用自定义界面
                                uiData.setTitle(mCheckUpdateInfo.getAppName()+"  更新");
                                uiData.setDownloadUrl(mCheckUpdateInfo.getNewAppUrl());
                                StringBuffer sb = new StringBuffer();
                                sb.append("发布时间：").append(mCheckUpdateInfo.getNewAppReleaseTime()).append("\n")
                                        .append("版本：").append(mCheckUpdateInfo.getNewAppVersionName()).append("\n")
                                        .append("大小：").append(mCheckUpdateInfo.getNewAppSize()).append("M\n")
                                        .append("更新内容：").append(mCheckUpdateInfo.getNewAppUpdateDesc());
                                uiData.setContent(sb.toString());


                                builder.setCustomVersionDialogListener(new CustomVersionDialogListener() {
                                    @Override
                                    public Dialog getCustomVersionDialog(Context context, UIData versionBundle) {
                                        View view = View.inflate(context, R.layout.checkupdatelibrary_update_dialog_layout, null);
                                          TextView updateTitle = (TextView) view.findViewById(R.id.updateTitle);
                                          TextView updateTime = (TextView) view.findViewById(R.id.updateTime);
                                          TextView updateVersion = (TextView) view.findViewById(R.id.updateVersion);
                                          TextView updateSize = (TextView) view.findViewById(R.id.updateSize);
                                          TextView updateDesc = (TextView) view.findViewById(R.id.updateDesc);
                                          Button cancelBtn =   view.findViewById(R.id.versionchecklib_version_dialog_cancel);
                                          Button commitBtn =   view.findViewById(R.id.versionchecklib_version_dialog_commit);
                                        updateTitle.setText(mCheckUpdateInfo.getAppName()+"  更新");
                                        updateTime.setText("发布时间："+mCheckUpdateInfo.getNewAppReleaseTime());
                                        updateVersion.setText("版本："+mCheckUpdateInfo.getNewAppVersionName());
                                        updateSize.setText("大小："+mCheckUpdateInfo.getNewAppSize()+"M");
                                        updateDesc.setText(mCheckUpdateInfo.getNewAppUpdateDesc());
                                        updateDesc.setMovementMethod(ScrollingMovementMethod.getInstance());
                                        Dialog dialog =   new AlertDialog.Builder(context).setView(view).show();
                                        //updateDesc.setText(versionBundle.getContent());
                                       // updateDesc.setText(versionBundle.getContent());
                                        if(mCheckUpdateInfo.getIsForceUpdate() == 1){
                                            cancelBtn.setVisibility(View.GONE);
                                        }
                                        return dialog;
                                    }
                                });
                            }


                            if(mCheckUpdateInfo.getIsForceUpdate() == 1){
                                //强制更新操作
                                builder.setForceUpdateListener(new ForceUpdateListener() {
                                    @Override
                                    public void onShouldForceUpdate() {
                                    }
                                });
                            }
                            return uiData;
                        }else{
                            Toast.makeText(context, "已是最新版本", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                    }
                    @Override
                    public void onRequestVersionFailure(String message) {
                        Toast.makeText(context, "请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                });
        Toast.makeText(context, "正在检查更新，请稍等", Toast.LENGTH_SHORT).show();
        //不显示下载中对话框
        builder.setShowDownloadingDialog(false);
        //下载失败不显示失败对话框
        builder.setShowDownloadFailDialog(false);





       /* if (silentDownloadCheckBox.isChecked())
            builder.setSilentDownload(true);
        if (forceDownloadCheckBox.isChecked())
            builder.setForceRedownload(true);
        if (!showDownloadingCheckBox.isChecked())
            builder.setShowDownloadingDialog(false);
        if (!showNotificationCheckBox.isChecked())
            builder.setShowNotification(false);
        if (customNotificationCheckBox.isChecked())
            builder.setNotificationBuilder(createCustomNotification());
        if (!showDownloadFailedCheckBox.isChecked())
            builder.setShowDownloadFailDialog(false);
        if(silentDownloadCheckBoxAndInstall.isChecked()) {
            builder.setDirectDownload(true);
            builder.setShowNotification(false);
            builder.setShowDownloadingDialog(false);
            builder.setShowDownloadFailDialog(false);
        }*/


        /*//更新界面选择
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.btn1:
                break;
            case R.id.btn2:
                builder.setCustomVersionDialogListener(createCustomDialogOne());
                break;
            case R.id.btn3:
                builder.setCustomVersionDialogListener(createCustomDialogTwo());
                break;
        }*/

        //下载进度界面选择
        /*switch (radioGroup2.getCheckedRadioButtonId()) {
            case R.id.btn21:
                break;
            case R.id.btn22:
                builder.setCustomDownloadingDialogListener(createCustomDownloadingDialog());
                break;
        }*/
        //下载失败界面选择

        //自定义下载路径
        builder.setDownloadAPKPath(Environment.getExternalStorageDirectory() + "/BookApk/");
        //String address = etAddress.getText().toString();
        /*if (address != null && !"".equals(address))
            builder.setDownloadAPKPath(address);*/


        builder.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel() {
                //Toast.makeText(CameraSettingsActivity.this,"取消更新",Toast.LENGTH_SHORT).show();
            }
        });
        builder.excuteMission(context);
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
}
