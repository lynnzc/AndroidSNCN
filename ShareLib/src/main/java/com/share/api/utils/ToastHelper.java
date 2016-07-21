package com.share.api.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Lynn on 3/28/16.
 */
public class ToastHelper {
    private ToastHelper() {
    }

    public static void showToast(Context context, String msg) {
        if (msg == null || msg.equals("")) {
            return;
        }
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
