package com.share.api.tencent.qq;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.share.api.IShareCallback;
import com.share.api.IShareStep;
import com.share.api.ShareEnv;
import com.tencent.connect.share.QQShare;

/**
 * 负责初始化QQ分享需要的资源, 并且调用分享
 * Created by Lynn on 3/29/16.
 */
public class QQShareBuilder {
    private QQShareBuilder() {
    }

    public static QQContentStep with(Context context) {
        return new QQShareStep(context);
    }

    public interface QQContentStep {
        //图文信息
        DefaultOptionsStep setDefault(@NonNull String title, @NonNull String targetUrl);

        //本地图片
        IShareStep setImageOnly(@NonNull String imagePath);

        //显示app name
        QQContentStep attachAppName(boolean isAppNameAttached);

        //设置分享的回调方法
        QQContentStep setShareCallback(IShareCallback callback);
    }

    public interface DefaultOptionsStep extends IShareStep {
        DefaultOptionsStep setSummary(String summary);

        DefaultOptionsStep setImageUrl(String imageUrl);
    }

    public static class QQShareStep implements QQContentStep, DefaultOptionsStep {
        private Bundle msg;
        private Context context;
        private boolean isAppNameAttached;

        private QQShareStep() {
        }

        public QQShareStep(Context context) {
            this.context = context;
            //分享的内容bundle
            msg = new Bundle();
            //默认附带app name
            isAppNameAttached = true;
        }

        public QQContentStep setShareCallback(IShareCallback callback) {
            QQShareApiImp.getInstance().setShareCallback(callback);
            return this;
        }

        //图文信息的必选项
        @Override
        public DefaultOptionsStep setDefault(@NonNull String title, @NonNull String targetUrl) {
            msg.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            msg.putString(QQShare.SHARE_TO_QQ_TITLE, title);
            msg.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
            return this;
        }

        //只是图片选项
        @Override
        public IShareStep setImageOnly(@NonNull String imagePath) {
            msg.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
            msg.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imagePath);
            return this;
        }

        @Override
        public QQContentStep attachAppName(boolean isAppNameAttached) {
            //是否附上当前app的名字信息到分享
            this.isAppNameAttached = isAppNameAttached;
            return this;
        }

        @Override
        public DefaultOptionsStep setSummary(String summary) {
            msg.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
            return this;
        }

        @Override
        public DefaultOptionsStep setImageUrl(String imageUrl) {
            msg.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
            return this;
        }

        @Override
        public void commit() {
            if (msg == null) {
                return;
            }

            if (isAppNameAttached) {
                msg.putString(QQShare.SHARE_TO_QQ_APP_NAME, "来自" +
                        "test" + "的分享");
            }

            //TODO 分享额外选项 (默认是不隐藏分享到QZone按钮且不自动打开分享到QZone的对话框) 并没有开放设置
//            msg.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0);
            QQShareApiImp.getInstance().shareWithContent(context, msg, ShareEnv.SHARE_TO_QQ);
        }
    }
}
