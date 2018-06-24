package com.iceboy.destinedchat.view;

/**
 * Created by hncboy on 2018/6/15.
 */
public interface AddFriendView {

    /**
     * 开始搜索
     */
    void onStartSearch();

    /**
     * 搜索成功
     */
    void onSearchSuccess();

    /**
     * 搜索失败
     */
    void onSearchFailed();

    /**
     * 添加好友成功
     */
    void onAddFriendSuccess();

    /**
     * 添加好友失败
     */
    void onAddFriendFailed();
}
