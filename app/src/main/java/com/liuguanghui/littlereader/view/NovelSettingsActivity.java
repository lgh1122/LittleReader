package com.liuguanghui.littlereader.view;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.liuguanghui.littlereader.R;
import com.qiangxi.checkupdatelibrary.Q;
import com.qiangxi.checkupdatelibrary.bean.CheckUpdateInfo;
import com.qiangxi.checkupdatelibrary.callback.CheckUpdateCallback;
import com.qiangxi.checkupdatelibrary.dialog.ForceUpdateDialog;
import com.qiangxi.checkupdatelibrary.dialog.UpdateDialog;

import static com.qiangxi.checkupdatelibrary.dialog.ForceUpdateDialog.FORCE_UPDATE_DIALOG_PERMISSION_REQUEST_CODE;
import static com.qiangxi.checkupdatelibrary.dialog.UpdateDialog.UPDATE_DIALOG_PERMISSION_REQUEST_CODE;

public class NovelSettingsActivity extends PreferenceActivity   {


    private  Preference testcontentprovide;
    private  Preference updatePreference;
    private  Preference usedHelpPre;
    private AppCompatDelegate mDelegate;
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {

                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);


                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {

                if (TextUtils.isEmpty(stringValue)) {

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        preference.setSummary(null);
                    } else {
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {

                preference.setSummary(stringValue);
            }
            return true;
        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //过滤已经的XML文件资源，并将当前的preference层添加到这个preference层当中
        addPreferencesFromResource(R.xml.pref_general);

        bindPreferenceSummaryToValue(findPreference("example_text"));
        bindPreferenceSummaryToValue(findPreference("quality_list"));

        testcontentprovide = findPreference("testcontentprovide");
        updatePreference = findPreference("cameraUpdate");
        usedHelpPre = findPreference("usedHelp");
        initClick();
    }

    private void initClick() {


        testcontentprovide.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                getMsgs();
                return false;
            }
        });
        updatePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                int permissionStatus = ContextCompat.checkSelfPermission(NovelSettingsActivity.this, "android.permission-group.STORAGE");

                Toast.makeText(NovelSettingsActivity.this,permissionStatus+"",Toast.LENGTH_SHORT).show();
                initData();
                return false;
            }
        });

        usedHelpPre.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(NovelSettingsActivity.this,usedHelpPre.getTitle(),Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(NovelSettingsActivity.this, NovelSettingsActivity.class));
                return false;
            }
        });

    }
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    public void getMsgs(){
        Uri uri = Uri.parse("content://sms/");




        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasWriteContactsPermission =  checkSelfPermission(Manifest.permission.READ_CONTACTS);

            Toast.makeText(this,"检查权限"+hasWriteContactsPermission,Toast.LENGTH_SHORT).show();


            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},REQUEST_CODE_ASK_PERMISSIONS);
                System.out.println("没有权限" );
                Log.i("TESTLITT","地址:" + "没有权限");
                return;
            }else{
                ContentResolver resolver = getContentResolver();
                //获取的是哪些列的信息
                Cursor cursor = resolver.query(uri, new String[]{"address","date","type","body"}, null, null, null);
                while(cursor.moveToNext())
                {
                    String address = cursor.getString(0);
                    String date = cursor.getString(1);
                    String type = cursor.getString(2);
                    String body = cursor.getString(3);
                    Log.i("TESTLITT","地址:" + address);
                    Log.i("TESTLITT","内容:" + body);
                   /* System.out.println("地址:" + address);
                    System.out.println("时间:" + date);
                    System.out.println("类型:" + type);
                    System.out.println("内容:" + body);
                    System.out.println("======================");*/
                }
                cursor.close();
            }

            System.out.println("测试完" );
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //如果用户同意所请求的权限
        if(REQUEST_CODE_ASK_PERMISSIONS == requestCode){
            Toast.makeText(this, "用户同意了获取短信权限", Toast.LENGTH_SHORT).show();
        }else{
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //UPDATE_DIALOG_PERMISSION_REQUEST_CODE和FORCE_UPDATE_DIALOG_PERMISSION_REQUEST_CODE这两个常量是library中定义好的
                //所以在进行判断时,必须要结合这两个常量进行判断.
                //非强制更新对话框
                if (requestCode == UPDATE_DIALOG_PERMISSION_REQUEST_CODE) {
                    //进行下载操作
                    mUpdateDialog.download();
                }
                //强制更新对话框
                else if (requestCode == FORCE_UPDATE_DIALOG_PERMISSION_REQUEST_CODE) {
                    //进行下载操作
                    mForceUpdateDialog.download();
                }
            } else {
                //用户不同意,提示用户,如下载失败,因为您拒绝了相关权限
                Toast.makeText(this, "请开启读写sd卡权限,不然无法正常工作", Toast.LENGTH_SHORT).show();
//            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                Log.e("tag", "false.请开启读写sd卡权限,不然无法正常工作");
//            } else {
//                Log.e("tag", "true.请开启读写sd卡权限,不然无法正常工作");
//            }

            }
        }

    }

    private CheckUpdateInfo mCheckUpdateInfo;
    /**
     * 强制更新
     */
    private   ForceUpdateDialog mForceUpdateDialog = null;
    /**
     * 非强制更新
     */
    private  UpdateDialog mUpdateDialog = null;
    private void initData() {
        //被注释的代码用来进行检查更新,这里为了方便,模拟一些假数据
        Q.checkUpdate("post", getVersionCode(), "http://10.3.29.67:9085/version/find",null, new CheckUpdateCallback() {
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
                    Toast.makeText(NovelSettingsActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCheckUpdateFailure(String failureMessage, int errorCode) {
                Toast.makeText(NovelSettingsActivity.this, "无法连接服务端获取更新", Toast.LENGTH_SHORT).show();
                //failureMessage:一般为try{}catch(){}捕获的异常信息
                //errorCode:暂时没什么用
            }
        });

    }


    public void forceUpdateDialogClick( ) {

        if (mCheckUpdateInfo.getIsForceUpdate() == 0) {
            mForceUpdateDialog = new ForceUpdateDialog(NovelSettingsActivity.this);
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
            mUpdateDialog = new UpdateDialog(NovelSettingsActivity.this);
            mUpdateDialog.setAppSize(mCheckUpdateInfo.getNewAppSize())
                    .setDownloadUrl(mCheckUpdateInfo.getNewAppUrl())
                    .setTitle(mCheckUpdateInfo.getAppName() + "有更新啦")
                    .setReleaseTime(mCheckUpdateInfo.getNewAppReleaseTime())
                    .setVersionName(mCheckUpdateInfo.getNewAppVersionName())
                    .setUpdateDesc(mCheckUpdateInfo.getNewAppUpdateDesc())
                    .setFileName("都邦水印相机.apk")
                    .setFilePath(Environment.getExternalStorageDirectory().getPath() + "/checkupdatelib")
                    //该方法需设为true,才会在通知栏显示下载进度,默认为false,即不显示
                    //该方法只会控制下载进度的展示,当下载完成或下载失败时展示的通知不受该方法影响
                    //即不管该方法是置为false还是true,当下载完成或下载失败时都会在通知栏展示一个通知
                    .setShowProgress(true)
                    .setIconResId(R.mipmap.ic_launcher)
                    .setAppName(mCheckUpdateInfo.getAppName()).show();
        }
    }
    /**
     * 获取当前应用版本号
     */
    private int getVersionCode() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }


    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public  ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }
    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }
    @Override
    public final boolean onMenuItemSelected(int featureId, android.view.MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home){
            //这种方式会创建多个activity实例
            //startActivity(new Intent(CameraSettingsActivity.this, CameraActivity.class));
            this.finish();
            return true;
        }
        return super.onMenuItemSelected(featureId,item);
    }
    private static void bindPreferenceSummaryToValue(Preference preference) {

        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

}
