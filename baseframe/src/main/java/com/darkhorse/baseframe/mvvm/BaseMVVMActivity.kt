package com.darkhorse.baseframe.mvvm

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.darkhorse.baseframe.base.BaseActivity
import java.lang.reflect.ParameterizedType

/**
 * @author chenzhenjie
 * @time 2019/8/24 11:23
 */
abstract class BaseMVVMActivity<D : BaseData, VM : BaseViewModel<D>, B : ViewDataBinding> :
    BaseActivity() {
    protected lateinit var mViewModel: VM
    protected val mData: D by lazy {
        mViewModel.mData
    }

    protected lateinit var mBinding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val pType = type as ParameterizedType?
            val cls = pType!!.actualTypeArguments[1]
            if (cls is Class<*>) {
                val viewModelProvider = ViewModelProviders.of(this)
                val vmClass = cls as Class<VM>
                mViewModel = viewModelProvider.get(vmClass)
            }
        }
        super.onCreate(savedInstanceState)
    }

    override fun preSetContentView() {
        super.preSetContentView()
    }


    override fun getContentView(): View {
        mBinding = DataBindingUtil.setContentView(mActivity, getLayoutId())
        mBinding.lifecycleOwner = this
        mData.isFinishActivity.observe(this, Observer { aBoolean ->
            if (aBoolean) {
                mActivity.finish()
            }
        })
        initDataBinding()
        return mBinding.root
    }


    override fun onDestroy() {
        mBinding.unbind()
        super.onDestroy()
    }

    protected abstract fun initDataBinding()
}
