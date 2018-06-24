package com.iceboy.destinedchat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DateUtils;
import com.iceboy.destinedchat.widget.ReceiveMessageItemView;
import com.iceboy.destinedchat.widget.SendMessageItemView;

import java.util.List;

/**
 * Created by hncboy on 2018/6/13.
 * 聊天界面的消息适配器
 */
public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<EMMessage> mMessages;

    //发送的消息
    private static final int ITEM_TYPE_SEND_MESSAGE = 0;
    //接收的消息
    private static final int ITEM_TYPE_RECEIVE_MESSAGE = 1;

    public MessageListAdapter(Context context, List<EMMessage> messages) {
        mContext = context;
        mMessages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_SEND_MESSAGE) {
            return new SendItemViewHolder(new SendMessageItemView(mContext));
        } else {
            return new ReceiveItemViewHolder(new ReceiveMessageItemView(mContext));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        boolean showTimestamp = false;
        if (position == 0 || shouldShowTimestamp(position)) {
            showTimestamp = true;
        }
        if (holder instanceof SendItemViewHolder) {
            ((SendItemViewHolder) holder).mSendMessageItemView.bindView(mMessages.get(position), showTimestamp);
        } else {
            ((ReceiveItemViewHolder) holder).mReceiveMessageItemView.bindView(mMessages.get(position), showTimestamp);
        }
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage message = mMessages.get(position);
        //返回消息的方向类型，区分是发送的消息还是接收到的消息
        return message.direct() == EMMessage.Direct.SEND ? ITEM_TYPE_SEND_MESSAGE : ITEM_TYPE_RECEIVE_MESSAGE;
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    /**
     * 判断是否显示时间戳，如果两个消息时间太近，就不显示时间戳
     *
     * @param position
     * @return
     */
    private boolean shouldShowTimestamp(int position) {
        long currentItemTimestamp = mMessages.get(position).getMsgTime();
        long preItemTimestamp = mMessages.get(position - 1).getMsgTime();
        boolean closeEnough = DateUtils.isCloseEnough(currentItemTimestamp, preItemTimestamp);
        return !closeEnough;
    }

    /**
     * 增加一条新消息并更更新
     * @param message
     */
    public void addNewMessage(EMMessage message) {
        mMessages.add(message);
        notifyDataSetChanged();
    }

    private static class SendItemViewHolder extends RecyclerView.ViewHolder {

        private SendMessageItemView mSendMessageItemView;

        private SendItemViewHolder(SendMessageItemView itemView) {
            super(itemView);
            mSendMessageItemView = itemView;
        }
    }

    private static class ReceiveItemViewHolder extends RecyclerView.ViewHolder {

        private ReceiveMessageItemView mReceiveMessageItemView;

        private ReceiveItemViewHolder(ReceiveMessageItemView itemView) {
            super(itemView);
            mReceiveMessageItemView = itemView;
        }
    }
}
