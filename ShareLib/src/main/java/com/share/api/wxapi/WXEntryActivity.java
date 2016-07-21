package com.share.api.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.share.api.IShareCallback;
import com.share.api.tencent.wechat.WechatShareApiImp;
import com.share.api.utils.ToastHelper;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

/**
 * 微信的分享后回调的activity
 * Created by Lynn on 3/30/16.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            WechatShareApiImp.getInstance().handleResponse(getIntent(), this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WechatShareApiImp.getInstance().handleResponse(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        IShareCallback callback = WechatShareApiImp.getInstance().getShareCallback();
        try {
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    if (callback != null) {
                        callback.onSuccess();
                    }
                    ToastHelper.showToast(this, "分享成功");
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    if (callback != null) {
                        callback.onCancel();
                    }
                    ToastHelper.showToast(this, "分享取消");
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    if (callback != null) {
                        callback.onFailed();
                    }
                    ToastHelper.showToast(this, "分享失败 ");
                    break;
                default:
                    break;
            }
        } finally {
            if (callback != null) {
                callback.onFinally();
            }
            //分享回调要结束该activity
            finish();
        }
    }
}
