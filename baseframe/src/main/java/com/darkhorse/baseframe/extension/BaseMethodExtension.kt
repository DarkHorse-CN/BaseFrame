package com.example.baseframe.utils

import android.widget.Toast
import com.blankj.ALog

fun toast(msg: String) {
    Toast.makeText(AppManager.currentActivity(), msg, Toast.LENGTH_SHORT).show()
}

fun a(msg: String) {
    ALog.a(msg)
}

fun d(msg: String) {
    ALog.d(msg)
}

fun e(msg: String) {
    ALog.e(msg)
}

fun i(msg: String) {
    ALog.i(msg)
}


fun v(msg: String) {
    ALog.v(msg)
}

fun w(msg: String) {
    ALog.w(msg)
}

fun json(msg: String) {
    ALog.json(msg)
}

fun xml(msg: String) {
    ALog.xml(msg)
}
