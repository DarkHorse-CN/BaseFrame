package com.darkhorse.baseframe.extension

import android.annotation.TargetApi
import android.graphics.Outline
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.darkhorse.baseframe.utils.DisplayUtils

/**
 * Description:
 * Created by DarkHorse on 2018/7/23.
 */

fun ImageView.loadImg(
    url: String?,
    placeholder: Drawable? = null,
    error: Drawable? = null,
    bitmapTransformation: BitmapTransformation? = CenterCrop()
) {
    if (bitmapTransformation == null) {
        Glide.with(this.context)
            .load(url)
            .apply(
                RequestOptions()
                    .placeholder(placeholder)
                    .error(error)
            )
            .into(this)
    } else {
        Glide.with(this.context)
            .load(url)
            .apply(
                RequestOptions.bitmapTransform(bitmapTransformation)
                    .placeholder(placeholder)
                    .error(error)
            )
            .into(this)
    }
}

/**
 * 裁剪圆角
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun View.clipRoundRect(radius: Float) {
    this.clipToOutline = true
    this.outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, DisplayUtils.dp2px(radius))
        }
    }
}

/**
 * 裁剪圆形
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun View.clipCircle(clipCircle: Boolean) {
    this.clipToOutline = clipCircle
    this.outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setOval(0, 0, view.width, view.height)
        }
    }
}

/**
 * 设置背景渐变色
 */
fun View.setGradualBackground(
    start: Int,
    end: Int,
    orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.TOP_BOTTOM
) {
    val bg = GradientDrawable(orientation, intArrayOf(start, end))
    this.background = bg
}


fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}