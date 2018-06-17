package com.iceboy.destinedchat.model.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hncboy on 2018/6/17.
 *
 *  总,对Basic,Now,Suggestion,Forecast和Weather类进行了引用
 * {
 *     "HeWeather":[
 *         {
 *             "status":"ok",
 *             "basic":{},
 *             "aqi":{},
 *             "now":{},
 *             "suggestion":{},
 *             "daily_forecast":[]
 *         }
 *     ]
 * }
 */
public class Weather {

    public String status; //返回相应的状态

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList; //forecast中包含的是一个数组
}
