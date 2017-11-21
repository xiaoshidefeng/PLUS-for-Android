package com.example.cw.slidemeuetest.MainActivityFragment.Fragmenttwo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cw.slidemeuetest.MainActivity;
import com.example.cw.slidemeuetest.R;
import com.example.cw.slidemeuetest.LoginRegist.Register_main;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NewPostActivity extends AppCompatActivity {

    //返回
    private TextView Tvback;

    //标题输入
    private EditText etTitle;

    //进度条
    private ProgressBar progressBar;


    //内容输入
    private EditText etContent;

    private ImageView imSend;

    //更新token api
    private static String tokenValidTestUrl = "http://lsuplus.top/api/refresh/?token=";

    //发贴
    private static String NewPostUrl = "http://lsuplus.top/api/discuss/store?token=";

    //token
    private String token;
    //用户id
    private String userid;

    //标题
    private String title;
    //内容
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        initview();

        //隐藏进度条
        progressBar.setVisibility(View.GONE);

        Tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptNewPost();
            }
        });

    }

    private void initview() {
        Tvback = (TextView)findViewById(R.id.id_BackText);
        etTitle = (EditText)findViewById(R.id.id_etposttitle);
        etContent = (EditText)findViewById(R.id.id_etpostcontent);
        imSend = (ImageView)findViewById(R.id.id_IMNewpost);
        progressBar = (ProgressBar)findViewById(R.id.id_Pbnewpost);
    }

    //输入格式提示
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptNewPost() {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        etTitle.setError(null);
        etContent.setError(null);

        boolean cancel = false;
        View focusView = null;

        title = etTitle.getText().toString();
        content = etContent.getText().toString();

//        content.replace("\n","\n\n");
        Log.e("status",content);

        // 标题判断
        if(title.equals("")||title==null){
            etTitle.setError(getString(R.string.error_null_posttitle));
            focusView = etTitle;
            cancel = true;
        }
        // 内容判断
        if(content.equals("")||content==null){
            etContent.setError(getString(R.string.error_null_postcontent));
            focusView = etContent;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else {
            //进度条开始转动
            progressBar.setVisibility(View.VISIBLE);

            //访问网络
            getuserinfo();
        }
    }

    private void getuserinfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token","");
        int i =  sharedPreferences.getInt("id",0);
        userid = String.valueOf(i);

        if(token.equals("")){
            Toast.makeText(NewPostActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);

            return;
        }

        RefreshToken();
    }

    //更新token
    private void RefreshToken(){

        //测试token是否过期
        //开启子线程访问网络 测试token模块
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                try {

                    URL url = new URL(tokenValidTestUrl+token);

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
                    final StringBuilder response = new StringBuilder();
                    String line=null;
                    while ((line=reader.readLine())!=null){
                        response.append(line);
                    }

                    token = connection.getHeaderField("Authorization");
                    token = token.substring(7,token.length());
                    savaToken();
                    sendHttpURLConnectionNewPost();

                }   catch (Exception e) {
                    try {
                        int status_code = connection.getResponseCode();
                        if (status_code==400){
                            String error = connection.getResponseMessage();
                            if(error == "token_invalid"){
                                //登录时间到达两周 需要重新登录

                                Toast.makeText(NewPostActivity.this,"长时间未登录 请重新登录！",Toast.LENGTH_SHORT).show();

                                //跳转到登录界面
                                Intent intent = new Intent(NewPostActivity.this, Register_main.class);
                                startActivity(intent);


                            }else {

                                Toast.makeText(NewPostActivity.this,"未知错误！",Toast.LENGTH_SHORT).show();
                            }

                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    Log.e("errss", e.getMessage());
                }
            }
        }).start();

    }

    private void savaToken() {
        //保存token
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token",token);
        editor.commit();
    }

    private void sendHttpURLConnectionNewPost() {
        //开启子线程访问网络 回复帖子模块
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                try {
//                    String newpostcontent =token+ "&title=" + title+"&body="+content+
//                            "&user_id="+userid;
                    URL url = new URL(NewPostUrl+token);
                    Log.e("status",url.toString());

                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
//                    connection.connect();
                    //连接超时设置
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    //设置运行输入,输出:
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    //Post方式不能缓存,需手动设置为false
                    connection.setUseCaches(false);
                    //2设置http请求数据的类型为表单类型

//                    connection.setRequestProperty("Content-type","application/x-www-form-urlencoded");

                    String data = "title=" + URLEncoder.encode(title,"utf-8") + "&body=" +
                            URLEncoder.encode(content,"utf-8")+"&user_id=" +
                            URLEncoder.encode(userid,"utf-8");
                    OutputStream out = connection.getOutputStream();
                    out.write(data.getBytes());
                    out.flush();

                    if (connection.getResponseCode() == 200) {
                        // 获取响应的输入流对象
                        InputStream is = connection.getInputStream();
                        // 创建字节输出流对象
                        ByteArrayOutputStream message = new ByteArrayOutputStream();
                        // 定义读取的长度
                        int len = 0;
                        // 定义缓冲区
                        byte buffer[] = new byte[1024];
                        // 按照缓冲区的大小，循环读取
                        while ((len = is.read(buffer)) != -1) {
                            // 根据读取的长度写入到os对象中
                            message.write(buffer, 0, len);
                        }
                        // 释放资源
                        is.close();

                    }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //隐藏进度条
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(NewPostActivity.this,"发帖成功",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(NewPostActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });


                }   catch (Exception e) {

                    Log.e("error", e.getMessage());

                }
            }
        }).start();
    }


}
