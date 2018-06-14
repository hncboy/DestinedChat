package com.iceboy.destinedchat.ui.fragment;

import android.view.View;
import android.widget.RelativeLayout;

import com.iceboy.destinedchat.R;
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

    @BindView(R.id.read_rlyt)
    RelativeLayout mReadcRlyt;

    @BindView(R.id.weather_rlyt)
    RelativeLayout mWeatherRlyt;

    @BindView(R.id.sports_rlyt)
    RelativeLayout mSportsRlyt;

    @BindView(R.id.location_rlyt)
    RelativeLayout mLocationRlyt;

    @BindView(R.id.nearby_rlyt)
    RelativeLayout mNearbyRlyt;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void init() {
    }

    @OnClick({R.id.dynamic_rlyt, R.id.read_rlyt, R.id.weather_rlyt,
            R.id.sports_rlyt, R.id.location_rlyt, R.id.nearby_rlyt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dynamic_rlyt:
                //TODO 动态
                break;
            case R.id.read_rlyt:
                //TODO 阅读
                break;
            case R.id.weather_rlyt:
                //TODO 天气
                break;
            case R.id.sports_rlyt:
                //TODO 运动
                break;
            case R.id.location_rlyt:
                startActivity(MineLocationActivity.class, false);
                //TODO 我的位置
                break;
            case R.id.nearby_rlyt:
                //TODO 附近的人
                break;
        }
    }
}
