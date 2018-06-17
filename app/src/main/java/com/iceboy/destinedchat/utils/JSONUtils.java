package com.iceboy.destinedchat.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.iceboy.destinedchat.model.area.City;
import com.iceboy.destinedchat.model.area.Country;
import com.iceboy.destinedchat.model.area.Province;
import com.iceboy.destinedchat.model.weather.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hncboy on 2018/6/17.
 * 解析服务器返回的JSON格式的数据
 */
public class JSONUtils {

    private static final String TAG = "JSONUtils";

    /**
     * 解析和处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                Log.i(TAG, "handleProvinceResponse: allProvinces = " + response);
                JSONArray allProvinces = new JSONArray(response); //将返回的数据存入到JSONArray对象中
                for (int i = 0; i < allProvinces.length(); i++) { //循环遍历JSONArray
                    JSONObject provinceObject = allProvinces.getJSONObject(i); //取出每个对象
                    //组装成实体类对象
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save(); //将数据存储到数据库
                    Log.i(TAG, "handleProvinceResponse: 成功");
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountryResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCountries = new JSONArray(response);
                for (int i = 0; i < allCountries.length(); i++) {
                    JSONObject countryObject = allCountries.getJSONObject(i);
                    Country country = new Country();
                    country.setCountryName(countryObject.getString("name"));
                    country.setWeatherId(countryObject.getString("weather_id"));
                    country.setCityId(cityId);
                    country.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response) {
        try {
            //将整个json实例化保存在jsonObject中
            JSONObject jsonObject = new JSONObject(response);
            //从jsonObject中取出键为HeWeather的数据保存在数组中
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            //取出数组的第一项并以字符串形式保存
            String weatherContent = jsonArray.getJSONObject(0).toString();
            //调用fromJson方法将weatherContent转换为Weather对象
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
