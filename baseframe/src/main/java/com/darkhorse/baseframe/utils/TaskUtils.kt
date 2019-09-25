package com.darkhorse.baseframe.utils

import java.util.concurrent.TimeUnit

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

object TaskUtils {

    fun delayTask(timestamp: Long, iDisposableTask: IDisposableTask): Disposable {
        val disposableObserver = createObserver(iDisposableTask)

        Observable.timer(timestamp, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(disposableObserver)
        return disposableObserver
    }

    fun delayAsyncTask(timestamp: Long, iDisposableTask: IDisposableTask): Disposable {
        val disposableObserver = createObserver(iDisposableTask)

        Observable.timer(timestamp, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.io())
            .subscribe(disposableObserver)
        return disposableObserver
    }


    fun intervalTask(initialDelay: Long, period: Long, iDisposableTask: IDisposableTask): Disposable {
        val disposableObserver = createObserver(iDisposableTask)
        Observable.interval(initialDelay, period, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(disposableObserver)

        return disposableObserver
    }

    fun intervalAsyncTask(
        initialDelay: Long,
        period: Long,
        iDisposableTask: IDisposableTask
    ): Disposable {
        val disposableObserver = createObserver(iDisposableTask)
        Observable.interval(initialDelay, period, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.io())
            .subscribe(disposableObserver)

        return disposableObserver
    }

    private fun createObserver(iDisposableTask: IDisposableTask): DisposableObserver<Long> {
        return object : DisposableObserver<Long>() {

            override fun onNext(times: Long) {
                iDisposableTask.run(times, this)
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        }
    }

    interface IDisposableTask {
        fun run(times: Long, disposable: Disposable)
    }

}


