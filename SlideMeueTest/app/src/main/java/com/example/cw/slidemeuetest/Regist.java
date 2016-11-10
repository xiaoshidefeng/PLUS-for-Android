package com.example.cw.slidemeuetest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Regist extends AppCompatActivity {

    //返回
    private TextView Btnback;

    //进度条
    private ProgressBar progressBar;

    //注册按钮
    private Button BtnRegist;

    //用户邮箱输入框
    private EditText EtEmail;

    //用户姓名
    private EditText EtName;

    //用户密码输入框1
    private EditText EtPasswordOne;

    //用户密码输入框2
    private EditText EtPasswordTwo;

    //关于LSUPLUS
    private Button BtnAboutLsuPlus;

    //用户邮箱
    private String email;
    //用户姓名
    private String name;

    //用户密码1
    private String passwordOne;

    //用户密码2
    private String passwordTwo;

    //注册接口
    String RegistUrl = "http://lsuplus.top/auth/register/";

    //登录接口
    public  String loninUrl="http://lsuplus.top/api/v1/user/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        //初始化控件
        initView();

        //隐藏进度条
        progressBar.setVisibility(View.GONE);

        sendLogoutHttpURLConnection();

        //点击注册按钮
        BtnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //点击确定按钮后 强制隐藏键盘
                InputHid();

                //尝试注册
                attemptLogin();

            }
        });

        //返回
        Btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initView() {
        //初始化控件
        Btnback= (TextView)findViewById(R.id.id_registerBackText);
        progressBar=(ProgressBar)findViewById(R.id.id_LoninProgress);
        EtEmail = (EditText)findViewById(R.id.id_ETaccountregist);
        EtName = (EditText)findViewById(R.id.id_ETname);
        EtPasswordOne = (EditText)findViewById(R.id.id_ETpasswordOne);
        EtPasswordTwo = (EditText)findViewById(R.id.id_ETpasswordTwo);
        BtnRegist = (Button)findViewById(R.id.id_Btnregist);
        BtnAboutLsuPlus = (Button)findViewById(R.id.id_btnAbout);


    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        EtEmail.setError(null);
        EtPasswordOne.setError(null);
        EtPasswordTwo.setError(null);

        // Store values at the time of the login attempt.
        email = EtEmail.getText().toString();
        name =EtName.getText().toString();
        passwordOne = EtPasswordOne.getText().toString();
        passwordTwo = EtPasswordTwo.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordOne) && !isPasswordValid(passwordOne)) {
            EtPasswordOne.setError(getString(R.string.error_invalid_password));
            focusView = EtPasswordOne;
            cancel = true;
        }else if (passwordOne==null||passwordOne.equals("")){
            EtPasswordOne.setError(getString(R.string.error_null_password));
            focusView = EtPasswordOne;
            cancel = true;
        }else if (passwordTwo==null||passwordOne.equals("")){
            EtPasswordTwo.setError(getString(R.string.error_null_password));
            focusView = EtPasswordTwo;
            cancel = true;
        }else if(!passwordOne.equals(passwordTwo)){
            EtPasswordTwo.setError(getString(R.string.error_uneqully_password));
            focusView = EtPasswordTwo;
            cancel = true;
        }

        // 确认姓名输入是否正确
        if(name.equals("")||name==null){
            EtName.setError(getString(R.string.error_null_password));
            focusView = EtName;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            EtEmail.setError(getString(R.string.error_field_required));
            focusView = EtEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            EtEmail.setError(getString(R.string.error_invalid_email));
            focusView = EtEmail;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            showProgress(true);
//            mAuthTask = new UserLoginTask(email, password);
//            mAuthTask.execute((Void) null);

            //开启子线程访问网络 POST
            sendRegistHttpURLConnection();

            //进度条开始转动
            progressBar.setVisibility(View.VISIBLE);

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    //注册账号 发送post请求至服务器
    private void sendRegistHttpURLConnection() {
        //开启子线程访问网络 注册模块
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;

                try {
                    URL url = new URL(RegistUrl+"?email="+email+
                    "&name="+name+"&password="+passwordOne+"&password_confirmation="+passwordTwo);
//                    URL url =new URL("http://lsuplus.top/auth/register/?email=17@qq.com&name=17e&password=123456&password_confirmation=123456");
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.connect();

                    //连接超时
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    //获取输入流
                    InputStream in = connection.getInputStream();

                    //对获取的流进行读取
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                    StringBuilder response = new StringBuilder();
                    String line=null;
                    while ((line=reader.readLine())!=null){
                        response.append(line);

                    }

                    //后台登录
                    sendLoginHttpURLConnection();

                    //回到主界面
                    Intent intent1 = new Intent(Regist.this,MainActivity.class);
                    startActivity(intent1);

                }   catch (Exception e) {
                    Log.e("errss", e.getMessage());

                }
            }
        }).start();
    }

    //后台登录 获取用户信息
    private void sendLoginHttpURLConnection() {
        //开启子线程访问网络 后台登录模块
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                try {
                    String userpassword = email+":"+passwordOne;
                    URL url = new URL(loninUrl+email);
                    final String basicAuth = "Basic " + Base64.encodeToString(userpassword.getBytes(), Base64.NO_WRAP);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestProperty ("Authorization", basicAuth);
                    connection.setRequestMethod("GET");
                    connection.connect();

                    //连接超时设置
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    //获取输入流
                    InputStream in = connection.getInputStream();

                    //对获取的流进行读取
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                    StringBuilder response = new StringBuilder();
                    String line=null;
                    while ((line=reader.readLine())!=null){
                        response.append(line);
                    }

                    //创建JSON对象
                    JSONObject userJSON = new JSONObject(response.toString());
                    String status = userJSON.getString("status");

                    //解析JSON数据
                    if(status.equals("success")){
                        JSONObject data = userJSON.getJSONObject("data");
                        String user = data.getString("user");
                        String email = data.getString("email");
                        int id = data.getInt("id");
                        String userinfo = user+"\n"+email;

                        //将用户信息放入SharedPrerences中保存
                        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user",user);
                        editor.putString("email",email);
                        editor.putString("password",passwordOne);
                        editor.putInt("id",id);
                        editor.commit();

                        //发送广播 通知MainActivity更新用户ui
                        Intent intent = new Intent();
                        intent.setAction("com.example.broadcasttest.USERUI_BROADCAST");
                        sendBroadcast(intent);
                    }

                    //开启ui线程来通知用户登录成功
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Regist.this,"注册成功",Toast.LENGTH_SHORT).show();
                            //隐藏进度条
                            progressBar.setVisibility(View.GONE);
                            //返回MainActivity
                            finish();
                        }
                    });

                }   catch (Exception e) {
                    Log.e("errss", e.getMessage());
                }
            }
        }).start();
    }


    //退出登录 sendLogout
    private void sendLogoutHttpURLConnection() {
        //开启子线程访问网络 退出登录模块
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;

                try {
                    URL url = new URL("http://lsuplus.top/auth/logout");
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    //连接超时
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);


                }   catch (Exception e) {
                    Log.e("errss", e.getMessage());

                }
            }
        }).start();
    }

    //点击确定按钮后 强制隐藏键盘
    private void InputHid(){

        InputMethodManager immEm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        immEm.hideSoftInputFromWindow(EtEmail.getWindowToken(), 0);
        
        InputMethodManager immNm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        immNm.hideSoftInputFromWindow(EtName.getWindowToken(), 0);

        InputMethodManager immPo = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        immPo.hideSoftInputFromWindow(EtPasswordOne.getWindowToken(), 0);

        InputMethodManager immPt = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        immPt.hideSoftInputFromWindow(EtPasswordTwo.getWindowToken(), 0);

    }

}
