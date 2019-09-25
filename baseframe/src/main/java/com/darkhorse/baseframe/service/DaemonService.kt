package com.darkhorse.baseframe.service

import android.app.Notification
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.darkhorse.baseframe.DeamonProcess
import com.darkhorse.baseframe.extension.i
import com.darkhorse.baseframe.utils.AppManager

/**
 * 守护进程服务
 *
 * @author DarkHorse
 */
class DaemonService : Service() {

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            i("与GuardService 建立连接")
        }

        override fun onServiceDisconnected(name: ComponentName) {
            i("与GuardService 断开连接")

            //重新启动GuardService
            startService(Intent(this@DaemonService, GuardService::class.java))
            bindService(
                Intent(this@DaemonService, GuardService::class.java),
                this, Context.BIND_IMPORTANT
            )
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return object : DeamonProcess.Stub() {
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startForeground(1, Notification())

        if (AppManager.getActivityStack().size <= 0) {
            AppManager.startLaunchActivity()
        }

        //绑定GuardService服务
        bindService(
            Intent(this, GuardService::class.java),
            mServiceConnection, Context.BIND_IMPORTANT
        )
        return Service.START_STICKY
    }

}
