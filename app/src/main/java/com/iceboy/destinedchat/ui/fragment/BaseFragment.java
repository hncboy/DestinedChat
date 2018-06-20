package com.iceboy.destinedchat.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Objects;

import butterknife.ButterKnife;

/**
 * Created by hncboy on 2018/6/8.
 */
public abstract class BaseFragment extends Fragment {

    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    protected abstract void init();

    protected abstract int getLayoutRes();

    protected void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setCancelable(true);
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    protected void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    protected void startActivity(Class activity, String key, String extra) {
        Intent intent = new Intent(getContext(), activity);
        intent.putExtra(key, extra);
        startActivity(intent);
    }

    protected void startActivity(Class activity, String key, String extra, boolean finish) {
        Intent intent = new Intent(getContext(), activity);
        intent.putExtra(key, extra);
        startActivity(intent);
        if (finish) {
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    protected void startActivity(Class activity, boolean finish) {
        Intent intent = new Intent(getContext(), activity);
        startActivity(intent);
        if (finish) {
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    /**
     * 完成活动时重置ProgressDialog，因为我们将重新使用之前创建的片段，
     * 并且如果我们不在此处重置，则对话框将引用已完成的活动。
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mProgressDialog = null;
    }

    protected void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
