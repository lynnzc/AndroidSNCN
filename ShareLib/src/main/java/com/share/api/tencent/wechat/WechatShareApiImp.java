package com.share.api.tencent.wechat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.share.api.BaseShareApiImp;
import com.share.api.ShareEnv;
import com.share.api.utils.ToastHelper;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Wechat分享api实现类
 * Created by Lynn on 3/29/16.
 */
public class WechatShareApiImp extends BaseShareApiImp {
    private IWXAPI mIwxapi;
    private WXMediaMessage msg;
    /**
     * SHARE_TO_WECHAT_Session = 5;
     * SHARE_TO_WECHAT_MOMENTS = 6;
     * SHARE_TO_WECHAT_FAVORITE = 7;
     */
    private int shareType;

    private WechatShareApiImp() {
    }

    private static class WechatShareApiImpHolder {
        private static final WechatShareApiImp INSTANCE = new WechatShareApiImp();
    }

    public static WechatShareApiImp getInstance() {
        return WechatShareApiImpHolder.INSTANCE;
    }

    @Override
    public void init(Context context) {
        if (mIwxapi == null) {
            //通过WXAPIFactory 获得IWXAPI实例
            mIwxapi = WXAPIFactory.createWXAPI(context.getApplicationContext(), ShareEnv.Wechat_APP_KEY, true);
            mIwxapi.registerApp(ShareEnv.Wechat_APP_KEY);
        }
        shareType = 0;
    }

    @Override
    public void share(Activity activity) {
        if (msg == null) {
            ToastHelper.showToast(activity, "微信分享失败, 无法获取分享信息");
            return;
        }

        //创造一个Req
        SendMessageToWX.Req request = new SendMessageToWX.Req();
        //绑定资源
        request.message = msg;
        //用于唯一标识这个请求
        request.transaction = buildTransaction();
        //设置分享渠道
        request.scene = getScene();
        Log.d("wechat share", "send request");
        mIwxapi.sendReq(request);
    }

    @Override
    public void handleResponse(Intent intent, Object response) {
        if (intent != null && response instanceof IWXAPIEventHandler) {
            mIwxapi.handleIntent(intent, (IWXAPIEventHandler) response);
        }
    }

    public void shareWithContent(Context context, final WXMediaMessage msg, final int shareType) {
        this.msg = msg;
        this.shareType = shareType;

        share((Activity) context);
        //不需要打开回调activity
//        context.startActivity(new Intent(context, WXEntryActivity.class));
    }

    private String buildTransaction() {
        //暂时没有指定content type
        return String.valueOf(shareType == 0 ? System.currentTimeMillis() :
                getType() + System.currentTimeMillis());
    }

    private String getType() {
        return "";
    }

    //根据分享渠道字段设置scene
    private int getScene() {
        switch (shareType) {
            default:
            case ShareEnv.SHARE_TO_WECHAT_Session:
                return SendMessageToWX.Req.WXSceneSession;
            case ShareEnv.SHARE_TO_WECHAT_MOMENTS:
                return SendMessageToWX.Req.WXSceneTimeline;
            case ShareEnv.SHARE_TO_WECHAT_FAVORITE:
                return SendMessageToWX.Req.WXSceneFavorite;
        }
    }
}
