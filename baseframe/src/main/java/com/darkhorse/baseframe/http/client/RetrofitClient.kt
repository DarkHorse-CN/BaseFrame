package com.darkhorse.httphelper.client

import com.darkhorse.httphelper.converter.BaseConvert
import com.darkhorse.httphelper.converter.BaseConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

/**
 * Created by DarkHorse on 2018/2/4.
 */

object RetrofitClient {

    val mBuilder: Retrofit.Builder = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

    val mRetrofit: Retrofit
        get() = mBuilder.build()

    fun addBaseUrl(url: String): RetrofitClient {
        mBuilder.baseUrl(url)
        return this
    }

    fun addConverterFactory(converter: BaseConvert): RetrofitClient {
        mBuilder.addConverterFactory(BaseConverterFactory(converter))
        return this
    }

    fun setClient(okHttpClient: OkHttpClient): RetrofitClient {
        mBuilder.client(okHttpClient)
        return this
    }

}
