package com.iceboy.destinedchat.app;

import com.hyphenate.chat.EMClient;

/**
 * Created by hncboy on 2018/6/9.
 * 存放一些常量
 */
public class Constant {

    public static String sAvatarUrl = "http://destinedchat-1251129737.cos.ap-shanghai.myqcloud.com/avatar/";
    public static String sMineUsername = EMClient.getInstance().getCurrentUser();
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
        public static final int MAX_UNREAD_COUNT = 99;
    }

    public static class Database {
        /**
         * 数据库命名
         */
        public static final String DATABASE_NAME = "destinedchat";
    }
}
