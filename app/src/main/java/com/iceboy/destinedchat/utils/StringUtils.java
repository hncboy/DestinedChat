package com.iceboy.destinedchat.utils;

/**
 * Created by hncboy on 2018/6/9.
 */
public class StringUtils {

    /**
     * 用户名的长度必须是3-20位，首字母必须为英文字符，其他字符则除了英文外还可以是数字或者下划线。
     */
    private static final String USER_NAME_REGEX = "^[a-zA-Z]\\w{2,19}$";

    /**
     * 密码是长度3-20位的数字
     */
    private static final String PASSWORD_REGEX = "^[0-9]{3,20}$";

    public static boolean checkUserName(String username) {
        return username.matches(USER_NAME_REGEX);
    }

    public static boolean checkPassword(String password) {
        return password.matches(PASSWORD_REGEX);
    }
}
