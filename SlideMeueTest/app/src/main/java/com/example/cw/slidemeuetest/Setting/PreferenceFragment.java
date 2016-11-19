package com.example.cw.slidemeuetest.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import com.example.cw.slidemeuetest.R;

/**
 * Created by cw on 2016/11/15.
 */

public class PreferenceFragment extends android.preference.PreferenceFragment {

    //nightmode key设置
    private static final String PREF_KEY_NIGHTMODE = "night_switch";

    //修改密码 key设置
    private static final String PREF_KEY_CHANGEPW = "key_changepw";


    //夜间模式开关
    private SwitchPreference nightModeSwitch;

    //修改密码编辑框
    private Preference changePws;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_preferences);

        //初始化进控件
        initView();

//        nightModeSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object o) {
//
//            }
//        });

        //修改密码
        changePws.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ChangePassword();
                return false;
            }
        });

    }

    //修改密码
    private void ChangePassword() {
        Intent intent = new Intent("com.example.cw.slidemeuetest.ACTION_START");
        intent.addCategory("android.intent.category.DEFAULT");
        startActivity(intent);
    }


    private void initView() {
        nightModeSwitch = (SwitchPreference)findPreference(PREF_KEY_NIGHTMODE);
        changePws = (PreferenceScreen)findPreference(PREF_KEY_CHANGEPW);

    }
}
