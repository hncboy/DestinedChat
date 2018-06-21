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

    @BindView(R.id.nearby_rlyt)
    RelativeLayout mNearbyRlyt;

    @BindView(R.id.music_rlyt)
    RelativeLayout mMusicRlyt;

    @BindView(R.id.video_rlyt)
    RelativeLayout mVideoRlyt;

    @BindView(R.id.read_rlyt)
    RelativeLayout mReadRlyt;

    @BindView(R.id.sports_rlyt)
    RelativeLayout mSportsRlyt;

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

    @OnClick({R.id.dynamic_rlyt, R.id.weather_rlyt, R.id.location_rlyt, R.id.nearby_rlyt,
            R.id.read_rlyt, R.id.sports_rlyt, R.id.music_rlyt, R.id.video_rlyt})
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
            case R.id.nearby_rlyt:
                //TODO 附近的人
                toast(getString(R.string.people_nearby));
                break;
            case R.id.read_rlyt:
                //TODO 阅读
                toast(getString(R.string.read));
                break;
            case R.id.sports_rlyt:
                //TODO 运动
                toast(getString(R.string.sports));
                break;
            case R.id.music_rlyt:
                //TODO 音乐
                toast(getString(R.string.music));
                break;
            case R.id.video_rlyt:
                //TODO 视频
                toast(getString(R.string.video));
                break;
        }
    }
}
