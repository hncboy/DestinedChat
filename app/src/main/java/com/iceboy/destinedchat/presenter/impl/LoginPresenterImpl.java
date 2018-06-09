package com.iceboy.destinedchat.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.iceboy.destinedchat.adapter.EMCallBackAdapter;
import com.iceboy.destinedchat.presenter.LoginPresenter;
import com.iceboy.destinedchat.utils.StringUtils;
import com.iceboy.destinedchat.utils.ThreadUtils;
import com.iceboy.destinedchat.view.LoginView;

/**
 * Created by hncboy on 2018/6/9.
 */
public class LoginPresenterImpl implements LoginPresenter {

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
            } else {
                mLoginView.onPasswordError();
            }
        } else {
            mLoginView.onUserNameError();
        }
    }

    /**
     * 登录环信
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
