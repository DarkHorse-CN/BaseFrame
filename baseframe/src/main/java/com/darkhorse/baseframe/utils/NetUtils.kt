package com.darkhorse.baseframe.utils

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.TelephonyManager
import android.text.format.Formatter
import androidx.annotation.RequiresPermission
import com.darkhorse.baseframe.extension.logE
import com.darkhorse.baseframe.receiver.NetworkChangedReceiver
import java.lang.reflect.Method
import java.net.*
import java.util.*
import kotlin.collections.HashSet


object NetUtils {

    enum class NetworkType {
        NETWORK_ETHERNET, NETWORK_WIFI, NETWORK_4G, NETWORK_3G, NETWORK_2G, NETWORK_UNKNOWN, NETWORK_NO
    }

    /**
     * 判断网络是否连接
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    fun isConnected(): Boolean {
        val info = getActiveNetworkInfo()
        return info != null && info.isConnected
    }

    /**
     * 判断网络是否可用
     */
     fun isAvailable(): Boolean {
        val info = getActiveNetworkInfo()
        return info?.isAvailable ?: false
    }

    /**
     * 判断是否与目标ip可以ping通
     */
    @RequiresPermission(INTERNET)
    fun isAvailableByPing(ip: String): Boolean {
        val result: ShellUtils.CommandResult = ShellUtils.execCmd(String.format("ping -c 1 %s", ip), false)
        return result.result === 0
    }

    /**
     * 判断与目标DNS是否连通
     */
    @RequiresPermission(INTERNET)
    fun isAvailableByDns(domain: String): Boolean {
        val inetAddress: InetAddress?
        try {
            inetAddress = InetAddress.getByName(domain)
            return inetAddress != null
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 判断移动网络是否打开
     */
    fun getMobileDataEnabled(): Boolean {
        try {
            val tm = AppManager.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
                    ?: return false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return tm.isDataEnabled
            }
            @SuppressLint("PrivateApi")
            val getMobileDataEnabledMethod: Method = tm.javaClass.getDeclaredMethod("getDataEnabled")
            return getMobileDataEnabledMethod.invoke(tm) as Boolean
        } catch (exception: Exception) {
            logE(exception.message)
        }
        return false
    }

    /**
     * 判断是否移动网络
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    fun isMobileData(): Boolean {
        val info = getActiveNetworkInfo()
        return (null != info && info.isAvailable
                && info.type == ConnectivityManager.TYPE_MOBILE)
    }

    /**
     * 判断是否4G网络
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    fun is4G(): Boolean {
        val info = getActiveNetworkInfo()
        return (info != null && info.isAvailable
                && info.subtype == TelephonyManager.NETWORK_TYPE_LTE)
    }

    /**
     * 判断是否打开wifi
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    fun getWifiEnabled(): Boolean {
        @SuppressLint("WifiManagerLeak")
        val manager = AppManager.getSystemService(WIFI_SERVICE) as WifiManager?
                ?: return false
        return manager.isWifiEnabled
    }

    /**
     * wifi开关
     */
    @RequiresPermission(CHANGE_WIFI_STATE)
    fun setWifiEnabled(enabled: Boolean) {
        @SuppressLint("WifiManagerLeak")
        val manager = AppManager.getSystemService(WIFI_SERVICE) as WifiManager?
                ?: return
        if (enabled == manager.isWifiEnabled) return
        manager.isWifiEnabled = enabled
    }

    /**
     * 判断是否wifi数据
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    fun isWifiConnected(): Boolean {
        val cm = AppManager.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
                ?: return false
        val ni = cm.activeNetworkInfo
        return ni != null && ni.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * 获取移动网络运营商名称
     */
    fun getNetworkOperatorName(): String? {
        val tm = AppManager.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
                ?: return ""
        return tm.networkOperatorName
    }

    /**
     * 获取当前网络类型
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    fun getNetworkType(): NetworkType? {
        if (isEthernet()) {
            return NetworkType.NETWORK_ETHERNET
        }
        val info = getActiveNetworkInfo()
        return if (info != null && info.isAvailable) {
            if (info.type == ConnectivityManager.TYPE_WIFI) {
                NetworkType.NETWORK_WIFI
            } else if (info.type == ConnectivityManager.TYPE_MOBILE) {
                when (info.subtype) {
                    TelephonyManager.NETWORK_TYPE_GSM, TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> NetworkType.NETWORK_2G
                    TelephonyManager.NETWORK_TYPE_TD_SCDMA, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> NetworkType.NETWORK_3G
                    TelephonyManager.NETWORK_TYPE_IWLAN, TelephonyManager.NETWORK_TYPE_LTE -> NetworkType.NETWORK_4G
                    else -> {
                        val subtypeName = info.subtypeName
                        if (subtypeName.equals("TD-SCDMA", ignoreCase = true)
                                || subtypeName.equals("WCDMA", ignoreCase = true)
                                || subtypeName.equals("CDMA2000", ignoreCase = true)) {
                            NetworkType.NETWORK_3G
                        } else {
                            NetworkType.NETWORK_UNKNOWN
                        }
                    }
                }
            } else {
                NetworkType.NETWORK_UNKNOWN
            }
        } else NetworkType.NETWORK_NO
    }

    /**
     * 判断是否以太网
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    private fun isEthernet(): Boolean {
        val cm = AppManager.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
                ?: return false
        val info = cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET) ?: return false
        val state = info.state ?: return false
        return state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING
    }

    /**
     * 获取当前网络
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    private fun getActiveNetworkInfo(): NetworkInfo? {
        val cm = AppManager.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
                ?: return null
        return cm.activeNetworkInfo
    }

    /**
     * 获取网络ip地址
     */
    @RequiresPermission(INTERNET)
    fun getIPAddress(useIPv4: Boolean): String? {
        try {
            val nis = NetworkInterface.getNetworkInterfaces()
            val adds: LinkedList<InetAddress> = LinkedList()
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement()
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp || ni.isLoopback) continue
                val addresses = ni.inetAddresses
                while (addresses.hasMoreElements()) {
                    adds.addFirst(addresses.nextElement())
                }
            }
            for (add in adds) {
                if (!add.isLoopbackAddress) {
                    val hostAddress = add.hostAddress
                    val isIPv4 = hostAddress.indexOf(':') < 0
                    if (useIPv4) {
                        if (isIPv4) return hostAddress
                    } else {
                        if (!isIPv4) {
                            val index = hostAddress.indexOf('%')
                            return if (index < 0) hostAddress.toUpperCase() else hostAddress.substring(0, index).toUpperCase()
                        }
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 获取广播ip地址
     */
    fun getBroadcastIpAddress(): String? {
        try {
            val nis = NetworkInterface.getNetworkInterfaces()
            val adds: LinkedList<InetAddress> = LinkedList()
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement()
                if (!ni.isUp || ni.isLoopback) continue
                val ias: List<InterfaceAddress> = ni.interfaceAddresses
                var i = 0
                val size = ias.size
                while (i < size) {
                    val ia: InterfaceAddress = ias[i]
                    val broadcast: InetAddress = ia.getBroadcast()
                    if (broadcast != null) {
                        return broadcast.hostAddress
                    }
                    i++
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 获取域名ip地址
     */
    @RequiresPermission(INTERNET)
    fun getDomainAddress(domain: String?): String? {
        val inetAddress: InetAddress
        return try {
            inetAddress = InetAddress.getByName(domain)
            inetAddress.hostAddress
        } catch (e: UnknownHostException) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 获取wifi的IP的地址
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    fun getIpAddressByWifi(): String? {
        @SuppressLint("WifiManagerLeak") val wm = AppManager.getSystemService(WIFI_SERVICE) as WifiManager?
                ?: return ""
        return Formatter.formatIpAddress(wm.dhcpInfo.ipAddress)
    }

    /**
     * 根据 WiFi 获取网关 IP 地址
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    fun getGatewayByWifi(): String? {
        @SuppressLint("WifiManagerLeak") val wm = AppManager.getSystemService(WIFI_SERVICE) as WifiManager?
                ?: return ""
        return Formatter.formatIpAddress(wm.dhcpInfo.gateway)
    }

    /**
     * 根据 WiFi 获取子网掩码 IP 地址
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    fun getNetMaskByWifi(): String? {
        @SuppressLint("WifiManagerLeak") val wm = AppManager.getSystemService(WIFI_SERVICE) as WifiManager?
                ?: return ""
        return Formatter.formatIpAddress(wm.dhcpInfo.netmask)
    }

    /**
     * 根据 WiFi 获取服务端 IP 地址
     */
    @RequiresPermission(ACCESS_WIFI_STATE)
    fun getServerAddressByWifi(): String? {
        @SuppressLint("WifiManagerLeak") val wm = AppManager.getSystemService(WIFI_SERVICE) as WifiManager?
                ?: return ""
        return Formatter.formatIpAddress(wm.dhcpInfo.serverAddress)
    }


    /**
     * 注册网络监听器
     */
    fun registerNetworkStatusChangedListener(listener: OnNetworkStatusChangedListener?) {
        NetworkChangedReceiver.registerListener(listener)
    }

    /**
     * 取消网络监听器
     */
    fun unregisterNetworkStatusChangedListener(listener: OnNetworkStatusChangedListener?) {
        NetworkChangedReceiver.unregisterListener(listener)
    }


    interface OnNetworkStatusChangedListener {
        fun onConnected(networkType: NetworkType?)

        fun onDisconnected()
    }
}