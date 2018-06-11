package com.iceboy.destinedchat.model;

import com.iceboy.destinedchat.app.Constant;

/**
 * Created by hncboy on 2018/6/11.
 */
public class ContactListItemModel {

    private String username;

    public String avatar = Constant.sAvatarUrl + username;

    public boolean showFirstLetter = true;

    public void setShowFirstLetter(boolean showFirstLetter) {
        this.showFirstLetter = showFirstLetter;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }

    public boolean isShowFirstLetter() {
        return showFirstLetter;
    }

    public char getFirstLetter() {
        return username.charAt(0);
    }

    /**
     * 得到联系人的首字母
     *
     * @return
     */
    public String getFirstLetterString() {
        return String.valueOf(getFirstLetter()).toUpperCase();
    }
}
