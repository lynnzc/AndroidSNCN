package com.example.lynn.sinashare.demo;

import android.app.Application;

import com.example.lynn.sinashare.ShareEnv;

/**
 * demo application
 * Created by Lynn on 3/25/16.
 */
public class ShareDemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化分享用法
        ShareEnv.init(getApplicationContext(), false);
    }
}
