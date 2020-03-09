package com.darkhorse.httphelper.interfaces

/**
 * Description:
 * Created by DarkHorse on 2018/7/23.
 */
interface ISingleTokenListener {
    fun getTokenKey(): String

    fun getToken(): String
}