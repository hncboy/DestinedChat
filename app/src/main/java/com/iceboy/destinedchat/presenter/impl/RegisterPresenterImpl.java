package com.iceboy.destinedchat.presenter.impl;

import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.iceboy.destinedchat.app.Constant;
import com.iceboy.destinedchat.model.User;
import com.iceboy.destinedchat.presenter.RegisterPresenter;
import com.iceboy.destinedchat.utils.StringUtils;
import com.iceboy.destinedchat.utils.ThreadUtils;
import com.iceboy.destinedchat.view.RegisterView;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by hncboy on 2018/6/9.
 */
public class RegisterPresenterImpl implements RegisterPresenter {
    private static final String TAG = "RegisterPresenterImpl";

    private RegisterView mRegisterView;

    public RegisterPresenterImpl(RegisterView registerView) {
        mRegisterView = registerView;
    }

    @Override
    public void register(String username, String password, String repeatPassword) {
        //检查用户名是否符合规范
        if (StringUtils.checkUserName(username)) {
            //检查密码是否符合规范
            if (StringUtils.checkPassword(password)) {
                //检查确认密码与输入密码是否一致
                if (password.equals(repeatPassword)) {
                    mRegisterView.onStartRegister();
                    registerBmob(username, password);
                } else {
                    //重复密码输入错误
                    mRegisterView.onRepeatPasswordError();
                }
            } else {
                //密码不符合规范
                mRegisterView.onPasswordError();
            }
        } else {
            //用户名不符合规范
            mRegisterView.onUserNameError();
        }
    }

    /**
     * 注册到bmob后端云
     *
     * @param username
     * @param password
     */
    private void registerBmob(final String username, final String password) {
        User user = new User(username, password);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    registerEaseMob(username, password);
                } else {
                    notifyRegisterFailed(e);
                }
            }
        });
    }

    /**
     * 注册失败
     *
     * @param e
     */
    private void notifyRegisterFailed(BmobException e) {
        if (e.getErrorCode() == Constant.ErrorCode.USER_ALREADY_EXIST) {
            Log.e(TAG, "notifyRegisterFailed: " + e.getErrorCode(), null);
            mRegisterView.onResisterUserExist();
        } else {
            mRegisterView.onRegisterError();
        }
    }

    /**
     * 注册成功
     *
     * @param username
     * @param password
     */
    private void registerEaseMob(final String username, final String password) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    //注册环信的用户
                    EMClient.getInstance().createAccount(username, password);
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRegisterView.onRegisterSuccess();
                        }
                    });
                } catch (HyphenateException e) {
                    //注册失败
                    e.printStackTrace();
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRegisterView.onRegisterError();
                        }
                    });
                }
            }
        });
    }
}
