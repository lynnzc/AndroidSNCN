package com.share.api.alipay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alipay.share.sdk.openapi.APImageObject;
import com.alipay.share.sdk.openapi.APMediaMessage;
import com.alipay.share.sdk.openapi.APTextObject;
import com.alipay.share.sdk.openapi.APWebPageObject;
import com.share.api.IShareCallback;
import com.share.api.IShareStep;

/**
 * 负责初始化支付宝钱包分享需要的资源, 并且调用分享
 * Created by Lynn on 3/28/16.
 */
public class AlipayShareBuilder {
    /**
     * 利用StepBuilder 实现多种资源分享
     */
    private AlipayShareBuilder() {
    }

    public static AlipayContentStep with(Context context) {
        return new AlipayShareStep(context);
    }

    public interface AlipayContentStep {
        //分享文本类型
        IShareStep setText(String text);

        //分享图片类型
        IShareStep setImage(String imageUrl);

        IShareStep setImage(Bitmap bitmap);

        IShareStep setImage(int resId);

        //分享网页类型
        TitleStep setWebpage(String webpage);

        //设置回调callback
        AlipayContentStep setShareCallback(IShareCallback callback);
    }

    public interface TitleStep {
        DescriptionStep setTitle(String title);
    }

    public interface DescriptionStep {
        ThumbUrlStep setDescription(String description);
    }

    public interface ThumbUrlStep {
        IShareStep setThumbUrl(String thumbUrl);
    }

    public static class AlipayShareStep implements AlipayContentStep, IShareStep, TitleStep, DescriptionStep,
            ThumbUrlStep {
        private Context context;
        private APMediaMessage mAlipayMsg;
        /**
         * 文字 - 1, 网页 - 2, 图片 - 3
         */
        private int shareContentType;

        public AlipayShareStep(Context context) {
            this.context = context;
            // 1. 初始化支付宝的分享消息
            shareContentType = 0;
            mAlipayMsg = new APMediaMessage();
        }

        @Override
        public AlipayShareStep setShareCallback(IShareCallback callback) {
            AlipayShareApiImp.getInstance().setShareCallback(callback);
            return this;
        }

        /**
         * 分享文本
         *
         * @param text
         * @return
         */
        @Override
        public IShareStep setText(String text) {
            shareContentType = AlipayShareApiImp.SHARE_CONTENT_TEXT;
            APTextObject textObject = new APTextObject();
            textObject.text = text;
            mAlipayMsg.mediaObject = textObject;
            return this;
        }

        /**
         * 分享网页
         *
         * @param imageUrl
         * @return
         */
        @Override
        public TitleStep setWebpage(String imageUrl) {
            //初始化一个APWebPageObject并填充网页链接地址
            shareContentType = AlipayShareApiImp.SHARE_CONTENT_WEB;
            APWebPageObject webPageObject = new APWebPageObject();
            webPageObject.webpageUrl = imageUrl;

            mAlipayMsg.mediaObject = webPageObject;
            return this;
        }

        /**
         * 分享图片
         *
         * @param bitmap
         * @return
         */
        @Override
        public IShareStep setImage(Bitmap bitmap) {
            shareContentType = AlipayShareApiImp.SHARE_CONTENT_IMAGE;
            APImageObject imageObject = new APImageObject(bitmap);
            if (bitmap != null) {
                bitmap.recycle();
            }

            mAlipayMsg.mediaObject = imageObject;
            return this;
        }

        /**
         * 分享图片
         *
         * @param imageUrl
         * @return
         */
        @Override
        public IShareStep setImage(String imageUrl) {
            shareContentType = AlipayShareApiImp.SHARE_CONTENT_IMAGE;
            APImageObject imageObject = new APImageObject();
            imageObject.imageUrl = imageUrl;

            mAlipayMsg.mediaObject = imageObject;
            return this;
        }

        @Override
        public IShareStep setImage(int resId) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
            return setImage(bitmap);
        }

        /**
         * 设置描述
         *
         * @param description
         * @return
         */
        @Override
        public ThumbUrlStep setDescription(String description) {
            mAlipayMsg.description = description;
            return this;
        }

        /**
         * 设置缩略图的url
         *
         * @param thumbUrl
         * @return
         */
        @Override
        public IShareStep setThumbUrl(String thumbUrl) {
            mAlipayMsg.thumbUrl = thumbUrl;
            return this;
        }

        /**
         * 设置标题
         *
         * @param title
         * @return
         */
        @Override
        public DescriptionStep setTitle(String title) {
            mAlipayMsg.title = title;
            return this;
        }

        @Override
        public void commit() {
            AlipayShareApiImp.getInstance().shareToFriend(context, mAlipayMsg, shareContentType);
        }
    }
}
