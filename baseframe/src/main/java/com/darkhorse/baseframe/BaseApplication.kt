package com.darkhorse.baseframe

import android.support.multidex.MultiDexApplication
import com.blankj.ALog


/**
 * Description:
 * Created by DarkHorse on 2018/5/18.
 */
abstract class BaseApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        initALog()
        initUtils()
    }

    abstract fun initUtils()

    private fun initALog(){
        ALog.init(this)
                .setLogSwitch(BuildConfig.DEBUG)
    }
}
