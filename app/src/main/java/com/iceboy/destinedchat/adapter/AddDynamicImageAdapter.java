package com.iceboy.destinedchat.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.iceboy.destinedchat.R;

import java.io.File;
import java.util.List;

/**
 * Created by hncboy on 2018/6/19.
 * 添加上传图片的适配器
 */
public class AddDynamicImageAdapter extends BaseAdapter {

    /**
     * 最多可以显示9张照片
     */
    private static final int MAXIMAGES = 9;
    private List<String> mPhotoList;
    private Context mContext;

    public AddDynamicImageAdapter(List<String> photoList, Context context) {
        mPhotoList = photoList;
        mContext = context;
    }

    /**
     * 让GridView中的数据数目加1最后一个显示+号
     *
     * @return 返回GridView中的数量
     */
    @Override
    public int getCount() {
        int count = mPhotoList == null ? 1 : mPhotoList.size() + 1;
        if (count > MAXIMAGES) {
            return mPhotoList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AddImageViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_add_dynamic_image_item, parent, false);
            holder = new AddImageViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (AddImageViewHolder) convertView.getTag();
        }

        if (mPhotoList != null && position < mPhotoList.size()) {
            final File file = new File(mPhotoList.get(position));
            Glide.with(mContext).load(file).into(holder.photo);
            holder.deletePhotoBtn.setVisibility(View.VISIBLE);
            holder.deletePhotoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (file.exists()) {
                        file.delete();
                    }
                    mPhotoList.remove(position);
                    notifyDataSetChanged();
                }
            });
        } else {
            Glide.with(mContext).load(R.drawable.add_dynamic_photo).into(holder.photo);
            holder.photo.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.deletePhotoBtn.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class AddImageViewHolder {

        private ImageView photo;
        private Button deletePhotoBtn;

        AddImageViewHolder(View view) {
            photo = view.findViewById(R.id.photo);
            deletePhotoBtn = view.findViewById(R.id.deletePhotoBtn);
        }
    }
}
