package com.darkhorse.baseframe.service

import android.app.Notification
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.darkhorse.baseframe.DeamonProcess


import com.darkhorse.baseframe.extension.logI

class GuardService : Service() {

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            logI("与DaemonService 建立连接")
        }

        override fun onServiceDisconnected(name: ComponentName) {
            logI("与DaemonService 断开连接")

            //重新启动DaemonService
            startService(Intent(this@GuardService, DaemonService::class.java))
            bindService(
                Intent(this@GuardService, DaemonService::class.java),
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

        //绑定DaemonService服务
        bindService(
            Intent(this, DaemonService::class.java),
            mServiceConnection, Context.BIND_IMPORTANT
        )
        return Service.START_STICKY
    }
}
