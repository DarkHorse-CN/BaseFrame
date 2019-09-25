package com.darkhorse.baseframe.base

import com.darkhorse.baseframe.utils.AppManager

/**
 * Description:
 * Created by DarkHorse on 2018/5/21.
 */
class MyApplication : BaseApplication() {
    override fun initUtils() {
        AppManager.init(this)
    }

    override fun onCreate() {
        super.onCreate()

    }
}
