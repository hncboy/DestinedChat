package com.iceboy.destinedchat.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.app.Constant;
import com.iceboy.destinedchat.model.weather.Forecast;
import com.iceboy.destinedchat.model.weather.Weather;
import com.iceboy.destinedchat.service.AutoUpdateWeatherService;
import com.iceboy.destinedchat.utils.HttpUtils;
import com.iceboy.destinedchat.utils.JSONUtils;
import com.iceboy.destinedchat.utils.ThreadUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends BaseActivity {

    private String mWeatherId;
    public DrawerLayout mDrawerLayout;
    public SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.weather_layout)
    ScrollView mWeatherLayout;

    @BindView(R.id.title_city)
    TextView mTitleCity;

    @BindView(R.id.title_update_time)
    TextView mTitleUpdateTime;

    @BindView(R.id.degree_text)
    TextView mDegreeText;

    @BindView(R.id.weather_info_text)
    TextView mWeatherInfoText;

    @BindView(R.id.forecast_layout)
    LinearLayout mForecastLayout;

    @BindView(R.id.AQI)
    TextView mAQI;

    @BindView(R.id.aqi_text)
    TextView mAqiText;

    @BindView(R.id.pm25_text)
    TextView mPm25Text;

    @BindView(R.id.comfort_text)
    TextView mComfortText;

    @BindView(R.id.car_wash_text)
    TextView mCarWashText;

    @BindView(R.id.sport_text)
    TextView mSportText;

    @BindView(R.id.bing_pic_img)
    ImageView mBingPicImg;

    @BindView(R.id.back)
    ImageView mBackImg;

    @Override
    public int getLayoutRes() {

        return R.layout.activity_weather;
    }

    @Override
    protected void init() { //将背景图和状态栏融合在一起
        initStatusBar();
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mSwipeRefresh = findViewById(R.id.swipe_refresh);
        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        initBingPic();
        initWeather();
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });
    }

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    /**
     * 初始化沉浸式状态栏
     */
    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            //得到当前活动的decorView
            View decorView = getWindow().getDecorView();
            //改变系统UI，活动的布局会显示在状态栏上面
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            //设置状态栏为透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 初始化天气
     */
    private void initWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString(Constant.Extra.WEATHER, null);
        if (weatherString != null) {
            //有缓存时直接解析天气数据
            Weather weather = JSONUtils.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            //无缓存时取到天气id去服务器查询，
            mWeatherId = getIntent().getStringExtra(Constant.Extra.WEATHER_ID);
            //请求数据前隐藏ScrollView
            mWeatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }
    }

    /**
     * 初始化必应图片
     */
    private void initBingPic() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String bingPic = prefs.getString(Constant.Extra.BINGPIC, null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(mBingPicImg);
        } else {
            loadBingPic();
        }
    }

    /**
     * 根据天气id请求城市天气信息
     */
    public void requestWeather(final String weatherId) {
        String weatherUrl = Constant.sWeatherUrl + weatherId + Constant.sWeatherKey;
        HttpUtils.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast(getString(R.string.failed_to_get_weather_information));
                        mSwipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = JSONUtils.handleWeatherResponse(responseText);
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && Constant.Extra.OK.equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString(Constant.Extra.WEATHER, responseText);
                            editor.apply();
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        } else {
                            toast(getString(R.string.failed_to_get_weather_information));
                        }
                        mSwipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        //保证每次请求天气消息的同时也会刷新背景图片
        loadBingPic();
    }

    /**
     * 处理并展示Weather实体类中的数据
     */
    @SuppressLint("SetTextI18n")
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName; //城市的名字
        String updateTime = weather.basic.update.updateTime.split(" ")[1]; //更新的时间
        String degree = weather.now.temperature + getString(R.string.celsius); //温度
        String weatherInfo = weather.now.more.info; //天气状况
        mTitleCity.setText(cityName);
        mTitleUpdateTime.setText(updateTime);
        mDegreeText.setText(degree);
        mWeatherInfoText.setText(weatherInfo);

        mForecastLayout.removeAllViews(); //移除预报中的所有view
        for (Forecast forecast : weather.forecastList) {
            //添加weather_forecast_item布局
            View view = LayoutInflater.from(this).inflate(R.layout.weather_forecast_item, mForecastLayout, false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max + getString(R.string.celsius));
            minText.setText(forecast.temperature.min + "/");
            mForecastLayout.addView(view); //添加view
        }
        setAqiAndPm25(weather);
        if (weather.suggestion != null) {
            String comfort = getString(R.string.comfort) + weather.suggestion.comfort.info;
            String carWash = getString(R.string.car_wash_index) + weather.suggestion.carWash.info;
            String sport = getString(R.string.sports_advice) + weather.suggestion.sport.info;
            mComfortText.setText(comfort);
            mCarWashText.setText(carWash);
            mSportText.setText(sport);
        }
        mWeatherLayout.setVisibility(View.VISIBLE); //设置可见
        //激活服务
        Intent intent = new Intent(this, AutoUpdateWeatherService.class);
        startService(intent);
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        HttpUtils.sendOkHttpRequest(Constant.sBingPicUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString(Constant.Extra.BINGPIC, bingPic);
                editor.apply();
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(mBingPicImg);
                    }
                });
            }
        });
    }

    //设置空气质量指数的分级
    private void setAqiAndPm25(Weather weather) {
        if (weather.aqi != null) {
            int aqi = 0;
            int pm25 = 0;
            try {
                aqi = Integer.parseInt(weather.aqi.city.aqi);
                pm25 = Integer.parseInt(weather.aqi.city.pm25);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mAqiText.setText(weather.aqi.city.aqi);
            mPm25Text.setText(weather.aqi.city.pm25);

            if (aqi == 0) {
                mAqiText.setTextColor(Color.WHITE);
            } else if (aqi < 50) { //优
                mAqiText.setTextColor(Color.rgb(0, 228, 0));
                mAQI.setTextColor(Color.rgb(0, 228, 0));
                mAQI.setText(getString(R.string.excellent));
            } else if (aqi < 100) { //良
                mAqiText.setTextColor(Color.rgb(255, 255, 0));
                mAQI.setTextColor(Color.rgb(255, 255, 0));
                mAQI.setText(getString(R.string.good));
            } else if (aqi < 150) { //轻度污染
                mAqiText.setTextColor(Color.rgb(255, 126, 0));
                mAQI.setTextColor(Color.rgb(255, 126, 0));
                mAQI.setText(getString(R.string.mild_pollution));
            } else if (aqi < 200) { //中度污染
                mAqiText.setTextColor(Color.rgb(255, 0, 0));
                mAQI.setTextColor(Color.rgb(255, 0, 0));
                mAQI.setText(getString(R.string.moderately_polluted));
            } else if (aqi < 300) { //重度污染
                mAqiText.setTextColor(Color.rgb(153, 0, 76));
                mAQI.setTextColor(Color.rgb(153, 0, 76));
                mAQI.setText(getString(R.string.severe_pollution));
            } else if (aqi > 300) { //严重污染
                mAqiText.setTextColor(Color.rgb(126, 0, 35));
                mAQI.setTextColor(Color.rgb(126, 0, 35));
                mAQI.setText(getString(R.string.serious_pollution));
            }
            if (pm25 == 0) {
                mPm25Text.setTextColor(Color.WHITE);
            } else if (pm25 < 35) {
                mPm25Text.setTextColor(Color.rgb(0, 228, 0));
            } else if (pm25 < 75) {
                mPm25Text.setTextColor(Color.rgb(255, 255, 0));
            } else if (pm25 < 115) {
                mPm25Text.setTextColor(Color.rgb(255, 126, 0));
            } else if (pm25 < 150) {
                mPm25Text.setTextColor(Color.rgb(255, 0, 0));
            } else if (pm25 < 250) {
                mPm25Text.setTextColor(Color.rgb(153, 0, 76));
            } else if (pm25 > 250) {
                mPm25Text.setTextColor(Color.rgb(126, 0, 35));
            }
        }
    }
}
