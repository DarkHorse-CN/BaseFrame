package com.darkhorse.baseframe.ui.fragment

import android.view.View
import com.darkhorse.baseframe.R
import com.darkhorse.baseframe.databinding.FragmentFmvvmBinding
import com.darkhorse.baseframe.mvvm.BaseMVVMFragment
import com.darkhorse.baseframe.mvvm.data.FMvvmData
import com.darkhorse.baseframe.mvvm.viewmodel.FMvvmViewModel

class FMvvmFragment : BaseMVVMFragment<FMvvmData, FMvvmViewModel, FragmentFmvvmBinding>() {
    override fun initDataBinding() {
        mBinding.let {
            it.mData = mData
            it.mView = this
        }
    }

    override fun getLayoutId() = R.layout.fragment_fmvvm

    override fun initView(rootView: View) {

    }

    override fun initData() {

    }

    override fun lazyLoad() {

    }
}
