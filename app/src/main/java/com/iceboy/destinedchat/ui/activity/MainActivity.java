package com.iceboy.destinedchat.ui.activity;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.adapter.MyPagerAdapter;
import com.iceboy.destinedchat.ui.fragment.ContactsFragment;
import com.iceboy.destinedchat.ui.fragment.DiscoverFragment;
import com.iceboy.destinedchat.ui.fragment.MessageFragment;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.function1)
    ImageView mMine;

    @BindView(R.id.function2)
    ImageView mPlus;

    @BindView(R.id.bnve)
    BottomNavigationViewEx bnve;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    NavigationView mNavView;

    @Override
    protected void init() {
        initViewPager();
        mNavView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
        bnve.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    private void initViewPager() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new MessageFragment());
        fragmentList.add(new ContactsFragment());
        fragmentList.add(new DiscoverFragment());

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(adapter);
        bnve.setupWithViewPager(mViewPager);
    }

    @OnClick({R.id.function1, R.id.function2})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.function1:
                mDrawerLayout.openDrawer(mNavView);
                break;
            case R.id.function2:
                String[] items = {getString(R.string.add_friends), getString(R.string.group_chat)};
                AlertDialog alertDialog  = new AlertDialog
                        .Builder(this)
                        .setItems(items, mOnClickListener)
                        .show();
                //show后设置dialog的大小和位置
                Window dialogWindow = alertDialog.getWindow();
                assert dialogWindow != null;
                WindowManager.LayoutParams params = dialogWindow.getAttributes();
                params.width = 500;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                params.x = 500;
                params.y = -550;
                dialogWindow.setAttributes(params);
                break;
        }
    }

    /**
     * dialog的点击事件
     */
    private DialogInterface.OnClickListener mOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    //TODO 添加好友
                    Toast.makeText(MainActivity.this, getString(R.string.add_friends), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    //TODO 发起群聊
                    Toast.makeText(MainActivity.this, getString(R.string.group_chat), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 底部导航的监听事件
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new
            BottomNavigationViewEx.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_message:
                            mTitle.setText(getString(R.string.message));
                            mViewPager.setCurrentItem(0);
                            break;
                        case R.id.navigation_contacts:
                            mTitle.setText(getString(R.string.contacts));
                            mViewPager.setCurrentItem(1);
                            break;
                        case R.id.navigation_discover:
                            mTitle.setText(getString(R.string.discover));
                            mViewPager.setCurrentItem(2);
                            break;
                    }
                    return true;
                }
            };

    /**
     * 侧滑窗口的监听
     */
    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new
            NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    mDrawerLayout.closeDrawers();
                    return true;
                }
            };
}
