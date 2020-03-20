package com.darkhorse.baseframe.extension

import android.util.Log
import android.widget.Toast
import com.darkhorse.baseframe.aspectj.annotation.SingleClick
import com.darkhorse.baseframe.utils.AppManager
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser


/**
 * 弹框信息
 */
fun toast(msg: Any?, duration: Int = Toast.LENGTH_SHORT) {
    if (msg == null) {
        Toast.makeText(AppManager.mApplication, "Toast content is null!", duration).show()
        logI("Toast content is null!")
        return
    }
    Toast.makeText(AppManager.mApplication, msg.toString(), Toast.LENGTH_SHORT).show()
}

/**
 * 获取打印信息所在方法名，行号等信息
 *
 * @return
 */
private fun getAutoJumpLogInfo(): Array<String> {
    val infos = arrayOf("", "")
    val elements = Thread.currentThread().stackTrace
    for (i in elements.indices) {
        val element = elements[i]
        if ("LogExtension.kt" == element.fileName) {
            val fileName = elements[i + 2].fileName
            val line = elements[i + 2].lineNumber
            infos[0] = "$fileName($line)"
            return infos
        }
    }
    return infos
}

fun logV(msg: Any?) {
    if (AppManager.isDebug()) {
        val infos = getAutoJumpLogInfo()
        Log.v(infos[0], infos[1] + msg.toString())
    }
}

fun logI(msg: Any?) {
    val infos = getAutoJumpLogInfo()
    Log.i(infos[0], infos[1] + msg.toString())
}

fun logD(msg: Any?) {
    if (AppManager.isDebug()) {
        val infos = getAutoJumpLogInfo()
        Log.d(infos[0], infos[1] + msg.toString())
    }
}

fun logW(msg: Any?) {
    val infos = getAutoJumpLogInfo()
    Log.w(infos[0], infos[1] + msg.toString())
}

fun logE(msg: Any?) {
    val infos = getAutoJumpLogInfo()
    Log.e(infos[0], infos[1] + msg.toString())
}

fun logJson(msg: Any?) {
    val gson = GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create()
    val json = gson.toJson(msg)
    val infos = getAutoJumpLogInfo()
    Log.i(infos[0], infos[1] + msg.toString())
}
