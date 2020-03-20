package com.darkhorse.baseframe.retrofit

data class BaseResponse<T>(
        var status: Int,
        var code: Int,
        var msg: String,
        var data: T
)