package com.share.api;

import android.content.Context;

import com.share.api.alipay.AlipayShareApiImp;
import com.share.api.sina.SinaShareApiImp;
import com.share.api.tencent.qq.QQShareApiImp;
import com.share.api.tencent.wechat.WechatShareApiImp;
import com.share.api.utils.ThreadManager;

/**
 * 分享环境 在application中初始化
 * Created by Lynn on 3/25/16.
 */
public class ShareEnv {
    //Weibo
    public static final int SHARE_SINA_CLIENT = 1;
    public static final int SHARE_SINA_ALL_IN_ONE = 2;

    //QQ
    //QZONE
    public static final int SHARE_TO_QQ = 3;
    public static final int SHARE_TO_QZONE = 4;

    //Wechat
    public static final int SHARE_TO_WECHAT_Session = 5;
    public static final int SHARE_TO_WECHAT_MOMENTS = 6;
    public static final int SHARE_TO_WECHAT_FAVORITE = 7;

    //Alipay

    //根据不同渠道来初始化分享
    public static void init(final Context context, String sinaKey, String qqkey, String wechatKey, String alipayKey) {
        //APP ID / KEY等分享必须字段的初始化
        //SINA
        KeyConfig.initWeibo(sinaKey);
        //QQ
        //QQZONE
        KeyConfig.initQQ(qqkey);
        //Wechat
        KeyConfig.initWeChat(wechatKey);
        //Alipay
        KeyConfig.initAlipay(alipayKey);

        //必须要先调用初始化才能使用, 不然会报错
        ThreadManager.runOnSingleThread(new Runnable() {
            @Override
            public void run() {
                register(context);
            }
        });
    }

    /**
     * 初始化不同分享渠道的sdk单例
     */
    private static void register(Context context) {
        //初始化新浪微博
        SinaShareApiImp.getInstance().init(context);
        //初始化QQ
        //初始化QZONE
        QQShareApiImp.getInstance().init(context);
        //初始化Wechat
        WechatShareApiImp.getInstance().init(context);
        //初始化Alipay
        AlipayShareApiImp.getInstance().init(context);
    }
}
