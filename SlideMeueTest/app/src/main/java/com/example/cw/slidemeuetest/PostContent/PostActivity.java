package com.example.cw.slidemeuetest.PostContent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cw.slidemeuetest.LoginRegist.Register_main;
import com.example.cw.slidemeuetest.R;
import com.example.cw.slidemeuetest.util.DisscussConst;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    private ProgressDialog progressDialog;

    private TextView tvnull;

    //返回
    private TextView Tvback;
    //标题
    private TextView maintitle;

    //FAB 按钮
//    private FloatingActionButton floatingActionButton;

    //显示状态
    private Boolean showedit = false;

    private ProgressBar progressBar;

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
    private String smalltail;

    private PostAdapter postAdapter;


    private Handler myHandler;

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initview();
        //进度条开始转动
        progressBar.setVisibility(View.VISIBLE);
        getPostinfo();


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


                    SharedPreferences sharedPreferences = getSharedPreferences("postInfo", Context.MODE_PRIVATE);
                    postid = String.valueOf(sharedPreferences.getInt("postid",0));

                    SharedPreferences sharedPreferences2 = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    token = sharedPreferences2.getString("token","");
                    userid = String.valueOf(sharedPreferences2.getInt("id",0));
                    smalltail = sharedPreferences2.getString("smalltail","");

                    if(token.equals("")){
                        Toast.makeText(PostActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    replystr = etreply.getText().toString();

                    if(smalltail.equals("来自Plus客户端")){
                        //彩蛋 特殊小尾巴
//                        replystr = replystr +"\n\n"+"      *"+ smalltail+"*" +
//                                "\n        一切伟大的行动和思想\n       都有一个微不足道的开始";
                        replystr = replystr +"\n\n"+"*"+ "——————————"+smalltail+"*"+"\n"+
                                "\n\n一切伟大的行动和思想\n\n都有一个微不足道的开始";
                    }else if(!smalltail.equals("")||smalltail==null){
                        replystr = replystr +"\n\n"+"*"+ "——————————"+smalltail+"*";
                    }

                    RefreshToken();

                    etreply.setText("");
                    //点击send按钮后 强制隐藏键盘
                    InputMethodManager immPw = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    immPw.hideSoftInputFromWindow(etreply.getWindowToken(), 0);
                    etreply.clearFocus();
                    progressDialog = new ProgressDialog (PostActivity.this);
                    progressDialog.setMessage("请稍后...");
                    //进度条周围不可点击
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    //进度条开始转动
                    //progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    static class MyHandler extends Handler {
        WeakReference<PostActivity> mActivityReference;

        MyHandler(PostActivity activity) {
            mActivityReference = new WeakReference<PostActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PostActivity activity = mActivityReference.get();

        }
    }

    private void getAllPost() {
        //TODO　获取用户登录信息 并传出
        RequestBody body = new FormBody.Builder()
                .add("body", "abc")//添加键值对
                .add("discussion_id", "321")
                .build();
        Request request = new Request.Builder().url(DisscussConst.SEND_COMMENTS).build();
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
        progressBar = (ProgressBar)findViewById(R.id.id_Pbnewreply);
        llEdit = (LinearLayout)findViewById(R.id.id_llEdit);
        imSend = (ImageView)findViewById(R.id.id_IMSendpost);
//        floatingActionButton = (FloatingActionButton)findViewById(R.id.id_FABonepost);
        etreply = (EditText)findViewById(R.id.id_etReply);
        tvnull = (TextView)findViewById(R.id.id_tvpostonenull) ;
        itembeanpost = new ArrayList<>();
        listviewpostone = (ListView)findViewById(R.id.id_lvPostContent);
        listviewpostone.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.id_FABonepost);
//        fab.attachToListView(listviewpostone); // or attachToRecyclerView

//        llEdit.setVisibility(View.GONE);

        postAdapter = new PostAdapter(getApplicationContext(),
                itembeanpost);



        View footerView = getLayoutInflater().inflate(R.layout.foot_layout, null, false);

        listviewpostone.addFooterView(footerView);
        listviewpostone.setEmptyView(tvnull);
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
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //登录时间到达两周 需要重新登录
                                        //隐藏进度条
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(PostActivity.this,"长时间未登录 请重新登录！",Toast.LENGTH_SHORT).show();

                                        //跳转到登录界面
                                        Intent intent = new Intent(PostActivity.this, Register_main.class);
                                        startActivity(intent);
                                    }
                                });



                            }else {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //隐藏进度条
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(PostActivity.this,"未知错误！",Toast.LENGTH_SHORT).show();
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
                        //开启ui线程
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                listviewpostone.setAdapter(postAdapter);

                                //隐藏进度条
                                progressBar.setVisibility(View.GONE);
                            }
                        });

                    }else {

                        return;
                    }

                }   catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //隐藏进度条
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(PostActivity.this,"获取贴子信息失败",Toast.LENGTH_SHORT).show();
                        }
                    });
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

                    URL url = new URL(replyUrl+token);
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

                    String data = "body=" + URLEncoder.encode(replystr,"utf-8") + "&user_id=" +
                            URLEncoder.encode(userid,"utf-8")+"&discussion_id=" +
                            URLEncoder.encode(postid,"utf-8");
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

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getPostinfo();
                                progressDialog.dismiss();

                            }
                        });

                    }
//                    connection.connect();
//
//                    //连接超时设置
//                    connection.setConnectTimeout(8000);
//                    connection.setReadTimeout(8000);
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
//                    //创建JSON对象
//                    JSONObject jsonObject = new JSONObject(response.toString());

//                    if(jsonObject.has("status")){
//                        //如果回复成功
//                        String status = jsonObject.getString("status");
//                        Log.e("status",status);


                    else{

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();

                                Toast.makeText(PostActivity.this,"回帖失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }


                }   catch (Exception e) {

                    Log.e("error", e.getMessage());
                }
            }
        }).start();
    }

}
