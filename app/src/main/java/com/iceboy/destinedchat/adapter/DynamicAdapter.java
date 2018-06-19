package com.iceboy.destinedchat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.iceboy.destinedchat.model.Dynamic;
import com.iceboy.destinedchat.ui.widget.DynamicListItemView;

import java.util.List;


/**
 * Created by hncboy on 2018/6/19.
 * 动态的内容的适配器
 */
public class DynamicAdapter extends RecyclerView.Adapter<DynamicAdapter.DynamicItemViewHolder> {

    private List<Dynamic> mDynamicList;
    private Context mContext;

    public DynamicAdapter(Context context, List<Dynamic> dynamicList) {
        mDynamicList = dynamicList;
        mContext = context;
    }

    @NonNull
    @Override
    public DynamicItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DynamicListItemView dynamicListItemView = new DynamicListItemView(mContext);
        return new DynamicItemViewHolder(dynamicListItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DynamicItemViewHolder holder, int position) {
        Dynamic dynamic = mDynamicList.get(position);
        holder.mItemView.bindView(dynamic);
    }

    @Override
    public int getItemCount() {
        return mDynamicList == null ? 0 : mDynamicList.size();
    }

    public void addDynamicList(List<Dynamic> dynamicList) {
        mDynamicList = dynamicList;
    }

    static class DynamicItemViewHolder extends RecyclerView.ViewHolder {

        private DynamicListItemView mItemView;

        private DynamicItemViewHolder(DynamicListItemView itemView) {
            super(itemView);
            mItemView = itemView;
        }
    }
}
