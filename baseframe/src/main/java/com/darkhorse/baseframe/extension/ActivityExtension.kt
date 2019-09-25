package com.darkhorse.baseframe.extension

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.darkhorse.baseframe.utils.AppManager

/**
 * Description:
 * Created by DarkHorse on 2018/7/23.
 */
fun Activity.startActivity(
    clz: Class<out Activity>,
    bundle: Bundle? = null,
    isFinished: Boolean = false
) {
    d("${this.localClassName} -> ${clz.canonicalName}")
    AppManager.startActivity(this, clz, bundle, isFinished)
}

fun Activity.startActivityForResult(
    clz: Class<out Activity>,
    requestCode: Int,
    bundle: Bundle? = null
) {
    d("${this.localClassName} -> ${clz.canonicalName}")
    AppManager.startActivityForResult(this, clz, requestCode, bundle)
}

fun Activity.startBrowser(url: String) {
    AppManager.startBrowser(this, url)
}

fun Fragment.startBrowser(url: String) {
    if (activity != null) {
        AppManager.startBrowser(activity as Activity, url)
    }
}

fun Activity.setImmersion() {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }
}