package com.darkhorse.baseframe.extension

import android.graphics.drawable.Drawable
import com.darkhorse.baseframe.utils.AppManager

fun getString(stringId: Int): String = AppManager.mApplication.getString(stringId)

fun getStringArray(arrayId: Int): Array<String> = AppManager.mApplication.resources.getStringArray(arrayId)

fun getDrawable(stringId: Int): Drawable? = AppManager.mApplication.getDrawable(stringId)

