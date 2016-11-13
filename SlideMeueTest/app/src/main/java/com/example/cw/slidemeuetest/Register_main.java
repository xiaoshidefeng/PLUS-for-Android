package com.example.cw.slidemeuetest;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cw.slidemeuetest.jbcrypt.BCrypt;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Register_main extends AppCompatActivity {
    //账号输入
    private EditText etAccount;

    //密码输入
    private EditText etPassword;
    //

    //登录按钮
    private Button Btnlonin;

    //扫码登录按钮
//    private Button btnScan;

    //扫码结果显示
    private TextView tvResult;

    //登录接口 新的token验证 获取token值
    private  String loninUrl="http://lsuplus.top/api/user/login/";

    //登录接口 用token获取用户信息
    private String getUserInfoUrl = "http://lsuplus.top/api/user/me/?token=";

    //修改密码接口
    private String forgetUrl = "http://lsuplus.top/password/email/?email=";

    //获取的账号
    private String account=null;

    //获取的密码
    private  String password=null;

    //获取的token
    private String token;

    //bcrypt加密后的密码
    private String BCpassword;

    //用户信息
    private String userinfo=null;

    //用户id
    private int id;

    //管理员
    private String admin = "";

    //load字样
    private TextView loadText;

    //进度显示
    private TextView loadNumText;

    //进度条
    private ProgressBar progressBar;

    //返回
    private TextView Btnback;

    //忘记密码
    private Button BtnforgetPass;

    //要修改的邮箱
    private String forgetEmail;

    //注册账号
    private Button BtnRegist;

    //帮助按钮
    private Button BtnHelp;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        //隐藏标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        //初始化控件
        initView();

        //隐藏进度条
        progressBar.setVisibility(View.GONE);

        Btnlonin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.id_Btnlogin){

                    //点击登录按钮后 强制隐藏键盘
                    InputMethodManager immPw = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    immPw.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
                    InputMethodManager immUn = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    immUn.hideSoftInputFromWindow(etAccount.getWindowToken(), 0);

                    //获取文本框
                    account=etAccount.getText().toString();
                    password=etPassword.getText().toString();


                    //尝试登录
                    attemptLogin();


                }

            }
        });


        //返回
        Btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //忘记密码
        BtnforgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inputEmail = new EditText(Register_main.this);
                new AlertDialog.Builder(Register_main.this).setTitle("请输入要修改的邮箱")
                       // setMessage("请输入要修改的邮箱：").
                        .setView(
inputEmail).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //点击确定按钮时 用post方法发送
                        forgetEmail=inputEmail.getText().toString();
                        sendForgetpasswordHttpURLConnection();

                        //点击确定按钮后 强制隐藏键盘
                        InputMethodManager immPw = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        immPw.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
                        InputMethodManager immUn = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        immUn.hideSoftInputFromWindow(etAccount.getWindowToken(), 0);
                        Toast.makeText(Register_main.this,"已发送邮件到"+forgetEmail+"请注意查收", Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("取消", null).show();

            }
        });

        //注册账号
        BtnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register_main.this,Regist.class);
                startActivity(intent);
            }
        });


    }



    private void sendHttpURLConnection() {
        //开启子线程访问网络 获取token模块
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                try {
                    String userpassword = "?email="+account+"&password="+password;
                    URL url = new URL(loninUrl+userpassword);

                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
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

                    if(userJSON.has("token")){
                        //如果登录成功
                        token = userJSON.getString("token");

                        //GET获取用户信息
                        sendHttpURLConnectionGETuserInfo();

                    }else if(userJSON.has("error")){
                        Toast.makeText(Register_main.this,"账号密码错误！",Toast.LENGTH_SHORT).show();
                        return;
                    }


                }   catch (Exception e) {
                    Log.e("errss", e.getMessage());
                }
            }
        }).start();
    }

    //通过GET方法 用token获取用户信息
    private void sendHttpURLConnectionGETuserInfo() {
        //开启子线程访问网络 获取用户信息模块
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                try {

                    URL url = new URL(getUserInfoUrl+token);

                    connection = (HttpURLConnection)url.openConnection();
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

                    if(userJSON.has("user")){
                        //token成功
                        JSONObject userJ = userJSON.getJSONObject("user");
                        id = userJ.getInt("id");
                        String name = userJ.getString("name");
                        String email = userJ.getString("email");
                        String created_at = userJ.getString("created_at");
                        String updated_at = userJ.getString("updated_at");
                        if(userJ.has("admin")){
                            //是否为管理员
                            admin = userJ.getString("admin");
                        }

                        //保存用户信息
                        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token",token);
                        editor.putInt("id",id);
                        editor.putString("name",name);
                        editor.putString("email",email);
                        editor.putString("created_at",created_at);
                        editor.putString("updated_at",updated_at);
                        editor.putString("admin",admin);
                        editor.commit();

                        //发送广播 通知MainActivity更新用户ui
                        Intent intent = new Intent();
                        intent.setAction("com.example.broadcasttest.USERUI_BROADCAST");
                        sendBroadcast(intent);

                        //开启ui线程来通知用户登录成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Register_main.this,"登录成功",Toast.LENGTH_SHORT).show();
                                //隐藏进度条
                                progressBar.setVisibility(View.GONE);
                                //返回MainActivity
                                finish();
                            }
                        });

                    }else {
                        Toast.makeText(Register_main.this,"未知错误！",Toast.LENGTH_SHORT).show();
                        return;
                    }

                }   catch (Exception e) {
                    Log.e("errss", e.getMessage());
                }
            }
        }).start();
    }

    private void initView() {
        //初始化控件
        etAccount=(EditText)findViewById(R.id.id_ETaccount);
        etPassword=(EditText)findViewById(R.id.id_ETpassword);
        Btnlonin=(Button)findViewById(R.id.id_Btnlogin);
        Btnback=(TextView)findViewById(R.id.id_registerBackText);
        progressBar=(ProgressBar)findViewById(R.id.id_LoninProgress);
        BtnforgetPass = (Button) findViewById(R.id.loginChangePw);
        BtnRegist = (Button)findViewById(R.id.id_btnRegist);
        BtnHelp = (Button)findViewById(R.id.id_btnHelp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            String result= data.getExtras().getString("result");
            tvResult.setText(result);
        }
    }

    private void DoBCrpty(){
        //BCrypt加密
        String  originalPassword = password;
        BCpassword= BCrypt.hashpw(originalPassword, BCrypt.gensalt(12));
        //把密码加密结果放入Share
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Toast.makeText(Register_main.this,BCpassword,Toast.LENGTH_SHORT).show();
        editor.putString("BCpassword",BCpassword);
        editor.commit();
    }

    //修改密码 发送post请求至服务器
    private void sendForgetpasswordHttpURLConnection() {
        //开启子线程访问网络 修改密码模块
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;

                try {
                    URL url = new URL(forgetUrl+forgetEmail);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
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

    //输入格式提示
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
        etAccount.setError(null);
        etPassword.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            etPassword.setError(getString(R.string.error_invalid_password));
            focusView = etPassword;
            cancel = true;
        }else if(password.equals("")||password==null){
            etPassword.setError(getString(R.string.error_null_password));
            focusView = etPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(account)) {
            etAccount.setError(getString(R.string.error_field_required));
            focusView = etAccount;
            cancel = true;
        } else if (!isEmailValid(account)) {
            etAccount.setError(getString(R.string.error_invalid_email));
            focusView = etAccount;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            // showProgress(true);
            // mAuthTask = new UserLoginTask(email, password);
            // mAuthTask.execute((Void) null);

            //进度条开始转动
            progressBar.setVisibility(View.VISIBLE);

            //访问网络
            sendHttpURLConnection();
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
}