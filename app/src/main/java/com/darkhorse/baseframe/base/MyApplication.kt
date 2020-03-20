package com.darkhorse.baseframe.base

import com.darkhorse.baseframe.constant.GlobalVal
import com.darkhorse.baseframe.extension.logE
import com.darkhorse.baseframe.extension.logI
import com.darkhorse.baseframe.extension.toast
import com.darkhorse.baseframe.okhttp.HttpHelper
import com.darkhorse.baseframe.utils.AppManager
import com.darkhorse.baseframe.utils.NetUtils
import com.darkhorse.httphelper.interfaces.INetWorkCheckListener
import com.example.httphelper.retrofit.API
import com.example.httphelper.retrofit.MyConverter
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Description:
 * Created by DarkHorse on 2018/5/21.
 */
class MyApplication : BaseApplication() {
    override fun initUtils() {
        //初始化全局管理器
        AppManager.init(this)


        AppManager.startLogcatCatch(7, GlobalVal.DIR_LOGCAT,"logcat | grep \"(${AppManager.getProcessId()})\"")

        //初始化Http请求辅助类
//                .supportNetworkCheck()
        HttpHelper.addBaseUrl("http://www.baidu.com")
                .supportMulBaseUrl()
                .supportNetworkCheck(object : INetWorkCheckListener {
                    override fun netWorkError() {
                        AppManager.curActivity().runOnUiThread {
                            toast("网络异常,请检查网络配置")
                        }
                    }
                })
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .setConvert(MyConverter())
                .init()

        API.setNewsUrl("http://v.juhe.cn")

        NetUtils.registerNetworkStatusChangedListener(object : NetUtils.OnNetworkStatusChangedListener {
            override fun onConnected(networkType: NetUtils.NetworkType?) {
                logI("$networkType 网络已连接")
            }

            override fun onDisconnected() {
                logI("网络已断开")
            }
        })
    }

    override fun onCreate() {
        super.onCreate()

    }
}
