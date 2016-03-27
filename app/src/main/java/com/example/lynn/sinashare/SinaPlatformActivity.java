package com.example.lynn.sinashare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lynn.sinashare.sina.SinaShareApiImp;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;

/**
 * TODO 打算只用一个activity作为所有平台的基类回调
 * 协助新浪分享response和调用callback的acitvity
 * Created by Lynn on 3/25/16.
 */
public class SinaPlatformActivity extends Activity implements IWeiboHandler.Response {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        share();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        SinaShareApiImp.getInstance().handleSinaResponse(intent, this);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        BaseShareCallback callback = SinaShareApiImp.getInstance().getShareCallback();
        try {
            if (baseResponse != null) {
                switch (baseResponse.errCode) {
                    case WBConstants.ErrorCode.ERR_OK:
                        //OK
                        ToastHelper(this, "share 成功");
                        if (callback != null) {
                            callback.onSuccess();
                        }
                        break;
                    case WBConstants.ErrorCode.ERR_CANCEL:
                        //CANCEL
                        ToastHelper(this, "share 删除");
                        if (callback != null) {
                            callback.onCancel();
                        }
                        break;
                    case WBConstants.ErrorCode.ERR_FAIL:
                        //FAIL
                        ToastHelper(this, "share 失败");
                        if (callback != null) {
                            callback.onFailed();
                        }
                        break;
                }
                //分享回调要结束该activity
                finish();
            }
        } finally {
            if (callback != null) {
                callback.onFinally();
            }
        }
    }

    private void share() {
        //调用实际的分享
        SinaShareApiImp.getInstance().share(this);
    }

    private void ToastHelper(Context context, String msg) {
        if (msg == null || msg.equals("")) {
            return;
        }
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
