package com.example.cw.slidemeuetest.MainActivityFragment.Fragmenttwo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cw.slidemeuetest.MainActivity;
import com.example.cw.slidemeuetest.R;
import com.example.cw.slidemeuetest.util.DisscussConst;
import com.example.cw.slidemeuetest.util.IsNull;
import com.example.cw.slidemeuetest.util.TokenUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    //标题
    private String title;
    //内容
    private String content;

    private Handler myHandler;

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        myHandler = new MyHandler(this);
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

        try {
            TokenUtil.reFreshToken(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //输入格式提示
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptNewPost() {
        // Reset errors.
        etTitle.setError(null);
        etContent.setError(null);

        boolean cancel = false;
        View focusView = null;

        title = etTitle.getText().toString();
        content = etContent.getText().toString();

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
            if(IsNull.isNullField(TokenUtil.getToken(this))){
                Toast.makeText(NewPostActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                return;
            }
            //进度条开始转动
            progressBar.setVisibility(View.VISIBLE);

            //发帖
            creatPost();
        }
    }


    static class MyHandler extends Handler {
        WeakReference<NewPostActivity> mActivityReference;

        MyHandler(NewPostActivity activity) {
            mActivityReference = new WeakReference<NewPostActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            NewPostActivity activity = mActivityReference.get();
            if (msg.what == 1) {
                try {
                    formatPost(activity, new JSONObject(msg.obj.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    //隐藏进度条
                    activity.progressBar.setVisibility(View.GONE);
                }
            }
        }

        private void formatPost(NewPostActivity activity, JSONObject jsonObject) throws JSONException {
            if (jsonObject.has("data")) {
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
            } else if (jsonObject.has("error")) {
                Toast.makeText(activity, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 发新帖子
     * 目前全部都是默认日常帖子 （暂时没空改UI）
     * @param
     */
    private void creatPost() {
        RequestBody body = new FormBody.Builder()
                .add("body", content)//添加键值对
                .add("title", title)
                .add("categories", "daily")
                .build();
        Request request = new Request.Builder()
                .url(DisscussConst.NEW_POST)
                .addHeader("Authorization", "Bearer " + TokenUtil.getToken(getApplicationContext()))
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = response.body().string();
                myHandler.sendMessage(msg);
            }
        });
    }

}
