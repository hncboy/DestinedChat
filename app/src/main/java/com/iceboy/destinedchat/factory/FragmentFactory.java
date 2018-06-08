package com.iceboy.destinedchat.factory;

import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.ui.fragment.BaseFragment;
import com.iceboy.destinedchat.ui.fragment.ContactsFragment;
import com.iceboy.destinedchat.ui.fragment.MessageFragment;
import com.iceboy.destinedchat.ui.fragment.MineFragment;

/**
 * Created by hncboy on 2018/6/8.
 * 对fragment的操作
 */
public class FragmentFactory {

    private static FragmentFactory sFragmentFactory;

    private BaseFragment mMessageFragment;
    private BaseFragment mContactsFragment;
    private BaseFragment mMineFragment;

    public static FragmentFactory getInstance() {
        if (sFragmentFactory == null) {
            synchronized (FragmentFactory.class) {
                if (sFragmentFactory == null) {
                    sFragmentFactory = new FragmentFactory();
                }
            }
        }
        return sFragmentFactory;
    }

    public BaseFragment getFragment(int id) {
        switch (id) {
            case R.id.navigation_message:
                return getMessageFragment();
            case R.id.navigation_contacts:
                return getContactsFragment();
            case R.id.navigation_mine:
                return getMineFragment();
        }
        return null;
    }

    private BaseFragment getMessageFragment() {
        if (mMessageFragment == null) {
            mMessageFragment = new MessageFragment();
        }
        return mMessageFragment;
    }

    private BaseFragment getContactsFragment() {
        if (mContactsFragment == null) {
            mContactsFragment = new ContactsFragment();
        }
        return mContactsFragment;
    }

    private BaseFragment getMineFragment() {
        if (mMineFragment == null) {
            mMineFragment = new MineFragment();
        }
        return mMineFragment;
    }
}
