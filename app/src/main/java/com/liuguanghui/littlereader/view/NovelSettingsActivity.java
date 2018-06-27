package com.liuguanghui.littlereader.view;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
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

import com.liuguanghui.littlereader.R;
import com.liuguanghui.littlereader.util.UpdateVersionUtil;

public class NovelSettingsActivity extends PreferenceActivity   {


    private  Preference testcontentprovide;
    private  Preference updatePreference;
    private  Preference usedHelpPre;
    private AppCompatDelegate mDelegate;

    private  UpdateVersionUtil updateVersionUtil ;
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
                updateVersionUtil = new UpdateVersionUtil(NovelSettingsActivity.this,"http://10.3.29.67:9085/version/find");
                updateVersionUtil.initData();
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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int permissionStatus = ContextCompat.checkSelfPermission(NovelSettingsActivity.this, Manifest.permission.READ_SMS);
            Toast.makeText(NovelSettingsActivity.this,permissionStatus+"",Toast.LENGTH_SHORT).show();

            if(permissionStatus == PackageManager.PERMISSION_GRANTED){
                printSms();
            } else{
                requestPermissions(new String[]{Manifest.permission.READ_SMS},REQUEST_CODE_ASK_PERMISSIONS);
            }
        }else{
            printSms();
        }

    }
    private  void printSms(){
        ContentResolver resolver = getContentResolver();
        //获取的是哪些列的信息
        Uri uri = Uri.parse("content://sms/");
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //如果用户同意所请求的权限
        if(REQUEST_CODE_ASK_PERMISSIONS == requestCode){
            int permissionStatus = ContextCompat.checkSelfPermission(NovelSettingsActivity.this, Manifest.permission.READ_SMS);
            if(permissionStatus == PackageManager.PERMISSION_GRANTED){
                printSms();
            }else{
                Toast.makeText(NovelSettingsActivity.this,"用户拒绝了短信访问权限",Toast.LENGTH_SHORT).show();
            }
        }else{
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if( grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "许可权限，开始下载", Toast.LENGTH_SHORT).show();
                    updateVersionUtil.permissionsResultDown();
                } else {
                    //用户不同意,提示用户,如下载失败,因为您拒绝了相关权限
                    Toast.makeText(this, "请开启读写sd卡权限,不然无法保存更新文件", Toast.LENGTH_SHORT).show();
                }

            }
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
