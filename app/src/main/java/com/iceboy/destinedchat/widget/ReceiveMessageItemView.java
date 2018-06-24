package com.iceboy.destinedchat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.app.Constant;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hncboy on 2018/6/13.
 * 接收消息的item
 */
public class ReceiveMessageItemView extends RelativeLayout {

    @BindView(R.id.timestamp)
    TextView mTimestamp;

    @BindView(R.id.avatar)
    CircleImageView mAvatar;

    @BindView(R.id.receive_message)
    TextView mReceiveMessage;

    public ReceiveMessageItemView(Context context) {
        this(context, null);
    }

    public ReceiveMessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_receive_message_item, this);
        ButterKnife.bind(this, this);
    }

    /**
     * 绑定接收消息的view
     *
     * @param emMessage
     * @param showTimestamp
     */
    public void bindView(EMMessage emMessage, boolean showTimestamp) {
        Glide.with(this).load(Constant.sAvatarUrl + emMessage.getUserName()).into(mAvatar);
        updateTimestamp(emMessage, showTimestamp);
        updateMessageBody(emMessage);
    }

    /**
     * 更新消息的body
     *
     * @param emMessage
     */
    private void updateMessageBody(EMMessage emMessage) {
        EMMessageBody body = emMessage.getBody();
        if (body instanceof EMTextMessageBody) {
            mReceiveMessage.setText(((EMTextMessageBody) body).getMessage());
        } else {
            mReceiveMessage.setText(getContext().getString(R.string.no_text_message));
        }
    }

    /**
     * 更新消息的接收时间
     *
     * @param emMessage
     * @param showTimestamp
     */
    private void updateTimestamp(EMMessage emMessage, boolean showTimestamp) {
        if (showTimestamp) {
            mTimestamp.setVisibility(VISIBLE);
            String time = DateUtils.getTimestampString(new Date(emMessage.getMsgTime()));
            mTimestamp.setText(time);
        } else {
            mTimestamp.setVisibility(GONE);
        }
    }
}
