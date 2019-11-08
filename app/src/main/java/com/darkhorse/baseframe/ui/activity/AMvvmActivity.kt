package com.darkhorse.baseframe.ui.activity

import com.darkhorse.baseframe.R
import com.darkhorse.baseframe.databinding.ActivityAmvvmBinding
import com.darkhorse.baseframe.mvvm.BaseMVVMActivity
import com.darkhorse.baseframe.mvvm.data.AMvvmData
import com.darkhorse.baseframe.mvvm.viewmodel.AMvvmViewModel

class AMvvmActivity : BaseMVVMActivity<AMvvmData, AMvvmViewModel, ActivityAmvvmBinding>() {
    override fun initDataBinding() {
        mBinding.let {
            it.mData = mData
            it.mView = this
        }
    }

    override fun getLayoutId() = R.layout.activity_amvvm

    override fun initView() {

    }

    override fun initData() {

    }

}