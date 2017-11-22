package com.example.cw.slidemeuetest.LoginRegist;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.example.cw.slidemeuetest.R;
import com.example.cw.slidemeuetest.jbcrypt.BCrypt;
import com.example.cw.slidemeuetest.util.UserConst;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    //plus网址
    private String plus = "http://lsuplus.top";


    //登录接口 新的token验证 获取token值
    private  String loninUrl="http://lsuplus.top/api/user/login/";

    //登录接口 用token获取用户信息
    private String getUserInfoUrl = "http://lsuplus.top/api/user/me/?token=";

    //修改密码接口
    private String forgetUrl = "http://lsuplus.top/password/email/?email=";


    //头像Url
    private String ImgUrl;

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
    public ProgressBar progressBar;

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

    private Handler myHandler;

    private OkHttpClient client = new OkHttpClient();




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

        myHandler = new MyHandler(this);

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

        BtnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register_main.this,HelpActivity.class);
                startActivity(intent);
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
                        .setView(inputEmail).setPositiveButton("确定", new DialogInterface.OnClickListener() {
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

    static class MyHandler extends Handler {
        WeakReference<Register_main> mActivityReference;

        MyHandler(Register_main activity) {
            mActivityReference = new WeakReference<Register_main>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Register_main activity = mActivityReference.get();
            if (msg.what == 1) {
                try {
                    Log.e("1111", msg.obj.toString());
                    formatUser(activity, new JSONObject(msg.obj.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    //隐藏进度条
                    activity.progressBar.setVisibility(View.GONE);
                }
            } else if (msg.what == 2) {
                Toast.makeText(activity, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                activity.progressBar.setVisibility(View.GONE);
            }
        }

        public void formatUser(Register_main activity, JSONObject jsonObject) throws JSONException {
            if(jsonObject.has("result")) {
                JSONObject userJ = (JSONObject) jsonObject.getJSONObject("result");
                //token成功
                int id = userJ.getInt("id");
                String name = userJ.getString("name");
                String email = userJ.getString("email");
                String created_at = userJ.getString("created_at");
                String updated_at = userJ.getString("updated_at");

                String admin = "";
                String ImgUrl = "";
                if (userJ.has("role")) {
                    //是否为管理员
                    admin = userJ.getString("role");
                }
                if (userJ.has("avatar")) {
                    ImgUrl = userJ.getString("avatar");
                }

                //保存用户信息
                SharedPreferences sharedPreferences = activity.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token", activity.token);
                editor.putInt("id", id);
                editor.putString("name", name);
                editor.putString("email", email);
                editor.putString("created_at", created_at);
                editor.putString("updated_at", updated_at);
                editor.putString("admin", admin);
                editor.putString("imgurl", ImgUrl);
                editor.commit();

                //发送广播 通知MainActivity更新用户ui
                Intent intent = new Intent();
                intent.setAction("com.example.broadcasttest.USERUI_BROADCAST");
                activity.sendBroadcast(intent);
                Toast.makeText(activity, "登录成功", Toast.LENGTH_SHORT).show();
                //返回MainActivity
                activity.finish();

            } else {
                Toast.makeText(activity, "登录失败", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void doLogin() {
        RequestBody body = new FormBody.Builder()
                .add("email", account)//添加键值对
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(UserConst.LOGIN)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.has("result")) {
                        token = jsonObject.getString("result");
                        getUserInfo();
                    } else if (jsonObject.has("error")){
                        String error = jsonObject.getString("error");
                        Message msg = new Message();
                        msg.what = 2;
                        msg.obj = error;
                        myHandler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = 2;
                        msg.obj = "未知错误";
                        myHandler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void getUserInfo() {
        Request request = new Request.Builder()
                .url(UserConst.GET_USER_DETAILS + "?token=" + token)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = new Message();
                msg.what=1;
                msg.obj = response.body().string();
                myHandler.sendMessage(msg);
            }
        });
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
                    URL url = new URL(UserConst.GET_CONFIRM_CODE + "&email=" + forgetEmail);
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
            focusView.requestFocus();
        } else {
            //进度条开始转动
            progressBar.setVisibility(View.VISIBLE);

            //访问网络 登录
            doLogin();
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
}