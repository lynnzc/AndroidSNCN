package com.example.lynn.sinashare;

import android.content.Context;

import com.example.lynn.sinashare.sina.SinaShareApiImp;

/**
 * 分享环境
 * Created by Lynn on 3/25/16.
 */
public class ShareEnv {
    public static final int SHARE_CLIENT = 1;
    public static final int SHARE_ALL_IN_ONE = 2;
    //新浪微博
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
     * WeiboSDKDemo 应用对应的权限，第三方开发者一般不需要这么多，可直接设置成空即可。
     * 详情请查看 Demo 中对应的注释。
     */
    public static String SINA_SCOPE;

    //QQ

    //Wechat

    //Alipay

    //根据不同渠道来初始化分享
    public static void init(Context context, boolean isRelease) {
        //is release
        if (isRelease) {
            initRelease(context);
        } else {
            initDebug(context);
        }
    }

    public static void initRelease(Context context) {

        register(context);
    }

    public static void initDebug(Context context) {
        //新浪微博
        SINA_APP_KEY = "1350719244";
        SINA_REDIRECT_URL = "http://www.baidu.com";
        SINA_SCOPE =
                "email,direct_messages_read,direct_messages_write,"
                        + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                        + "follow_app_official_microblog," + "invitation_write";

        //QQ

        //Wechat

        //Alipay

        register(context);
    }

    private static void register(Context context) {
        //初始化新浪微博
        SinaShareApiImp.getInstance().initWeibo(context);
        //初始化QQ

        //初始化Wechat

        //初始化Alipay

    }
}
