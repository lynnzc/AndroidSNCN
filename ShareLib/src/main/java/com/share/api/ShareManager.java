package com.share.api;

import android.content.Context;

import com.share.api.sina.SinaShareBuilder;
import com.share.api.alipay.AlipayShareBuilder;
import com.share.api.tencent.qq.QQShareBuilder;
import com.share.api.tencent.qq.qqzone.QQZoneShareBuilder;
import com.share.api.tencent.wechat.WechatShareBuilder;

/**
 * share wrapper helper
 * Created by Lynn on 3/25/16.
 */
public class ShareManager {
    //防止实例化
    private ShareManager() {
    }

    public static SinaShareBuilder.SinaPreferenceStep shareToSina(Context context) {
        return SinaShareBuilder.with(context);
    }

    public static AlipayShareBuilder.AlipayContentStep shareToAlipay(Context context) {
        return AlipayShareBuilder.with(context);
    }

    public static QQShareBuilder.QQContentStep shareToQQ(Context context) {
        return QQShareBuilder.with(context);
    }

    public static QQZoneShareBuilder.QQZonePreferenceStep shareToQQZone(Context context) {
        return QQZoneShareBuilder.with(context);
    }

    public static WechatShareBuilder.WechatContentStep shareToWechat(Context context) {
        return WechatShareBuilder.with(context);
    }
}
