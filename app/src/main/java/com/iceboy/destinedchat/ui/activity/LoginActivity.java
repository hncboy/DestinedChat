package com.iceboy.destinedchat.ui.activity;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.iceboy.destinedchat.R;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 0;

    @BindView(R.id.username_et)
    EditText mUserNameEt;

    @BindView(R.id.password_et)
    EditText mPasswordEt;

    @BindView(R.id.login_btn)
    Button mLoginBtn;

    @BindView(R.id.register_fab)
    FloatingActionButton mRegisterFab;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {

    }

    @OnClick({R.id.login_btn, R.id.register_fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                startLogin();
                break;
            case R.id.register_fab:
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                        this, mRegisterFab, mRegisterFab.getTransitionName());
                startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                break;
        }
    }

    /**
     * 登录
     */
    private void startLogin() {

    }
}
