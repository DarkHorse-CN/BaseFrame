package com.darkhorse.httphelper.interfaces

/**
 * Description:
 * Created by DarkHorse on 2018/6/13.
 */
interface IDoubleTokenListener {
    fun getShortTokenKey(): String

    fun getLongTokenKey(): String

    fun getShortToken(): String

    fun getLongToken(): String

    fun isShortTokenExpire(result: String): Boolean

    fun refreshShortToken()
}