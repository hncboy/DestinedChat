package com.iceboy.destinedchat.app;

import com.hyphenate.chat.EMClient;

/**
 * Created by hncboy on 2018/6/9.
 * 存放一些常量
 */
public class Constant {

    public static String sAvatarUrl = "http://destinedchat-1251129737.cos.ap-shanghai.myqcloud.com/avatar/";
    public static String sAreaAddress = "http://guolin.tech/api/china";
    public static String sBingPicUrl = "http://guolin.tech/api/bing_pic";
    public static String sWeatherUrl = "http://guolin.tech/api/weather?cityid=";
    public static String sWeatherKey = "&key=225ea40bffde4e64b59678e45617bc8d";
    public static String sMineAvatarUrl = sAvatarUrl + EMClient.getInstance().getCurrentUser();


    public static class ErrorCode {
        /**
         * 用户名重复的code
         */
        public static final int USER_ALREADY_EXIST = 202;
    }

    public static class Extra {
        public static final String USERNAME = "username";
        public static final String TYPE = "type";
        public static final String BLOG = "blog";
        public static final String GITHUB = "github";
        public static final String WEATHER = "weather";
        public static final String WEATHER_ID = "weather_id";
        public static final String PROVINCE = "province";
        public static final String CITY = "city";
        public static final String COUNTRY = "country";
        public static final String BINGPIC = "bing_pic";
        public static final String OK = "ok";
        public static final int MAX_UNREAD_COUNT = 99;
    }

    public static class Database {
        /**
         * 数据库命名
         */
        public static final String DATABASE_NAME = "destinedchat";
    }
}
