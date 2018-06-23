package com.iceboy.destinedchat.ui.activity;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iceboy.destinedchat.R;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutAuthorActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.toolbar_function1)
    ImageView mBack;

    @BindView(R.id.toolbar_function2)
    ImageView mToolbarPlus;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_about_author;
    }

    @OnClick({R.id.toolbar_function1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_function1:
                finish();
                break;
        }
    }

    @Override
    protected void init() {
        initToolbar();

    }

    private void initToolbar() {
        mTitle.setText(getString(R.string.about_author));
        mBack.setImageDrawable(getDrawable(R.drawable.ic_arrow_back_white_24dp));
        mToolbarPlus.setVisibility(View.GONE);
    }
}
