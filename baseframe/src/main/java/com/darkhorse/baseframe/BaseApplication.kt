package com.darkhorse.baseframe

import android.support.multidex.MultiDexApplication
import com.darkhorse.baseframe.utils.AppManager

/**
 * Description:
 * Created by DarkHorse on 2018/5/18.
 */
abstract class BaseApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        initUtils()
        AppManager.init(this)
    }

    abstract fun initUtils()

}
