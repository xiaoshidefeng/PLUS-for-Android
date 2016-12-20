package com.example.cw.slidemeuetest.LoginRegist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.cw.slidemeuetest.R;

public class AboutActivity extends AppCompatActivity {

    private TextView btnback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        btnback = (TextView) findViewById(R.id.id_registerBackText);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
