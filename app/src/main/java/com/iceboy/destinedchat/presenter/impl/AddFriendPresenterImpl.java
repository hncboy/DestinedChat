package com.iceboy.destinedchat.presenter.impl;

import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.iceboy.destinedchat.app.Constant;
import com.iceboy.destinedchat.database.db.DatabaseManager;
import com.iceboy.destinedchat.event.AddFriendEvent;
import com.iceboy.destinedchat.model.AddFriendItemModel;
import com.iceboy.destinedchat.model.UserModel;
import com.iceboy.destinedchat.presenter.AddFriendPresenter;
import com.iceboy.destinedchat.utils.ThreadUtils;
import com.iceboy.destinedchat.view.AddFriendView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by hncboy on 2018/6/15.
 */
public class AddFriendPresenterImpl implements AddFriendPresenter {

    private static final String TAG = "AddFriendPresenterImpl";

    private AddFriendView mAddFriendView;
    private ArrayList<AddFriendItemModel> mAddFriendItems;

    public AddFriendPresenterImpl(AddFriendView addFriendView) {
        mAddFriendView = addFriendView;
        mAddFriendItems = new ArrayList<>();
        //注册EventBus
        EventBus.getDefault().register(this);
    }

    @Override
    public void searchFriend(final String keyword) {
        mAddFriendView.onStartSearch();
        //在bmob中查询用户
        BmobQuery<UserModel> query = new BmobQuery<>();
        //按关键词查找并且排除当前用户，付费功能
        query.addWhereContains(Constant.Extra.USERNAME, keyword)
                .addWhereNotEqualTo(Constant.Extra.USERNAME, EMClient.getInstance().getCurrentUser());
        query.findObjects(new FindListener<UserModel>() {
            @Override
            public void done(List<UserModel> list, BmobException e) {
                processResult(list, e, keyword);
            }
        });
    }

    /**
     * 处理查询到的结果
     *
     * @param list
     * @param e
     */
    private void processResult(final List<UserModel> list, final BmobException e, final String keyword) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                if (e == null && list.size() > 0) {
                    //查找当前用户的所有联系人
                    //本地模糊查询
                    List<UserModel> newList = blurryQuery(list, keyword);
                    List<String> contacts = DatabaseManager.getInstance().queryAllContacts();
                    mAddFriendItems.clear();
                    for (int i = 0; i < newList.size(); i++) {
                        AddFriendItemModel item = new AddFriendItemModel();
                        //得到用户的注册事件
                        item.timestamp = newList.get(i).getCreatedAt();
                        item.username = newList.get(i).getUsername();
                        //如果当前用户的联系人里有包含该姓名，则显示已添加该好友
                        item.isAdded = contacts.contains(item.username);
                        mAddFriendItems.add(item);
                    }
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAddFriendView.onSearchSuccess();
                        }
                    });
                } else {
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAddFriendView.onSearchFailed();
                        }
                    });
                }
            }
        });
    }

    /**
     * 本地模糊查询
     *
     * @param list
     * @return
     */
    private List<UserModel> blurryQuery(List<UserModel> list, String keyword) {
        List<UserModel> newList = new ArrayList<>();
        //匹配关键字
        Pattern pattern = Pattern.compile(keyword);
        for (int i = 0; i < list.size(); i++) {
            //用户名和关键字模糊匹配
            Matcher matcher = pattern.matcher(list.get(i).getUsername());
            if (matcher.find()) {
                //如果能找到则添加到newList
                newList.add(list.get(i));
            }
        }
        return newList;
    }

    @Override
    public void onDestroy() {
        //解除注册
        EventBus.getDefault().unregister(this);
    }

    @Override
    public List<AddFriendItemModel> getAddFriendList() {
        return mAddFriendItems;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void addFriend(AddFriendEvent event) {
        try {
            EMClient.getInstance().contactManager().addContact(event.getFriendName(), event.getReason());
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAddFriendView.onAddFriendSuccess();
                }
            });
        } catch (HyphenateException e) {
            e.printStackTrace();
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAddFriendView.onAddFriendFailed();
                }
            });
        }
    }
}
