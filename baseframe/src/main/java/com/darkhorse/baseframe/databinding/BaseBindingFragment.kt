package com.darkhorse.baseframe.databinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.darkhorse.baseframe.base.BaseFragment

abstract class BaseBindingFragment<B : ViewDataBinding> : BaseFragment() {

    protected lateinit var mBinding: B

    override fun getRootView(): View? = mBinding.root

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate<B>(layoutInflater, getLayoutId(), container, false)
                .apply {
                    lifecycleOwner = viewLifecycleOwner
                }
        initDataBinding()
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    abstract fun initDataBinding()
}
