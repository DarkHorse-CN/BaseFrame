package com.example.baseframe.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.darkhorse.baseframe.BaseActivity
import com.darkhorse.baseframe.R
import java.util.*

/**
 * Description:
 * Created by DarkHorse on 2018/6/8.
 */
object AppManager {
    var isExit = false
    var mTimer = Timer()

    val mActivityStack: Stack<BaseActivity> by lazy {
        Stack<BaseActivity>()
    }

    /**
     * 添加Activity
     */
    fun addActivity(activity: BaseActivity) {
        mActivityStack.push(activity)
    }

    /**
     * 关闭指定Activity
     */
    fun finishActivity(activity: BaseActivity) {
        mActivityStack.remove(activity)
        activity.finish()
    }

    /**
     * 关闭当前Activity
     */
    fun finish() {
        mActivityStack.pop().finish()
    }

    /**
     * 退出APP并关闭所有Activity
     */
    fun exit() {
        val currentTime = TimeUtils.currentTime()
        if (isExit) {
            AppManager.exitNow()
        } else {
            isExit = true
            toast(AppManager.currentActivity().getString(R.string.base_hint_exit_app))
            mTimer.schedule(object : TimerTask() {
                override fun run() {
                    isExit = false
                }
            }, 3000)
        }
    }

    /**
     * 直接退出APP
     */
    fun exitNow() {
        for (activity in mActivityStack) {
            finishActivity(activity)
        }
    }

    /**
     * 获取当前Activity
     */
    fun currentActivity() = mActivityStack.peek()!!

    /**
     * 启动Activity
     */
    fun startActivity(clz: Class<out Activity>, bundle: Bundle?, isFinished: Boolean) {
        val activity = currentActivity()
        val intent = Intent(activity, clz)
        if (bundle != null) {
            intent.putExtra("data", bundle)
        }
        activity.startActivity(intent)
        if (isFinished) {
            activity.finish()
        }
    }

    /**
     * 启动ActivityForResult
     */
    fun startActivityForResult(clz: Class<out Activity>, requestCode: Int, bundle: Bundle?) {
        val activity = currentActivity()
        val intent = Intent(activity, clz)
        if (bundle != null) {
            intent.putExtra("data", bundle)
        }
        activity.startActivityForResult(intent, requestCode)
    }
}
