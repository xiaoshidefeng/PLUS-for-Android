package com.example.cw.slidemeuetest.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cw.slidemeuetest.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class FeedBackActivity extends AppCompatActivity {


    //返回
    private TextView tvback;

    //反馈连接地址
    private String feedbackUrl = "http://lsuplus.top/feedback";

    //联系方式输入框
    private EditText etcontact;
    private String strcontact;

    //反馈内容
    private EditText etfeedback;
    private String strfeedback;

    //发送
    private ImageView imSend;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        initview();
        //进度条隐藏
        progressBar.setVisibility(View.GONE);

        imSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSendFeedback();
            }
        });

        tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initview() {
        tvback = (TextView)findViewById(R.id.id_BackText);
        etcontact = (EditText)findViewById(R.id.id_etcontactway);
        etfeedback = (EditText)findViewById(R.id.id_etfeedbackcontent);
        progressBar = (ProgressBar)findViewById(R.id.id_Pbfeedback);
        imSend = (ImageView)findViewById(R.id.id_IMfeedback);
    }

    //输入格式提示
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptSendFeedback() {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        etfeedback.setError(null);

        boolean cancel = false;
        View focusView = null;

        strcontact = etcontact.getText().toString();
        strfeedback = etfeedback.getText().toString();

//        content.replace("\n","\n\n");
//        Log.e("status",content);

        // 标题判断
//        if(title.equals("")||title==null){
//            etTitle.setError(getString(R.string.error_null_posttitle));
//            focusView = etTitle;
//            cancel = true;
//        }
        // 内容判断
        if(strfeedback.equals("")||strfeedback==null){
            etfeedback.setError(getString(R.string.error_null_postcontent));
            focusView = etfeedback;
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

            //访问网络 反馈
            sendHttpURLConnectionFeedback();
        }
    }

    private void sendHttpURLConnectionFeedback() {
        //开启子线程访问网络 回复帖子模块
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                try {
//                    String newpostcontent =token+ "&title=" + title+"&body="+content+
//                            "&user_id="+userid;
                    URL url = new URL(feedbackUrl);
                    //Log.e("status",url.toString());

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

                    String data = "contact=" + URLEncoder.encode(strcontact,"utf-8") + "&body=" +
                            URLEncoder.encode(strfeedback,"utf-8");

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

//                    //获取输入流
//                    InputStream in = connection.getInputStream();
//
//                    //对获取的流进行读取
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
//                    StringBuilder response = new StringBuilder();
//                    String line=null;
//                    while ((line=reader.readLine())!=null){
//                        response.append(line);
//                    }
//
//
//
//
//                    //创建JSON对象
//                    JSONObject jsonObject = new JSONObject(response.toString());
//
//                    if(jsonObject.has("status")){
                    //如果登录成功
//                        String status = jsonObject.getString("status");
                    //Toast.makeText(NewPostActivity.this,"发帖成功",Toast.LENGTH_SHORT).show();
//                        Log.e("status",status);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //隐藏进度条
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(FeedBackActivity.this,"反馈成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(FeedBackActivity.this, Setting.class);
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
