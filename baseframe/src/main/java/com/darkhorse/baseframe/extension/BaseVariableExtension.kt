package com.darkhorse.baseframe.extension

import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*


fun Float.decimal(num: Int): String {
    var str = String.format(Locale.CHINESE,"%.${num}f", this)
    str = str.replace(",", ".")
    return str
}

fun Long.toTimeString(format: String) = SimpleDateFormat(format,Locale.CHINESE).format(Date(this))

fun String.toTime(simpleDateFormat: SimpleDateFormat) = simpleDateFormat.parse(this).time

fun String.toMD5(): String {
    val md5 = MessageDigest.getInstance("MD5")
    val bytes = md5.digest(this.toByteArray())
    val sb = StringBuilder()
    for (b: Byte in bytes) {
        val num = b.toInt() and 0xFF
        val str = Integer.toHexString(num)
        if (str.length == 1) {
            sb.append("0")
        }
        sb.append(str)
    }
    return sb.toString()
}
