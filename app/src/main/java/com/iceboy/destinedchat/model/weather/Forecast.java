package com.iceboy.destinedchat.model.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hncboy on 2018/6/17.
 *
 * 未来几天的天气预报
 * "daily_forecast":[
 *     {
 *         "date":"2018-6-17",
 *         "cond":{
 *             "txt_d":"多云"
 *         }
 *         "tmp":{
 *             "max":"29",
 *             "min":"22"
 *         }
 *     },
 *     ......
 * ]
 */
public class Forecast {

    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature {
        public String max;
        public String min;
    }

    public class More {
        @SerializedName("txt_d")
        public String info;
    }
}
