package com.iceboy.destinedchat.adapter;

import android.content.Context;

import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridViewAdapter;

import java.util.List;

/**
 * Created by hncboy on 2018/6/19.
 */
public class NineGridViewNotClickAdapter extends NineGridViewAdapter {


    public NineGridViewNotClickAdapter(Context context, List<ImageInfo> imageInfo) {
        super(context, imageInfo);
    }
}
