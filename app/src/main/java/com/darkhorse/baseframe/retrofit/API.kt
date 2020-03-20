package com.example.httphelper.retrofit

import com.darkhorse.baseframe.http.BaseAPI
import com.darkhorse.baseframe.okhttp.HttpHelper
import com.darkhorse.baseframe.retrofit.NewsService
import com.darkhorse.baseframe.utils.SPUtils

/**
 * Created by DarkHorse on 2018/2/4.
 */

object API : BaseAPI() {
    const val KEY_NEWS = "key_news"
    const val HEADER_NEWS = "$HEADER_URL:$KEY_NEWS"

    const val SUCCESS_STATUS = 1
    const val ERROR_STATUS = -1

    val newsService by lazy {
        HttpHelper.getService(NewsService::class.java)
    }

    fun setNewsUrl(url: String) {
        SPUtils.put(API.KEY_NEWS, url)
    }
}
