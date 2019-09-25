package com.darkhorse.baseframe.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.darkhorse.baseframe.utils.AppManager

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        //开启服务
        AppManager.startLaunchActivity()
    }
}