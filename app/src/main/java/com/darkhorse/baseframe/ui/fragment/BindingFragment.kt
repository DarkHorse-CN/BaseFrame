package com.darkhorse.baseframe.ui.fragment

import android.view.View

import com.darkhorse.baseframe.R
import com.darkhorse.baseframe.databinding.FragmentBindingBinding
import com.darkhorse.baseframe.databinding.BaseBindingFragment

class BindingFragment : BaseBindingFragment<FragmentBindingBinding>() {
    override fun initDataBinding() {
        mBinding.let {
            it.mView = this
        }
    }

    override fun getLayoutId() = R.layout.fragment_binding

    override fun initView(rootView: View) {

    }

    override fun initData() {

    }

    override fun lazyLoad() {

    }
}
