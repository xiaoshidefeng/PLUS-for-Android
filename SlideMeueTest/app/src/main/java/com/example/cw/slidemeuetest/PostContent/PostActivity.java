package com.example.cw.slidemeuetest.PostContent;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cw.slidemeuetest.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
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

    //返回
    private TextView Tvback;
    //标题
    private TextView maintitle;

    private int Id;
    private String PostOne;
    private String PostMainTitle;
    private String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initview();
        getPostinfo();

        Tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void getPostinfo() {
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
                "创建于"+firsttime,
                userimg
        ));

        maintitle.setText(PostMainTitle);

        sendHttpURLConnectionGETuserInfo();
    }

    private void initview() {
        Tvback = (TextView)findViewById(R.id.id_registerBackText);
        maintitle = (TextView)findViewById(R.id.id_TvpostMaintitle);
        itembeanpost = new ArrayList<>();

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
//                                int fir = content.indexOf("![\\");
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
                                listviewpostone = (ListView)findViewById(R.id.id_lvPostContent);

                                listviewpostone.setAdapter(new PostAdapter(getApplicationContext(),itembeanpost));

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
}
