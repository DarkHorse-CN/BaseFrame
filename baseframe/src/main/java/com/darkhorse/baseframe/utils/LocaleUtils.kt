package com.darkhorse.baseframe.utils

import java.util.*

object LocaleUtils {
    //华语
    val zh by lazy {
        Locale("zh")
    }
    val zh_CN by lazy {
        Locale("zh", "CN")
    }
    val zh_MO by lazy {
        Locale("zh", "MO")
    }
    val zh_TW by lazy {
        Locale("zh", "TW")
    }
    val zh_HK by lazy {
        Locale("zh", "HK")
    }
    val zh_SG by lazy {
        Locale("zh", "SG")
    }

    //英语
    val en by lazy {
        Locale("en")
    }

    //柬语
    val km by lazy {
        Locale("km")
    }
    val km_KH by lazy {
        Locale("km", "KH")
    }

}
