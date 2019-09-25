package com.darkhorse.baseframe.utils

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.lang.reflect.Type
import java.util.ArrayList

object SPUtils {

    private val mSharedPreferences: SharedPreferences by lazy {
        AppManager.mApplication.getSharedPreferences(AppManager.getPackageName(), Context.MODE_PRIVATE)
    }
    private val mGson: Gson by lazy {
        Gson()
    }

    fun put(key: String, value: Any) {
        val editor = mSharedPreferences.edit()
        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Long -> editor.putLong(key, value)
            is Float -> editor.putFloat(key, value)
            else -> editor.putString(key, mGson.toJson(value))
        }
        editor.apply()
    }

    operator fun get(key: String, defaultvalue: String): String? {
        return mSharedPreferences.getString(key, defaultvalue)
    }

    operator fun get(key: String, defaultvalue: Int): Int {
        return mSharedPreferences.getInt(key, defaultvalue)
    }

    operator fun get(key: String, defaultvalue: Boolean): Boolean {
        return mSharedPreferences.getBoolean(key, defaultvalue)
    }

    operator fun get(key: String, defaultvalue: Float): Float {
        return mSharedPreferences.getFloat(key, defaultvalue)
    }


    operator fun get(key: String, defaultvalue: Long): Long {
        return mSharedPreferences.getLong(key, defaultvalue)
    }

    operator fun <T> get(key: String, list: List<T>): List<T> {
        val json = get(key, "")
        if (TextUtils.isEmpty(json)) {
            return list
        }
        val gson = Gson()
        return gson.fromJson(json, object : TypeToken<ArrayList<T>>() {

        }.type)
    }

    operator fun <T> get(key: String, set: Set<T>): Set<T> {
        val json = get(key, "")
        if (TextUtils.isEmpty(json)) {
            return set
        }
        val gson = Gson()
        return gson.fromJson(json, object : TypeToken<ArrayList<T>>() {

        }.type)
    }

    operator fun <T : Any> get(key: String, t: T): T {
        val json = get(key, "")
        if (TextUtils.isEmpty(json)) {
            return t
        }
        val gson = Gson()
        return gson.fromJson(json, t.javaClass as Type)
    }

    fun remove(key: String) {
        val editor = mSharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }
}
