package com.example.cw.slidemeuetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Regist extends AppCompatActivity {

    //返回
    private TextView Btnback;

    //进度条
    private ProgressBar progressBar;

    //注册按钮
    private Button BtnRegist;

    //用户邮箱输入框
    private EditText EtEmail;

    //用户密码输入框1
    private EditText EtPasswordOne;

    //用户密码输入框2
    private EditText EtPasswordTwo;

    //关于LSUPLUS
    private Button BtnAboutLsuPlus;

    //用户邮箱
    private String userEmail;

    //用户密码1
    private String userPassWordOne;

    //用户密码2
    private String userPassWordTwo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        //初始化控件
        initView();

        //隐藏进度条
        progressBar.setVisibility(View.GONE);


        //点击注册按钮
        BtnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        //返回
        Btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initView() {
        //初始化控件
        Btnback= (TextView)findViewById(R.id.id_registerBackText);
        progressBar=(ProgressBar)findViewById(R.id.id_LoninProgress);
        EtEmail = (EditText)findViewById(R.id.id_ETaccountregist);
        EtPasswordOne = (EditText)findViewById(R.id.id_ETpasswordOne);
        EtPasswordTwo = (EditText)findViewById(R.id.id_ETpasswordTwo);
        BtnRegist = (Button)findViewById(R.id.id_Btnregist);
        BtnAboutLsuPlus = (Button)findViewById(R.id.id_btnAbout);


    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        EtEmail.setError(null);
        EtPasswordOne.setError(null);
        EtPasswordTwo.setError(null);

        // Store values at the time of the login attempt.
        String email = EtEmail.getText().toString();
        String passwordOne = EtPasswordOne.getText().toString();
        String passwordTwo = EtPasswordTwo.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordOne) && !isPasswordValid(passwordOne)) {
            EtPasswordOne.setError(getString(R.string.error_invalid_password));
            focusView = EtPasswordOne;
            cancel = true;
        }else if (passwordOne==null||passwordOne.equals("")){
            EtPasswordOne.setError(getString(R.string.error_null_password));
            focusView = EtPasswordOne;
            cancel = true;
        }else if (passwordTwo==null||passwordOne.equals("")){
            EtPasswordTwo.setError(getString(R.string.error_null_password));
            focusView = EtPasswordTwo;
            cancel = true;
        }else if(!passwordOne.equals(passwordTwo)){
            EtPasswordTwo.setError(getString(R.string.error_uneqully_password));
            focusView = EtPasswordTwo;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            EtEmail.setError(getString(R.string.error_field_required));
            focusView = EtEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            EtEmail.setError(getString(R.string.error_invalid_email));
            focusView = EtEmail;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            showProgress(true);
//            mAuthTask = new UserLoginTask(email, password);
//            mAuthTask.execute((Void) null);
            return;
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
