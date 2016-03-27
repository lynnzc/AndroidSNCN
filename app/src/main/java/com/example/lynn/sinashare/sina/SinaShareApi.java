package com.example.lynn.sinashare.sina;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.sina.weibo.sdk.api.share.IWeiboHandler;

/**
 * 新浪分享的api 初始化接口
 * Created by Lynn on 3/25/16.
 */
public interface SinaShareApi {
    /**
     * 初始化微博分享需要的上下午配置
     *
     * @param context
     */
    void initWeibo(Context context);

    /**
     * 分享操作
     *
     * @param activity
     */
    void share(Activity activity);

    /**
     * 分享回调
     *
     * @param intent
     * @param response
     */
    void handleSinaResponse(Intent intent, IWeiboHandler.Response response);
}
