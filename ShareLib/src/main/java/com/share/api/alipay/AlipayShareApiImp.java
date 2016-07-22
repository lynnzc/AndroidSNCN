package com.share.api.alipay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alipay.share.sdk.openapi.APAPIFactory;
import com.alipay.share.sdk.openapi.APMediaMessage;
import com.alipay.share.sdk.openapi.IAPAPIEventHandler;
import com.alipay.share.sdk.openapi.IAPApi;
import com.alipay.share.sdk.openapi.SendMessageToZFB;
import com.share.api.BaseShareApiImp;
import com.share.api.KeyConfig;
import com.share.api.ShareEnv;
import com.share.api.utils.ThreadManager;

/**
 * 支付宝分享api实现类
 * Created by Lynn on 3/28/16.
 */
public class AlipayShareApiImp extends BaseShareApiImp {
    private IAPApi mIAPApi;
    private APMediaMessage msg;
    private int shareContentType;
    public static final int SHARE_CONTENT_TEXT = 1;
    public static final int SHARE_CONTENT_WEB = 2;
    public static final int SHARE_CONTENT_IMAGE = 3;

    private AlipayShareApiImp() {
    }

    private static class AlipayShareHolder {
        private static final AlipayShareApiImp INSTANCE = new AlipayShareApiImp();
    }

    public static AlipayShareApiImp getInstance() {
        return AlipayShareHolder.INSTANCE;
    }

    @Override
    public void init(Context context) {
        mIAPApi = APAPIFactory.createZFBApi(context.getApplicationContext(), KeyConfig.getAlipayAppKey(), false);
    }

    @Override
    public void share(Activity activity) {
        Log.d("是否支持分享", isShareAllowed() + "");

        if (isShareAllowed() && msg == null) {
            return;
        }

        ThreadManager.runOnSingleThread(new Runnable() {
            @Override
            public void run() {
                //包装请求信息
                SendMessageToZFB.Req request = new SendMessageToZFB.Req();
                request.message = msg;
                request.transaction = buildTransaction();
                //发送请求
                mIAPApi.sendReq(request);
                Log.d("alipay share", "分享调用");
            }
        });
//        activity.finish();
    }

    @Override
    public void handleResponse(Intent intent, Object response) {
        if (response instanceof IAPAPIEventHandler) {
            mIAPApi.handleIntent(intent, (IAPAPIEventHandler) response);
        }
    }

    /**
     * 分享到支付宝钱包的分享数据和分享类型, 并且跳转到分享平台activity
     *
     * @param msg
     * @param shareContentType
     */
    public void shareToFriend(Context context, APMediaMessage msg, int shareContentType) {
        this.msg = msg;
        this.shareContentType = shareContentType;
        //跳转到分享平台activity
//        context.startActivity(new Intent(context, AlipayPlatformActivity.class));
        share((Activity) context);
    }

    /**
     * 判断当前设备是否已经安装支付宝钱包 && 判断当前设备是否支持分享到支付宝
     *
     * @return
     */
    public boolean isShareAllowed() {
        return mIAPApi.isZFBAppInstalled() && mIAPApi.isZFBSupportAPI();
    }

    private String buildTransaction() {
        return String.valueOf(shareContentType == 0 ?
                System.currentTimeMillis() : getType(shareContentType) + System.currentTimeMillis());
    }

    private String getType(int type) {
        switch (type) {
            default:
            case SHARE_CONTENT_TEXT:
                return "文本";
            case SHARE_CONTENT_WEB:
                return "网页";
            case SHARE_CONTENT_IMAGE:
                return "图片";
        }
    }
}
