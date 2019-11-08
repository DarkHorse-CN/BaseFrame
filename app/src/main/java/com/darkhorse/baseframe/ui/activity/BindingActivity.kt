package com.darkhorse.baseframe.ui.activity

import com.darkhorse.baseframe.R
import com.darkhorse.baseframe.databinding.ActivityBindingBinding
import com.darkhorse.baseframe.databinding.BaseBindingActivity

class BindingActivity : BaseBindingActivity<ActivityBindingBinding>() {
    override fun initDataBinding() {
        mBinding.let {
            it.mView = this
        }
    }

    override fun getLayoutId() = R.layout.activity_binding

    override fun initView() {

    }

    override fun initData() {

    }

}