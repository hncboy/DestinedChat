package com.iceboy.destinedchat.ui.fragment;


import android.annotation.SuppressLint;
import android.os.SystemClock;

import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.view.ContactView;

import butterknife.BindView;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

import static com.iceboy.destinedchat.utils.ThreadUtils.runOnUiThread;

/**
 * Created by hncboy on 2018/6/8.
 * 联系人界面
 */
public class ContactFragment extends BaseFragment implements ContactView {

    @BindView(R.id.swipe_refresh)
    WaveSwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_contacts;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void init() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.white, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
    }

    /**
     * 刷新的监听
     */
    private WaveSwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new WaveSwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

        }
    };

    @Override
    public void onGetContactListSuccess() {

    }

    @Override
    public void onGetContactListFailed() {

    }

    @Override
    public void onDeleteFriendFailed() {

    }

    @Override
    public void onDeleteFriendSuccess() {

    }
}
