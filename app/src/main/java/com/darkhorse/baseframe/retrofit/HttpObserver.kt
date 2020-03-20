package com.darkhorse.baseframe.retrofit

import com.example.httphelper.retrofit.API
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class HttpObserver<T> : Observer<BaseResponse<T>> {
    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(t: BaseResponse<T>) {
        if (t.status == API.SUCCESS_STATUS) {
            onSuccess(t.data, t.msg)
        } else {
            onFailure(t.code, t.msg)
        }
    }

    override fun onError(e: Throwable) {
        onFailure(API.ERROR_STATUS, e.toString())
    }

    abstract fun onSuccess(bean: T, msg: String)

    abstract fun onFailure(code: Int, msg: String)
}