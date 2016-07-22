package com.share.api.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Created by Lynn on 7/22/16.
 */

public class AppInstalledCheckHelper {
    private static final String QQ_PACKAGE_NAME = "com.tencent.qqlite";
    private static final String QQ_PACKAGE_NAME_ALIAS = "com.tencent.mobileqq";
    private static final String WECHAT_PACKAGE_NAME = "com.tencent.mm";

    /**
     * check whether qq client is installed on your device or not
     */
    public static boolean isQQClientInstalled(Context context) {
        return isPackageExisted(context, QQ_PACKAGE_NAME, QQ_PACKAGE_NAME_ALIAS);
    }

    /**
     * check whether wechat client is installed von your device or not
     */
    public static boolean isWeChatClientInstalled(Context context) {
        return isPackageExisted(context, WECHAT_PACKAGE_NAME);
    }

    public static boolean isPackageExisted(Context context, String... packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;

                for (int s = 0; s < packageName.length; i++) {
                    if (pn.equalsIgnoreCase(packageName[s])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
