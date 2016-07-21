package com.share.api.tencent.qq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.share.api.IShareCallback;
import com.share.api.utils.ThreadManager;
import com.share.api.utils.ToastHelper;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * 协助QQ/QQZone分享response和调用callback的acitvity
 * Created by Lynn on 3/29/16.
 */
public class QQEntryActivity extends Activity {
    public static final String QQ_ISSHARE = "qq_is_share";
    private IUiListener qqShareListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final IShareCallback callback = QQShareApiImp.getInstance().getShareCallback();
        //设置回调接口
        qqShareListener = new IUiListener() {
            @Override
            public void onCancel() {
                if (callback != null) {
                    callback.onCancel();
                }
                ToastHelper.showToast(QQEntryActivity.this, "取消分享");
            }

            @Override
            public void onComplete(Object response) {
                // TODO Auto-generated method stub
                if (callback != null) {
                    callback.onSuccess();
                }
                ToastHelper.showToast(QQEntryActivity.this, "成功分享 : " + response.toString());
            }

            @Override
            public void onError(UiError e) {
                // TODO Auto-generated method stub
                if (callback != null) {
                    callback.onFailed();
                }
                ToastHelper.showToast(QQEntryActivity.this, "错误分享 : " + e.errorMessage + e.errorDetail);
            }
        };

        if (getIntent() != null && getIntent().getBooleanExtra(QQ_ISSHARE, false)) {
            //分享
            share();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        QQShareApiImp.getInstance().handleResponse(data, new QQResponse(qqShareListener, requestCode, resultCode));
        qqShareListener = null;
        //结束该activity
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (qqShareListener != null) {
            qqShareListener = null;
        }
        QQShareApiImp.getInstance().releaseResource();
    }

    public IUiListener getQQShareListener() {
        return qqShareListener;
    }

    public class QQResponse {
        private int requestCode;
        private int resultCode;
        private IUiListener qqShareListener;

        public QQResponse(IUiListener qqShareListener, int requestCode, int resultCode) {
            this.qqShareListener = qqShareListener;
            this.requestCode = requestCode;
            this.resultCode = resultCode;
        }

        public int getRequestCode() {
            return requestCode;
        }

        public void setRequestCode(int requestCode) {
            this.requestCode = requestCode;
        }

        public int getResultCode() {
            return resultCode;
        }

        public void setResultCode(int resultCode) {
            this.resultCode = resultCode;
        }

        public IUiListener getQQShareListener() {
            return qqShareListener;
        }
    }

    private void share() {
        ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                //触发分享操作
                QQShareApiImp.getInstance().share(QQEntryActivity.this);
            }
        });
    }
}
