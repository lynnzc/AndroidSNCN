package com.example.lynn.sinashare.sina;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.lynn.sinashare.BaseShareCallback;
import com.example.lynn.sinashare.R;
import com.example.lynn.sinashare.ShareEnv;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;

/**
 * 分享的使用类
 * 负责初始化新浪分享需要的资源, 并且调用分享
 * Created by Lynn on 3/25/16.
 */
public class SinaShareBuilder {
    private WeiboMultiMessage weiboMessage;
    private Context context;
    //client  = 1, all in one = 2
    private int shareType;

    private SinaShareBuilder() {
    }

    public static SinaShareBuilder with(Context context) {
        return new SinaShareBuilder(context);
    }

    private SinaShareBuilder(Context context) {
        this.context = context;
        // 1. 初始化微博的分享消息
        weiboMessage = new WeiboMultiMessage();
        shareType = ShareEnv.SHARE_ALL_IN_ONE;
    }

    public SinaShareBuilder setShareType(int type) {
        this.shareType = type;
        return this;
    }

    public SinaShareBuilder setCallback(BaseShareCallback callback) {
        SinaShareApiImp.getInstance().setShareCallback(callback);
        return this;
    }

    public SinaShareBuilder setText(String msg) {
        TextObject textObject = new TextObject();
        textObject.text = msg;

        weiboMessage.textObject = textObject;
        return this;
    }

    public SinaShareBuilder setImageRes(Bitmap img) {
        ImageObject imageObject = new ImageObject();
        //设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        imageObject.setImageObject(bitmap);

        weiboMessage.imageObject = imageObject;
        return this;
    }

    public void share() {
        //TODO 多媒体资源
        // 1. 初始化微博的分享消息
//        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
//        if (hasText) {
//            weiboMessage.textObject = getTextObj();
//        }
//
//        if (hasImage) {
//            weiboMessage.imageObject = getImageObj();
//        }

//        // 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
//        if (hasWebpage) {
//            weiboMessage.mediaObject = getWebpageObj();
//        }
//        if (hasMusic) {
//            weiboMessage.mediaObject = getMusicObj();
//        }
//        if (hasVideo) {
//            weiboMessage.mediaObject = getVideoObj();
//        }
//        if (hasVoice) {
//            weiboMessage.mediaObject = getVoiceObj();
//        }

        if (shareType == ShareEnv.SHARE_CLIENT) {
            SinaShareApiImp.getInstance().shareWithClient(context, weiboMessage);
        } else {
            SinaShareApiImp.getInstance().shareAllInOne(context, weiboMessage);
        }
    }
}
