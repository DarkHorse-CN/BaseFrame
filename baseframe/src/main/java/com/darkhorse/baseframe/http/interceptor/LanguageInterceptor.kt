package com.darkhorse.baseframe.http.interceptor

import com.darkhorse.baseframe.http.minterface.ILanguageListener
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Description:
 * Created by DarkHorse on 2018/5/16.
 */
class LanguageInterceptor(private val iInternational: ILanguageListener) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        builder.addHeader(iInternational.getLanguageKey(), iInternational.getLanguage())
        return chain.proceed(builder.build())
    }
}
