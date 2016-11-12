package com.example.cw.slidemeuetest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //检测网络情况的广播
    private IntentFilter intentFilter;

    private NetworkChangeReciver networkChangeReciver;

    //网页
    private WebView webView;

    //Register情况
    private TextView userName;

    private TextView userEmail;

    public String user;

    public String email;

    //用户id
    private int id;

    //扫码登录接口
    public  String QRloninUrl="http://lsuplus.top/QRLogin/";

    //扫码结果
    private String QrScanResult;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //透明状态栏实现
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        webView=(WebView)findViewById(R.id.id_webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://lsuplus.top/");

        setSupportActionBar(toolbar);


        //检测网络状态
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReciver = new NetworkChangeReciver();
        registerReceiver(networkChangeReciver,intentFilter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        registerBroadcastReceiver();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //打开app时更新登录ui
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if(slideOffset==0.1)
                Log.d("slide", "onDrawerSlide: ");
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                String user = sharedPreferences.getString("user","");
                String email = sharedPreferences.getString("email","");
                userName =(TextView)findViewById(R.id.id_userNameText);
                userEmail =(TextView)findViewById(R.id.id_userEmailText);
                if(user!=""||!user.equals("")) {
                    userName.setText(user);
                    userEmail.setText(email);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Log.d("slide", "onDrawerOpened: ");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Log.d("slide", "onDrawerClosed: ");
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                Log.d("slide", "onDrawerStateChanged: ");
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkChangeReciver,intentFilter);
    }


    //网络检测
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReciver);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection Simplifiab0leIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            //二维码
            Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
            startActivityForResult(intent,0);

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            //设置
            Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            ExitLog();
            userName.setText("立即登录");
            userEmail.setText("");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void login(View v)
    {
        userName =(TextView)findViewById(R.id.id_userNameText);
        String loginStatus = (String) userName.getText();
        if(loginStatus.equals("立即登录")){
            //还未登录
            Intent intent = new Intent(MainActivity.this, Register_main.class);
            startActivity(intent);
        }else{

            return;
        }
//        //暂时开启 测试ui
//            Intent intent = new Intent(MainActivity.this, Register_main.class);
//            startActivity(intent);

    }

    private void registerBroadcastReceiver(){
        UserBroadcastReceiver receiver = new UserBroadcastReceiver();
        IntentFilter filter = new IntentFilter("com.example.broadcasttest.USERUI_BROADCAST");
        registerReceiver(receiver, filter);
    }

    public class UserBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String user = sharedPreferences.getString("user","");
            String email = sharedPreferences.getString("email","");
            id = sharedPreferences.getInt("id",0);
            userName =(TextView)findViewById(R.id.id_userNameText);
            userEmail =(TextView)findViewById(R.id.id_userEmailText);
            if(user!=""||!user.equals("")) {
                userName.setText(user);
                userEmail.setText(email);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            QrScanResult= data.getExtras().getString("result");
            sendQrloginHttpURLConnection();
        }
    }

    private void sendQrloginHttpURLConnection() {
        //开启子线程访问网络 扫码登录模块
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;

                //获取SharedPreferences里的用户信息
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                String user = sharedPreferences.getString("user","");
                String email = sharedPreferences.getString("email","");
                id = sharedPreferences.getInt("id",0);
                String password = sharedPreferences.getString("password","");

                try {
                    String userpassword = userName+":"+userEmail;
                    URL url = new URL(QRloninUrl+QrScanResult+"?userid="+id+"&password="+password);

                    //basic64加密
                    final String basicAuth = "Basic " + Base64.encodeToString(userpassword.getBytes(), Base64.NO_WRAP);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestProperty ("Authorization", basicAuth);
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

                }   catch (Exception e) {
                    Log.e("errss", e.getMessage());

                }
            }
        }).start();
    }



    //退出登录
    private void ExitLog(){
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user","");
        editor.putString("email","");
        editor.putString("password","");
        editor.putInt("id",0);
        editor.commit();

        //退出登录 sendLogout
        sendLogoutHttpURLConnection();
//        Intent intent = new Intent();
//        intent.setAction("com.example.broadcasttest.USERUI_BROADCAST");
//        sendBroadcast(intent);
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

}

