package com.darkhorse.httphelper.interceptor

import android.text.TextUtils
import com.darkhorse.baseframe.http.BaseAPI
import com.darkhorse.baseframe.utils.SPUtils
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by DarkHorse on 2018/2/4.
 */

class MulUrlInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        val headers = request.headers(BaseAPI.HEADER_URL)
        if (headers.isNotEmpty()) {
            builder.removeHeader(BaseAPI.HEADER_URL)
            val url = SPUtils.get(headers[0],"")
            if (url.isNotEmpty()) {
                val newUrl = url.toHttpUrlOrNull()
                if(newUrl!=null){
                    val oldUrl = request.url
                    val fullUrl = oldUrl
                            .newBuilder()
                            .scheme(newUrl.scheme)
                            .host(newUrl.host)
                            .port(newUrl.port)
                            .build()
                    return chain.proceed(builder.url(fullUrl).build())
                }
            }
        }
        return chain.proceed(builder.build())
    }
}
