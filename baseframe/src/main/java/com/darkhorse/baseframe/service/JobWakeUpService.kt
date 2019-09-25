package com.darkhorse.baseframe.service

import android.app.ActivityManager
import android.app.Service
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent


import com.darkhorse.baseframe.utils.AppManager

class JobWakeUpService : JobService() {
    private val jobWakeUpId = 1
    private var jobScheduler: JobScheduler? = null
    private var builder: JobInfo.Builder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        //        d("onStartCommand ");

        builder = JobInfo.Builder(jobWakeUpId, ComponentName(this, JobWakeUpService::class.java))
            .setMinimumLatency(100)
            .setPersisted(true)
        jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler!!.schedule(builder!!.build())
        return Service.START_STICKY
    }

    override fun onStartJob(params: JobParameters): Boolean {
        //        d("onStartJob");

        //判断服务有没有在运行
        val isGuardServiceAlive = isServiceRunning(GuardService::class.java.name)
        val isDaemonServiceAlive = isServiceRunning(DaemonService::class.java.name)

        if (!isGuardServiceAlive || !isDaemonServiceAlive) {
            startService(Intent(this, GuardService::class.java))
            startService(Intent(this, DaemonService::class.java))
        }
        AppManager.back2Foreground()

        jobScheduler!!.schedule(builder!!.build())
        return false
    }

    override fun onStopJob(params: JobParameters): Boolean {
        //        d("onStopJob");
        return true
    }

    /**
     * 判断某个服务是否在运行
     *
     * @param serviceName
     * @return
     */
    private fun isServiceRunning(serviceName: String): Boolean {
        var isWork = false
        val myAM = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val myList = myAM.getRunningServices(100)
        if (myList.size <= 0) {
            return false
        }
        for (i in myList.indices) {
            val mName = myList[i].service.className
            if (mName == serviceName) {
                isWork = true
                break
            }
        }
        return isWork
    }

}