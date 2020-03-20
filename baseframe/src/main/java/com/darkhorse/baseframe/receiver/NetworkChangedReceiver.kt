package com.darkhorse.baseframe.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.darkhorse.baseframe.extension.logE
import com.darkhorse.baseframe.utils.AppManager
import com.darkhorse.baseframe.utils.NetUtils
import me.jessyan.autosize.utils.LogUtils

object NetworkChangedReceiver : BroadcastReceiver() {
    private var mType: NetUtils.NetworkType? = null
    private val mListeners: MutableSet<NetUtils.OnNetworkStatusChangedListener> = HashSet()

    @SuppressLint("MissingPermission")
    fun registerListener(listener: NetUtils.OnNetworkStatusChangedListener?) {
        if (listener == null) return
        val preSize = mListeners.size
        mListeners.add(listener)
        if (preSize == 0 && mListeners.size == 1) {
            mType = NetUtils.getNetworkType()
            val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            AppManager.mApplication.registerReceiver(this, intentFilter)
        }
    }

    fun unregisterListener(listener: NetUtils.OnNetworkStatusChangedListener?) {
        if (listener == null) return
        val preSize = mListeners.size
        mListeners.remove(listener)
        if (preSize == 1 && mListeners.size == 0) {
            AppManager.mApplication.unregisterReceiver(this)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action) { // debouncing
            val networkType: NetUtils.NetworkType? = NetUtils.getNetworkType()
            if (mType == networkType) return
            logE(networkType)
            mType = networkType
            if (networkType == NetUtils.NetworkType.NETWORK_NO) {
                for (listener in mListeners) {
                    listener.onDisconnected()
                }
            } else {
                for (listener in mListeners) {
                    listener.onConnected(networkType)
                }
            }
        }
    }

}