package com.iceboy.destinedchat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.app.Constant;
import com.iceboy.destinedchat.event.AddFriendEvent;
import com.iceboy.destinedchat.model.AddFriendItem;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hncboy on 2018/6/15.
 * 添加好友的item
 */
public class AddFriendItemView extends RelativeLayout {

    @BindView(R.id.avatar)
    CircleImageView mAvatar;

    @BindView(R.id.username)
    TextView mUsername;

    @BindView(R.id.timestamp)
    TextView mTimestamp;

    @BindView(R.id.add)
    Button mAdd;

    public AddFriendItemView(Context context) {
        this(context, null);
    }

    public AddFriendItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_add_friend_item, this);
        ButterKnife.bind(this, this);
    }

    public void bindView(AddFriendItem addFriendItemModel) {
        mUsername.setText(addFriendItemModel.username);
        mTimestamp.setText(addFriendItemModel.timestamp);
        Glide.with(this).load(Constant.sAvatarUrl + addFriendItemModel.username).into(mAvatar);

        if (addFriendItemModel.isAdded) {
            mAdd.setText(getContext().getString(R.string.added));
            mAdd.setEnabled(false);
        } else {
            mAdd.setText(getContext().getString(R.string.add));
            mAdd.setEnabled(true);
        }
    }

    @OnClick(R.id.add)
    public void onClick() {
        String friendName = mUsername.getText().toString().trim();
        String addFriendReason = getContext().getString(R.string.add_friend_reason);
        AddFriendEvent event = new AddFriendEvent(friendName, addFriendReason);
        //发布事件
        EventBus.getDefault().post(event);
    }
}
