package com.iceboy.destinedchat.presenter;

import com.iceboy.destinedchat.model.AddFriendItem;

import java.util.List;

/**
 * Created by hncboy on 2018/6/15.
 */
public interface AddFriendPresenter {

    /**
     * 搜索好友
     * @param keyword
     */
    void searchFriend(String keyword);

    void onDestroy();

    List<AddFriendItem> getAddFriendList();
}
