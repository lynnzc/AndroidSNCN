package com.share.api.apshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.alipay.share.sdk.openapi.BaseReq;
import com.alipay.share.sdk.openapi.BaseResp;
import com.alipay.share.sdk.openapi.IAPAPIEventHandler;
import com.share.api.IShareCallback;
import com.share.api.alipay.AlipayShareApiImp;
import com.share.api.utils.ToastHelper;

/**
 * 协助支付宝钱包分享response和调用callback的acitvity
 * Created by Lynn on 3/30/16.
 */
public class ShareEntryActivity extends Activity implements IAPAPIEventHandler {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            AlipayShareApiImp.getInstance().handleResponse(getIntent(), this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        AlipayShareApiImp.getInstance().handleResponse(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    /**
     * 分享结果回调信息处理
     */
    @Override
    public void onResp(BaseResp baseResp) {
        IShareCallback callback = AlipayShareApiImp.getInstance().getShareCallback();
        try {
            if (baseResp != null) {
                switch (baseResp.errCode) {
                    case BaseResp.ErrCode.ERR_OK:
                        ToastHelper.showToast(this, "share 成功");
                        if (callback != null) {
                            callback.onSuccess();
                        }
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        ToastHelper.showToast(this, "share 删除");
                        if (callback != null) {
                            callback.onCancel();
                        }
                        break;
                    case BaseResp.ErrCode.ERR_AUTH_DENIED:
                        //授权禁止也回调失败
                    case BaseResp.ErrCode.ERR_SENT_FAILED:
                        ToastHelper.showToast(this, "share 失败" + baseResp.errStr + baseResp.transaction);
                        if (callback != null) {
                            callback.onFailed();
                        }
                        break;
                    default:
                        break;
                }
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
