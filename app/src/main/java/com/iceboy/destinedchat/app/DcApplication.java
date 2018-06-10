package com.iceboy.destinedchat.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;

import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by hncboy on 2018/6/9.
 * app的初始化
 */
public class DcApplication extends Application {

    private static final String TAG = "DcApplication";
    private CosXmlService cosXmlService;

    @Override
    public void onCreate() {
        super.onCreate();
        initHyphenate();
        initBmob();
        initCos();
        initImageLoader();
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
            System.out.println("----------------------------------");
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        System.out.println("===============================");
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
}
