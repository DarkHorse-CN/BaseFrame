package com.darkhorse.baseframe.mvvm;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.darkhorse.baseframe.base.BaseFragment
import java.lang.reflect.ParameterizedType

/**
 * Description:
 * Created by DarkHorse on 2018/7/12.
 */
abstract class BaseMVVMFragment<D : BaseData, VM : BaseViewModel<D>, B : ViewDataBinding> :
    BaseFragment() {

    protected val mData by lazy {
        mViewModel.mData
    }

    protected lateinit var mViewModel: VM

    protected lateinit var mBinding: B

    override fun getRootView(): View? = mBinding.root

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate<B>(layoutInflater, getLayoutId(), container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
            }
        mViewModel = ViewModelProviders.of(this).get((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VM>)
        mData.isFinishActivity.observe(this, Observer {
            if (it != null) {
                mActivity.finish()
            }
        })
        initDataBinding()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        mBinding.unbind()
        super.onDestroyView()
    }

    abstract fun initDataBinding()
}
