package com.example.cw.slidemeuetest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGHT = 1300; // 延迟2秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = new Intent(WelcomeActivity.this,
                        MainActivity.class);
                WelcomeActivity.this.startActivity(mainIntent);
                WelcomeActivity.this.finish();
            }

        }, SPLASH_DISPLAY_LENGHT);
    }
}
