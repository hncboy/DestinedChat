package com.iceboy.destinedchat.model.area;

import org.litepal.crud.LitePalSupport;

/**
 * Created by hncboy on 2018/6/17.
 * 省的数据表
 */
public class Province extends LitePalSupport {

    private int id;
    private String provinceName; //记录省的名字
    private int provinceCode; //记录省的代号

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
