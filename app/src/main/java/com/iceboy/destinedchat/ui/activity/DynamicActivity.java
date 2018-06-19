package com.iceboy.destinedchat.ui.activity;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.adapter.DynamicAdapter;
import com.iceboy.destinedchat.database.db.DatabaseManager;
import com.iceboy.destinedchat.model.Dynamic;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class DynamicActivity extends BaseActivity {

    private static final String TAG = "DynamicActivity";

    private List<Dynamic> dynamicList;
    private DynamicAdapter mAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.toolbar_function1)
    ImageView mBack;

    @BindView(R.id.toolbar_function2)
    ImageView mToolbarPlus;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_dynamic;
    }

    @Override
    protected void init() {
        initToolbar();
        intiData();
        mSwipeRefresh.setOnRefreshListener(mOnRefreshListener);
        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DynamicAdapter(this, dynamicList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @OnClick({R.id.toolbar_function1, R.id.toolbar_function2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_function1:
                finish();
                break;
            case R.id.toolbar_function2:
                startActivity(PublishDynamicActivity.class, false);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        intiData();
    }

    private void intiData() {
        //查询Dynamic表数据
        BmobQuery<Dynamic> query = new BmobQuery<>();
        query.order("-createdAt"); //按发布时间排序
        query.include("writer"); //希望在查询时同时查询出用户的信息
        query.findObjects(new FindListener<Dynamic>() {
            @Override
            public void done(List<Dynamic> list, BmobException e) {
                if (mSwipeRefresh.isRefreshing()) {
                    mSwipeRefresh.setRefreshing(false);
                }
                Log.i(TAG, "done: what e = " + e);
                Log.i(TAG, "done: what list = " + list.size());
                if (e == null) {
                    dynamicList = showFriends(list);
                    Log.i(TAG, "done: username = " + dynamicList.get(0).getWriter().getUsername());
                    mAdapter.addDynamicList(dynamicList);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Log.i(TAG, "done: failed");
                }
            }
        });
    }

    /**
     * 只显示好友的动态
     *
     * @param list
     * @return
     */
    private List<Dynamic> showFriends(List<Dynamic> list) {
        //获取所有好友
        List<String> contacts = DatabaseManager.getInstance().queryAllContacts();
        for (int i = 0; i < list.size(); i++) {
            String username = list.get(i).getWriter().getUsername();
            if (!contacts.contains(username)) {
                list.remove(i);
            }
        }
        return list;
    }

    private void initToolbar() {
        mTitle.setText(getString(R.string.dynamic));
        mBack.setImageDrawable(getDrawable(R.drawable.ic_arrow_back_white_24dp));
    }

    /**
     * 设置下拉刷新监听
     */
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener =
            new SwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    intiData();
                }
            };
}
