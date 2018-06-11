package com.iceboy.destinedchat.presenter;

import com.iceboy.destinedchat.model.ContactListItemModel;

import java.util.List;

/**
 * Created by hncboy on 2018/6/11.
 */
public interface ContactPresenter {

    /**
     * 从服务器得到联系人
     */
    void getContactFromServer();

    /**
     * 获取联系人列表
     * @return
     */
    List<ContactListItemModel> getContactList();

    /**
     * 刷新列表
     */
    void refreshContactList();

    /**
     * 删除好友
     * @param name
     */
    void deleteFriend(String name);
}
