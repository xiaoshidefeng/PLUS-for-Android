package com.example.cw.slidemeuetest.Setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.widget.Toast;

import com.example.cw.slidemeuetest.R;

/**
 * Created by cw on 2016/11/15.
 */

public class PreferenceFragment extends android.preference.PreferenceFragment {

    //nightmode key设置
    private static final String PREF_KEY_NIGHTMODE = "night_switch";

    //修改密码 key设置
    private static final String PREF_KEY_CHANGEPW = "key_changepw";

    //关于与帮助key设置
    private static final String PREF_KEY_ABOUTHELP = "key_about_help";

    //账号设置 key设置
    private static final String PREF_KEY_USERSETTING = "key_usersetting";

    //退出账号 key设置
    private static final String PREF_KEY_EXIT = "key_exit";


    //夜间模式开关
    private SwitchPreference nightModeSwitch;

    //修改密码编辑框
    private Preference changePws;

    //退出账号
    private Preference exit;

    //用户名
    private String username;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_preferences);

        getusername();
        if(username==null||username.equals("")){
            ((PreferenceGroup)findPreference(PREF_KEY_ABOUTHELP)).removePreference(findPreference(PREF_KEY_EXIT));
            getPreferenceScreen().removePreference(findPreference(PREF_KEY_USERSETTING));

            //没有登录时的控件初始化
            initViewNoUser();
        }else {
            //登录时的控件初始化
            initViewHaveUser();

            //修改密码
            changePws.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    ChangePassword();
                    return false;
                }
            });

            //退出账号
            exit.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    ConfirmExit();
                    return false;
                }
            });
        }



//        nightModeSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object o) {
//
//            }
//        });



    }

    private void initViewNoUser() {
        //没有登录时的控件初始化
        nightModeSwitch = (SwitchPreference)findPreference(PREF_KEY_NIGHTMODE);

    }

    //修改密码
    private void ChangePassword() {
        Intent intent = new Intent("com.example.cw.slidemeuetest.ACTION_START");
        intent.addCategory("android.intent.category.DEFAULT");
        startActivity(intent);
    }


    private void initViewHaveUser() {
        nightModeSwitch = (SwitchPreference)findPreference(PREF_KEY_NIGHTMODE);
        changePws = (PreferenceScreen)findPreference(PREF_KEY_CHANGEPW);
        exit = (Preference)findPreference(PREF_KEY_EXIT);
    }

    //退出登录
    private void ExitLog(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        username = sharedPreferences.getString("name","");
        editor.putString("token","");
        editor.putInt("id",0);
        editor.putString("name","");
        editor.putString("email","");
        editor.putString("created_at","");
        editor.putString("updated_at","");
        editor.putString("admin","");
        editor.commit();
        ((PreferenceGroup)findPreference(PREF_KEY_ABOUTHELP)).removePreference(findPreference(PREF_KEY_EXIT));
        getPreferenceScreen().removePreference(findPreference(PREF_KEY_USERSETTING));
        Toast.makeText(getActivity(),username+"消失在了异次元空间里",Toast.LENGTH_LONG).show();
    }

    //获取用户信息
    private void getusername(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        username = sharedPreferences.getString("name","");
    }


    //点击退出 弹出对话框 确认是否退出
    private void ConfirmExit(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("确定要退出吗？");//设置对话框的标题
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  //这个是设置确定按钮

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //确定退出
                ExitLog();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {  //取消按钮

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                return;

            }
        });
        AlertDialog b=builder.create();
        b.show();  //必须show一下才能看到对话框，跟Toast一样的道理
    }

}
