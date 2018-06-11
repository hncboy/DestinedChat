package com.iceboy.destinedchat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.app.Constant;
import com.iceboy.destinedchat.model.ContactListItemModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hncboy on 2018/6/11.
 * 联系人的自定义view
 */
public class ContactListItemView extends RelativeLayout {

    @BindView(R.id.section)
    TextView mSection;

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
    public void bindView(ContactListItemModel contactListItemModel) {
        ImageLoader.getInstance().displayImage(contactListItemModel.getAvatarUrl(), mAvatar);
        mUsername.setText(contactListItemModel.getUsername());
        if (contactListItemModel.isShowFirstLetter()) {
            mSection.setVisibility(VISIBLE);
            mSection.setText(contactListItemModel.getFirstLetterString());
        } else {
            mSection.setVisibility(GONE);
        }
    }
}
