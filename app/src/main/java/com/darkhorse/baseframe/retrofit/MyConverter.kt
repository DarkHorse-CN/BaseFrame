package com.example.httphelper.retrofit

import com.darkhorse.baseframe.extension.logE
import com.darkhorse.baseframe.retrofit.BaseResponse
import com.darkhorse.httphelper.converter.BaseConvert
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.RequestBody
import okhttp3.ResponseBody

/**
 * Description:
 * Created by DarkHorse on 2018/5/14.
 */
class MyConverter : BaseConvert {

    override fun beanToRequest(gson: Gson, adapter: TypeAdapter<out Any>, bean: Any): RequestBody? {
        return null
    }

    override fun responseToBean(gson: Gson, adapter: TypeAdapter<out Any>, responseBody: ResponseBody): Any? {
        val originalBody = responseBody.string()
        return try {
            val bean = adapter.fromJson(originalBody)
            bean
        } catch (e: Exception) {
            BaseResponse(API.ERROR_STATUS,-1,e.toString(),"")
        }
    }
}
