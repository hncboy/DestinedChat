package com.iceboy.destinedchat.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by hncboy on 2018/6/17.
 * 处理和服务器的交互
 */
public class HttpUtils {

    /**
     * 传入请求地址和回调接口
     *
     * @param address
     * @param callback
     */
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient(); //创建一个OkHttpClient实例
        Request request = new Request.Builder().url(address).build(); //创建一个Request对象用于发起http请求
        client.newCall(request).enqueue(callback); //注册一个回调处理服务器的响应
    }
}
