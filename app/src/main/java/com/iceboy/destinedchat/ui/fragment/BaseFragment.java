package com.iceboy.destinedchat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by hncboy on 2018/6/8.
 */
public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected abstract void init();

    protected abstract int getLayoutRes();
}
