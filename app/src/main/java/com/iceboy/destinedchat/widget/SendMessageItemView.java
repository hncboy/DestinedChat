package com.iceboy.destinedchat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
 * 发送消息的item
 */
public class SendMessageItemView extends RelativeLayout {

    @BindView(R.id.avatar)
    CircleImageView mAvatar;

    @BindView(R.id.send_message)
    TextView mSendMessage;

    @BindView(R.id.send_message_progress)
    ImageView mSendMessageProgress;

    @BindView(R.id.timestamp)
    TextView mTimestamp;

    public SendMessageItemView(Context context) {
        this(context, null);
    }

    public SendMessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_send_message_item, this);
        ButterKnife.bind(this, this);
    }

    /**
     * 绑定发送消息的view
     *
     * @param emMessage
     * @param showTimestamp
     */
    public void bindView(EMMessage emMessage, boolean showTimestamp) {
        Glide.with(this).load(Constant.sMineAvatarUrl).into(mAvatar);
        updateTimestamp(emMessage, showTimestamp);
        updateMessageBody(emMessage);
        updateSendingStatus(emMessage);
    }


    /**
     * 更新消息的状态
     *
     * @param emMessage
     */
    private void updateSendingStatus(EMMessage emMessage) {
        switch (emMessage.status()) {
            case INPROGRESS: //发送过程中
                mSendMessageProgress.setVisibility(VISIBLE);
                break;
            case SUCCESS: //发送成功
                mSendMessageProgress.setVisibility(GONE);
                break;
            case FAIL: //发送失败
                mSendMessageProgress.setImageResource(R.drawable.msg_error);
                break;
        }
    }

    /**
     * 更新消息的body
     *
     * @param emMessage
     */
    private void updateMessageBody(EMMessage emMessage) {
        EMMessageBody body = emMessage.getBody();
        //如果body属于文本消息，则显示消息的内容
        if (body instanceof EMTextMessageBody) {
            mSendMessage.setText(((EMTextMessageBody) body).getMessage());
        } else {
            mSendMessage.setText(getContext().getString(R.string.no_text_message));
        }
    }

    /**
     * 更新时间
     *
     * @param emMessage
     * @param showTimestamp
     */
    private void updateTimestamp(EMMessage emMessage, boolean showTimestamp) {
        if (showTimestamp) {
            mTimestamp.setVisibility(VISIBLE);
            //获取消息的时间并转换成字符串
            String time = DateUtils.getTimestampString(new Date(emMessage.getMsgTime()));
            mTimestamp.setText(time);
        } else {
            mTimestamp.setVisibility(GONE);
        }
    }
}
