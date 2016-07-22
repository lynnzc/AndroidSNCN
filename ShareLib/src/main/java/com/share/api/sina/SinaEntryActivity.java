package com.share.api.sina;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.share.api.IShareCallback;
import com.share.api.utils.ToastHelper;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;

/**
 * 协助新浪获取分享response和调用callback的acitvity
 * Created by Lynn on 3/25/16.
 */
public class SinaEntryActivity extends Activity implements IWeiboHandler.Response {
    public static final String SINA_ISSHARE = "sina_is_share";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
            // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
            // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
            // 失败返回 false，不调用上述回调
            SinaShareApiImp.getInstance().handleResponse(getIntent(), this);
        }

        if (getIntent() != null && getIntent().getBooleanExtra(SINA_ISSHARE, false)) {
            //调用分享, 并且防止回调多次调用
            SinaShareApiImp.getInstance().share(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        SinaShareApiImp.getInstance().handleResponse(intent, this);
    }

    /**
     * 接收微客户端博请求的数据。
     * 当微博客户端唤起当前应用并进行分享时，该方法被调用。
     */
    @Override
    public void onResponse(BaseResponse baseResponse) {
        IShareCallback callback = SinaShareApiImp.getInstance().getShareCallback();
        try {
            if (baseResponse != null) {
                switch (baseResponse.errCode) {
                    case WBConstants.ErrorCode.ERR_OK:
                        //OK
                        ToastHelper.showToast(this, "share 成功");
                        if (callback != null) {
                            callback.onSuccess();
                        }
                        break;
                    case WBConstants.ErrorCode.ERR_CANCEL:
                        //CANCEL
                        ToastHelper.showToast(this, "share 删除");
                        if (callback != null) {
                            callback.onCancel();
                        }
                        break;
                    case WBConstants.ErrorCode.ERR_FAIL:
                        //FAIL
                        ToastHelper.showToast(this, "share 失败");
                        if (callback != null) {
                            callback.onFailed();
                        }
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
