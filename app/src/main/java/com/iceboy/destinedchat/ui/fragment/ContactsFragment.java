package com.iceboy.destinedchat.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iceboy.destinedchat.R;

import butterknife.BindView;

/**
 * Created by hncboy on 2018/6/8.
 * 联系人界面
 */
public class ContactsFragment extends BaseFragment {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.more)
    ImageView add;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_contacts;
    }

    @Override
    protected void init() {
        title.setText(getString(R.string.contacts));
        add.setVisibility(View.VISIBLE);
        add.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
    }
}
