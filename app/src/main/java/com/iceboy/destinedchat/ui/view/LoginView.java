package com.iceboy.destinedchat.ui.view;

/**
 * Created by hncboy on 2018/6/9.
 */
public interface LoginView {

    /**
     * 用户名错误
     */
    void onUserNameError();

    /**
     * 密码错误
     */
    void onPasswordError();

    /**
     * 开始登录
     */
    void onStartLogin();

    /**
     * 登录成功
     */
    void onLoginSuccess();

    /**
     * 登录失败
     */
    void onLoginFailed();
}
