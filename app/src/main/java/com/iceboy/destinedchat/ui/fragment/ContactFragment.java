package com.iceboy.destinedchat.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.adapter.ContactListAdapter;
import com.iceboy.destinedchat.adapter.EMContactListenerAdapter;
import com.iceboy.destinedchat.app.Constant;
import com.iceboy.destinedchat.model.ContactListItem;
import com.iceboy.destinedchat.presenter.ContactPresenter;
import com.iceboy.destinedchat.presenter.impl.ContactPresenterImpl;
import com.iceboy.destinedchat.ui.activity.ChatActivity;
import com.iceboy.destinedchat.ui.view.ContactView;
import com.iceboy.destinedchat.ui.widget.SlideBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;

/**
 * Created by hncboy on 2018/6/8.
 * 联系人界面
 */
public class ContactFragment extends BaseFragment implements ContactView {

    private static final String TAG = "ContactFragment";

    private static final int POSITION_NOT_FOUND = -1;
    private ContactPresenter mContactPresenter;
    private ContactListAdapter mContactListAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @BindView(R.id.section)
    TextView mSection;

    @BindView(R.id.slide_bar)
    SlideBar mSlideBar;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh)
    SmartRefreshLayout mSwipeRefreshLayout;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_contacts;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void init() {
        mContactPresenter = new ContactPresenterImpl(this);
        initView();
        EMClient.getInstance().contactManager().setContactListener(mEMContactListener);
        mContactPresenter.getContactFromServer();
    }

    private void initView() {
        initRecyclerView();
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        //设置 Header 为 贝塞尔雷达 样式
        //mSwipeRefreshLayout.setEnableRefresh(false);
        mSwipeRefreshLayout.setRefreshHeader(new BezierRadarHeader(Objects.requireNonNull(getContext())).setEnableHorizontalDrag(true));
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mSlideBar.setOnSlidingBarChangeListener(mOnSlideBarChangeListener);
    }

    private void initRecyclerView() {
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mContactListAdapter = new ContactListAdapter(getContext(), mContactPresenter.getContactList());
        mContactListAdapter.setmOnItemClickListener(mOnItemClickListener);
        mRecyclerView.setAdapter(mContactListAdapter);
    }

    @Override
    public void onGetContactListSuccess() {
        mContactListAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.finishRefresh(2000, true);
    }

    @Override
    public void onGetContactListFailed() {
        toast(getString(R.string.get_contacts_error));
        mSwipeRefreshLayout.finishRefresh(2000, false);
    }

    @Override
    public void onDeleteFriendFailed() {
        hideProgress();
        toast(getString(R.string.delete_friend_failed));
    }

    @Override
    public void onDeleteFriendSuccess() {
        hideProgress();
        toast(getString(R.string.delete_friend_success));
        mContactPresenter.refreshContactList();
    }

    /**
     * 显示是否删除好友的dialog
     *
     * @param name
     */
    private void showDeleteFriendDialog(final String name) {
        String message = String.format(getString(R.string.delete_friend_message), name);
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.delete_friend))
                .setMessage(message)
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showProgress(getString(R.string.deleting_friend));
                        mContactPresenter.deleteFriend(name);
                    }
                })
                .show();
    }

    /**
     * RecyclerView滚动直到界面出现对应section的联系人
     *
     * @param section 首字母
     */
    private void scrollToSection(String section) {
        int sectionPosition = getSectionPosition(section);
        if (sectionPosition != POSITION_NOT_FOUND) {
            //将对应的有关的item移到最上面，偏移量为0
            mLinearLayoutManager.scrollToPositionWithOffset(sectionPosition, 0);
            //mRecyclerView.smoothScrollToPosition(sectionPosition);
        }
    }

    /**
     * @param section 首字符
     * @return 在联系人列表中首字符是section的第一个联系人在联系人列表中的位置
     */
    private int getSectionPosition(String section) {
        List<ContactListItem> contactListItemModels = mContactListAdapter.getmContactListItemModels();
        for (int i = 0; i < contactListItemModels.size(); i++) {
            //遍历查找联系人列表中首先首字母为section的i
            if (section.equals(contactListItemModels.get(i).getFirstLetterString())) {
                Log.i(TAG, "getSectionPosition: section =" + section);
                Log.i(TAG, "getSectionPosition: i =" + i);
                return i;
            }
        }
        Log.i(TAG, "getSectionPosition: POSITION_NOT_FOUND");
        return POSITION_NOT_FOUND;
    }

    /**
     * 监听联系人的点击事件
     */
    private ContactListAdapter.OnItemClickListener mOnItemClickListener = new ContactListAdapter.OnItemClickListener() {

        /**
         * 点击跳转到聊天界面
         * @param name
         */
        @Override
        public void onItemClick(String name) {
            startActivity(ChatActivity.class, Constant.Extra.USERNAME, name);
        }

        /**
         * 长按删除好友
         * @param name
         */
        @Override
        public void onItemLongClick(String name) {
            showDeleteFriendDialog(name);
        }
    };

    /**
     * 监听好友的添加和删除
     */
    private EMContactListenerAdapter mEMContactListener = new EMContactListenerAdapter() {

        @Override
        public void onContactAdded(String s) {
            mContactPresenter.refreshContactList();
        }

        @Override
        public void onContactDeleted(String s) {
            mContactPresenter.refreshContactList();
        }
    };

    /**
     * 滑动字母的监听
     */
    private SlideBar.OnSlideBarChangeListener mOnSlideBarChangeListener = new SlideBar.OnSlideBarChangeListener() {
        @Override
        public void onSectionChange(int index, String section) {
            mSection.setVisibility(View.VISIBLE);
            mSection.setText(section);
            scrollToSection(section);
        }

        @Override
        public void onSlidingFinish() {
            mSection.setVisibility(View.GONE);
        }
    };

    /**
     * 设置下拉刷新监听
     */
    private OnRefreshListener mOnRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            mContactPresenter.refreshContactList();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().contactManager().removeContactListener(mEMContactListener);
    }
}
