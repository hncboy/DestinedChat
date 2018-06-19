package com.iceboy.destinedchat.ui.activity;

import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.presenter.impl.SplashPresenterImpl;
import com.iceboy.destinedchat.ui.view.SplashView;

public class SplashActivity extends BaseActivity implements SplashView {

    private static final int DELAY = 2000;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init() {
        new SplashPresenterImpl(this).checkLoginStatus();
    }

    /**
     * 没有登录的话延迟2s跳转到登录界面
     */
    @Override
    public void onNotLogin() {
        postDelay(new Runnable() {
            @Override
            public void run() {
                startActivity(LoginActivity.class);
            }
        }, DELAY);
    }

    /**
     * 已经登录的话直接跳转到主界面
     */
    @Override
    public void onLoggedIn() {
        startActivity(MainActivity.class);
    }
}
