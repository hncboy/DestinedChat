package com.iceboy.destinedchat.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by hncboy on 2018/6/9.
 */
public class UserModel extends BmobUser {

    public UserModel(String userName, String password) {
        setUsername(userName);
        setPassword(password);
    }


}
