package com.darkhorse.baseframe

import android.view.View
import com.darkhorse.baseframe.databinding.ActivityMainBinding
import com.darkhorse.baseframe.databinding.BaseBindingActivity

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {
    override fun initDataBinding() {
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initView() {
    }

    override fun initData() {
    }

    fun gotoAOP(view: View) {
        startActivity(AOPActivity::class.java)
    }

}
