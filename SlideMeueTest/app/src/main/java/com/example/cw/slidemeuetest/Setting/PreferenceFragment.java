package com.example.cw.slidemeuetest.Setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.widget.EditText;
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

    //开源许可key
    private static final String PREF_KEY_LICENSE = "key_license";


    //特别感谢
    private static final String PREF_KEY_SPECIALTHANKS = "key_specialthanks";

    //关于项目
    private static final String PREF_KEY_ABOUTPROJECT = "key_aboutproject";

    //账号设置 key设置
    private static final String PREF_KEY_USERSETTING = "key_usersetting";

    //小尾巴
    private static final String PREF_KEY_SMALLTAIL = "key_smalltail";

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

    //特别感谢
    private Preference thanks;

    //开源许可
    private Preference license;

    //关于项目
    private Preference aboutproject;

    //小尾巴
    private Preference smalltail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_preferences);


        
        getusername();
        if(username==null||username.equals("")||!isNetworkAvailable(this.getActivity())){
            ((PreferenceGroup)findPreference(PREF_KEY_ABOUTHELP)).removePreference(findPreference(PREF_KEY_EXIT));
            getPreferenceScreen().removePreference(findPreference(PREF_KEY_USERSETTING));

            //没有登录时的控件初始化
            initViewNoUser();

            //点击监听
            ClickEvent();

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


            //点击监听
            ClickEvent();

        }



//        nightModeSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object o) {
//
//            }
//        });



    }

    //点击监听
    private void ClickEvent() {

        smalltail.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Smalltailset();
                return false;
            }
        });

        thanks.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SpecialThanks();
                return false;
            }


        });

        license.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(),LincenseActivity.class);
                startActivity(intent);
                return false;
            }
        });

        aboutproject.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AboutProject();
                return false;
            }
        });
    }

    //小尾巴
    private void Smalltailset(){

        final EditText tailedit = new EditText(getActivity());

        tailedit.setText(smalltail.getSummary());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.smalltail);//设置对话框的标题
        builder.setView(tailedit);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String tail = tailedit.getText().toString();
                if(!tail.equals("来自Android客户端")){
                    //保存小尾巴信息
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("smalltail",tail);
                    editor.putString("tailchange","changed");
                    editor.commit();
                    smalltail.setSummary(tail);
                }else {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("smalltail",tail);
                    editor.putString("tailchange","nochange");
                    editor.commit();
                    smalltail.setSummary(tail);
                }
            }
        });
        builder.setNegativeButton("取消",null);


        builder.show();

    }


    //关于项目
    private void AboutProject(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.aboutproject);//设置对话框的标题
        builder.setMessage(R.string.aboutprojectmsg);
        builder.setPositiveButton(R.string.gotogithub, new DialogInterface.OnClickListener() {  //这个是设置确定按钮

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //去点赞
                Uri uri = Uri.parse("https://github.com/xiaoshidefeng/PLUS-for-Android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

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
    //特别感谢
    private void SpecialThanks() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.specialthanks);//设置对话框的标题
        builder.setMessage(R.string.specialthanksmsg);
        builder.setPositiveButton(R.string.gotogithub, new DialogInterface.OnClickListener() {  //这个是设置确定按钮

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //去点赞
                Uri uri = Uri.parse("https://github.com/Robinson28years/lsuplusclub");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

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


    private void initViewNoUser() {
        //没有登录时的控件初始化
        nightModeSwitch = (SwitchPreference)findPreference(PREF_KEY_NIGHTMODE);
        thanks = (Preference)findPreference(PREF_KEY_SPECIALTHANKS);
        aboutproject = (Preference)findPreference(PREF_KEY_ABOUTPROJECT);
        license = (Preference)findPreference(PREF_KEY_LICENSE);
        smalltail = (Preference)findPreference(PREF_KEY_SMALLTAIL);

        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String s = sharedPreferences2.getString("smalltail","");
        String change = sharedPreferences2.getString("tailchange","");
        if(change.equals("changed")){
            smalltail.setSummary(s);
        }

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
        thanks = (Preference)findPreference(PREF_KEY_SPECIALTHANKS);
        aboutproject = (Preference)findPreference(PREF_KEY_ABOUTPROJECT);
        license = (Preference)findPreference(PREF_KEY_LICENSE);
        smalltail = (Preference)findPreference(PREF_KEY_SMALLTAIL);


        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String s = sharedPreferences2.getString("smalltail","");
        String change = sharedPreferences2.getString("tailchange","");
        if(change.equals("changed")){
            smalltail.setSummary(s);
        }

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
        editor.putString("imgurl",null);
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

    //判断当前网络是否可用
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

}
