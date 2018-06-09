package com.iceboy.destinedchat.utils;

import android.os.Looper;
import android.os.Handler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * Created by hncboy on 2018/6/9.
 */
public class ThreadUtils {

    /**
     * 得到一个线程池
     * 这个线程池可以在线程死后（或发生异常时）重新启动一个线程来替代原来的线程继续执行下去
     */
    private static Executor sExecutor = Executors.newSingleThreadExecutor();

    /**
     * new Handler(),那么这个会默认用当前线程的looper
     * Looper.getMainLooper()表示放到主UI线程去处理
     */
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    /**
     * 处理UI
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        sHandler.post(runnable);
    }

    /**
     * 开启一个线程池
     * @param runnable
     */
    public static void runOnBackgroundThread(Runnable runnable) {
        sExecutor.execute(runnable);
    }
}
