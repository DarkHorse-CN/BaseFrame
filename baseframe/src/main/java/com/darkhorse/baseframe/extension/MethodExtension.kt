package com.darkhorse.baseframe.extension

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
import com.darkhorse.baseframe.BaseActivity
import com.darkhorse.baseframe.utils.AppManager

fun toast(msg: Any, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(AppManager.mApplication, msg.toString(), duration).show()
}

fun d(msg: Any) {
    Log.d(AppManager.currentActivity().javaClass.name, msg.toString())
}

fun e(msg: Any) {
    Log.e(AppManager.currentActivity().javaClass.name, msg.toString())
}

fun i(msg: Any) {
    Log.i(AppManager.currentActivity().javaClass.name, msg.toString())
}

fun v(msg: Any) {
    Log.v(AppManager.currentActivity().javaClass.name, msg.toString())
}

fun w(msg: Any) {
    Log.w(AppManager.currentActivity().javaClass.name, msg.toString())
}

fun Activity.startActivity(clz: Class<out Activity>, bundle: Bundle? = null, isFinished: Boolean = false) {
    AppManager.startActivity(clz, bundle, isFinished)
}

fun Activity.startActivityForResult(clz: Class<out Activity>, requestCode: Int, bundle: Bundle? = null) {
    AppManager.startActivityForResult(clz, requestCode, bundle)
}

fun Fragment.startActivity(clz: Class<out BaseActivity>, bundle: Bundle? = null, isFinished: Boolean = false) {
    AppManager.startActivity(clz, bundle, isFinished)
}

fun Fragment.startActivityForResult(clz: Class<out BaseActivity>, requestCode: Int, bundle: Bundle? = null) {
    AppManager.startActivityForResult(clz, requestCode, bundle)
}

