package com.darkhorse.baseframe.utils

import com.google.gson.Gson

import java.lang.reflect.Type

object JsonUtils {

    private val mGson: Gson by lazy {
        Gson()
    }

    fun toJson(value: Any): String {
        return mGson.toJson(value)
    }

    fun <T> fromJson(json: String, classOfT: Class<T>): T? {
        return mGson.fromJson(json, classOfT)
    }

    fun fromJson(json: String, type: Type): Any? {
        return mGson.fromJson<Any>(json, type)
    }
}
