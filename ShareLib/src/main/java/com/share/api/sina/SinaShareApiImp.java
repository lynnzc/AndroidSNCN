package com.share.api.sina;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.share.api.BaseShareApiImp;
import com.share.api.KeyConfig;
import com.share.api.utils.ThreadManager;
import com.share.api.utils.ToastHelper;
import com.sina.weibo.sdk.ApiUtils;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * 微博分享的api实现类
 * Created by Lynn on 3/25/16.
 */
public class SinaShareApiImp extends BaseShareApiImp {
    /**
     * 微博微博分享接口实例
     */
    private IWeiboShareAPI mWeiboShareAPI = null;
    private WeiboMultiMessage msg = null;
    private WeiboAuthListener weiboAuthListener;
    private boolean isClientOnly;

    private SinaShareApiImp() {
    }

    private static class SinaShareHolder {
        private static final SinaShareApiImp INSTANCE = new SinaShareApiImp();
    }

    public static SinaShareApiImp getInstance() {
        return SinaShareHolder.INSTANCE;
    }

    @Override
    public void init(final Context context) {
        // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context.getApplicationContext(), KeyConfig.getWeiboAppKey());
        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        mWeiboShareAPI.registerApp();
        isClientOnly = false;

        //all in one 的授权回调listener
        weiboAuthListener = new WeiboAuthListener() {

            @Override
            public void onWeiboException(WeiboException arg0) {
            }

            @Override
            public void onComplete(Bundle bundle) {
                // TODO Auto-generated method stub
                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(context.getApplicationContext(), newToken);
//                ToastHelper.showToast(context, "onAuthorizeComplete token = " + newToken.getToken());
            }

            @Override
            public void onCancel() {
            }
        };
    }

    @Override
    public void share(final Activity activity) {
        ThreadManager.runOnSingleThread(new Runnable() {
            @Override
            public void run() {
                sendMultiMessage(activity);
            }
        });
//        activity.finish();
    }

    @Override
    public void handleResponse(Intent intent, Object response) {
        if (response instanceof IWeiboHandler.Response) {
            mWeiboShareAPI.handleWeiboResponse(intent, (IWeiboHandler.Response) response);
        }

        if (getShareCallback() != null) {
            setShareCallback(null);
        }
    }

    /**
     * 只调用本地客户端分享
     *
     * @param context
     * @param msg
     */
    public void shareWithClient(Context context, WeiboMultiMessage msg) {
        if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
            /*ApiUtils.BUILD_INT_VER_2_2*/
            if (mWeiboShareAPI.getWeiboAppSupportAPI() >= ApiUtils.BUILD_INT_VER_2_2) {
                this.msg = msg;
                isClientOnly = true;
                //跳转到新浪的分享回调activity
//                share((Activity) context);
                context.startActivity(new Intent(context, SinaEntryActivity.class)
                        .putExtra(SinaEntryActivity.SINA_ISSHARE, true));
            } else {
                // 2.2以前不支持分享声音voice文件, 这里没有继承voice，所以不存在影响
                // 并且只支持单挑信息分享, 这里不考虑低于2.2的api, 没有集成单条信息分享的调用
                ToastHelper.showToast(context, "没有集成低于2.2版本的分享");
            }
        } else {
            //API 不支持新浪分享
            ToastHelper.showToast(context, "微博客户端不支持 SDK 分享或微博客户端未安装或微博客户端是非官方版本。");
        }
    }

    /**
     * 调用All in one, 如果有客户端优先调用客户端, 否则调用网页授权分享
     *
     * @param context
     * @param msg
     */
    public void shareAllInOne(Context context, WeiboMultiMessage msg) {
        this.msg = msg;
        isClientOnly = false;
//        share((Activity) context);
        context.startActivity(new Intent(context, SinaEntryActivity.class)
                .putExtra(SinaEntryActivity.SINA_ISSHARE, true));
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     * <p>
     */
    private void sendMultiMessage(Activity activity) {
        if (msg == null) {
            ToastHelper.showToast(activity, "微博分享失败, 无法获取分享信息");
            return;
        }

        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = msg;

        // 3. 发送请求消息到微博，唤起微博分享界面
        if (isClientOnly) {
            mWeiboShareAPI.sendRequest(activity, request);
        } else {
            AuthInfo authInfo = new AuthInfo(activity, KeyConfig.getWeiboAppKey(), KeyConfig.getWeiboRedirectUrl(), KeyConfig.getWeiboScope());
            Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(activity.getApplicationContext());
            String token = "";

            if (accessToken != null) {
                token = accessToken.getToken();
            }

            mWeiboShareAPI.sendRequest(activity, request, authInfo, token, weiboAuthListener);
        }
    }
}
