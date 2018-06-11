package com.iceboy.destinedchat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.iceboy.destinedchat.model.ContactListItemModel;
import com.iceboy.destinedchat.widget.ContactListItemView;

import java.util.List;

/**
 * Created by hncboy on 2018/6/11.
 * 联系人列表的适配器
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactItemViewHolder> {

    private Context mContext;
    private List<ContactListItemModel> mContactListItemModels;
    private OnItemClickListener mOnItemClickListener;

    public ContactListAdapter(Context context, List<ContactListItemModel> items) {
        mContext = context;
        mContactListItemModels = items;
    }

    @NonNull
    @Override
    public ContactItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContactListItemView itemView = new ContactListItemView(mContext);
        return new ContactItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactItemViewHolder holder, int position) {
        final ContactListItemModel item = mContactListItemModels.get(position);
        holder.mItemView.bindView(item);
        holder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(item.getUsername());
                }
            }
        });
        holder.mItemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemLongClick(item.getUsername());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContactListItemModels == null ? 0 : mContactListItemModels.size();
    }

    static class ContactItemViewHolder extends RecyclerView.ViewHolder {

        private ContactListItemView mItemView;

        public ContactItemViewHolder(ContactListItemView itemView) {
            super(itemView);
            mItemView = itemView;
        }
    }

    public interface OnItemClickListener {

        void onItemClick(String name);

        void onItemLongClick(String name);
    }

    public List<ContactListItemModel> getmContactListItemModels() {
        return mContactListItemModels;
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
