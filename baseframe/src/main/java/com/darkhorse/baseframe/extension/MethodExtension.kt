package com.darkhorse.baseframe.extension

import android.util.Log
import android.widget.Toast
import com.darkhorse.baseframe.utils.AppManager

fun toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(AppManager.currentActivity(), msg, duration).show()
}

fun d(msg: String) {
    Log.d(AppManager.currentActivity().javaClass.name, msg)
}

fun e(msg: String) {
    Log.e(AppManager.currentActivity().javaClass.name, msg)
}

fun i(msg: String) {
    Log.i(AppManager.currentActivity().javaClass.name, msg)
}


fun v(msg: String) {
    Log.v(AppManager.currentActivity().javaClass.name, msg)
}

fun w(msg: String) {
    Log.w(AppManager.currentActivity().javaClass.name, msg)
}

