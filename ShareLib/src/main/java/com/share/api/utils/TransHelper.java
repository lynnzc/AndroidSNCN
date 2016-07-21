package com.share.api.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * 转换资源编码的helper
 * Created by Lynn on 3/29/16.
 */
public class TransHelper {
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
