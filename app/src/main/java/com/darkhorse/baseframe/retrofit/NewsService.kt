package com.darkhorse.baseframe.retrofit

import com.example.httphelper.retrofit.API
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsService {

    @Headers(API.HEADER_NEWS)
    @GET("/toutiao/index")
    fun getNews(@Query("type") type: String, @Query("key") key: String = "cc6110713852d4eaffd8d0a3cfd97f05"): Observable<BaseResponse<String>>
}