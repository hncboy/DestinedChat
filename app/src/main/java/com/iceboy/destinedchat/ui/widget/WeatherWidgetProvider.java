package com.iceboy.destinedchat.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.app.Constant;
import com.iceboy.destinedchat.model.weather.Weather;
import com.iceboy.destinedchat.ui.activity.WeatherActivity;
import com.iceboy.destinedchat.utils.JSONUtils;

/**
 * Created by hncboy on 2018/6/23.
 * 天气的小部件
 */
public class WeatherWidgetProvider extends AppWidgetProvider {

    private Weather mWeather;

    /**
     * 每次窗口小部件被更新都调用一次该方法
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        initWeatherInfo(context);

        // 获取AppWidget对应的视图
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        //点击小部件跳转到天气页面
        Intent intent = new Intent(context, WeatherActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, R.id.container_weather, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //设置remoteViews的点击事件
        remoteViews.setOnClickPendingIntent(R.id.container_weather, pendingIntent);

        //给布局添加数据
        remoteViews.setTextViewText(R.id.degree_text, mWeather.now.temperature + context.getString(R.string.celsius));
        remoteViews.setTextViewText(R.id.weather_info_text, mWeather.now.more.info);
        remoteViews.setTextViewText(R.id.title_city, mWeather.basic.cityName);

        for (int appWidgetId : appWidgetIds) {
            // 调用集合管理器对集合进行更新
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    /**
     * 初始化天气数据
     *
     * @param context
     */
    private void initWeatherInfo(Context context) {
        //获取SharedPreferences里的天气数据
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String weatherString = prefs.getString(Constant.Extra.WEATHER, null);
        mWeather = JSONUtils.handleWeatherResponse(weatherString);
    }

    /**
     * 个方法会在添加Widget或者改变Widget的大小时候被调用。
     * 在这个方法中我们还可以根据Widget的大小来选择性的显示或隐藏某些控件。
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     * @param newOptions
     */
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    /**
     * 接收窗口小部件点击时发送的广播
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    /**
     * 每删除一次窗口小部件就调用一次
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    /**
     * 当最后一个该窗口小部件删除时调用该方法
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    /**
     * 当该窗口小部件第一次添加到桌面时调用该方法
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    /**
     * 当小部件从备份恢复时调用该方法
     */
    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}
