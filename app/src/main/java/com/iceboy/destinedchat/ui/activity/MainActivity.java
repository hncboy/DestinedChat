package com.iceboy.destinedchat.ui.activity;

import android.content.Context;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.adapter.EMCallBackAdapter;
import com.iceboy.destinedchat.adapter.EMConnectionListenerAdapter;
import com.iceboy.destinedchat.adapter.EMMessageListenerAdapter;
import com.iceboy.destinedchat.adapter.MyPagerAdapter;
import com.iceboy.destinedchat.app.Constant;
import com.iceboy.destinedchat.app.cos.IDataRequestListener;
import com.iceboy.destinedchat.app.cos.CosModel;
import com.iceboy.destinedchat.ui.fragment.ContactFragment;
import com.iceboy.destinedchat.ui.fragment.DiscoverFragment;
import com.iceboy.destinedchat.ui.fragment.ConversationFragment;
import com.iceboy.destinedchat.utils.ThreadUtils;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_LIST_CODE = 0;

    private CircleImageView mAvatar;
    private String mUsername;
    private Badge mBadge;

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.toolbar_function1)
    ImageView mToolbarAvatar;

    @BindView(R.id.toolbar_function2)
    ImageView mToolbarPlus;

    @BindView(R.id.bnve)
    BottomNavigationViewEx bnve;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    NavigationView mNavView;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        initUserInfo();
        initViewPager();
        initBadge();
        mNavView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
        bnve.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListenerAdapter);
        EMClient.getInstance().addConnectionListener(mEMConnectionListener);
    }

    /**
     * 改变未读消息的数量
     */
    private void updateUnreadCount() {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //获取未读消息的数量
                int count = EMClient.getInstance().chatManager().getUnreadMessageCount();
                Log.i(TAG, "run: " + count);
                if (count > Constant.Extra.MAX_UNREAD_COUNT) {
                    count = Constant.Extra.MAX_UNREAD_COUNT;
                }
                if (count == 0) {
                    mBadge.hide(true);
                } else {
                    mBadge.setBadgeNumber(count);
                }
            }
        });
    }

    /**
     * 初始化底部未读消息小红点，绑定底部导航栏
     */
    private void initBadge() {
        mBadge = new QBadgeView(this)
                .setBadgeNumber(0)
                .setGravityOffset(30, 2, true)
                .bindTarget(bnve.getBottomNavigationItemView(0));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUnreadCount();
    }

    /**
     * 初始化用户信息
     */
    private void initUserInfo() {
        mUsername = EMClient.getInstance().getCurrentUser();
        TextView username = mNavView.getHeaderView(0).findViewById(R.id.username);
        mAvatar = mNavView.getHeaderView(0).findViewById(R.id.avatar);
        ImageLoader.getInstance().displayImage(Constant.sAvatarUrl + mUsername, mAvatar);
        //Glide.with(this).load(Constant.sAvatarUrl + mUsername.getUsername()).into(mAvatar);
        mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFromAlbum();
            }
        });
        username.setText(mUsername);
    }

    /**
     * 初始化翻页
     */
    private void initViewPager() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new ConversationFragment());
        fragmentList.add(new ContactFragment());
        fragmentList.add(new DiscoverFragment());

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(adapter);
        bnve.setupWithViewPager(mViewPager);
    }

    @OnClick({R.id.toolbar_function1, R.id.toolbar_function2})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_function1:
                mDrawerLayout.openDrawer(mNavView);
                break;
            case R.id.toolbar_function2:
                setMoreFunction();
                break;
        }
    }

    /**
     * * 设置右上角的dialog
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
                    startActivity(AddFriendActivity.class, false);
                    break;
                case 1:
                    //TODO 发起群聊
                    toast(getString(R.string.group_chat));
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
                            mTitle.setText(getString(R.string.conversation));
                            mViewPager.setCurrentItem(0);
                            break;
                        case R.id.navigation_contacts:
                            mTitle.setText(getString(R.string.contact));
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
                            startActivity(WebActivity.class, Constant.Extra.TYPE, Constant.Extra.GITHUB);
                            break;
                        case R.id.nav_personal_blog:
                            startActivity(WebActivity.class, Constant.Extra.TYPE, Constant.Extra.BLOG);
                            break;
                        case R.id.nav_money:
                            donate();
                            break;
                        case R.id.nav_about_author:
                            startActivity(AboutAuthorActivity.class, false);
                            break;
                        case R.id.nav_update_theme:
                            //TODO 更换主题
                            toast(getString(R.string.update_theme));
                        case R.id.nav_settings:
                            //TODO 通用设置
                            toast(getString(R.string.settings));
                            break;
                        case R.id.nav_quit:
                            showProgress(getString(R.string.logouting));
                            EMClient.getInstance().logout(true, mEMCallBackAdapter);
                            break;
                    }
                    return true;
                }
            };

    private void donate() {
        new android.app.AlertDialog.Builder(this)
                .setTitle(R.string.donate_author)
                .setMessage(R.string.thank_you)
                .setNegativeButton(R.string.forget_it, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toast(getString(R.string.thank_you_very_much));
                    }
                })
                .setPositiveButton(R.string.donate_little, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openAliPay2Pay();
                        toast(getString(R.string.really_thankful));
                    }
                })
                .show();
    }

    /**
     * 支付
     */
    private void openAliPay2Pay() {
        if (openAlipayPayPage(this, Constant.ALIPAY_PERSON)) {
            toast(getString(R.string.jump_success));
        } else {
            toast(getString(R.string.jump_failed));
        }
    }

    /**
     * 打开支付宝页面
     *
     * @param context
     * @param qrcode
     * @return
     */
    private boolean openAlipayPayPage(Context context, String qrcode) {
        try {
            qrcode = URLEncoder.encode(qrcode, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            openUri(context, Constant.ALIPAYQR + qrcode + "%3F_s%3Dweb-other&_t=" + System.currentTimeMillis());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 发送一个intent
     *
     * @param context
     * @param s
     */
    private static void openUri(Context context, String s) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        context.startActivity(intent);
    }

    /**
     * 退出登录的监听
     */
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
                    toast(getString(R.string.logout_failed));
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
                InputStream inputStream = getContentResolver().openInputStream(pathUri);
                uploadAvatar(inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Glide.with(this).load(pathUri).into(mAvatar);
        }
    }

    /**
     * 上传头像
     *
     * @param inputStream
     */
    private void uploadAvatar(InputStream inputStream) {
        showProgress(getString(R.string.updating));
        new CosModel(getApplication()).uploadPic(mUsername, inputStream, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object object) {
                // object 是图片的url地址
                String avatarUrl = object.toString();
                Log.i(TAG, "loadUrl: " + avatarUrl);
                //mBuilder.setCoverUrl("http://" + mCoverUrl);
                hideProgress();
            }
        });
    }

    /**
     * 收到消息的监听事件
     */
    private EMMessageListenerAdapter mEMMessageListenerAdapter = new EMMessageListenerAdapter() {

        @Override
        public void onMessageReceived(List<EMMessage> list) {
            updateUnreadCount();
        }
    };

    /**
     * 监听连接中断
     */
    private EMConnectionListenerAdapter mEMConnectionListener = new EMConnectionListenerAdapter() {

        @Override
        public void onDisconnected(int i) {
            if (i == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(LoginActivity.class);
                        toast(getString(R.string.user_login_another_device));
                    }
                });
            }
        }
    };
}
