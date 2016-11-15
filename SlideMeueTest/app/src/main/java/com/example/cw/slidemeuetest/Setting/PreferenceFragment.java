package com.example.cw.slidemeuetest.Setting;

import android.os.Bundle;
import android.preference.SwitchPreference;

import com.example.cw.slidemeuetest.R;

/**
 * Created by cw on 2016/11/15.
 */

public class PreferenceFragment extends android.preference.PreferenceFragment {

    //夜间模式开关
    private SwitchPreference nightModeSwitch;

    //key设置
    private static final String PREF_KEY_NIGHTMODE = "night_switch";

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

    }

    private void initView() {
        nightModeSwitch = (SwitchPreference)findPreference(PREF_KEY_NIGHTMODE);
    }
}
