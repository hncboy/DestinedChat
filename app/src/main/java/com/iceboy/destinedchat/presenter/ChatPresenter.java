package com.iceboy.destinedchat.presenter;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by hncboy on 2018/6/12.
 */
public interface ChatPresenter {

    /**
     * 发送消息
     * @param username
     * @param message
     */
    void sendMessage(String username, String message);

    /**
     * 获取消息
     * @return
     */
    List<EMMessage> getMessages();

    /**
     * 加载消息
     * @param username
     */
    void loadMessages(String username);

    /**
     * 加载更多消息
     * @param username
     */
    void loadMoreMessages(String username);

    /**
     * 消息已读
     * @param username
     */
    void makeMessageRead(String username);
}
