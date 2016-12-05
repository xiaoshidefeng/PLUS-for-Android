package com.example.cw.slidemeuetest.PostContent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.cw.slidemeuetest.R;

public class PostActivity extends AppCompatActivity {

    //帖子地址
    private String GetAllPostUrl = "http://lsuplus.top/api/discuss";

    //返回
    private TextView Tvback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initview();

        Tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initview() {
        Tvback = (TextView)findViewById(R.id.id_registerBackText);

    }
}
