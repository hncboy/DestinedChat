package com.iceboy.destinedchat.model.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hncboy on 2018/6/17.
 *
 * 基本信息
 * "basic":{
 *     "city":"宁波",
 *     "id":"CN101210401,
 *     "update":{
 *         "loc":"2018-06-17 12:48"
 *     }
 * }
 *
 * 由于JSON中的一些字段可能不太适合直接作为Java字段来命名
 * 因此使用@SerializedName注解的方式来让JSON字段和java字段之间建立映射关系
 */
public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}