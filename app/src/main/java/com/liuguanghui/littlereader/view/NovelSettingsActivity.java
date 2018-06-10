package com.liuguanghui.littlereader.view;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;

import com.liuguanghui.littlereader.R;

public class NovelSettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

//https://www.jb51.net/article/75874.htm
    /** Called when the activity is first created. */
    ListPreference lp;//创建一个ListPreference对象


    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
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
        //初始化这个ListPreference对象
        //方式一
        /*lp=(ListPreference)findPreference(getString(R.string.example_list));
        //设置获取ListPreference中发生的变化
        lp.setOnPreferenceChangeListener(this);
         *//**让ListPreference中的摘要内容（即summary）显示为当前ListPreference中的实体对应的值
         * 这个方法的作用是为了当下一次打开这个程序时会显示上一次的设置的summary(摘要)
         * 如果没有添加这个方法，当再次打开这个程序时，它将不会显示上一次程序设置的值，而
         * 是显示默认值*//**//*
         *//*
        lp.setSummary(lp.getEntry());*/
        bindPreferenceSummaryToValue(findPreference("example_text"));
        bindPreferenceSummaryToValue(findPreference("example_list"));

    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // TODO Auto-generated method stub
        if(preference instanceof ListPreference){
            //把preference这个Preference强制转化为ListPreference类型
            ListPreference listPreference=(ListPreference)preference;
            //获取ListPreference中的实体内容
            CharSequence[] entries=listPreference.getEntries();
            //获取ListPreference中的实体内容的下标值
            int index=listPreference.findIndexOfValue((String)newValue);
            //把listPreference中的摘要显示为当前ListPreference的实体内容中选择的那个项目
            listPreference.setSummary(entries[index]);
        }

        return true;
    }
}
