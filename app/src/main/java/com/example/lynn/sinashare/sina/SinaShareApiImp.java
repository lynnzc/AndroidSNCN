package com.example.lynn.sinashare.sina;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lynn.sinashare.BaseShareCallback;
import com.example.lynn.sinashare.ShareEnv;
import com.example.lynn.sinashare.SinaPlatformActivity;
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
public class SinaShareApiImp implements SinaShareApi {
    /**
     * 微博微博分享接口实例
     */
    private static IWeiboShareAPI mWeiboShareAPI = null;
    private Context context;
    private WeiboMultiMessage msg = null;
    private BaseShareCallback callback = null;
    private boolean isClientOnly;

    private SinaShareApiImp() {
    }

    private static final class SinaShareHolder {
        private final static SinaShareApiImp INSTANCE = new SinaShareApiImp();
    }

    public static SinaShareApiImp getInstance() {
        return SinaShareHolder.INSTANCE;
    }

    @Override
    public void initWeibo(Context context) {
        // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context.getApplicationContext(), ShareEnv.SINA_APP_KEY);
        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        mWeiboShareAPI.registerApp();
        this.context = context;
        isClientOnly = false;
    }

    @Override
    public void share(Activity activity) {
        sendMultiMessage(activity);
    }

    @Override
    public void handleSinaResponse(Intent intent, IWeiboHandler.Response response) {
        mWeiboShareAPI.handleWeiboResponse(intent, response);
    }

    public void setShareCallback(BaseShareCallback callback) {
        this.callback = callback;
    }

    public BaseShareCallback getShareCallback() {
        return this.callback;
    }

    public void shareWithClient(Context context, WeiboMultiMessage msg) {
        if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
            int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
            if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
                this.msg = msg;
                isClientOnly = true;
                //跳转到新浪的分享回调activity
                context.startActivity(new Intent(context, SinaPlatformActivity.class));
            }

        } else {
            Toast.makeText(context, "unsupported", Toast.LENGTH_SHORT).show();
        }
    }

    public void shareAllInOne(Context context, WeiboMultiMessage msg) {
        this.msg = msg;
        isClientOnly = false;
        context.startActivity(new Intent(context, SinaPlatformActivity.class));
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     * <p/>
     */
    private void sendMultiMessage(Activity activity) {
        if (msg == null) {
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
            AuthInfo authInfo = new AuthInfo(context, ShareEnv.SINA_APP_KEY, ShareEnv.SINA_REDIRECT_URL, ShareEnv.SINA_SCOPE);
            Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(context.getApplicationContext());
            String token = "";

            if (accessToken != null) {
                token = accessToken.getToken();
            }

            mWeiboShareAPI.sendRequest(activity, request, authInfo, token, new WeiboAuthListener() {

                @Override
                public void onWeiboException(WeiboException arg0) {
                }

                @Override
                public void onComplete(Bundle bundle) {
                    // TODO Auto-generated method stub
                    Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                    AccessTokenKeeper.writeAccessToken(context.getApplicationContext(), newToken);
                    Toast.makeText(context.getApplicationContext(), "onAuthorizeComplete token = " + newToken.getToken(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancel() {
                }
            });
        }
    }
}
