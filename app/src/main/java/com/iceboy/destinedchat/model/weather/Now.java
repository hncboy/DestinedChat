package com.iceboy.destinedchat.model.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hncboy on 2018/6/17.
 *
 * 温度和天气状况
 * "now":{
 *     "tmp":"28",
 *     "cond":{
 *         "txt":"多云"
 *     }
 * }
 */
public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;
    }
}