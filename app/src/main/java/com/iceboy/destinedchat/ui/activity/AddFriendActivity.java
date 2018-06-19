package com.iceboy.destinedchat.ui.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.adapter.AddFriendListAdapter;
import com.iceboy.destinedchat.presenter.AddFriendPresenter;
import com.iceboy.destinedchat.presenter.impl.AddFriendPresenterImpl;
import com.iceboy.destinedchat.ui.view.AddFriendView;

import butterknife.BindView;
import butterknife.OnClick;

public class AddFriendActivity extends BaseActivity implements AddFriendView {

    private AddFriendPresenter mAddFriendPresenter;
    private AddFriendListAdapter mAddFriendListAdapter;

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.toolbar_function1)
    ImageView mBack;

    @BindView(R.id.toolbar_function2)
    ImageView mFunction2;

    @BindView(R.id.username)
    EditText mUsername;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.search)
    ImageView mSearch;

    @BindView(R.id.friend_not_found)
    TextView mFriendNotFound;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_add_friend;
    }

    @Override
    protected void init() {
        mAddFriendPresenter = new AddFriendPresenterImpl(this);
        mUsername.setOnEditorActionListener(mOnEditorActionListener);
        initToolbar();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAddFriendListAdapter = new AddFriendListAdapter(this, mAddFriendPresenter.getAddFriendList());
        mRecyclerView.setAdapter(mAddFriendListAdapter);
    }

    private void initToolbar() {
        mTitle.setText(getString(R.string.add_friends));
        mBack.setImageDrawable(getDrawable(R.drawable.ic_arrow_back_white_24dp));
        mFunction2.setVisibility(View.GONE);
    }

    @OnClick({R.id.toolbar_function1, R.id.search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_function1:
                finish();
                break;
            case R.id.search:
                searchFriend();
                break;
        }
    }

    /**
     * 搜索好友
     */
    private void searchFriend() {
        hideKeyBoard();
        String keyword = mUsername.getText().toString().trim();
        mAddFriendPresenter.searchFriend(keyword);
    }

    @Override
    public void onStartSearch() {
        showProgress(getString(R.string.searching));
    }

    @Override
    public void onSearchSuccess() {
        hideProgress();
        mFriendNotFound.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mAddFriendListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSearchFailed() {
        hideProgress();
        mFriendNotFound.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onAddFriendSuccess() {
        toast(getString(R.string.add_friend_success));
    }

    @Override
    public void onAddFriendFailed() {
        toast(getString(R.string.add_friend_failed));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAddFriendPresenter.onDestroy();
    }

    /**
     * 搜索好友的监听
     */
    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchFriend();
                return true;
            }
            return false;
        }
    };
}
