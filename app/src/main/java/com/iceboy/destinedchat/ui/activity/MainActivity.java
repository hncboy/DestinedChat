package com.iceboy.destinedchat.ui.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.factory.FragmentFactory;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.bnve)
    BottomNavigationViewEx bnve;

    @Override
    protected void init() {
        setFragment(R.id.navigation_message);
        bnve.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    /**
     * 底部导航的监听事件
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new
            BottomNavigationViewEx.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    setFragment(item.getItemId());
                    return true;
                }
            };

    /**
     * 设置fragment的切换
     * @param id
     */
    public void setFragment(int id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, FragmentFactory.getInstance().getFragment(id)).commit();
    }
}
