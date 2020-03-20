package com.darkhorse.baseframe.extension

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment

/**
 * Description:
 * Created by DarkHorse on 2018/7/23.
 */
fun Activity.startActivity(
        clz: Class<out Activity>,
        bundle: Bundle? = null,
        isFinished: Boolean = false
) {
    logI("${this.localClassName} -> ${clz.canonicalName}")
    this.startActivity(clz, bundle, isFinished)
}

fun Activity.startActivityForResult(
        clz: Class<out Activity>,
        requestCode: Int,
        bundle: Bundle? = null
) {
    logI("${this.localClassName} -> ${clz.canonicalName}")
    startActivityForResult(clz, requestCode, bundle)
}

fun Activity.startBrowser(url: String) {
    val intent = Intent()
            .setAction("android.intent.action.VIEW")
            .setData(Uri.parse(url))
    startActivity(intent)
}

fun Fragment.startBrowser(url: String) {
    val intent = Intent()
            .setAction("android.intent.action.VIEW")
            .setData(Uri.parse(url))
    activity?.startActivity(intent)
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

/**
 * 设置全屏
 */
fun Activity.setFullScreen() {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}