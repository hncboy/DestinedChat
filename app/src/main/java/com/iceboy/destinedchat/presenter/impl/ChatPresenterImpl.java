package com.iceboy.destinedchat.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.iceboy.destinedchat.adapter.EMCallBackAdapter;
import com.iceboy.destinedchat.presenter.ChatPresenter;
import com.iceboy.destinedchat.utils.ThreadUtils;
import com.iceboy.destinedchat.ui.view.ChatView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hncboy on 2018/6/12.
 */
public class ChatPresenterImpl implements ChatPresenter {

    public static final int DEFAULT_PAGE_SIZE = 20;
    private ChatView mChatView;
    private List<EMMessage> mEMMessageList;
    private boolean hasMoreData = true;

    public ChatPresenterImpl(ChatView chatView) {
        mChatView = chatView;
        mEMMessageList = new ArrayList<>();
    }

    @Override
    public void sendMessage(final String username, final String message) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                //创建一条文本消息
                EMMessage emMessage = EMMessage.createTxtSendMessage(message, username);
                //状态为正在发送
                emMessage.setStatus(EMMessage.Status.INPROGRESS);
                //监听消息状态
                emMessage.setMessageStatusCallback(mEMCallBackAdapter);
                mEMMessageList.add(emMessage);
                //发送消息
                EMClient.getInstance().chatManager().sendMessage(emMessage);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.onStartSendMessage();
                    }
                });
            }
        });
    }

    @Override
    public List<EMMessage> getMessages() {
        return mEMMessageList;
    }

    @Override
    public void loadMessages(final String username) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                //获取制定用户的会话
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
                if (conversation != null) {
                    //获取此会话的所有消息
                    List<EMMessage> messages = conversation.getAllMessages();
                    mEMMessageList.addAll(messages);
                    //指定会话消息未读数清零
                    conversation.markAllMessagesAsRead();
                }
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.onMessagesLoaded();
                    }
                });
            }
        });
    }

    @Override
    public void loadMoreMessages(final String username) {
        if (hasMoreData) {
            ThreadUtils.runOnBackgroundThread(new Runnable() {
                @Override
                public void run() {
                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
                    EMMessage firstMessage = mEMMessageList.get(0);
                    //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
                    //获取firstMessage之前的size条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
                    final List<EMMessage> messages = conversation.loadMoreMsgFromDB(firstMessage.getMsgId(), DEFAULT_PAGE_SIZE);
                    //当获取到的message条数跟默认的一样，表示还有更多的消息
                    hasMoreData = (messages.size() == DEFAULT_PAGE_SIZE);
                    //从头开始插入所有messages
                    mEMMessageList.addAll(0, messages);
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mChatView.onMoreMessagesLoaded(messages.size());
                        }
                    });
                }
            });
        }
    }

    @Override
    public void makeMessageRead(final String username) {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
                conversation.markAllMessagesAsRead();
            }
        });
    }

    private EMCallBackAdapter mEMCallBackAdapter = new EMCallBackAdapter() {

        @Override
        public void onSuccess() {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mChatView.onSendMessageSuccess();
                }
            });
        }

        @Override
        public void onError(int i, String s) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mChatView.onSendMessageFailed();
                }
            });
        }
    };
}
