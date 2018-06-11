package com.iceboy.destinedchat.app;

/**
 * Created by hncboy on 2018/6/9.
 * 存放一些常量
 */
public class Constant {

    /**
     * 获取用户头像
     */
    public static String sAvatarUrl = "http://destinedchat-1251129737.cos.ap-shanghai.myqcloud.com/avatar/";

    public static class ErrorCode {
        /**
         * 用户名重复的code
         */
        public static final int USER_ALREADY_EXIST = 202;
    }

    public static class Database {
        /**
         * 数据库命名
         */
        public static final String DATABASE_NAME = "destinedchat";
    }
}
