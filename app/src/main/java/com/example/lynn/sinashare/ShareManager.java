package com.example.lynn.sinashare;

import android.content.Context;

import com.example.lynn.sinashare.sina.SinaShareBuilder;

/**
 * share wrapper helper
 * Created by Lynn on 3/25/16.
 */
public class ShareManager {
    //防止实例化
    private ShareManager() {
    }

    public static SinaShareBuilder shareToSina(Context context) {
        return SinaShareBuilder.with(context);
    }
}
