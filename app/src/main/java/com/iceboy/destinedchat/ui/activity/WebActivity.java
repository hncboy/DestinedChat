package com.iceboy.destinedchat.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.iceboy.destinedchat.R;

/**
 * 用于加载个人博客和github
 */
public class WebActivity extends BaseActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void init() {
        showProgress(getString(R.string.loading));
        //延迟加载1s，使动画不那么难看
        postDelay(new Runnable() {
            @Override
            public void run() {
                hideProgress();
                String type = getIntent().getStringExtra("type");
                //得到WebView对象
                WebView webView = findViewById(R.id.web_view);
                //通过WebView得到WebSettings对象
                WebSettings webSettings = webView.getSettings();
                //设置支持Javascript的参数
                webSettings.setJavaScriptEnabled(true);
                //支持屏幕缩放
                webSettings.setSupportZoom(true);
                webSettings.setBuiltInZoomControls(true);
                //WebViewClient主要帮助WebView处理各种通知、请求事件
                webView.setWebViewClient(new WebViewClient());
                if (type.equals("github")) {
                    webView.loadUrl("https://github.com/hncboy");
                } else if (type.equals("blog")) {
                    webView.loadUrl("http://hncboy.top/");
                }
            }
        }, 1000);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_web;
    }
}
