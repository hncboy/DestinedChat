package com.iceboy.destinedchat.ui.view;

/**
 * Created by hncboy on 2018/6/9.
 */
public interface SplashView {

    /**
     * 没有登录，进入登录界面
     */
    void onNotLogin();

    /**
     * 已经登录过，进入主界面
     */
    void onLoggedIn();
}