package com.iceboy.destinedchat.ui.fragment;

import android.view.View;
import android.widget.RelativeLayout;

import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.ui.activity.ChooseAreaActivity;
import com.iceboy.destinedchat.ui.activity.DynamicActivity;
import com.iceboy.destinedchat.ui.activity.MineLocationActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hncboy on 2018/6/8.
 * 发现界面
 */
public class DiscoverFragment extends BaseFragment {

    @BindView(R.id.dynamic_rlyt)
    RelativeLayout mDynamicRlyt;

    @BindView(R.id.weather_rlyt)
    RelativeLayout mWeatherRlyt;

    @BindView(R.id.location_rlyt)
    RelativeLayout mLocationRlyt;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void init() {
    }

    @OnClick({R.id.dynamic_rlyt, R.id.weather_rlyt, R.id.location_rlyt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dynamic_rlyt:
                startActivity(DynamicActivity.class, false);
                break;
            case R.id.weather_rlyt:
                startActivity(ChooseAreaActivity.class, false);
                break;
            case R.id.location_rlyt:
                startActivity(MineLocationActivity.class, false);
                break;
        }
    }
}
