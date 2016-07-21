package com.share.api.sina;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.share.api.IShareCallback;
import com.share.api.IShareStep;
import com.share.api.R;
import com.share.api.ShareEnv;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.utils.Utility;

/**
 * 分享的使用类 用了Step Builder 的pattern
 * 负责初始化新浪分享需要的资源, 并且调用分享
 * Created by Lynn on 3/25/16.
 */
public class SinaShareBuilder {
    private SinaShareBuilder() {
    }

    public static SinaPreferenceStep with(Context context) {
        return new SinaShareStep(context);
    }

    public interface SinaPreferenceStep extends SinaContentStep {
        //设置分享类型, all in one / 只分享到client, 不设置默认是all in one
        SinaPreferenceStep isClientOnly(boolean isClientOnly);

        //设置分享回调的callback
        SinaPreferenceStep setShareCallback(IShareCallback callback);
    }

    public interface SinaIShareStep extends SinaContentStep, IShareStep {
        //组合接口的用法
        //这样做的目的是必须至少选择文字或者图片或者网页的其中之一才能发起commit
    }

    /**
     * 设置主题的资源分享
     */
    public interface SinaContentStep {
        //设置文本
        SinaIShareStep setText(String msg);

        //设置图片资源
        SinaIShareStep setImage(Bitmap bitmap);

        //设置网页资源
        SinaWebpageOptionsStep setWebpage(String url);
    }

    public interface SinaWebpageOptionsStep extends SinaIShareStep {
        //设置分享网页的文本
        SinaWebpageOptionsStep setWebpageText(String title, String description, String defaultText);

        SinaWebpageOptionsStep setWebpageText(String title, String description);

        //设置分享网页的缩略图
        //设置 Bitmap 类型的图片
        //注意：最终压缩过的缩略图大小不得超过 32kb
        SinaWebpageOptionsStep setWebpageThumbImage(Bitmap bitmap);
    }

    public static class SinaShareStep implements SinaIShareStep, SinaPreferenceStep, SinaWebpageOptionsStep {
        private WeiboMultiMessage weiboMessage;
        private WebpageObject webpageObject = null;
        private Context context;
        //client  = 1, all in one = 2
        private int shareType;

        public SinaShareStep(Context context) {
            this.weiboMessage = new WeiboMultiMessage();
            // 1. 初始化微博的分享消息
            this.context = context;
            this.shareType = ShareEnv.SHARE_SINA_ALL_IN_ONE;
        }

        /**
         * 是否能用客户端分享, 如果是则不会调用web分享
         *
         * @param isClientOnly
         * @return
         */
        public SinaPreferenceStep isClientOnly(boolean isClientOnly) {
            if (isClientOnly) {
                shareType = ShareEnv.SHARE_SINA_CLIENT;
            } else {
                shareType = ShareEnv.SHARE_SINA_ALL_IN_ONE;
            }
            return this;
        }

        /**
         * 设置分享的回调
         *
         * @param callback
         * @return
         */
        public SinaPreferenceStep setShareCallback(IShareCallback callback) {
            SinaShareApiImp.getInstance().setShareCallback(callback);
            return this;
        }

        /**
         * 要分享的微博内容，限140个字。
         */
        public SinaShareStep setText(String msg) {
            TextObject textObject = new TextObject();
            textObject.text = msg;

            weiboMessage.textObject = textObject;
            return this;
        }

        /**
         * 分享到微博的图片(图片大小<32k)
         *
         * @param img
         * @return
         */
        public SinaShareStep setImage(Bitmap img) {
            ImageObject imageObject = new ImageObject();
            //设置缩略图
            //注意：最终压缩过的缩略图大小不得超过 32kb
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            imageObject.setImageObject(bitmap);

            weiboMessage.imageObject = imageObject;
            return this;
        }

        /**
         * 分享网页类型 网页url
         *
         * @param url
         * @return
         */
        @Override
        public SinaWebpageOptionsStep setWebpage(String url) {
            webpageObject = new WebpageObject();
            //唯一标识
            webpageObject.identify = Utility.generateGUID();
            //分享目标的url
            webpageObject.actionUrl = url;
            //绑定网页资源到分享的资源上
            weiboMessage.mediaObject = webpageObject;
            return this;
        }

        @Override
        public SinaWebpageOptionsStep setWebpageText(String title, String description) {
            //defaultText 表示默认的文案, 但是没有看到实际显示
            return setWebpageText(title, description, "");
        }

        @Override
        public SinaWebpageOptionsStep setWebpageText(String title, String description, String defaultText) {
            if (webpageObject != null) {
                webpageObject.title = title;
                webpageObject.description = description;
                webpageObject.defaultText = defaultText;
            }
            return this;
        }

        @Override
        public SinaWebpageOptionsStep setWebpageThumbImage(Bitmap bitmap) {
            if (webpageObject != null) {
                webpageObject.setThumbImage(bitmap);
            }
            return this;
        }

        public void commit() {
            //TODO 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
            //目前只实现了其中的分享网页

            if (shareType == ShareEnv.SHARE_SINA_CLIENT) {
                SinaShareApiImp.getInstance().shareWithClient(context, weiboMessage);
            } else {
                SinaShareApiImp.getInstance().shareAllInOne(context, weiboMessage);
            }
        }
    }
}
