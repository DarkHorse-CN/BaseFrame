package com.darkhorse.baseframe.mvvm

import androidx.lifecycle.ViewModel

/**
 * @author chenzhenjie
 * @time 2019/8/24 12:05
 */
abstract class BaseViewModel<D : BaseData> : ViewModel() {

    val mData: D by lazy {
        createData()
    }

    protected abstract fun createData(): D

    protected fun finishActivity() {
        mData.isFinishActivity.postValue(true)
    }
}
