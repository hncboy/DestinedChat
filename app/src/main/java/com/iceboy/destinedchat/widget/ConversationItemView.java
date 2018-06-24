package com.iceboy.destinedchat.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DateUtils;
import com.hyphenate.chat.EMTextMessageBody;
import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.app.Constant;
import com.iceboy.destinedchat.ui.activity.ChatActivity;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hncboy on 2018/6/14.
 * 会话的item
 */
public class ConversationItemView extends RelativeLayout {

    private static final String TAG = "ConversationItemView";

    @BindView(R.id.avatar)
    CircleImageView mAvatar;

    @BindView(R.id.username)
    TextView mUsername;

    @BindView(R.id.last_message)
    TextView mLastMessage;

    @BindView(R.id.timestamp)
    TextView mTimestamp;

    @BindView(R.id.unread_count)
    TextView mUnreadCount;

    @BindView(R.id.conversation_item_container)
    RelativeLayout mConversationItemContainer;

    public ConversationItemView(Context context) {
        this(context, null);
    }

    public ConversationItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_conversation_item, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(final EMConversation emConversation) {
        mUsername.setText(emConversation.conversationId());
        Glide.with(this).load(Constant.sAvatarUrl + emConversation.conversationId()).into(mAvatar);
//        ImageLoader.getInstance().displayImage(Constant.sAvatarUrl + emConversation.conversationId(), mAvatar);
        updateLastMessage(emConversation);
        updateUnreadCount(emConversation);
        mConversationItemContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra(Constant.Extra.USERNAME, emConversation.conversationId());
                getContext().startActivity(intent);
            }
        });
    }

    /**
     * 更新未读消息的数量
     *
     * @param emConversation
     */
    private void updateUnreadCount(final EMConversation emConversation) {
        int unreadMsgCount = emConversation.getUnreadMsgCount();
        if (unreadMsgCount > 0) {
            mUnreadCount.setVisibility(VISIBLE);
            if (unreadMsgCount >= Constant.Extra.MAX_UNREAD_COUNT) {
                mUnreadCount.setText(R.string.max_unread_count);
            } else {
                mUnreadCount.setText(String.valueOf(unreadMsgCount));
            }
        } else {
            mUnreadCount.setVisibility(GONE);
        }
    }

    /**
     * 更新最后一条消息
     *
     * @param emConversation
     */
    private void updateLastMessage(EMConversation emConversation) {
        EMMessage emMessage = emConversation.getLastMessage();
        if (emMessage != null) {
            if (emMessage.getBody() instanceof EMTextMessageBody) {
                mLastMessage.setText(((EMTextMessageBody) emMessage.getBody()).getMessage());
            } else {
                mLastMessage.setText(R.string.no_text_message);
            }
            mTimestamp.setText(DateUtils.getTimestampString(new Date(emMessage.getMsgTime())));
        }
    }
}
