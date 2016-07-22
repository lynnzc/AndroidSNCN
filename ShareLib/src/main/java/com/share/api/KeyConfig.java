package com.share.api;

/**
 * Created by Lynn on 7/22/16.
 */

public class KeyConfig {
    //weibo
    private static String WEIBO_APP_KEY;
    private static String WEIBO_REDIRECT_URL;
    private static String WEIBO_SCOPE;
    //wechat
    private static String WECHAT_APP_KEY;
    //qq
    private static String QQ_APP_ID;
    //alipay
    private static String ALIPAY_APP_KEY;

    /**
     * 初始化微博的app key等信息
     */
    public static void initWeibo(String key, String redirectUrl, String scope) {
        WEIBO_APP_KEY = key;
        WEIBO_REDIRECT_URL = redirectUrl;
        WEIBO_SCOPE = scope;
    }

    /**
     * 初始化微博的app key
     */
    public static void initWeibo(String key) {
        WEIBO_APP_KEY = key;
        //回调地址
        WEIBO_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
        //应用高级功能
        WEIBO_SCOPE = "email,direct_messages_read,direct_messages_write,"
                + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                + "follow_app_official_microblog," + "invitation_write";
    }

    /**
     * 初始化QQ App Key
     */
    public static void initQQ(String id) {
        QQ_APP_ID = id;
    }

    /**
     * 初始化WeChat App Key
     */
    public static void initWeChat(String key) {
        WECHAT_APP_KEY = key;
    }

    /**
     * 初始化Alipay App Key
     */
    public static void initAlipay(String key) {
        ALIPAY_APP_KEY = key;
    }

    public static String getWeiboAppKey() {
        return WEIBO_APP_KEY;
    }

    public static String getWeiboRedirectUrl() {
        return WEIBO_REDIRECT_URL;
    }

    public static String getWeiboScope() {
        return WEIBO_SCOPE;
    }

    public static String getWeChatAppKey() {
        return WECHAT_APP_KEY;
    }

    public static String getQQAppId() {
        return QQ_APP_ID;
    }

    public static String getAlipayAppKey() {
        return ALIPAY_APP_KEY;
    }
}
