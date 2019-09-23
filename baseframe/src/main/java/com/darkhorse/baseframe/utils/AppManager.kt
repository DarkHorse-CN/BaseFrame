package com.darkhorse.baseframe.utils

import android.app.Activity
import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import java.util.*

/**
 * Description:
 * Created by DarkHorse on 2018/6/8.
 */
object AppManager : LifecycleObserver {
    private var isExit = false
    private var mTimer = Timer()

    private val mActivityStack: Stack<Activity> by lazy {
        Stack<Activity>()
    }

    var mApplication: Application? = null

    fun init(application: Application) {
        mApplication = application
    }

    /**
     * 添加Activity
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun addActivity(owner: LifecycleOwner) {
        mActivityStack.push(owner as Activity)
    }

    /**
     * 移除Activity
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun removeActivity(owner: LifecycleOwner) {
        mActivityStack.remove(owner as Activity)
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
    fun currentActivity(): Activity? =
            if (mActivityStack.size > 0) {
                mActivityStack.peek()
            } else {
                null
            }

    fun context(): Context = if (currentActivity() == null) {
        mApplication as Context
    } else {
        currentActivity() as Context
    }

    /**
     * 启动Activity
     */
    fun startActivity(activity: Activity, clz: Class<out Activity>, bundle: Bundle? = null, isFinished: Boolean = false) {
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
    fun startActivityForResult(activity: Activity, clz: Class<out Activity>, requestCode: Int, bundle: Bundle? = null) {
        val intent = Intent(activity, clz)
        if (bundle != null) {
            intent.putExtra("data", bundle)
        }
        activity.startActivityForResult(intent, requestCode)
    }
}
