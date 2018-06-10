package com.iceboy.destinedchat.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.adapter.EMCallBackAdapter;
import com.iceboy.destinedchat.adapter.MyPagerAdapter;
import com.iceboy.destinedchat.ui.fragment.ContactsFragment;
import com.iceboy.destinedchat.ui.fragment.DiscoverFragment;
import com.iceboy.destinedchat.ui.fragment.MessageFragment;
import com.iceboy.destinedchat.utils.ThreadUtils;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_LIST_CODE = 0;
    private CircleImageView mAvatar;

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
        initUserInfo();
        initViewPager();
        mNavView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
        bnve.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    /**
     * 初始化用户信息
     */
    private void initUserInfo() {
        TextView username = mNavView.getHeaderView(0).findViewById(R.id.username);
        mAvatar = mNavView.getHeaderView(0).findViewById(R.id.avatar);
        mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFromAlbum();
            }
        });
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if (bmobUser != null) {
            username.setText(bmobUser.getUsername());
        }
    }

    /**
     * 初始化翻页
     */
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
                setMoreFunction();
                break;
        }
    }

    /**
     * 设置右上角的dialog
     */
    private void setMoreFunction() {
        String[] items = {getString(R.string.add_friends), getString(R.string.group_chat)};
        AlertDialog alertDialog = new AlertDialog
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
                    switch (item.getItemId()) {
                        case R.id.nav_github:
                            break;
                        case R.id.nav_settings:
                            break;
                        case R.id.nav_quit:
                            showProgress(getString(R.string.logouting));
                            EMClient.getInstance().logout(true, mEMCallBackAdapter);
                            break;
                    }
                    return true;
                }
            };

    private EMCallBackAdapter mEMCallBackAdapter = new EMCallBackAdapter() {

        @Override
        public void onSuccess() {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideProgress();
                    toast(getString(R.string.logout_success));
                    startActivity(LoginActivity.class, true);
                }
            });
        }

        @Override
        public void onError(int i, String s) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideProgress();
                    toast(getString(R.string.login_failed));
                }
            });
        }
    };

    /**
     * 拍照或相册选取头像
     */
    private void chooseFromAlbum() {
        // 自由配置选项
        ISListConfig config = new ISListConfig.Builder()
                // 是否多选, 默认true
                .multiSelect(false)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false)
                // “确定”按钮背景色
                .btnBgColor(Color.GRAY)
                // “确定”按钮文字颜色
                .btnTextColor(Color.BLUE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#378cef"))
                // 返回图标ResId
                .backResId(R.drawable.ic_arrow_back_white_24dp)
                // 标题
                .title(getString(R.string.choose_avatar))
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#4693EC"))
                // 裁剪大小。needCrop为true的时候配置
                .cropSize(1, 2, 200, 200)
                .needCrop(false)
                // 第一个是否显示相机，默认true
                .needCamera(true)
                // 最大选择图片数量，默认9
                .maxNum(9)
                .build();
        // 跳转到图片选择器
        ISNav.getInstance().toListActivity(this, config, REQUEST_LIST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //图片选择结果回调
        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            String fileName = "file://" + pathList.get(0);
            Uri pathUri = Uri.parse(fileName);
            try {
                String[] slash = fileName.split("/");
                String picName = slash[slash.length - 1];
                System.out.println(picName);
                InputStream inputStream = getContentResolver().openInputStream(pathUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Glide.with(this).load(pathUri).into(mAvatar);
        }
    }
}
