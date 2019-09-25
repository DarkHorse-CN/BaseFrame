package com.darkhorse.baseframe.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.darkhorse.baseframe.R

/**
 * Description:
 * Created by DarkHorse on 2018/7/31.
 */
abstract class BaseBindingDialog<B : ViewDataBinding> : Dialog {

    protected lateinit var mBinding: B;
    protected lateinit var mContext: Context;

    constructor(context: Context) : this(context, R.style.base_dialog_style)

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.inflate(layoutInflater, getLayoutId(), null, false)
        initDataBinding()
        val view = mBinding.root
        initDataBinding()
        initView()
        setContentView(view)
    }

    abstract fun getLayoutId(): Int

    abstract fun initDataBinding();

    abstract fun initView()

}
