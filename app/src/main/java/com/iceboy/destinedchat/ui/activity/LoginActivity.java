package com.iceboy.destinedchat.ui.activity;


import android.Manifest;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.transition.Explode;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.presenter.LoginPresenter;
import com.iceboy.destinedchat.presenter.impl.LoginPresenterImpl;
import com.iceboy.destinedchat.view.LoginView;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginView {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 0;

    private LoginPresenter mLoginPresenter;

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
        mLoginPresenter = new LoginPresenterImpl(this);
        mPasswordEt.setOnEditorActionListener(mOnEditorActionListener);
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
     * 开启登录
     */
    private void startLogin() {
        if (hasWriteExternalStoragePermission()) {
            login();
        } else {
            applyPermission();
        }
    }

    /**
     * 登录
     */
    private void login() {
        hideKeyBoard();
        String userName = mUserNameEt.getText().toString().trim();
        String password = mPasswordEt.getText().toString().trim();
        mLoginPresenter.login(userName, password);
    }

    /**
     * 判断是否有具有写入外部存储权限
     *
     * @return
     */
    private boolean hasWriteExternalStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PermissionChecker.PERMISSION_GRANTED;
    }

    /**
     * 申请权限
     */
    private void applyPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onUserNameError() {
        mUserNameEt.setError(getString(R.string.user_name_error));
    }

    @Override
    public void onPasswordError() {
        mPasswordEt.setError(getString(R.string.user_password_error));
    }

    @Override
    public void onStartLogin() {
        showProgress(getString(R.string.logining));
    }

    @Override
    public void onLoginSuccess() {
        hideProgress();
        toast(getString(R.string.login_success));

        Explode explode = new Explode();
        explode.setDuration(500);

        getWindow().setExitTransition(explode);
        getWindow().setEnterTransition(explode);
        ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
        Intent i2 = new Intent(this, MainActivity.class);
        startActivity(i2, oc2.toBundle());
    }

    @Override
    public void onLoginFailed() {
        hideProgress();
        toast(getString(R.string.login_failed));
    }

    /**
     * 监听软键盘的完成事件
     */
    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                startLogin();
                return true;
            } else {
                return false;
            }
        }
    };
}