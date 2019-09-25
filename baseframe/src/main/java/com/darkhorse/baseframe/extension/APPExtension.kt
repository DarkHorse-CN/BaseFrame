package com.darkhorse.baseframe.extension

import android.graphics.drawable.Drawable
import com.darkhorse.baseframe.utils.AppManager

fun getString(stringId: Int): String = AppManager.getApplication().getString(stringId)

fun getStringArray(arrayId: Int): Array<String> = AppManager.getApplication().resources.getStringArray(arrayId)

fun getDrawable(stringId: Int): Drawable? = AppManager.getApplication().getDrawable(stringId)

