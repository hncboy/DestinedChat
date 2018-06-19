package com.iceboy.destinedchat.ui.view;

/**
 * Created by hncboy on 2018/6/11.
 */
public interface ContactView {

    /**
     * 得到联系人列表成功
     */
    void onGetContactListSuccess();

    /**
     * 得到联系人列表失败
     */
    void onGetContactListFailed();

    /**
     * 删除好友失败
     */
    void onDeleteFriendFailed();

    /**
     * 删除好友成功
     */
    void onDeleteFriendSuccess();
}
