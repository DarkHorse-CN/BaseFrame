package com.darkhorse.httphelper.client

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Created by DarkHorse on 2018/2/4.
 */

object HttpClient {

    private val mBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

    var mOkHttpClient: OkHttpClient = mBuilder.build()

    fun addInterceptor(interceptor: Interceptor): HttpClient {
        mBuilder.addInterceptor(interceptor)
        return this
    }

    fun setTimeout(connectTimeout: Long, readTimeout: Long): HttpClient {
        mBuilder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
        mBuilder.readTimeout(readTimeout, TimeUnit.MILLISECONDS)
        return this
    }

    fun addCookieJar(): HttpClient {
//        mBuilder.cookieJar(object : CookieJar {
//            @SuppressLint("DefaultLocale")
//            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
//                val json = Gson().toJson(cookies)
//                HttpSpUtils.put(url.host().toLowerCase(), json)
//            }
//
//            override fun loadForRequest(url: HttpUrl): List<Cookie>? {
//                val json = HttpSpUtils[url.host(), ""]
//                return null
//            }
//        })
        return this
    }

}
