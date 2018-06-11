package com.iceboy.destinedchat.database.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.iceboy.destinedchat.app.Constant;
import com.iceboy.destinedchat.database.dao.ContactDao;
import com.iceboy.destinedchat.database.dao.DaoMaster;
import com.iceboy.destinedchat.database.dao.DaoSession;
import com.iceboy.destinedchat.database.entity.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hncboy on 2018/6/11.
 * 操作数据库
 */
public class DatabaseManager {

    private static DatabaseManager sInstance;
    private DaoSession mDaoSession;

    public static DatabaseManager getInstance() {
        if (sInstance == null) {
            synchronized (DatabaseManager.class) {
                if (sInstance == null) {
                    sInstance = new DatabaseManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 初始化数据库
     * @param context
     */
    public void init(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, Constant.Database.DATABASE_NAME, null);
        SQLiteDatabase writableDatabase = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(writableDatabase);
        mDaoSession = daoMaster.newSession();
    }

    /**
     * 保存联系人
     * @param username
     */
    public void saveContact(String username) {
        Contact contact = new Contact();
        contact.setUsername(username);
        mDaoSession.getContactDao().save(contact);
    }

    /**
     * 查询所有联系人
     * @return
     */
    public List<String> queryAllContacts() {
        List<Contact> list = mDaoSession.getContactDao().queryBuilder().list();
        ArrayList<String> contacts = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String contact = list.get(i).getUsername();
            contacts.add(contact);
        }
        return contacts;
    }

    /**
     * 删除所有联系人
     */
    public void deleteAllContacts() {
        ContactDao contactDao = mDaoSession.getContactDao();
        contactDao.deleteAll();
    }
}
