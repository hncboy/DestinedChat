package com.iceboy.destinedchat.adapter;

import com.hyphenate.EMContactListener;

/**
 * Created by hncboy on 2018/6/11.
 */
public class EMContactListenerAdapter implements EMContactListener {

    @Override
    public void onContactAdded(String s) {
        //增加了联系人时回调此方法
    }

    @Override
    public void onContactDeleted(String s) {
        //被删除时回调此方法
    }

    @Override
    public void onContactInvited(String s, String s1) {
        //收到好友邀请
    }

    @Override
    public void onFriendRequestAccepted(String s) {
        //好友请求被同意
    }

    @Override
    public void onFriendRequestDeclined(String s) {
        //好友请求被拒绝
    }
}
