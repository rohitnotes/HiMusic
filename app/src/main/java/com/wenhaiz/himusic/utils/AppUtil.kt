package com.wenhaiz.himusic.utils

import android.content.Context

/**
 * 获取应用信息的工具类
 *
 * Created by Wenhai on 2017/8/4.
 */
fun getAppVersionName(context: Context): String {
    return context.packageManager
            .getPackageInfo(context.packageName, 0)
            .versionName
}