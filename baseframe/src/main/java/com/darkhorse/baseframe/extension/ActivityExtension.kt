package com.darkhorse.baseframe.extension

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.darkhorse.baseframe.BaseActivity
import com.darkhorse.baseframe.utils.AppManager

/**
 * Description:
 * Created by DarkHorse on 2018/7/23.
 */
fun Activity.startActivity(clz: Class<out Activity>, bundle: Bundle? = null, isFinished: Boolean = false) {
    AppManager.startActivity(this, clz, bundle, isFinished)
}

fun Activity.startActivityForResult(clz: Class<out Activity>, requestCode: Int, bundle: Bundle? = null) {
    AppManager.startActivityForResult(this, clz, requestCode, bundle)
}

fun Fragment.startActivity(clz: Class<out BaseActivity>, bundle: Bundle? = null, isFinished: Boolean = false) {
    AppManager.startActivity(activity!!, clz, bundle, isFinished)
}

fun Fragment.startActivityForResult(clz: Class<out BaseActivity>, requestCode: Int, bundle: Bundle? = null) {
    AppManager.startActivityForResult(activity!!, clz, requestCode, bundle)
}