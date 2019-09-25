package com.darkhorse.baseframe.base

import android.app.Application
import androidx.multidex.MultiDexApplication
import java.util.*

/**
 * Description:
 * Created by DarkHorse on 2018/5/18.
 */
abstract class BaseApplication : MultiDexApplication() {

    lateinit var mApplication:Application;

    override fun onCreate() {
        super.onCreate()
        mApplication = this;
        initUtils()
    }

    abstract fun initUtils()


}
