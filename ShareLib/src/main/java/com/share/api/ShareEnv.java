package com.share.api;

import android.content.Context;
import android.util.Log;

import com.share.api.alipay.AlipayShareApiImp;
import com.share.api.sina.SinaShareApiImp;
import com.share.api.tencent.qq.QQShareApiImp;
import com.share.api.tencent.wechat.WechatShareApiImp;

/**
 * 分享环境 在application中初始化
 * Created by Lynn on 3/25/16.
 */
public class ShareEnv {
    //新浪微博
    public static final int SHARE_SINA_CLIENT = 1;
    public static final int SHARE_SINA_ALL_IN_ONE = 2;
    /**
     * 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY
     */
    public static String SINA_APP_KEY;

    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     */
    public static String SINA_REDIRECT_URL;

    /**
     * WeiboSDKDemo 新浪应用对应的高级权限，第三方开发者一般不需要这么多，可直接设置成空即可。
     * 详情请查看 Demo 中对应的注释。
     */
    public static String SINA_SCOPE;

    //QQ
    //QZONE
    public static final int SHARE_TO_QQ = 3;
    public static final int SHARE_TO_QZONE = 4;
    /**
     * APP_ID For QQ here
     */
    public static String QQ_APP_ID;

    //Wechat
    public static final int SHARE_TO_WECHAT_Session = 5;
    public static final int SHARE_TO_WECHAT_MOMENTS = 6;
    public static final int SHARE_TO_WECHAT_FAVORITE = 7;
    public static String Wechat_APP_KEY;

    //Alipay
    /**
     * APP ID for alipay share
     */
    public static String ALIPAY_APP_KEY;

    //根据不同渠道来初始化分享
    public static void init(Context context, boolean isRelease) {
        //is release
        //Application Context
        if (isRelease) {
            initRelease();
        } else {
            initDebug();
        }

        //必须要先调用初始化才能使用, 不然会报错
        register(context);
    }

    /**
     * Release 环境下的 APP ID / KEY等分享必须字段的初始化
     */
    public static void initRelease() {
        //TODO only for release
        //SINA
        SINA_APP_KEY = "sina_app_key";
        SINA_REDIRECT_URL = "redirect_url";
        SINA_SCOPE =
                "email,direct_messages_read,direct_messages_write,"
                        + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                        + "follow_app_official_microblog," + "invitation_write";

        //QQ
        //QQZONE
        QQ_APP_ID = "qq_app_id";

        //Wechat
        Wechat_APP_KEY = "wechat_app_id";

        //Alipay
        ALIPAY_APP_KEY = "alipay_app_key";

    }

    /**
     * Debug 环境下的APP ID / KEY等分享必须字段的初始化
     */
    public static void initDebug() {
        //TODO only for debug

        //SINA
        SINA_APP_KEY = "sina_debug_key";
        SINA_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
        SINA_SCOPE =
                "email,direct_messages_read,direct_messages_write,"
                        + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                        + "follow_app_official_microblog," + "invitation_write";

        //QQ
        //QQZONE
        QQ_APP_ID = "qq_debug_key";

        //Wechat
        Wechat_APP_KEY = "wechat_debug_key";

        //Alipay
        ALIPAY_APP_KEY = "alipay_debug_key";
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
