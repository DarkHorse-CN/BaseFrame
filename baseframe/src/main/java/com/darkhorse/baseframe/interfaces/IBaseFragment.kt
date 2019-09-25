package com.darkhorse.baseframe.interfaces

import android.view.View

interface IBaseFragment {

    fun getLayoutId(): Int

    fun initView(rootView: View)

    fun initData()

    fun lazyLoad()
}
