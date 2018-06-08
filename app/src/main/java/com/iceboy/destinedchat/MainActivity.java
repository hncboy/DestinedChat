package com.iceboy.destinedchat;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.iceboy.destinedchat.adapter.MyPagerAdapter;
import com.iceboy.destinedchat.ui.fragment.ContactsFragment;
import com.iceboy.destinedchat.ui.fragment.MessageFragment;
import com.iceboy.destinedchat.ui.fragment.MineFragment;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private List<Fragment> fragmentList;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.bnve)
    BottomNavigationViewEx bnve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initNavigation();
    }

    private void initNavigation() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new MessageFragment());
        fragmentList.add(new ContactsFragment());
        fragmentList.add(new MineFragment());
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);

        viewPager.setAdapter(adapter);
        bnve.setupWithViewPager(viewPager);

        //导航栏点击事件和ViewPager滑动事件，让两个控件相互关联
        bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //这里设置为：当点击到某子项，ViewPager就滑动到对应位置
                switch (item.getItemId()) {
                    case R.id.navigation_message:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_contacts:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_mine:
                        viewPager.setCurrentItem(2);
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                bnve.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
