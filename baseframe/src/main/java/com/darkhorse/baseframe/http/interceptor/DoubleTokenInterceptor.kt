package com.darkhorse.baseframe.http.interceptor

import com.darkhorse.httphelper.interfaces.IDoubleTokenListener
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.EOFException
import java.lang.Long
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException


/**
 * Description:
 * Created by DarkHorse on 2018/5/17.
 */
class DoubleTokenInterceptor(private val iDoubleTokenListener: IDoubleTokenListener) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val headers = request.headers(iDoubleTokenListener.getLongTokenKey())
        var response: Response;

        //判断是否刷新Token的请求
        if (headers.size <= 0) {
            val builder = request.newBuilder()
            builder.addHeader(iDoubleTokenListener.getShortTokenKey(), iDoubleTokenListener.getShortToken())
            response = chain.proceed(builder.build())

            val body = response.body!!
            val source = body.source()
            source.request(Long.MAX_VALUE)
            val buffer = source.buffer()

            var charset = UTF8
            val contentType = body.contentType()
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8)
                } catch (e: UnsupportedCharsetException) {
                    return response
                }
            }

            if (!isPlaintext(buffer)) {
                return response
            }

            val result = buffer.clone().readString(charset)

            //验证shortToken是否失效
            if (iDoubleTokenListener.isShortTokenExpire(result)) {
                synchronized(this) {
                    iDoubleTokenListener.refreshShortToken()
                }
                val newRequest = chain.request()
                        .newBuilder()
                        .addHeader(iDoubleTokenListener.getShortTokenKey(), iDoubleTokenListener.getShortToken())
                        .build()

                response = chain.proceed(newRequest)
            }
        } else {
            val builder = request.newBuilder()
            builder.removeHeader(iDoubleTokenListener.getLongTokenKey())
            builder.addHeader(iDoubleTokenListener.getLongTokenKey(), iDoubleTokenListener.getLongToken())
            response = chain.proceed(builder.build())
        }
        return response
    }

    private val UTF8 = Charset.forName("UTF-8")

    private fun isPlaintext(buffer: Buffer): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = (if (buffer.size < 64) buffer.size else 64).toLong()
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (e: EOFException) {
            return false // Truncated UTF-8 sequence.
        }

    }

}
