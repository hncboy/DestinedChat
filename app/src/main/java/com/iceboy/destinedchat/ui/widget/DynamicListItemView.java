package com.iceboy.destinedchat.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.adapter.NineGridViewNotClickAdapter;
import com.iceboy.destinedchat.app.Constant;
import com.iceboy.destinedchat.model.ContactListItem;
import com.iceboy.destinedchat.model.Dynamic;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hncboy on 2018/6/19.
 * 动态的item
 */
public class DynamicListItemView extends RelativeLayout {

    @BindView(R.id.username)
    TextView mUsername;

    @BindView(R.id.avatar)
    CircleImageView mAvatar;

    @BindView(R.id.timestamp)
    TextView mTimestamp;

    @BindView(R.id.content)
    TextView mContent;

    @BindView(R.id.nineGrid_view)
    NineGridView mNineGridView;

    public DynamicListItemView(Context context) {
        this(context, null);
    }

    public DynamicListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_dynamic_item, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(Dynamic dynamic) {
        //设置用户名字
        String username = dynamic.getWriter().getUsername();
        mUsername.setText(username);
        //设置发布时间
        String timestamp = dynamic.getCreatedAt();
        mTimestamp.setText(timestamp);
        //设置头像
        String avatarUrl = Constant.sAvatarUrl + username;
        Glide.with(this).load(avatarUrl).into(mAvatar);
        //设置内容
        String content = dynamic.getText();
        if (content == null || content.length() <= 0) {
            mContent.setVisibility(View.GONE);
        } else {
            mContent.setVisibility(View.VISIBLE);
            mContent.setText(content);
        }
        //设置图片
        int size = dynamic.getPhotoList().size();
        if (size == 0) {
            mNineGridView.setVisibility(View.GONE);
        } else {
            ArrayList<ImageInfo> imageInfos = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setThumbnailUrl(dynamic.getPhotoList().get(i));
                imageInfo.setBigImageUrl(dynamic.getPhotoList().get(i));
                imageInfos.add(imageInfo);
            }
            mNineGridView.setAdapter(new NineGridViewNotClickAdapter(getContext(), imageInfos));
//            mNineGridView.setAdapter(new NineGridViewClickAdapter(getContext(), imageInfos));
        }
    }
}
