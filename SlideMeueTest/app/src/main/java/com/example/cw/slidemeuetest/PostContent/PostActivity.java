package com.example.cw.slidemeuetest.PostContent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cw.slidemeuetest.R;
import com.example.cw.slidemeuetest.Register_main;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    private List<ItemBeanPost> itembeanpost = new ArrayList<>();
    private ListView listviewpostone;

    //帖子地址
    private String GetOnePostUrl = "http://lsuplus.top/api/discuss/";

    //plus网址
    private String plus = "http://lsuplus.top";

    //回复帖子地址
    private String replyUrl = "http://lsuplus.top/api/comment/store?token=";

    //更新token api
    private static String tokenValidTestUrl = "http://lsuplus.top/api/refresh/?token=";

    //返回
    private TextView Tvback;
    //标题
    private TextView maintitle;

    //FAB 按钮
//    private FloatingActionButton floatingActionButton;

    //显示状态
    private Boolean showedit = false;

    //编辑框与发送按钮
    private LinearLayout llEdit;

    //编辑框
    private EditText etreply;

    private ImageView imSend;

    //回复内容
    private String replystr;
    private String postid;
    private String token;
    private String userid;

    private int Id;
    private String PostOne;
    private String PostMainTitle;
    private String content;

    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initview();
        getPostinfo();

//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(showedit){
//                    llEdit.setVisibility(View.GONE);
//                    //点击fab按钮后 强制隐藏键盘
//                    InputMethodManager immPw = (InputMethodManager)
//                            getSystemService(Context.INPUT_METHOD_SERVICE);
//                    immPw.hideSoftInputFromWindow(etreply.getWindowToken(), 0);
//                    showedit = false;
//                }else {
//                    llEdit.setVisibility(View.VISIBLE);
//                    showedit = true;
//                }
//            }
//        });

        Tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        etreply.clearFocus();


        etreply.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(i==0&&i1==0&&i2!=0){
                    //imSend.setColorFilter(R.color.colorAccent);
                    imSend.setImageResource(R.drawable.ic_send_button_changed);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length = etreply.getText().length();
                if(length==0){
                    imSend.setImageResource(R.drawable.ic_send_button);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        imSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int length = etreply.getText().length();
                if(length==0){
                    return;
                }else {
                    replystr = etreply.getText().toString();
                    SharedPreferences sharedPreferences = getSharedPreferences("postInfo", Context.MODE_PRIVATE);
                    postid = String.valueOf(sharedPreferences.getInt("postid",0));

                    SharedPreferences sharedPreferences2 = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    token = sharedPreferences2.getString("token","");
                    userid = String.valueOf(sharedPreferences2.getInt("id",0));


                    RefreshToken();

                    etreply.setText("");
                    //点击send按钮后 强制隐藏键盘
                    InputMethodManager immPw = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    immPw.hideSoftInputFromWindow(etreply.getWindowToken(), 0);
                    etreply.clearFocus();
                }
            }
        });

    }

    private void getPostinfo() {
        itembeanpost.clear();
        SharedPreferences sharedPreferences = getSharedPreferences("postInfo", Context.MODE_PRIVATE);
        PostOne = sharedPreferences.getString("postone","");
        PostMainTitle = sharedPreferences.getString("maintitle","");
        Id = sharedPreferences.getInt("postid",0);
        String firstpost = sharedPreferences.getString("postone","");
        String userimg  = sharedPreferences.getString("userheadimg","");
        String usern = sharedPreferences.getString("username","");
        String firsttime = sharedPreferences.getString("creattime","");
        itembeanpost.add(new ItemBeanPost(
                usern,
                firstpost,
                firsttime,
                userimg
        ));

        maintitle.setText(PostMainTitle);

        sendHttpURLConnectionGETuserInfo();
    }

    private void initview() {
        Tvback = (TextView)findViewById(R.id.id_registerBackText);
        maintitle = (TextView)findViewById(R.id.id_TvpostMaintitle);
        llEdit = (LinearLayout)findViewById(R.id.id_llEdit);
        imSend = (ImageView)findViewById(R.id.id_IMSendpost);
//        floatingActionButton = (FloatingActionButton)findViewById(R.id.id_FABonepost);
        etreply = (EditText)findViewById(R.id.id_etReply);
        itembeanpost = new ArrayList<>();
        listviewpostone = (ListView)findViewById(R.id.id_lvPostContent);
        listviewpostone.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.id_FABonepost);
//        fab.attachToListView(listviewpostone); // or attachToRecyclerView

//        llEdit.setVisibility(View.GONE);

        postAdapter = new PostAdapter(getApplicationContext(),
                itembeanpost);
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
                    sendHttpURLConnectionReplyPost();

                }   catch (Exception e) {
                    try {
                        int status_code = connection.getResponseCode();
                        if (status_code==400){
                            String error = connection.getResponseMessage();
                            if(error == "token_invalid"){
                                //登录时间到达两周 需要重新登录

                                Toast.makeText(PostActivity.this,"长时间未登录 请重新登录！",Toast.LENGTH_SHORT).show();

                                //跳转到登录界面
                                Intent intent = new Intent(PostActivity.this, Register_main.class);
                                startActivity(intent);


                            }else {

                                Toast.makeText(PostActivity.this,"未知错误！",Toast.LENGTH_SHORT).show();
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

    //通过GET方法 获取单个贴子信息
    private void sendHttpURLConnectionGETuserInfo() {
        //开启子线程访问网络 获取单个贴子信息模块
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                try {

                    URL url = new URL(GetOnePostUrl+Id);

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
                    JSONObject PostJson = new JSONObject(response.toString());
                    //Log.e("errssss", response.toString());
                    if(PostJson.has("data")){
                        //创建JSON数组
                        JSONArray dataArray = PostJson.getJSONArray("data");
                        for(int i = 0;i<dataArray.length();i++){
                            JSONObject OnePostJson = dataArray.getJSONObject(i);
                            //Log.e("errssss", OnePostJson.toString());
                            //String title = OnePostJson.getString("title");
                            String created_at = OnePostJson.getString("created_at");
                            String userimgurl = OnePostJson.getString("avatar");
                            String username = OnePostJson.getString("name");
                            //int id = OnePostJson.getInt("id");
                            content = OnePostJson.getString("body");
                            //String contentimgurl = haveImg(content);

                            Log.e("errssss", username.toString());
                            Log.e("errssss", content.toString());
                            Log.e("errssss", userimgurl);

//                            if(contentimgurl!=null&&(!contentimgurl.equals(""))){
//                                //删文字
//                                6int fir = content.indexOf("![\\");
//                                //Log.e("errssss", String.valueOf(fir));
//
//                                int last1 = content.indexOf(contentimgurl);
//                                int last2 = 0;
//                                if(contentimgurl.contains(".jpg")){
//                                    last2 = contentimgurl.indexOf(".jpg");
//                                }else if(contentimgurl.contains(".jpeg")){
//                                    last2 = contentimgurl.indexOf(".jpeg");
//                                }else if(contentimgurl.contains(".png")){
//                                    last2 = contentimgurl.indexOf(".png");
//                                }
//                                content = content.substring(0,fir)+
//                                        content.substring(last2+last1+4,content.length());
//                            }

                            if(created_at.length()>10){
                                //时间过久
                                JSONObject creatat = new JSONObject(created_at.toString());
                                created_at = creatat.getString("date");
                                created_at = created_at.substring(0,19);
                            }

                            Log.e("errssss", created_at.toString());

                            itembeanpost.add(new ItemBeanPost(
                                    username,
                                    content,
                                    "回复于"+created_at,
                                    plus+userimgurl
                            ));

                        }
                        //开启ui线程来通知用户登录成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                View footerView = getLayoutInflater().inflate(R.layout.foot_layout, null, false);
                                //LinearLayout linearLayout = new LinearLayout(R.layout.foot_layout);
                                listviewpostone.addFooterView(footerView);

                                listviewpostone.setAdapter(postAdapter);

                            }
                        });

                    }else {

                        return;
                    }

                }   catch (Exception e) {
                    Log.e("errss catch", e.getMessage());
                }
            }
        }).start();
    }


    private void sendHttpURLConnectionReplyPost() {
        //开启子线程访问网络 回复帖子模块
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                try {
                    String replycontent =token+ "&body="+replystr+
                            "&user_id="+userid+"&discussion_id="+postid;
                    URL url = new URL(replyUrl+replycontent);
                    Log.e("status",url.toString());

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
                    JSONObject jsonObject = new JSONObject(response.toString());

                    if(jsonObject.has("status")){
                        //如果登录成功
                        String status = jsonObject.getString("status");
                        Log.e("status",status);
                        getPostinfo();

                    }else{

                        return;
                    }


                }   catch (Exception e) {

                    Log.e("error", e.getMessage());
                }
            }
        }).start();
    }

}
