package com.example.lynn.sinashare.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.lynn.sinashare.BaseShareCallback;
import com.example.lynn.sinashare.R;
import com.example.lynn.sinashare.ShareManager;
import com.example.lynn.sinashare.SimpleShareCallback;

/**
 * demo activity
 * Created by Lynn on 3/25/16.
 */
public class ShareDemoActivity extends AppCompatActivity {
    Button btn_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    private void initView() {
        btn_share = (Button) findViewById(R.id.btn_share);
    }

    private void initListener() {
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sina share", "clicked");
                actionToShare();
            }
        });
    }

    private void actionToShare() {
        //TODO 封装后的调用过程
        BaseShareCallback callback = new SimpleShareCallback() {
            @Override
            public void onSuccess() {
                Log.d("Sina分享回调", "成功分享");
            }
        };
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        ShareManager.shareToSina(this)
                .setCallback(callback)
                .setText("分享测试")
                .setImageRes(bitmap).share();
    }
}
