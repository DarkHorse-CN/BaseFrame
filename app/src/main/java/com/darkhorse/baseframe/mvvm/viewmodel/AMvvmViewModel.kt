package com.darkhorse.baseframe.mvvm.viewmodel

import com.darkhorse.baseframe.mvvm.BaseViewModel
import  com.darkhorse.baseframe.mvvm.data.AMvvmData

class AMvvmViewModel : BaseViewModel<AMvvmData>() {
    override fun createData() = AMvvmData()
}
