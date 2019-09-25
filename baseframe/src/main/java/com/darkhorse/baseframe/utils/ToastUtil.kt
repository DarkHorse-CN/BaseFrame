package com.darkhorse.baseframe.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.darkhorse.baseframe.R

object ToastUtil {

    //显示文本的Toast
    fun showTextToas(context: Context, message: String) {
        val toastview = LayoutInflater.from(context).inflate(R.layout.toast_text_layout, null)
        val text = toastview.findViewById<View>(R.id.tv_message) as TextView
        text.text = message
        val toast = Toast(context)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = toastview
        toast.show()
    }
}