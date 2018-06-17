package com.iceboy.destinedchat.app;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.SoundPool;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.adapter.EMMessageListenerAdapter;
import com.iceboy.destinedchat.database.db.DatabaseManager;
import com.iceboy.destinedchat.ui.activity.ChatActivity;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;

import org.litepal.LitePalApplication;

import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by hncboy on 2018/6/9.
 * 由于Application类是在APP启动的时候就启动，启动在所有Activity之前，所以可以使用它做资源的初始化操作，
 * 如图片资源初始化，WebView的预加载，推送服务的注册等等，注意不要执行耗时操作，会拖慢APP启动速度。
 */
public class DcApplication extends LitePalApplication {

    private static final String TAG = "DcApplication";
    private CosXmlService cosXmlService;
    private SoundPool mSoundPool;
    private int mCommonSound;
    private int mEspeciallySound;

    @Override
    public void onCreate() {
        super.onCreate();
        initHyphenate();
        initBmob();
        initCos();
        initImageLoader();
        initDatabase();
        initSoundPool();
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListenerAdapter);
    }

    /**
     * 初始化声音
     */
    private void initSoundPool() {
        mSoundPool = new SoundPool.Builder().setMaxStreams(2).build();
        //普通的声音
        mCommonSound = mSoundPool.load(this, R.raw.common, 1);
        //特别提示音
        mEspeciallySound = mSoundPool.load(this, R.raw.especially, 1);
    }

    /**
     * 初始化数据库，只初始化一次
     */
    private void initDatabase() {
        DatabaseManager.getInstance().init(this);
    }

    /**
     * 自定义图片加载器
     */
    private void initImageLoader() {
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });

        /**
         * ImageLoader框架的配置
         */
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
    }

    /**
     * 初始化腾讯云对象存储配置
     */
    private void initCos() {
        String appid = "1251129737";
        String region = "ap-shanghai";

        String secretId = "AKIDTsLellxYP9j7DTAfU9ctysdO5e2FhBy8";
        String secretKey = "Uq23C6wbBewDMPHxRly1scFhOwKXcXLL";
        long keyDuration = 600; //SecretKey 的有效时间，单位秒

        //创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion(appid, region)
                .setDebuggable(true)
                .builder();

        //创建获取签名类(请参考下面的生成签名示例，或者参考 sdk中提供的ShortTimeCredentialProvider类）
        LocalCredentialProvider localCredentialProvider = new LocalCredentialProvider(secretId, secretKey, keyDuration);

        //创建 CosXmlService 对象，实现对象存储服务各项操作.
        Context context = getApplicationContext(); //应用的上下文

        cosXmlService = new CosXmlService(context, serviceConfig, localCredentialProvider);
    }

    public CosXmlService getCosXmlService() {
        return cosXmlService;
    }

    /**
     * 初始化Bmob的配置
     */
    private void initBmob() {
        Bmob.initialize(this, "186746cb12ffccd0e7e9c75be6beec41");
    }

    /**
     * 初始化环信的配置
     */
    private void initHyphenate() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(true);
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(getPackageName())) {
            Log.e(TAG, "enter the service process!");
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        //初始化
        EMClient.getInstance().init(getApplicationContext(), options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

    /**
     * 获取app的名字
     *
     * @param pID
     * @return
     */
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        Log.e(TAG, "getAppName: " + processName);
        return processName;
    }

    /**
     * 收到消息的监听
     */
    private EMMessageListenerAdapter mEMMessageListenerAdapter = new EMMessageListenerAdapter() {

        @Override
        public void onMessageReceived(List<EMMessage> list) {
            if (isForeground()) {
                mSoundPool.play(mCommonSound, 1, 1, 0, 0, 1);
            } else {
                mSoundPool.play(mEspeciallySound, 1, 1, 0, 0, 1);
                showNotification(list.get(0));
            }
        }
    };

    /**
     * 如果app不再前台则显示通知
     *
     * @param emMessage
     */
    private void showNotification(EMMessage emMessage) {
        String contentText = "";
        String name = "";
        if (emMessage.getBody() instanceof EMTextMessageBody) {
            contentText = ((EMTextMessageBody) emMessage.getBody()).getMessage();
            name = emMessage.getUserName();
        }

        Intent chat = new Intent(this, ChatActivity.class);
        chat.putExtra(Constant.Extra.USERNAME, emMessage.getUserName());
        //用TaskStackBuilder来获取PendingIntent处理点击跳转到别的Activity
        PendingIntent pendingIntent = TaskStackBuilder.create(this)
                .addParentStack(ChatActivity.class)
                .addNextIntent(chat)
                // FLAG_CANCEL_CURRENT：如果要创建的PendingIntent已经存在了，
                // 那么在创建新的PendingIntent之前，原先已经存在的PendingIntent中的intent将不能使用
                .getPendingIntent(1, PendingIntent.FLAG_CANCEL_CURRENT);

        //获取NotificationManager的实例
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //定义Notification的各种属性
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.destinedchat)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                .setContentTitle(name)
                .setContentText(contentText)
                .setContentIntent(pendingIntent)
                .build();

        //设置震动和呼吸灯亮
        notification.defaults = Notification.DEFAULT_ALL;
        //发出通知
        notificationManager.notify(1, notification);
    }

    /**
     * 判断app是否在前台
     *
     * @return
     */
    public boolean isForeground() {
        //获取系统服务的ActivityManager
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //获得系统运行的进程
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = am.getRunningAppProcesses();
        if (runningAppProcessInfos == null) {
            return false;
        }
        //遍历所有进程，判断当前应用是否在前台,也就是说是否在屏幕顶部，用户正在进行交互
        for (ActivityManager.RunningAppProcessInfo info : runningAppProcessInfos) {
            if (info.processName.equals(getPackageName()) && info.importance ==
                    ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
}
