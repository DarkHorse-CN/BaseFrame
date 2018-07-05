package com.darkhorse.baseframe.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import java.util.*

/**
 * Description:
 * Created by DarkHorse on 2018/6/8.
 */
object AppManager {
    private var isExit = false
    private var mTimer = Timer()

    private val mActivityStack: Stack<Activity> by lazy {
        Stack<Activity>()
    }

    /**
     * 添加Activity
     */
    fun addActivity(activity: Activity) {
        mActivityStack.push(activity)
    }

    /**
     * 移除Activity
     */
    fun removeActivity(activity: Activity) {
        mActivityStack.remove(activity)
    }

    /**
     * 关闭指定Activity
     */
    fun finishActivity(activity: Activity) {
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
    fun appExit(hint: String, delay: Long) {
        if (isExit) {
            exitNow()
        } else {
            isExit = true
            Toast.makeText(currentActivity(), hint, Toast.LENGTH_SHORT).show()
            mTimer.schedule(object : TimerTask() {
                override fun run() {
                    isExit = false
                }
            }, delay)
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
    fun startActivity(clz: Class<out Activity>, bundle: Bundle? = null, isFinished: Boolean = false) {
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
    fun startActivityForResult(clz: Class<out Activity>, requestCode: Int, bundle: Bundle? = null) {
        val activity = currentActivity()
        val intent = Intent(activity, clz)
        if (bundle != null) {
            intent.putExtra("data", bundle)
        }
        activity.startActivityForResult(intent, requestCode)
    }
}
