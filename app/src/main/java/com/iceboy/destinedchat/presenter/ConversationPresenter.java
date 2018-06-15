package com.iceboy.destinedchat.presenter;

import com.hyphenate.chat.EMConversation;

import java.util.List;

/**
 * Created by hncboy on 2018/6/14.
 */
public interface ConversationPresenter {

    /**
     * 加载所有会话
     */
    void loadAllConversations();

    /**
     * 得到所有会话列表
     * @return
     */
    List<EMConversation> getConversations();
}
