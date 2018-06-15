package com.iceboy.destinedchat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hyphenate.chat.EMConversation;
import com.iceboy.destinedchat.widget.ConversationItemView;

import java.util.List;

/**
 * Created by hncboy on 2018/6/14.
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationItemViewHolder> {

    private static final String TAG = "ConversationAdapter";

    private Context mContext;
    private List<EMConversation> mEMConversations;

    public ConversationAdapter(Context context, List<EMConversation> conversations) {
        mContext = context;
        mEMConversations = conversations;
    }

    @NonNull
    @Override
    public ConversationItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversationItemViewHolder(new ConversationItemView(mContext));
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationItemViewHolder holder, int position) {
        holder.mConversationItemView.bindView(mEMConversations.get(position));
    }

    @Override
    public int getItemCount() {
        return mEMConversations.size();
    }

    static class ConversationItemViewHolder extends RecyclerView.ViewHolder {

        private ConversationItemView mConversationItemView;

        private ConversationItemViewHolder(ConversationItemView itemView) {
            super(itemView);
            mConversationItemView = itemView;
        }
    }
}
