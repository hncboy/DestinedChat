package com.iceboy.destinedchat.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.adapter.ConversationAdapter;
import com.iceboy.destinedchat.adapter.EMMessageListenerAdapter;
import com.iceboy.destinedchat.presenter.ConversationPresenter;
import com.iceboy.destinedchat.presenter.impl.ConversationPresenterImpl;
import com.iceboy.destinedchat.utils.ThreadUtils;
import com.iceboy.destinedchat.ui.view.ConversationView;

import java.util.List;

import butterknife.BindView;


/**
 * Created by hncboy on 2018/6/8.
 * 会话页面
 */
public class ConversationFragment extends BaseFragment implements ConversationView {

    private ConversationAdapter mConversationAdapter;
    private ConversationPresenter mConversationPresenter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_conversation;
    }

    @Override
    protected void init() {
        mConversationPresenter = new ConversationPresenterImpl(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mConversationAdapter = new ConversationAdapter(getContext(), mConversationPresenter.getConversations());
        mRecyclerView.setAdapter(mConversationAdapter);
        mConversationPresenter.loadAllConversations();
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListenerAdapter);
    }

    @Override
    public void onAllConversationLoaded() {
        mConversationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        mConversationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListenerAdapter);
    }

    /**
     * 收到消息的监听事件
     */
    private EMMessageListenerAdapter mEMMessageListenerAdapter = new EMMessageListenerAdapter() {

        @Override
        public void onMessageReceived(List<EMMessage> list) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast(getString(R.string.receive_new_message));
                    mConversationPresenter.loadAllConversations();
                    //收到消息时将RecyclerView滑到第一条
                    mRecyclerView.scrollToPosition(0);
                }
            });
        }
    };
}
