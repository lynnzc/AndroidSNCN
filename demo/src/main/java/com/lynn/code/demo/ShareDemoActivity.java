package com.lynn.code.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.share.api.IShareCallback;
import com.share.api.ShareManager;
import com.share.api.SimpleShareCallback;

/**
 * demo activity
 * Created by Lynn on 3/25/16.
 */
public class ShareDemoActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_sina;
    private ImageView iv_alipay;
    private ImageView iv_qq;
    private ImageView iv_qzone;
    private ImageView iv_wechat_session;
    private ImageView iv_wechat_moments;
    private IShareCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        bindListener();
    }

    private void initView() {
        iv_qq = (ImageView) findViewById(R.id.iv_qq);
        iv_qzone = (ImageView) findViewById(R.id.iv_qzone);
        iv_sina = (ImageView) findViewById(R.id.iv_sina);
        iv_alipay = (ImageView) findViewById(R.id.iv_alipay);
        iv_wechat_session = (ImageView) findViewById(R.id.iv_wechat_session);
        iv_wechat_moments = (ImageView) findViewById(R.id.iv_wechat_moments);
    }

    private void bindListener() {
        callback = new SimpleShareCallback() {
            @Override
            public void onSuccess() {
                Log.d("分享回调", "成功分享");
            }
        };

        iv_qq.setOnClickListener(this);
        iv_qzone.setOnClickListener(this);
        iv_sina.setOnClickListener(this);
        iv_alipay.setOnClickListener(this);
        iv_wechat_session.setOnClickListener(this);
        iv_wechat_moments.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        if (v == btn_qq) {
//            Log.d("qq share", "clicked");
//            actionToQQ();
//        } else if (v == btn_qqzone) {
//            Log.d("qqzone share", "clicked");
//            actionToQQZone();
//        } else if (v == btn_sina) {
//            Log.d("sina share", "clicked");
//            actionToSinaShare();
//        } else if (v == btn_alipay) {
//            Log.d("alipay share", "clicked");
//            actionToAlipayShare();
//        } else if (v == btn_wechat_session) {
//            Log.d("wechat session share", "clicked");
//            actionToWechatSession();
//        } else if (v == btn_wechat_moments) {
//            Log.d("wecaht moments share", "clicked");
//            actionToWechatMoments();
//        } else {
//            Log.d("nothing match", "nothing will continue");
//        }
        //Resources Id cannot be used in a switch statement in Android Library
        switch (v.getId()) {
            default:
            case R.id.iv_qq:
                Log.d("qq share", "clicked");
                actionToQQ();
                break;
            case R.id.iv_qzone:
                Log.d("qqzone share", "clicked");
                actionToQQZone();
                break;
            case R.id.iv_sina:
                Log.d("sina share", "clicked");
                actionToSinaShare();
                break;
            case R.id.iv_alipay:
                Log.d("alipay share", "clicked");
                actionToAlipayShare();
                break;
            case R.id.iv_wechat_session:
                Log.d("wechat session share", "clicked");
                actionToWechatSession();
                break;
            case R.id.iv_wechat_moments:
                Log.d("wechat moments share", "clicked");
                actionToWechatMoments();
                break;
        }
    }

    private void actionToSinaShare() {
        //TODO 封装后的调用过程
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        //图片, 文本, 多媒体资源 一起使用, 不会冲突
        ShareManager.shareToSina(this)
                .isClientOnly(false)
                .setShareCallback(callback)
                .setWebpage("http://www.baidu.com")
                .setWebpageText("这个是分享的标题", "这个是分享  的描述")
                .setWebpageThumbImage(bitmap)
                .setText("分享测试")
                .setImage(bitmap)
                .commit(); //只有调用commit才能分享
    }

    private void actionToAlipayShare() {
        ShareManager.shareToAlipay(this)
                .setShareCallback(callback)
                .setWebpage("https://www.baidu.com")
                .setTitle("分享网页标题")
                .setDescription("分享网页内容描述")
                .setThumbUrl("https://t.alipayobjects.com/images/rmsweb/T1vs0gXXhlXXXXXXXX.jpg")
                .commit();
    }

    private void actionToQQ() {
        ShareManager.shareToQQ(this)
                .setShareCallback(callback)
                .attachAppName(true)
                .setDefault("title", "http://baidu.com")
                .setSummary("summary")
                .setImageUrl("http://qzonestyle.gtimg.cn/qzone/vas/opensns/res/img/fenxiangxiaoxidaoQQ-dingxiangfenxiang-02.png")
                .commit();
    }

    private void actionToQQZone() {
        ShareManager.shareToQQZone(this)
                .setShareCallback(callback)
                .setTile("分享到QQ空间测试")
                .setTargetUrl("http://baidu.com")
                .setSummary("这个梗概是可选的，最多600个字符")
                .setImageUrl("http://qzonestyle.gtimg.cn/qzone/vas/opensns/res/img/fenxiangxiaoxidaoQQ-dingxiangfenxiang-02.png")
                .commit();
    }

    private void actionToWechatSession() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        ShareManager.shareToWechat(this)
                .setShareCallback(callback)
                .setWebpage("http://www.baidu.com")
                .setTitleAndDes("测试测试测试，标题要长长长长", "描述要帅帅帅")
                .setThumbBmp(bitmap)
                .commit();
    }

    private void actionToWechatMoments() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        ShareManager.shareToWechat(this)
                .toMoments(true)            //在选择具体的分享类型之前可以设置分享渠道, 不设置默认为会话
                .setShareCallback(callback) //在选择具体的分享类型之前可以设置callback
                .setWebpage("https://www.baidu.com/")
                .setTitleAndDes("百度一下分享测试", "测试测试测试测试")
                .setThumbBmp(bitmap)
                .commit();
    }
}
