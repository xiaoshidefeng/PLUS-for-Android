package com.example.cw.slidemeuetest.MainActivityFragment.Fragmenttwo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cw.slidemeuetest.R;

public class NewPostActivity extends AppCompatActivity {

    //返回
    private TextView Tvback;

    //标题输入
    private EditText etTitle;

    //内容输入
    private EditText etContent;

    private ImageView imSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        initview();



        Tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initview() {
        Tvback = (TextView)findViewById(R.id.id_BackText);
        etTitle = (EditText)findViewById(R.id.id_etposttitle);
        etContent = (EditText)findViewById(R.id.id_etpostcontent);
        imSend = (ImageView)findViewById(R.id.id_IMNewpost);
    }
}
