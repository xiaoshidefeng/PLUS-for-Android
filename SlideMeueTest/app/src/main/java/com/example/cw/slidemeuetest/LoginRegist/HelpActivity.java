package com.example.cw.slidemeuetest.LoginRegist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.cw.slidemeuetest.R;

public class HelpActivity extends AppCompatActivity {

    //返回
    private TextView tvback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        tvback = (TextView)findViewById(R.id.id_registerBackText);
        tvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
