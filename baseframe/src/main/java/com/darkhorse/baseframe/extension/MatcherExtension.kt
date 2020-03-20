package com.darkhorse.baseframe.extension

import java.util.regex.Pattern

/**
 * Ipv4 address check.
 */
private val IPV4_PATTERN = Pattern.compile(
        "^(" + "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
                "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")

/**
 * 判断是否IPv4地址
 */
fun String.isIPv4Address(): Boolean {
    return IPV4_PATTERN.matcher(this).matches()
}
