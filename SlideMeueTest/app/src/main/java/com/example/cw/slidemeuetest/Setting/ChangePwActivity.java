package com.example.cw.slidemeuetest.Setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.example.cw.slidemeuetest.MainActivity;
import com.example.cw.slidemeuetest.R;
import com.example.cw.slidemeuetest.Register_main;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChangePwActivity extends AppCompatActivity {

    //返回
    private TextView Btnback;

    //旧密码框
    private EditText etOldPw;

    //新密码框一
    private EditText etNewPw;

    //新密码框二
    private EditText etNewPwTwo;

    //修改密码 按钮
    private Button Btnchange;

    //进度条
    private ProgressBar progressBar;

    //token
    private String token;

    //更新token api
    private String tokenValidTestUrl = "http://lsuplus.top/api/refresh/?token=";

    //修改密码api
    private String tokenChangeUrl = "http://lsuplus.top/api/resetpaw/";

    //新token
    private String newtoken;

    //密码相关
    private String oldPassword;
    private String newPassword;
    private String newPasswordTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);

        //初始化
        initView();

        //隐藏进度条
        progressBar.setVisibility(View.GONE);

        Btnchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //点击确定按钮后 强制隐藏键盘
                InputHid();

                //尝试修改密码
                attemptChangePw();

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
        etOldPw = (EditText)findViewById(R.id.id_EToldpassword);
        etNewPw = (EditText)findViewById(R.id.id_ETchangePasswordOne);
        etNewPwTwo = (EditText)findViewById(R.id.id_ETchangePasswordTwo);
        Btnchange = (Button)findViewById(R.id.id_Btnchange);
        progressBar=(ProgressBar)findViewById(R.id.id_ChangeProgress);
        Btnback= (TextView)findViewById(R.id.id_changerBackText);

    }

    //输入格式提示
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptChangePw() {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        etOldPw.setError(null);
        etNewPw.setError(null);
        etNewPwTwo.setError(null);

        boolean cancel = false;
        View focusView = null;

        oldPassword = etOldPw.getText().toString();
        newPassword = etNewPw.getText().toString();
        newPasswordTwo = etNewPwTwo.getText().toString();


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(oldPassword) && !isPasswordValid(oldPassword)) {
            etOldPw.setError(getString(R.string.error_invalid_password));
            focusView = etOldPw;
            cancel = true;
        }else if(oldPassword.equals("")||oldPassword==null){
            etOldPw.setError(getString(R.string.error_null_password));
            focusView = etOldPw;
            cancel = true;
        }

        // Check for a valid new password, if the user entered one.
        if (!TextUtils.isEmpty(newPassword) && !isPasswordValid(newPassword)) {
            etNewPw.setError(getString(R.string.error_invalid_password));
            focusView = etNewPw;
            cancel = true;
        }else if(newPassword.equals("")||newPassword==null){
            etNewPw.setError(getString(R.string.error_null_password));
            focusView = etNewPw;
            cancel = true;
        }

        // Check for a valid new passwordtwo, if the user entered one.
        if (!TextUtils.isEmpty(newPasswordTwo) && !isPasswordValid(newPasswordTwo)) {
            etNewPwTwo.setError(getString(R.string.error_invalid_password));
            focusView = etNewPwTwo;
            cancel = true;
        }else if(newPasswordTwo.equals("")||newPasswordTwo==null){
            etNewPwTwo.setError(getString(R.string.error_null_password));
            focusView = etNewPwTwo;
            cancel = true;
        }

        //检测两次输入密码是否相同
        if(!newPassword.equals(newPasswordTwo)){
            etNewPwTwo.setError(getString(R.string.error_uneqully_password));
            focusView = etNewPwTwo;
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
        return password.length() > 5;
    }

    //点击确定按钮后 强制隐藏键盘
    private void InputHid(){

        InputMethodManager immEm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        immEm.hideSoftInputFromWindow(etOldPw.getWindowToken(), 0);

        InputMethodManager immNm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        immNm.hideSoftInputFromWindow(etNewPw.getWindowToken(), 0);

        InputMethodManager immPo = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        immPo.hideSoftInputFromWindow(etNewPwTwo.getWindowToken(), 0);

    }


    private void sendHttpURLConnection() {
        //取出token
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token","");

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
                    sendHttpURLConnectionChangePw();

                }   catch (Exception e) {
                    try {
                        int status_code = connection.getResponseCode();
                        if (status_code==400){
                            String error = connection.getResponseMessage();
                            if(error == "token_invalid"){
                                //登录时间到达两周 需要重新登录
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //隐藏进度条
                                        progressBar.setVisibility(View.GONE);

                                        Toast.makeText(ChangePwActivity.this,"长时间未登录 请重新登录！",Toast.LENGTH_SHORT).show();

                                        //跳转到登录界面
                                        Intent intent = new Intent(ChangePwActivity.this, Register_main.class);
                                        startActivity(intent);
                                    }
                                });
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //隐藏进度条
                                        progressBar.setVisibility(View.GONE);

                                        Toast.makeText(ChangePwActivity.this,"旧密码错误！",Toast.LENGTH_SHORT).show();

                                    }
                                });
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

    private void sendHttpURLConnectionChangePw() {
        //开启子线程访问网络 获取token模块
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                try {

                    URL url = new URL(tokenChangeUrl+"?token="+token+"&oldpassword="
                    +oldPassword+"&password="+newPassword+"&password_confirmation="+
                    newPasswordTwo);

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
                    final JSONObject userJSON = new JSONObject(response.toString());

                    if(userJSON.has("token")){
                        //如果修改成功
                        token = userJSON.getString("token");

                        //保存token
                        savaToken();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChangePwActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                            }
                        });

                        //成功后跳回主界面
                        Intent intent = new Intent(ChangePwActivity.this, MainActivity.class);
                        startActivity(intent);

                    }else if(userJSON.has("error")){

                        String errors = userJSON.getString("error");
                        if(errors.equals("old_password_error")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //隐藏进度条
                                    progressBar.setVisibility(View.GONE);

                                    Toast.makeText(ChangePwActivity.this,"旧密码错误",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                        return;
                    }


                }   catch (Exception e) {
                    Log.e("error", e.getMessage());
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


}
