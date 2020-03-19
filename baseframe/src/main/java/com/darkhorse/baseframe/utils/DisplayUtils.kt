package com.darkhorse.baseframe.utils

import com.darkhorse.baseframe.utils.AppManager

object DisplayUtils {
    @JvmStatic
    fun px2dp(pxValue: Float): Float {
        val scale = AppManager.mApplication.resources.displayMetrics.density
        return pxValue / scale + 0.5f
    }

    @JvmStatic
    fun dp2px(dpValue: Float): Float {
        val scale = AppManager.mApplication.resources.displayMetrics.density
        return dpValue * scale + 0.5f
    }

    @JvmStatic
    fun px2dp(pxValue: Int): Int {
        val scale = AppManager.mApplication.resources.displayMetrics.density
        return (pxValue / scale + 0.5).toInt()
    }

    @JvmStatic
    fun dp2px(dpValue: Int): Int {
        val scale = AppManager.mApplication.resources.displayMetrics.density
        return (dpValue * scale + 0.5).toInt()
    }
}
