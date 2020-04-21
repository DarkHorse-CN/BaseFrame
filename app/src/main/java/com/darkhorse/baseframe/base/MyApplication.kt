package com.darkhorse.baseframe.base

import com.alibaba.android.arouter.launcher.ARouter
import com.darkhorse.baseframe.constant.GlobalVal
import com.darkhorse.baseframe.extension.logE
import com.darkhorse.baseframe.extension.logI
import com.darkhorse.baseframe.extension.toast
import com.darkhorse.baseframe.okhttp.HttpHelper
import com.darkhorse.baseframe.utils.AppManager
import com.darkhorse.baseframe.utils.NetUtils
import com.darkhorse.baseframe.utils.ProcessUtils
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
        //启动日志捕获线程
        AppManager.startLogcatCatch(7, GlobalVal.DIR_LOGCAT, "logcat | grep \"(${ProcessUtils.getProcessId()})\"")

        //初始化网络请求工具类
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

        //网络监听工具
        NetUtils.registerNetworkStatusChangedListener(object : NetUtils.OnNetworkStatusChangedListener {
            override fun onConnected(networkType: NetUtils.NetworkType?) {
                logI("$networkType 网络已连接")
            }

            override fun onDisconnected() {
                logI("网络已断开")
            }
        })

        //初始化路由
        ARouter.init(this)
        if (AppManager.isDebug()) {
            ARouter.openLog();
            ARouter.openDebug()
        }
    }
}
