package com.share.api.tencent.wechat;

import android.content.Context;
import android.graphics.Bitmap;

import com.share.api.IShareCallback;
import com.share.api.IShareStep;
import com.share.api.ShareEnv;
import com.share.api.utils.TransHelper;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;

/**
 * 负责初始化Wechat分享需要的资源, 并且调用分享
 * Created by Lynn on 3/29/16.
 */
public class WechatShareBuilder {
    private WechatShareBuilder() {
    }

    public static WechatContentStep with(Context context) {
        return new WechatShareStep(context);
    }

    /**
     * 设置微信分享的渠道, 会话 or 朋友圈 or 收藏, 默认是会话
     * <p/>
     * 微信的分享不同资源的不同入口, 文本,图片等
     */
    public interface WechatContentStep {
        //设置这个可以分享到朋友圈
        WechatContentStep toMoments(boolean isMoments);

        //设置这个可以分享到微信收藏
        WechatContentStep toBookmark(boolean isBookmark);

        //分享文字类型
        IShareStep setText(String text);

        //分享图片类型
        IShareStep setImage(Bitmap bitmap);

        IShareStep setImage(Bitmap bitmap, int thumbSize);

        //分享网页
        WechatWebpageTextStep setWebpage(String webpageUrl);

        //分享回调方法
        WechatContentStep setShareCallback(IShareCallback callback);
    }

    /**
     * 网页分享类型, 设置分享的标题和描述
     */
    public interface WechatWebpageTextStep {
        WechatWebpageThumbStep setTitleAndDes(String title, String description);
    }

    /**
     * 网页分享类型, 设置分享的缩略图
     */
    public interface WechatWebpageThumbStep {
        IShareStep setThumbBmp(Bitmap bitmap);

        IShareStep setThumbBmp(Bitmap bitmap, int thumbSize);
    }

    public static class WechatShareStep implements WechatContentStep, IShareStep,
            WechatWebpageTextStep, WechatWebpageThumbStep {
        private Context context;
        private int shareType;
        private WXMediaMessage wxMsg;
        private static final int DEFAULT_THUMBSIZE = 150;

        private WechatShareStep() {
        }

        public WechatShareStep(Context context) {
            this.context = context;
            //初始化一个WXMediaMessage对象, 用于获取分享到微信的资源
            wxMsg = new WXMediaMessage();
            //默认的分享渠道是 会话
            shareType = ShareEnv.SHARE_TO_WECHAT_Session;
        }

        /**
         * 设置微信分线的回调方法
         */
        @Override
        public WechatContentStep setShareCallback(IShareCallback callback) {
            WechatShareApiImp.getInstance().setShareCallback(callback);
            return this;
        }

        /**
         * 改变微信分享渠道, 分享到朋友圈
         *
         * @param isMoments
         * @return
         */
        @Override
        public WechatContentStep toMoments(boolean isMoments) {
            if (isMoments) {
                shareType = ShareEnv.SHARE_TO_WECHAT_MOMENTS;
            }
            return this;
        }

        /**
         * 改变微信分享渠道, 分享到收藏
         *
         * @param isBookmark
         * @return
         */
        @Override
        public WechatContentStep toBookmark(boolean isBookmark) {
            if (isBookmark) {
                shareType = ShareEnv.SHARE_TO_WECHAT_FAVORITE;
            }
            return this;
        }

        @Override
        public IShareStep setText(String text) {
            //初始化一个WXTextObject对象, 填写分享的文本内容
            WXTextObject wxTextObject = new WXTextObject();
            wxTextObject.text = text;

            wxMsg.mediaObject = wxTextObject;
            wxMsg.description = text;
            return this;
        }

        @Override
        public IShareStep setImage(Bitmap bitmap) {
            return setImage(bitmap, DEFAULT_THUMBSIZE);
        }

        @Override
        public IShareStep setImage(Bitmap bitmap, int thumbSize) {
            //初始化一个WXImageObject对象, 分享bitmap图片资源
            WXImageObject wxImageObject = new WXImageObject(bitmap);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, thumbSize, thumbSize, true);
            bitmap.recycle();

            wxMsg.thumbData = TransHelper.bmpToByteArray(thumbBmp, true);
            wxMsg.mediaObject = wxImageObject;
            return this;
        }

        @Override
        public WechatWebpageTextStep setWebpage(String webpageUrl) {
            //初始化一个WXWebpageObject对象, 填写url
            WXWebpageObject wxWebpageObject = new WXWebpageObject();
            wxWebpageObject.webpageUrl = webpageUrl;

            //这个构造方法调用的语句如下
//            wsMsg = new WXMediaMessage(wxWebpageObject);
            wxMsg.mediaObject = wxWebpageObject;
            return this;
        }

        @Override
        public WechatWebpageThumbStep setTitleAndDes(String title, String description) {
            wxMsg.title = title;
            wxMsg.description = description;
            return this;
        }

        @Override
        public IShareStep setThumbBmp(Bitmap bitmap) {
            return setThumbBmp(bitmap, DEFAULT_THUMBSIZE);
        }

        @Override
        public IShareStep setThumbBmp(Bitmap bitmap, int thumbSize) {
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, thumbSize, thumbSize, true);
            wxMsg.thumbData = TransHelper.bmpToByteArray(thumbBmp, true);
            return this;
        }

        @Override
        public void commit() {
            if (wxMsg == null) {
                return;
            }

            WechatShareApiImp.getInstance().shareWithContent(context, wxMsg, shareType);
        }
    }
}
