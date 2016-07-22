package com.share.api.tencent.qq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.share.api.BaseShareApiImp;
import com.share.api.KeyConfig;
import com.share.api.ShareEnv;
import com.share.api.utils.AppInstalledCheckHelper;
import com.share.api.utils.ToastHelper;
import com.tencent.tauth.Tencent;

/**
 * QQ / QQZone分享api实现类
 * Created by Lynn on 3/29/16.
 */
public class QQShareApiImp extends BaseShareApiImp {
    private Tencent mTencent = null;
    private Bundle msg = null;
    /**
     * SHARE_TO_QQ = 3;
     * SHARE_TO_QZONE = 4;
     */
    private int shareType;

    private QQShareApiImp() {
    }

    private static class QQShareApiImpHolder {
        private static final QQShareApiImp INSTANCE = new QQShareApiImp();
    }

    public static QQShareApiImp getInstance() {
        return QQShareApiImpHolder.INSTANCE;
    }

    @Override
    public void init(Context context) {
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        if (mTencent == null) {
            mTencent = Tencent.createInstance(KeyConfig.getQQAppId(), context.getApplicationContext());
        }
    }

    @Override
    public void share(final Activity activity) {
        if (mTencent == null || msg == null) {
            ToastHelper.showToast(activity, "资源不能为空或者初始化失败");
            return;
        }

        if (!(activity instanceof QQEntryActivity)) {
            ToastHelper.showToast(activity, "回调activity有误");
            return;
        }

        //判断是否安装 qq客户端
        if (!AppInstalledCheckHelper.isQQClientInstalled(activity)) {
            //not installed
            ToastHelper.showToast(activity, "请先安装QQ客户端");
            return;
        }

        //分享调用
        switch (shareType) {
            default:
            case ShareEnv.SHARE_TO_QQ:
                //share to qq
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTencent.shareToQQ(activity, msg, ((QQEntryActivity) activity).getQQShareListener());
                    }
                });
                break;
            case ShareEnv.SHARE_TO_QZONE:
                //share to qqzone
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTencent.shareToQzone(activity, msg, ((QQEntryActivity) activity).getQQShareListener());
                    }
                });
                break;
        }
    }


    @Override
    public void handleResponse(Intent intent, Object response) {
        if (response instanceof QQEntryActivity.QQResponse) {
            Log.d("request code", ((QQEntryActivity.QQResponse) response).getRequestCode() + "");
            Tencent.onActivityResultData(
                    ((QQEntryActivity.QQResponse) response).getRequestCode(),
                    ((QQEntryActivity.QQResponse) response).getResultCode(),
                    intent,
                    ((QQEntryActivity.QQResponse) response).getQQShareListener());
        }
//        releaseResource();
    }

    public void shareWithContent(Context context, final Bundle msg, final int shareType) {
        this.msg = msg;
        this.shareType = shareType;
        //跳转到分享的activity
        context.startActivity(new Intent(context, QQEntryActivity.class).putExtra(QQEntryActivity.QQ_ISSHARE, true));
    }

    public void releaseResource() {
        if (mTencent != null) {
            //释放资源
            Log.d("release resource", "success");
            mTencent.releaseResource();
        }

        if (getShareCallback() != null) {
            //清空callback绑定, 会持有外部activity的实例导致内存泄漏
            setShareCallback(null);
        }
    }
}
