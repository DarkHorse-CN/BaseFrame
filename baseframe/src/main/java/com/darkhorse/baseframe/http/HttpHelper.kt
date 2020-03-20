package com.darkhorse.baseframe.okhttp

import com.darkhorse.baseframe.http.interceptor.*
import com.darkhorse.baseframe.http.minterface.ILanguageListener
import com.darkhorse.httphelper.client.HttpClient
import com.darkhorse.httphelper.client.RetrofitClient
import com.darkhorse.httphelper.converter.BaseConvert
import com.darkhorse.httphelper.interceptor.MulUrlInterceptor
import com.darkhorse.httphelper.interfaces.IDoubleTokenListener
import com.darkhorse.httphelper.interfaces.INetWorkCheckListener
import com.darkhorse.httphelper.interfaces.ISingleTokenListener
import okhttp3.Interceptor
import java.util.*

object HttpHelper {

    private val mInterceptorList: ArrayList<Interceptor> = ArrayList()
    private var mMulUrlInterceptor: MulUrlInterceptor? = null
    private var mNetWorkCheckInterceptor: NetWorkCheckInterceptor? = null
    private var mLanguageInterceptor: LanguageInterceptor? = null
    private var mTokenInterceptor: TokenInterceptor? = null
    private var mDoubleTokenInterceptor: DoubleTokenInterceptor? = null
    private var mRetryInterceptor: RetryInterceptor? = null

    /**
     * 添加BaseUrl
     */
    fun addBaseUrl(url: String): HttpHelper {
        RetrofitClient.addBaseUrl(url)
        return this
    }

    /**
     * 通过添加@Header，实现多种BaseUrl的支持
     */
    fun supportMulBaseUrl(): HttpHelper {
        mMulUrlInterceptor = MulUrlInterceptor()
        return this
    }

    /**
     * 添加国际化请求机制
     */
    fun supportLanguage(iLanguageListener: ILanguageListener): HttpHelper {
        if (mLanguageInterceptor == null) {
            mLanguageInterceptor = LanguageInterceptor(iLanguageListener)
        }
        return this
    }

    /**
     * 添加单Token请求机制
     */
    fun supportSingleToken(iSingleTokenListener: ISingleTokenListener): HttpHelper {
        mTokenInterceptor = TokenInterceptor(iSingleTokenListener)
        return this

    }

    /**
     * 添加双Token请求机制
     */
    fun supportDoubleToken(iDoubleTokenListener: IDoubleTokenListener): HttpHelper {
        mDoubleTokenInterceptor = DoubleTokenInterceptor(iDoubleTokenListener)
        return this
    }

    /**
     * 添加网络检查支持
     */
    fun supportNetworkCheck(iNetWorkCheckListener: INetWorkCheckListener): HttpHelper {
        mNetWorkCheckInterceptor = NetWorkCheckInterceptor(iNetWorkCheckListener)
        return this
    }

    /**
     * 添加网络重试支持
     */
    fun supportRetry(hint: String): HttpHelper {
        mRetryInterceptor = RetryInterceptor(hint)
        return this
    }

    /**
     * 添加Cookie支持
     */
//    fun addCookieAutoManager(): HttpHelper {
//        HttpClient.addCookieJar()
//        return this
//    }

    /**
     * 添加自定义转换器
     */
    fun setConvert(converter: BaseConvert): HttpHelper {
        RetrofitClient.addConverterFactory(converter)
        return this
    }

    /**
     * 添加拦截器
     */
    fun addInterceptor(interceptor: Interceptor): HttpHelper {
        mInterceptorList.add(interceptor)
        return this
    }

    /**
     * 设置超时时长
     */
    fun setTimeout(connectTimeout: Long, readTimeout: Long): HttpHelper {
        HttpClient.setTimeout(connectTimeout, readTimeout)
        return this
    }

    fun init() {
        if (mNetWorkCheckInterceptor != null) {
            HttpClient.addInterceptor(mNetWorkCheckInterceptor!!)
        }
        if (mMulUrlInterceptor != null) {
            HttpClient.addInterceptor(mMulUrlInterceptor!!)
        }
        if (mLanguageInterceptor != null) {
            HttpClient.addInterceptor(mLanguageInterceptor!!)
        }
        if (mTokenInterceptor != null) {
            HttpClient.addInterceptor(mTokenInterceptor!!)
        }
        if (mDoubleTokenInterceptor != null) {
            HttpClient.addInterceptor(mDoubleTokenInterceptor!!)
        }

        for (i in mInterceptorList) {
            HttpClient.addInterceptor(i)
        }

        if (mRetryInterceptor != null) {
            HttpClient.addInterceptor(mRetryInterceptor!!)
        }


        RetrofitClient.setClient(HttpClient.mOkHttpClient)
    }

    fun <T> getService(service: Class<T>): T {
        return RetrofitClient.mRetrofit.create(service)
    }
}
