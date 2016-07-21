package com.share.api.tencent.qq.qqzone;

import android.content.Context;
import android.os.Bundle;

import com.share.api.IShareCallback;
import com.share.api.IShareStep;
import com.share.api.ShareEnv;
import com.share.api.tencent.qq.QQShareApiImp;
import com.share.api.utils.ToastHelper;
import com.tencent.connect.share.QzoneShare;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 负责初始化QQ Zone分享需要的资源, 并且调用分享
 * Created by Lynn on 3/29/16.
 */
public class QQZoneShareBuilder {
    private QQZoneShareBuilder() {
    }

    public static QQZonePreferenceStep with(Context context) {
        return new QQZoneShareStep(context);
    }

    public interface QQZonePreferenceStep extends QQZoneTitleStep {
        QQZoneTitleStep setShareCallback(IShareCallback callback);
    }

    public interface QQZoneTitleStep {
        //必填 分享的标题，最多200个字符。
        QQZoneTargetUrlStep setTile(String title);
    }

    public interface QQZoneTargetUrlStep {
        //必填信息 点击跳转url URL字符串
        QQZoneOptionsStep setTargetUrl(String targetUrl);
    }

    public interface QQZoneOptionsStep extends IShareStep {
        //选填 分享的摘要，最多600字符。
        QQZoneOptionsStep setSummary(String summary);

        //选填 分享的图片, 以ArrayList<String>的类型传入，以便支持多张图片（注：图片最多支持9张图片，多余的图片会被丢弃）。
        QQZoneOptionsStep setImageUrl(String... imageUrls);
    }

    public static class QQZoneShareStep implements QQZonePreferenceStep,
            QQZoneTargetUrlStep, QQZoneOptionsStep {
        private Context context;
        private Bundle msg;

        public QQZoneShareStep(Context context) {
            this.context = context;
            msg = new Bundle();
            msg.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        }

        @Override
        public QQZoneTitleStep setShareCallback(IShareCallback callback) {
            //设置分享回调
            QQShareApiImp.getInstance().setShareCallback(callback);
            return this;
        }

        @Override
        public QQZoneTargetUrlStep setTile(String title) {
            //TODO 目前只接入了图文信息接口
            msg.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
            msg.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
            return this;
        }

        @Override
        public QQZoneOptionsStep setTargetUrl(String targetUrl) {
            //必须是http
            msg.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
            return this;
        }

        @Override
        public QQZoneOptionsStep setSummary(String summary) {
            msg.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
            return this;
        }

        @Override
        public QQZoneOptionsStep setImageUrl(String... imageUrls) {
            if (imageUrls.length <= 0) {
                return this;
            }
            ArrayList<String> imageList = new ArrayList<>();
            imageList.addAll(Arrays.asList(imageUrls));
            msg.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageList);
            return this;
        }

        @Override
        public void commit() {
            if (msg == null) {
                ToastHelper.showToast(context, "分享信息为空, 分享失败");
                return;
            }
            //分享到QQZone
            QQShareApiImp.getInstance().shareWithContent(context, msg, ShareEnv.SHARE_TO_QQZONE);
        }
    }
}
