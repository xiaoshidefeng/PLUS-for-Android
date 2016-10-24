package com.example.cw.slidemeuetest.Register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cw.slidemeuetest.R;
import com.xys.libzxing.zxing.activity.CaptureActivity;

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

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            if (msg.what==0){
                String responses =(String) msg.obj;
                //显示结果
                tvResult.setText(responses);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        initView();
        Btnlonin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.id_Btnlogin){
                    account=etAccount.getText().toString();
                    password=etPassword.getText().toString();
                    if(account.equals("")||account==null||password.equals("")||password==null){
                        //提示输入为空
                        Toast.makeText(Register_main.this,"请输入密码和账号",Toast.LENGTH_SHORT).show();
                        tvResult.setText("null");
                        return;
                    }
                    sendHttpURLConnection();
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

                    Message message = new Message();
                    message.what = 0;
                    message.obj=response.toString();
                    handler.sendMessage(message);


                }   catch (Exception e) {
                    Log.e("errss", e.getMessage());

                }
            }
        }).start();
    }

    private void initView() {
        //初始化控件
        btnScan=(Button)findViewById(R.id.id_btnScan);
        tvResult=(TextView)findViewById(R.id.id_tvResult);
        etAccount=(EditText)findViewById(R.id.id_ETaccount);
        etPassword=(EditText)findViewById(R.id.id_ETpassword);
        Btnlonin=(Button)findViewById(R.id.id_Btnlogin);
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
