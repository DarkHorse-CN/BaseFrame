package com.darkhorse.baseframe.utils

import java.io.DataOutputStream
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    /**
     * 时间戳转日期字符串
     */
    @JvmStatic
    fun timeStamp2DateString(timeStamp: Long, dateFormat: String): String {
        return SimpleDateFormat(dateFormat, Locale.CHINESE).format(timeStamp)
    }

    /**
     * 日期转日期字符串
     */
    @JvmStatic
    fun date2DateString(date: Date, dateFormat: String): String {
        return timeStamp2DateString(date.time, dateFormat);
    }

    /**
     * 时间戳转时间字符串
     */
    @JvmStatic
    fun timeStamp2TimeString(timeStamp: Long): String {
        val time = timeStamp / 1000;
        val hour = time / 60 / 60;
        val minute = time % (60 * 60) / 60
        val second = time % 60;

        val builder = StringBuilder();
        if (hour < 10) {
            builder.append(0)
        }
        builder.append(hour)
        builder.append(":")

        if (minute < 10) {
            builder.append(0)
        }
        builder.append(minute)
        builder.append(":")

        if (second < 10) {
            builder.append(0)
        }
        builder.append(second)

        return builder.toString()
    }

    /**
     * 字符串转时间戳
     */
    @JvmStatic
    fun string2TimeStamp(string: String, dateFormat: String): Long {
        return SimpleDateFormat(dateFormat, Locale.CHINESE).parse(string).time
    }

    /**
     * 获取当前时间的时间戳
     */
    @JvmStatic
    fun timeInMillis() = Calendar.getInstance().timeInMillis

    /**
     *
     */
    @JvmStatic
    fun timeInMillisString(dateFormat: String): String {
        return SimpleDateFormat(dateFormat, Locale.CHINESE).format(timeInMillis())
    }

    /**
     * 设置系统时间，需要设备获取root权限
     * @param timestamp 秒级
     * 2007-02-18 21:00:58
     */
    @JvmStatic
    fun setSystemDate(timestamp: Long) {
        val datestr = timeStamp2DateString(timestamp * 1000, "yyyy-MM-dd HH:mm:ss")
        val year = datestr.substring(0, 4)
        val month = datestr.substring(5, 7)
        val day = datestr.substring(8, 10)
        val hour = datestr.substring(11, 13)
        val minute = datestr.substring(14, 16)
        val second = datestr.substring(17, 19)

        //时间格式: MMDDhhmm[[CC]YY][.ss]
        val datetime = month + day + hour + minute + year + "." + second

        try {
            val process = Runtime.getRuntime().exec("su")
            val outputStream = DataOutputStream(process.outputStream)
            outputStream.writeBytes("setprop persist.sys.timezone Asia/Shanghai\n")
            outputStream.writeBytes("su 0 toybox date $datetime\n")
            outputStream.writeBytes("exit\n")
            outputStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 获取指定时间的星期
     */
    @JvmStatic
    fun getWeek(timeStamp: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timeStamp
        val i = cal.get(Calendar.DAY_OF_WEEK)
        return when (i) {
            1 -> "星期日"
            2 -> "星期一"
            3 -> "星期二"
            4 -> "星期三"
            5 -> "星期四"
            6 -> "星期五"
            7 -> "星期六"
            else -> ""
        }
    }
}
