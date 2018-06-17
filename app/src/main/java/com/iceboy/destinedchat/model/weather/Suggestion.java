package com.iceboy.destinedchat.model.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hncboy on 2018/6/17.
 *
 * 舒适度、洗车及运动建议
 * "suggestion":{
 *     "comf":{
 *         "txt":"白白天天气晴好，您在这种天气条件下，会感觉早晚凉爽、舒适，午后偏热。"
 *     }
 *     "cw":{
 *         "txt":"天气较好，较适宜进行各种运动，但因天气热，请适当减少运动时间，降低运动强度。"
 *     }
 *     "sport":{
 *         "txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"
 *     }
 * }
 */
public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public class Comfort {
        @SerializedName("txt")
        public String info;
    }

    public class CarWash {
        @SerializedName("txt")
        public String info;
    }

    public class Sport {
        @SerializedName("txt")
        public String info;
    }
}

