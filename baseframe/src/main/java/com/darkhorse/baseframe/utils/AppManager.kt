package com.darkhorse.baseframe.utils

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.darkhorse.baseframe.BaseActivity
import java.util.*

/**
 * Description:
 * Created by DarkHorse on 2018/6/8.
 */
object AppManager : LifecycleObserver {
    private var isExit = false
    private var mTimer = Timer()

    private val mActivityStack: Stack<BaseActivity> by lazy {
        Stack<BaseActivity>()
    }

    var mApplication: Application? = null

    fun init(application: Application) {
        mApplication = application
    }

    /**
     * 添加Activity
     */
    fun addActivity(activity: BaseActivity) {
        mActivityStack.push(activity)
    }

    /**
     * 移除Activity
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun removeActivity(owner: LifecycleOwner) {
        mActivityStack.remove(owner as BaseActivity)
    }

    /**
     * 关闭指定Activity
     */
    fun finishActivity(activity: BaseActivity) {
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
    fun currentActivity(): Activity = mActivityStack.peek()

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
