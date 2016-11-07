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
import android.util.Base64;
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

    //登录接口
    public  String loninUrl="http://lsuplus.top/api/v1/user/";

    //获取的账号
    private String account=null;

    //获取的密码
    private  String password=null;

    //bcrypt加密后的密码
    private String BCpassword;

    //用户信息
    private String userinfo=null;

    //用户id
    private int id;

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

                    //测试输入是否正常
                    if(account.equals("")||account==null||password.equals("")||password==null){
                        //提示输入为空
                        Toast.makeText(Register_main.this,"请输入邮箱和密码",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    progressBar.setVisibility(View.VISIBLE);

//                    //BCrypt加密 //已废弃
//                    DoBCrpty();

                    //访问网络
                    sendHttpURLConnection();

                }

            }
        });

//        btnScan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Register_main.this, CaptureActivity.class);
//                startActivityForResult(intent,0);
//            }
//        });

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
                new AlertDialog.Builder(Register_main.this).setTitle("请输入邮箱").
                        setIcon(android.R.drawable.ic_dialog_info).setView(
inputEmail).setPositiveButton("确定", null).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Toast.makeText(Register_main.this,inputEmail.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("取消", null).show();
                //Toast.makeText(Register_main.this,inputEmail.getText().toString(), Toast.LENGTH_LONG).show();
                
            }
        });
    }



    private void sendHttpURLConnection() {
        //开启子线程访问网络
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                try {
                    String userpassword = account+":"+password;
                    URL url = new URL(loninUrl+account);
                    final String basicAuth = "Basic " + Base64.encodeToString(userpassword.getBytes(), Base64.NO_WRAP);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestProperty ("Authorization", basicAuth);
                    connection.setRequestMethod("GET");
                    connection.connect();

                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
//                    connection.setInstanceFollowRedirects(true);
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
                        id = data.getInt("id");
                        userinfo = user+"\n"+email;

                        //将用户信息放入SharedPrerences中保存
                        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user",user);
                        editor.putString("email",email);
                        editor.putString("password",password);
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
                            Toast.makeText(Register_main.this,"登录成功",Toast.LENGTH_SHORT).show();
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

    private void initView() {
        //初始化控件
        // btnScan=(Button)findViewById(R.id.id_btnScan);
        etAccount=(EditText)findViewById(R.id.id_ETaccount);
        etPassword=(EditText)findViewById(R.id.id_ETpassword);
        Btnlonin=(Button)findViewById(R.id.id_Btnlogin);
        Btnback=(TextView)findViewById(R.id.id_registerBackText);
        progressBar=(ProgressBar)findViewById(R.id.id_LoninProgress);
        BtnforgetPass = (Button) findViewById(R.id.loginChangePw);
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
        Toast.makeText(Register_main.this,BCpassword,Toast.LENGTH_LONG).show();
        editor.putString("BCpassword",BCpassword);
        editor.commit();
    }


}