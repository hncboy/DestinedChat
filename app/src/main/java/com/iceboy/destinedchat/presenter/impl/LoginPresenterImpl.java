package com.iceboy.destinedchat.presenter.impl;

import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.iceboy.destinedchat.adapter.EMCallBackAdapter;
import com.iceboy.destinedchat.model.User;
import com.iceboy.destinedchat.presenter.LoginPresenter;
import com.iceboy.destinedchat.utils.StringUtils;
import com.iceboy.destinedchat.utils.ThreadUtils;
import com.iceboy.destinedchat.view.LoginView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by hncboy on 2018/6/9.
 */
public class LoginPresenterImpl implements LoginPresenter {

    private static final String TAG = "LoginPresenterImpl";

    private LoginView mLoginView;

    public LoginPresenterImpl(LoginView loginView) {
        mLoginView = loginView;
    }

    @Override
    public void login(String username, String password) {
        if (StringUtils.checkUserName(username)) {
            if (StringUtils.checkPassword(password)) {
                mLoginView.onStartLogin();
                startLogin(username, password);
                //loginBmob(username, password);
            } else {
                mLoginView.onPasswordError();
            }
        } else {
            mLoginView.onUserNameError();
        }
    }

    /**
     * 先登录bmob成功，获取缓存数据
     *
     * @param username
     * @param password
     */
    private void loginBmob(final String username, final String password) {
        BmobUser.loginByAccount(username, password, new LogInListener<User>() {

            @Override
            public void done(User user, BmobException e) {
                if (user != null) {
                    Log.i(TAG, "done: 登陆bmob中");
                    startLogin(username, password);
                }
            }
        });
    }

    /**
     * 登录环信
     *
     * @param username
     * @param password
     */
    private void startLogin(String username, String password) {
        EMClient.getInstance().login(username, password, mEMCallBack);
    }

    /**
     * 使用适配器覆写onSuccess和onError
     */
    private EMCallBackAdapter mEMCallBack = new EMCallBackAdapter() {

        @Override
        public void onSuccess() {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "done: 登陆环信中");
                    mLoginView.onLoginSuccess();
                }
            });
        }

        @Override
        public void onError(int i, String s) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoginView.onLoginFailed();
                }
            });
        }
    };
}
