package com.darkhorse.baseframe.extension

import android.util.Log
import android.widget.Toast
import com.darkhorse.baseframe.utils.AppManager
import com.darkhorse.baseframe.utils.ToastUtil
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser


fun toast(msg: Any, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(AppManager.curActivity(), msg.toString(), duration).show()
}

fun toast(msg: Any?) {
    if (msg == null) {
//        Toast.makeText(AppManager.curActivity(), "Toast content is null!", Toast.LENGTH_SHORT).show()
        ToastUtil.showTextToas(AppManager.curActivity(), "Toast content is null!".toString())
        return
    }
//    Toast.makeText(AppManager.curActivity(), msg.toString(), Toast.LENGTH_SHORT).show()
    ToastUtil.showTextToas(AppManager.curActivity(), msg.toString())
}


/**
 * 获取打印信息所在方法名，行号等信息
 *
 * @return
 */
private fun getAutoJumpLogInfos(): Array<String> {
    val infos = arrayOf("", "")
    val elements = Thread.currentThread().stackTrace
    for (i in elements.indices) {
        val element = elements[i]
        if ("LogExtension.kt" == element.fileName) {
            val fileName = elements[i + 1].fileName
            val line = elements[i + 1].lineNumber
            infos[0] = "$fileName($line)"
            return infos
        }
    }
    return infos
}

fun v(msg: Any) {
    if (AppManager.isDebug()) {
        val infos = getAutoJumpLogInfos()
        Log.v(infos[0], infos[1] + msg.toString())
    }
}

fun i(msg: Any) {
    val infos = getAutoJumpLogInfos()
    Log.i(infos[0], infos[1] + msg.toString())
}

fun d(msg: Any) {
    if (AppManager.isDebug()) {
        val infos = getAutoJumpLogInfos()
        Log.d(infos[0], infos[1] + msg.toString())
    }
}

fun w(msg: Any) {
    val infos = getAutoJumpLogInfos()
    Log.w(infos[0], infos[1] + msg.toString())
}

fun e(msg: Any) {
    val infos = getAutoJumpLogInfos()
    Log.e(infos[0], infos[1] + msg.toString())
}

fun json(msg: Any) {
    val gson = GsonBuilder().setPrettyPrinting().create()
    val json = gson.toJson(msg)
    val jsonParser = JsonParser()
    val jsonElement = jsonParser.parse(json)
    i(gson.toJson(jsonElement))
}
