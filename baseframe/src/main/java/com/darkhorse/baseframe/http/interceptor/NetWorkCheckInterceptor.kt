package com.darkhorse.baseframe.http.interceptor

import com.darkhorse.baseframe.utils.NetUtils
import com.darkhorse.httphelper.interfaces.INetWorkCheckListener
import okhttp3.Interceptor
import okhttp3.Response
import java.lang.Exception

/**
 * Description:
 * Created by DarkHorse on 2018/6/7.
 */
class NetWorkCheckInterceptor(private val iNetWorkCheckListener: INetWorkCheckListener) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!NetUtils.isAvailable()) {
            iNetWorkCheckListener.netWorkError()
            chain.call().cancel()
        }
        return chain.proceed(chain.request())
    }
}
