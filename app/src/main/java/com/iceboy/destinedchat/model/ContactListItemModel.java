package com.iceboy.destinedchat.model;


import com.iceboy.destinedchat.app.Constant;

/**
 * Created by hncboy on 2018/6/11.
 */
public class ContactListItemModel {

    private String username;
    private boolean showFirstLetter = true;

    public void setShowFirstLetter(boolean showFirstLetter) {
        this.showFirstLetter = showFirstLetter;
    }

    public String getAvatarUrl() {
        return Constant.sAvatarUrl + getUsername();
    }

    public boolean isShowFirstLetter() {
        return showFirstLetter;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
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
