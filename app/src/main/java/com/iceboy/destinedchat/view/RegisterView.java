package com.iceboy.destinedchat.view;

/**
 * Created by hncboy on 2018/6/9.
 */
public interface RegisterView {

    /**
     * 开始注册
     */
    void onStartRegister();

    /**
     * 注册失败
     */
    void onRegisterError();

    /**
     * 用户名存在
     */
    void onResisterUserExist();

    /**
     * 注册成功
     */
    void onRegisterSuccess();

    /**
     * 用户名错误
     */
    void onUserNameError();

    /**
     * 密码错误
     */
    void onPasswordError();

    /**
     * 再次输入密码错误
     */
    void onRepeatPasswordError();
}
