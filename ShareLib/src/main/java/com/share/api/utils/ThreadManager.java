package com.share.api.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 线程管理 helper
 * Created by Lynn on 3/31/16.
 */
public final class ThreadManager {
    //UI线程
    private volatile static Handler mMainThreadHandler;
    private static Executor mSingleThread;
    private static final Object mMainHandlerLock = new Object();

    /**
     * 获得UI线程的Handler
     * double-checked locking
     *
     * @return
     */
    public static Handler getMainHandler() {
        if (mMainThreadHandler == null) {
            synchronized (mMainHandlerLock) {
                if (mMainThreadHandler == null) {
                    mMainThreadHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        return mMainThreadHandler;
    }

    public static void runOnSingleThread(Runnable runnable) {
        if (mSingleThread == null) {
            mSingleThread = Executors.newSingleThreadExecutor();
        }

        mSingleThread.execute(runnable);
    }
}
