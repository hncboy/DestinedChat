package com.iceboy.destinedchat.ui.fragment;

import android.widget.TextView;

import com.iceboy.destinedchat.R;

import butterknife.BindView;

/**
 * Created by hncboy on 2018/6/8.
 * 消息界面
 */
public class MessageFragment extends BaseFragment {

    @BindView(R.id.title)
    TextView title;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_message;
    }

    @Override
    protected void init() {
        title.setText(getString(R.string.message));
    }
}
