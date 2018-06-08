package com.iceboy.destinedchat.ui.fragment;

import android.widget.TextView;

import com.iceboy.destinedchat.R;

import butterknife.BindView;

/**
 * Created by hncboy on 2018/6/8.
 * 我的界面
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.title)
    TextView title;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void init() {
        title.setText(R.string.mine);
    }
}
