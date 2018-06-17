package com.iceboy.destinedchat.ui.activity;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.app.Constant;

public class ChooseAreaActivity extends BaseActivity {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_choose_area;
    }

    @Override
    protected void init() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //判断是否有缓存数据，有的话直接跳转到之前选中的城市
        if (prefs.getString(Constant.Extra.WEATHER, null) != null) {
            startActivity(WeatherActivity.class);
        }
    }
}
