package com.iceboy.destinedchat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.iceboy.destinedchat.model.AddFriendItem;
import com.iceboy.destinedchat.ui.widget.AddFriendItemView;

import java.util.List;

/**
 * Created by hncboy on 2018/6/15.
 * 添加好友的适配器
 */
public class AddFriendListAdapter extends RecyclerView.Adapter<AddFriendListAdapter.AddFriendItemViewHolder> {

    private Context mContext;
    private List<AddFriendItem> mAddFriendItemModelList;

    public AddFriendListAdapter(Context context, List<AddFriendItem> list) {
        mContext = context;
        mAddFriendItemModelList = list;
    }

    @NonNull
    @Override
    public AddFriendItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddFriendItemViewHolder(new AddFriendItemView(mContext));
    }

    @Override
    public void onBindViewHolder(@NonNull AddFriendItemViewHolder holder, int position) {
        holder.mAddFriendItemView.bindView(mAddFriendItemModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return mAddFriendItemModelList.size();
    }

    static class AddFriendItemViewHolder extends RecyclerView.ViewHolder {

        private AddFriendItemView mAddFriendItemView;

        private AddFriendItemViewHolder(AddFriendItemView itemView) {
            super(itemView);
            mAddFriendItemView = itemView;
        }
    }
}
