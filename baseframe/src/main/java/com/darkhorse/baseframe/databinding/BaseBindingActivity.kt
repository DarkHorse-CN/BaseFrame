package com.darkhorse.baseframe.databinding

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.darkhorse.baseframe.base.BaseActivity

abstract class BaseBindingActivity<B : ViewDataBinding> : BaseActivity() {

    protected lateinit var mBinding: B

    override fun getContentView(): View {
        mBinding = DataBindingUtil.setContentView(mActivity, getLayoutId())
        mBinding.lifecycleOwner = this
        initDataBinding()
        return mBinding.root
    }

    protected abstract fun initDataBinding()
}
