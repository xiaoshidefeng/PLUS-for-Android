package com.example.cw.slidemeuetest.Setting;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.example.cw.slidemeuetest.R;

public class Setting extends ActionBarActivity {

    //Activity里嵌套Fragment
    private PreferenceFragment mPreferenceFragment;

    //返回
    private TextView Btnback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(themeId);
        setContentView(R.layout.activity_setting);

        if (savedInstanceState == null) {
            mPreferenceFragment = new PreferenceFragment();
            replaceFragment(R.id.settings_container, mPreferenceFragment);
        }

        initView();

        //返回按钮
        Btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }


    private void initView() {
        Btnback= (TextView)findViewById(R.id.id_registerBackText);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void replaceFragment(int viewId, android.app.Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }



}
