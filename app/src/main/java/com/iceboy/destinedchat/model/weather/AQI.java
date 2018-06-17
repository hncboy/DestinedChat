package com.iceboy.destinedchat.model.weather;

/**
 * Created by hncboy on 2018/6/17.
 * 空气质量指数
 * "aqi":{
 *     "city":{
 *         "aqi":"39",
 *         "pm25":"21"
 *     }
 * }
 */

public class AQI {

    public AQICity city;

    public class AQICity {

        public String aqi;
        public String pm25;
    }
}
