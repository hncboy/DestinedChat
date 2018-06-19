package com.iceboy.destinedchat.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.iceboy.destinedchat.presenter.ConversationPresenter;
import com.iceboy.destinedchat.utils.ThreadUtils;
import com.iceboy.destinedchat.ui.view.ConversationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by hncboy on 2018/6/14.
 */
public class ConversationPresenterImpl implements ConversationPresenter {

    private ConversationView mConversationView;
    private List<EMConversation> mEMConversations;

    public ConversationPresenterImpl(ConversationView conversationView) {
        mConversationView = conversationView;
        mEMConversations = new ArrayList<>();
    }

    @Override
    public void loadAllConversations() {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                //获取所有的会话列表
                Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
                mEMConversations.clear();
                mEMConversations.addAll(conversations.values());
                //按最近一条发送的消息排序
                Collections.sort(mEMConversations, new Comparator<EMConversation>() {
                    @Override
                    public int compare(EMConversation o1, EMConversation o2) {
                        return (int) (o2.getLastMessage().getMsgTime() - o1.getLastMessage().getMsgTime());
                    }
                });
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mConversationView.onAllConversationLoaded();
                    }
                });
            }
        });
    }

    @Override
    public List<EMConversation> getConversations() {
        return mEMConversations;
    }
}
