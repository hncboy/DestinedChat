package com.iceboy.destinedchat.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.iceboy.destinedchat.presenter.SplashPresenter;
import com.iceboy.destinedchat.ui.view.SplashView;

/**
 * Created by hncboy on 2018/6/9.
 */
public class SplashPresenterImpl implements SplashPresenter {

    private SplashView mSplashView;

    public SplashPresenterImpl(SplashView splashView) {
        mSplashView = splashView;
    }

    @Override
    public void checkLoginStatus() {
        if (EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected()) {
            mSplashView.onLoggedIn();
        } else {
            mSplashView.onNotLogin();
        }
    }
}
