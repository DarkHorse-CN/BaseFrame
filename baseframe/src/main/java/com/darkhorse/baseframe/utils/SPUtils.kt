package com.darkhorse.baseframe.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object SPUtils {

    /**
     * 保存在手机的文件名称，路径：Path = /data/data/<package name>/shared_prefs
    </package> */
    lateinit var mSharedPreferences: SharedPreferences

    fun init(context: Context, preferencesName: String) {
        mSharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
    }

    /**
     * 保存数据的方法，根据传入数据参数的类型使用不同的保存方法
     * @param key
     * @param valueObject
     */
    fun put(key: String, valueObject: Any) {
        try {
            val editor = mSharedPreferences.edit()
            when (valueObject) {
                is String -> editor.putString(key, valueObject)
                is Int -> editor.putInt(key, valueObject)
                is Boolean -> editor.putBoolean(key, valueObject)
                is Float -> editor.putFloat(key, valueObject)
                is Long -> editor.putLong(key, valueObject)
                else -> editor.putString(key, Gson().toJson(valueObject))
            }
            editor.apply()
        } catch (e: UninitializedPropertyAccessException) {
            e.printStackTrace()
        }
    }

    /**
     * 获取存储在SharedPreferences里的数据，根据默认数据类型调用不同的方法获取数据
     * @param key
     * @param defaultObject
     * @return
     */
    inline fun <reified T> get(key: String, defaultObject: T): T = when (defaultObject) {
        is String -> mSharedPreferences.getString(key, defaultObject as String) as T
        is Int -> mSharedPreferences.getInt(key, (defaultObject as Int)) as T
        is Boolean -> mSharedPreferences.getBoolean(key, (defaultObject as Boolean)) as T
        is Float -> mSharedPreferences.getFloat(key, (defaultObject as Float)) as T
        is Long -> mSharedPreferences.getLong(key, (defaultObject as Long)) as T
        is List<*> -> {
            val json = mSharedPreferences.getString(key, "")
            if (json != "") {
                Gson().fromJson<T>(json, object : TypeToken<List<*>>() {
                }.type)
            } else {
                defaultObject
            }
        }
        is Set<*> -> {
            val json = mSharedPreferences.getString(key, "")
            if (json != "") {
                Gson().fromJson<T>(json, object : TypeToken<Set<*>>() {
                }.type)
            } else {
                defaultObject
            }
        }
        else -> {
            val json = mSharedPreferences.getString(key, "")
            if (json != "") {
                Gson().fromJson<T>(json, T::class.java)
            } else {
                defaultObject
            }
        }
    }

    /**
     * 兼容Java
     */
    fun getString(key: String, default: String): String = mSharedPreferences.getString(key, default) ?: ""
    fun getInt(key: String, default: Int): Int = mSharedPreferences.getInt(key, default)
    fun getFloat(key: String, default: Float): Float = mSharedPreferences.getFloat(key, default)
    fun getBoolean(key: String, default: Boolean): Boolean = mSharedPreferences.getBoolean(key, default)
    fun getLong(key: String, default: Long): Long = mSharedPreferences.getLong(key, default)

    /**
     * 查询SharedPreferences是否含有某个key值
     * @param key
     * @return
     */
    fun contains(key: String): Boolean {
        var isContains = false
        try {

            isContains = mSharedPreferences.contains(key)
        } catch (e: UninitializedPropertyAccessException) {
            e.printStackTrace()
        }
        return isContains
    }

    /**
     * 获取SharedPreferences里所有的键值对
     * @return
     */
    fun getAll(): Map<String, *>? {
        return mSharedPreferences.all
    }

    /**
     * 移除某个key值对
     * @param context
     * @param key
     */
    fun remove(key: String) {
        val editor = mSharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    /**
     * 清空SharedPreferences里的所有数据
     * @param context
     */
    fun clear() {
        val editor = mSharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
