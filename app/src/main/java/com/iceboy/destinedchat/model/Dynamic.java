package com.iceboy.destinedchat.model;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by hncboy on 2018/6/19.
 * 动态的model
 */
public class Dynamic extends BmobObject implements Serializable {

    private BmobUser writer;
    private String text;
    private List<String> photoList;

    public BmobUser getWriter() {
        return writer;
    }

    public void setWriter(BmobUser writer) {
        this.writer = writer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<String> photoList) {
        this.photoList = photoList;
    }
}
