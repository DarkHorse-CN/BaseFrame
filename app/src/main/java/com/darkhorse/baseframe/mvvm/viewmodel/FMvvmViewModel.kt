package com.darkhorse.baseframe.mvvm.viewmodel

import com.darkhorse.baseframe.mvvm.BaseViewModel
import  com.darkhorse.baseframe.mvvm.data.FMvvmData

class FMvvmViewModel : BaseViewModel<FMvvmData>() {
    override fun createData() = FMvvmData()
}
