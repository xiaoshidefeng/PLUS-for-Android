package com.example.cw.slidemeuetest.Setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.cw.slidemeuetest.R;

public class LincenseActivity extends AppCompatActivity {


    //返回
    private TextView Btnback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lincense);
        Btnback= (TextView)findViewById(R.id.id_registerBackText);

        //返回按钮
        Btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
