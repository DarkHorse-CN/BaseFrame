package com.darkhorse.baseframe.mvvm

import androidx.lifecycle.MutableLiveData

abstract class BaseData {
    var isFinishActivity: MutableLiveData<Boolean> = MutableLiveData()
}
