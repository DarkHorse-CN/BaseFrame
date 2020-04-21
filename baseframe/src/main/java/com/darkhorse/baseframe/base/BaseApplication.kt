package com.darkhorse.baseframe.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.darkhorse.baseframe.utils.AppManager
import com.darkhorse.baseframe.utils.ProcessUtils
import com.darkhorse.baseframe.utils.SPUtils
import java.util.*

/**
 * Description:
 * Created by DarkHorse on 2018/5/18.
 */
abstract class BaseApplication : MultiDexApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        if(ProcessUtils.isMainProcess(this)){
            //初始化全局管理器
            AppManager.init(this)
            SPUtils.init(this, AppManager.getPackageName())
            //初始化全局配置
            initUtils()
        }
    }

    abstract fun initUtils()


}
