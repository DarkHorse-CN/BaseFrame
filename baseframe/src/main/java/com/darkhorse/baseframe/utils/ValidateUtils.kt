package com.darkhorse.baseframe.utils

import java.util.regex.Pattern

/**
 * Description:
 * Created by DarkHorse on 2018/8/27.
 */
object ValidateUtils {

    /**
     * 邮箱验证
     *
     * @param mail
     * @return
     */
    fun isValidEmail(mail: String): Boolean {
        val pattern = Pattern.compile("^[A-Za-z0-9][\\w._]*[a-zA-Z0-9]+@[A-Za-z0-9-_]+\\.([A-Za-z]{2,4})")
        val mc = pattern.matcher(mail)
        return mc.matches()
    }

    /**
     * 判断手机号是否合法
     */
    fun isPhone(mobiles: String): Boolean {
        // Pattern p =
        // Pattern.compile("^((13[0-9])|(17[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        val p = Pattern.compile("1[0-9]{10}")
        val m = p.matcher(mobiles)
        return m.matches()
    }


}
