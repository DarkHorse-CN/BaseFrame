package com.darkhorse.baseframe.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.darkhorse.baseframe.BuildConfig
import com.darkhorse.baseframe.base.BaseApplication
import com.darkhorse.baseframe.extension.d
import com.darkhorse.baseframe.extension.e
import com.darkhorse.baseframe.extension.toast
import com.darkhorse.baseframe.service.DaemonService
import com.darkhorse.baseframe.service.GuardService
import com.darkhorse.baseframe.service.JobWakeUpService
import java.io.File
import java.io.IOException
import java.util.*


/**
 * Description:
 * Created by DarkHorse on 2018/6/8.
 */
object AppManager : LifecycleObserver {
    private var exitTime = 0L

    private val mActivityStack: Stack<Activity> by lazy {
        Stack<Activity>()
    }

    lateinit var mApplication: BaseApplication

    fun init(application: BaseApplication): AppManager {
        mApplication = application
        return this;
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
        val time = System.currentTimeMillis()
        if (exitTime > time - delay) {
            exitNow()
        } else {
            toast(hint)
            exitTime = time
        }
    }

    /**
     * 直接退出APP
     */
    @JvmStatic
    fun exitNow() {
        for (activity in mActivityStack) {
            finishActivity(activity)
        }
    }

    /**
     * 强制退出APP
     */
    @JvmStatic
    fun forceExit() {
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    /**
     * 完美退出APP，跳过保活
     */
    @JvmStatic
    fun completeExit() {
        val process = Runtime.getRuntime().exec("su")
        val out = process.outputStream;
        val cmd = "am force-stop " + getPackageName() + " \n";
        try {
            out.write(cmd.toByteArray());
            out.flush();
        } catch (e: IOException) {
            e.printStackTrace();
        }
    }


    /**
     * 重启APP
     */
    @JvmStatic
    fun restartApp() {
        forceExit()
        startLaunchActivity()
    }

    /**
     * 启动App
     */
    @JvmStatic
    fun startLaunchActivity() {
        d("  AppManager.startLaunchActivity()")

        val application = getApplication();
        val startIntent =
                application.packageManager.getLaunchIntentForPackage(application.packageName)
        if (startIntent != null) {
            startIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            application.startActivity(startIntent)
        }
    }

    /**
     * 获取当前Activity
     */
    @JvmStatic
    fun curActivity(): Activity = mActivityStack.peek()

    /**
     *  获取Context
     */
    @JvmStatic
    fun getApplication() = mApplication

    /**
     * 启动Activity
     */
    @JvmStatic
    fun startActivity(
            activity: Activity,
            clz: Class<out Activity>,
            bundle: Bundle? = null,
            isFinished: Boolean = false
    ) {
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
    fun startActivityForResult(
            activity: Activity,
            clz: Class<out Activity>,
            requestCode: Int,
            bundle: Bundle? = null
    ) {
        val intent = Intent(activity, clz)
        if (bundle != null) {
            intent.putExtra("data", bundle)
        }
        activity.startActivityForResult(intent, requestCode)
    }

    /**
     * 跳转浏览器
     */
    fun startBrowser(activity: Activity, url: String) {
        val intent = Intent()
                .setAction("android.intent.action.VIEW")
                .setData(Uri.parse(url))
        activity.startActivity(intent)
    }

    /**
     * 获取应用版本号
     */
    fun getVersionCode() = mApplication.packageManager.getPackageInfo(mApplication.packageName, 0)

    /**
     * 获取版本号名称
     */
    @JvmStatic
    fun getVersionName() = BuildConfig.VERSION_NAME

    /**
     * 获取包名
     */
    @JvmStatic
    fun getPackageName() = mApplication.packageName

    /**
     * 获取缓存目录路径
     */
    @JvmStatic
    fun getCacheDirPath(): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            mApplication.externalCacheDir?.path + File.separator
        } else {
            mApplication.cacheDir.path + finish().toString()
        }
    }

    /**
     * 是否调试模式
     */
    @JvmStatic
    fun isDebug() = BuildConfig.DEBUG

    /**
     * 开启守护进程服务
     */
    fun startDaemonService(): AppManager {
        //双进程保护
        mApplication.startService(Intent(mApplication, DaemonService::class.java))
        mApplication.startService(Intent(mApplication, GuardService::class.java))

        return this;
    }

    /**
     * 开启程序前台唤醒
     */
    fun startWakeUpService(): AppManager {
        mApplication.startService(Intent(mApplication, JobWakeUpService::class.java))
        return this
    }

    /**
     * 开启奔溃日志捕捉
     */
    fun startCrashCatch(deleteDay: Int): AppManager {
        Thread.setDefaultUncaughtExceptionHandler(CrashCatchHelper(mApplication, deleteDay))
        return this
    }

    /**
     * 开始Logcat日志捕捉
     */
    fun startLogcatCatch(deleteDay: Int, path: String): AppManager {
        LogcatCatchHelper.getInstance().start(deleteDay, path)
        return this
    }

    /**
     * 获取应用Activity栈
     */
    @JvmStatic
    fun getActivityStack(): Stack<Activity> {
        return mActivityStack;
    }

    /**
     * 判断APP是否在前台
     */
    @JvmStatic
    fun isAppForeground(): Boolean {
        val activityManager = getSystemService(Service.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcessInfoList = activityManager.runningAppProcesses;
        if (runningAppProcessInfoList == null) {
            e("runningAppProcessInfoList is null")
            return false
        }
        for (runningAppProcessInfo in runningAppProcessInfoList) {
            if (runningAppProcessInfo.processName == getPackageName() && runningAppProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                return true
            }
        }
        return false
    }

    /**
     * 切换APP到前台
     */
    @SuppressLint("MissingPermission", "NewApi")
    @JvmStatic
    fun back2Foreground() {
        if (!isAppForeground()) {
            val activityManager = getSystemService(Service.ACTIVITY_SERVICE) as ActivityManager
            val taskInfoList = activityManager.getRunningTasks(100)
            for (taskInfo in taskInfoList) {
                if (getPackageName() == taskInfo.topActivity?.packageName) {
                    activityManager.moveTaskToFront(
                            taskInfo.id,
                            ActivityManager.MOVE_TASK_WITH_HOME
                    );
                    return;
                }
            }
        }
    }

    /**
     * 获取服务
     */
    fun getSystemService(name: String): Any? = mApplication.getSystemService(name)

}
