package com.iceboy.destinedchat.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iceboy.destinedchat.R;

import butterknife.BindView;
import butterknife.OnClick;

public class DynamicActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.toolbar_function1)
    ImageView mBack;

    @BindView(R.id.toolbar_function2)
    ImageView mToolbarPlus;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_dynamic;
    }

    @Override
    protected void init() {
        initToolbar();
    }

    @OnClick({R.id.toolbar_function1, R.id.toolbar_function2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_function1:
                finish();
                break;
            case R.id.toolbar_function2:
                startActivity(PublishDynamicActivity.class, false);
                break;
        }
    }

    private void initToolbar() {
        mTitle.setText(getString(R.string.dynamic));
        mBack.setImageDrawable(getDrawable(R.drawable.ic_arrow_back_white_24dp));
    }
}
