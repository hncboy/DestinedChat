package com.iceboy.destinedchat.view;

/**
 * Created by hncboy on 2018/6/12.
 */
public interface ChatView {

    /**
     * 开始发送消息
     */
    void onStartSendMessage();

    /**
     * 发送消息成功
     */
    void onSendMessageSuccess();

    /**
     * 发送消息失败
     */
    void onSendMessageFailed();

    /**
     * 加载消息
     */
    void onMessagesLoaded();

    /**
     * 加载更多消息
     * @param size
     */
    void onMoreMessagesLoaded(int size);

    /**
     * 没有更多数据
     */
    void onNoMoreData();
}
