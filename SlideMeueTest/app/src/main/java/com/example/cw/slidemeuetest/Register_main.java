package com.example.cw.slidemeuetest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.xys.libzxing.zxing.activity.CaptureActivity;

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
    private Button btnScan;

    //扫码结果显示
    private TextView tvResult;

    //登录接口
    public  String loninUrl="http://lsuplus.top/api/v1/user/";

    //获取的账号
    private String account=null;

    //获取的密码
    private  String password=null;

    //用户信息
    private String userinfo=null;

    //load字样
    private TextView loadText;

    //进度显示
    private TextView loadNumText;

    //进度条
    private ProgressBar progressBar;

    //返回
    private TextView Btnback;


   // private  SendHttpURLConnection sendHttpURLConnection;

//    private Handler handler = new Handler(){
//        public void handleMessage(Message msg){
//            if (msg.what==0){
//                String responses =(String) msg.obj;
//                //显示结果
//                tvResult.setText(responses);
//            }
//        }
//    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        initView();
        progressBar.setVisibility(View.GONE);
        Btnlonin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //progressBar.setProgress(50);
                if(view.getId()==R.id.id_Btnlogin){
                    //点击登录按钮后 强制隐藏键盘
                    InputMethodManager immPw = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    immPw.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
                    InputMethodManager immUn = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    immUn.hideSoftInputFromWindow(etAccount.getWindowToken(), 0);

                    account=etAccount.getText().toString();
                    password=etPassword.getText().toString();
                    if(account.equals("")||account==null||password.equals("")||password==null){
                        //提示输入为空
                        Toast.makeText(Register_main.this,"请输入邮箱和密码",Toast.LENGTH_SHORT).show();
                        //tvResult.setText("null");
                        return;
                    }
                    //sendHttpURLConnection.onPreExecute();
                    //sendHttpURLConnection.execute(loninUrl);
                    progressBar.setVisibility(View.VISIBLE);

                    sendHttpURLConnection();

//                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//                    String user = sharedPreferences.getString("user","");

//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    };

                    //finish();
                }

            }
        });
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register_main.this, CaptureActivity.class);
                startActivityForResult(intent,0);
            }
        });

        Btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

//    private class SendHttpURLConnection extends AsyncTask<String,Integer,String>{
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            loadText.setText("loading...");
//            progressBar.setProgress(50);
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            HttpURLConnection connection = null;
//            try {
//                String userpassword = account+":"+password;
//                URL url = new URL(loninUrl+account);
//                final String basicAuth = "Basic " + Base64.encodeToString(userpassword.getBytes(), Base64.NO_WRAP);
//                connection = (HttpURLConnection)url.openConnection();
//                connection.setRequestProperty ("Authorization", basicAuth);
//                connection.setRequestMethod("GET");
//                connection.connect();
//
//                connection.setConnectTimeout(8000);
//                connection.setReadTimeout(8000);
////                    connection.setInstanceFollowRedirects(true);
//                //获取输入流
//                InputStream in = connection.getInputStream();
//
//                //对获取的流进行读取
//                BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
//                StringBuilder response = new StringBuilder();
//                String line=null;
//                while ((line=reader.readLine())!=null){
//                    response.append(line);
//                }
//                JSONObject userJSON = new JSONObject(response.toString());
//                String status = userJSON.getString("status");
//                if(status.equals("success")){
//                    JSONObject data = userJSON.getJSONObject("data");
//                    String user = data.getString("user");
//                    String email = data.getString("email");
//
//                    userinfo = user+"\n"+email;
//                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("user",user);
//                    editor.putString("email",email);
//                    editor.commit();
//
//                }
////                Message message = new Message();
////                message.what = 0;
////                message.obj=userinfo.toString();
////                handler.sendMessage(message);
//
//
//            }   catch (Exception e) {
//                Log.e("errss", e.getMessage());
//            }
//            return null;
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//            //loadNumText.setText("load..." +"%");
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//        }
//    }

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
                    JSONObject userJSON = new JSONObject(response.toString());
                    String status = userJSON.getString("status");
                    if(status.equals("success")){
                        JSONObject data = userJSON.getJSONObject("data");
                        String user = data.getString("user");
                        String email = data.getString("email");

                        userinfo = user+"\n"+email;
                        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user",user);
                        editor.putString("email",email);
                        editor.commit();

                        Intent intent = new Intent();
                        //progressBar.setVisibility(View.GONE);
                        intent.setAction("com.example.broadcasttest.USERUI_BROADCAST");
                        sendBroadcast(intent);
                    }
//                Message message = new Message();
//                message.what = 0;
//                message.obj=userinfo.toString();
//                handler.sendMessage(message);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Register_main.this,"登录成功",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
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
        btnScan=(Button)findViewById(R.id.id_btnScan);
//        tvResult=(TextView)findViewById(R.id.id_tvResult);
        etAccount=(EditText)findViewById(R.id.id_ETaccount);
        etPassword=(EditText)findViewById(R.id.id_ETpassword);
        Btnlonin=(Button)findViewById(R.id.id_Btnlogin);
        Btnback=(TextView)findViewById(R.id.id_registerBackText);
        //=(TextView)findViewById(R.id.id_Load);
        //loadNumText=(TextView)findViewById(R.id.id_tvResult);
        progressBar=(ProgressBar)findViewById(R.id.id_LoninProgress);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            String result= data.getExtras().getString("result");
            tvResult.setText(result);
        }
    }


}
