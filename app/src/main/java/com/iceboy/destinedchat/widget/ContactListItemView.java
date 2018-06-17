package com.iceboy.destinedchat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.model.ContactListItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hncboy on 2018/6/11.
 * 联系人的item
 */
public class ContactListItemView extends RelativeLayout {

    private static final String TAG = "ContactListItemView";

    @BindView(R.id.section)
    TextView mSection;

    @BindView(R.id.divider_contact)
    View mDividerContact;

    @BindView(R.id.username)
    TextView mUsername;

    @BindView(R.id.avatar)
    CircleImageView mAvatar;

    public ContactListItemView(Context context) {
        this(context, null);
    }

    public ContactListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_contact_item, this);
        ButterKnife.bind(this, this);
    }

    /**
     * 绑定view，判断是否显示letter
     *
     * @param contactListItemModel
     */
    public void bindView(ContactListItem contactListItemModel) {
        mUsername.setText(contactListItemModel.getUsername());
        //Log.i(TAG, "bindView: username = " + contactListItemModel.getUsername());
        Glide.with(this)
                .load(contactListItemModel.getAvatarUrl())
                .into(mAvatar);
        //ImageLoader.getInstance().displayImage(contactListItemModel.getAvatarUrl(), mAvatar);
        //Log.i(TAG, "bindView: avatarUrl = " + contactListItemModel.getAvatarUrl());
        if (contactListItemModel.isShowFirstLetter()) {
            mSection.setVisibility(VISIBLE);
            mSection.setText(contactListItemModel.getFirstLetterString());
        } else {
            mSection.setVisibility(GONE);
            mDividerContact.setVisibility(VISIBLE);
        }
    }
}
