package com.iceboy.destinedchat.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.iceboy.destinedchat.database.db.DatabaseManager;
import com.iceboy.destinedchat.model.ContactListItem;
import com.iceboy.destinedchat.presenter.ContactPresenter;
import com.iceboy.destinedchat.utils.ThreadUtils;
import com.iceboy.destinedchat.view.ContactView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by hncboy on 2018/6/11.
 */
public class ContactPresenterImpl implements ContactPresenter {

    private ContactView mContactView;
    private List<ContactListItem> mContactListItemModels;

    public ContactPresenterImpl(ContactView contactView) {
        mContactView = contactView;
        mContactListItemModels = new ArrayList<>();
    }

    @Override
    public void getContactFromServer() {
        if (mContactListItemModels.size() > 0) {
            mContactView.onGetContactListSuccess();
            return;
        }
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    startGetContactList();
                    notifyGetContactListSuccess();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    notifyGetContactListFailed();
                }
            }
        });
    }

    /**
     * 更新好友列表失败
     */
    private void notifyGetContactListFailed() {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContactView.onGetContactListFailed();
            }
        });
    }

    /**
     * 更新好友列表成功
     */
    private void notifyGetContactListSuccess() {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContactView.onGetContactListSuccess();
            }
        });
    }

    /**
     * 得到联系人列表
     *
     * @throws HyphenateException
     */
    private void startGetContactList() throws HyphenateException {
        //获取所有的联系人
        List<String> contacts = EMClient.getInstance().contactManager().getAllContactsFromServer();
        //给联系人排序
        Collections.sort(contacts, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.charAt(0) - o2.charAt(0);
            }
        });
        //先删除数据库的所有联系人
        DatabaseManager.getInstance().deleteAllContacts();
        if (!contacts.isEmpty()) {
            for (int i = 0; i < contacts.size(); i++) {
                ContactListItem contactListItemModel = new ContactListItem();
                contactListItemModel.setUsername(contacts.get(i));
                if (itemInSameGroup(i, contactListItemModel)) {
                    contactListItemModel.setShowFirstLetter(false);
                }
                mContactListItemModels.add(contactListItemModel);
                saveContactToDatabase(contactListItemModel.getUsername());
            }
        }
    }

    /**
     * 当前联系人跟上个联系人比较，如果首字符相同则返回true
     * @param i 当前联系人下标
     * @param contactListItemModel 当前联系人数据模型
     * @return 表示当前联系人和上一联系人在同一组
     */
    private boolean itemInSameGroup(int i, ContactListItem contactListItemModel) {
        return i > 0 && (contactListItemModel.getFirstLetter() == mContactListItemModels.get(i - 1).getFirstLetter());
    }

    /**
     * 保存联系人到数据库
     * @param userName
     */
    private void saveContactToDatabase(String userName) {
        DatabaseManager.getInstance().saveContact(userName);
    }

    @Override
    public List<ContactListItem> getContactList() {
        return mContactListItemModels;
    }

    @Override
    public void refreshContactList() {
        mContactListItemModels.clear();
        getContactFromServer();
    }

    @Override
    public void deleteFriend(final String name) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(name);
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onDeleteFriendSuccess();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactView.onDeleteFriendFailed();
                        }
                    });
                }

            }
        });
    }
}
