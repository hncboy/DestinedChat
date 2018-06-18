package com.iceboy.destinedchat.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.iceboy.destinedchat.app.Constant;
import com.iceboy.destinedchat.model.weather.Weather;
import com.iceboy.destinedchat.utils.HttpUtils;
import com.iceboy.destinedchat.utils.JSONUtils;
import com.iceboy.destinedchat.utils.ThreadUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by hncboy on 2018/6/17.
 * 使app在后台自动更新天气和图片
 */
public class AutoUpdateWeatherService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
                updateBingPic();
            }
        });
        //创建定时任务
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int hour = 3 * 60 * 60 * 1000; //3小时
        //elapsedRealtime获取到系统开机至今所经历的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + hour;
        Intent i = new Intent(this, AutoUpdateWeatherService.class);
        //getService(Context, int, Intent, int) 启动一个服务组件
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        //第一个参数表示闹钟类型，第二个参数表示闹钟首次执行时间，第三个参数表示闹钟响应动作。
        //ELAPSED_REALTIME_WAKEUP表示让定时任务的触发时间从系统开机开始计算，但会唤醒CPU
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新天气
     */
    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString(Constant.Extra.WEATHER, null);
        if (weatherString != null) {
            //有缓存时直接解析天气数据
            Weather weather = JSONUtils.handleWeatherResponse(weatherString);
            final String weatherId = weather.basic.weatherId;
            String weatherUrl = Constant.sWeatherUrl + weatherId + Constant.sWeatherKey;
            HttpUtils.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    Weather weather = JSONUtils.handleWeatherResponse(responseText);
                    if (weather != null && Constant.Extra.OK.equals(weather.status)) {
                        SharedPreferences.Editor editor = PreferenceManager.
                                getDefaultSharedPreferences(AutoUpdateWeatherService.this).edit();
                        editor.putString(Constant.Extra.WEATHER, responseText);
                        editor.apply();
                    }
                }
            });
        }
    }

    /**
     * 更新必应每日一图
     */
    private void updateBingPic() {
        HttpUtils.sendOkHttpRequest(Constant.sBingPicUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(AutoUpdateWeatherService.this).edit();
                editor.putString(Constant.Extra.BINGPIC, bingPic);
                editor.apply();
            }
        });
    }
}
