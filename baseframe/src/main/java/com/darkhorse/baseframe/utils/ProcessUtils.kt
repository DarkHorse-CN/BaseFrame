package com.darkhorse.baseframe.utils

import android.app.ActivityManager
import android.content.Context


object ProcessUtils {

    /**
     * 获取进程ID
     */
    fun getProcessId() = android.os.Process.myPid()

    /**
     * 判断是否在主进程
     */
    fun isMainProcess(context: Context): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        val processInfos = am!!.runningAppProcesses
        val mainProcessName: String = context.getPackageName()
        val myPid = getProcessId()
        for (info in processInfos) {
            if (info.pid == myPid && mainProcessName == info.processName) {
                return true
            }
        }
        return false
    }
}