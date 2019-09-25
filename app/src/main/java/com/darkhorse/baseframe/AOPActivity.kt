package com.darkhorse.baseframe

import android.view.View
import com.darkhorse.baseframe.annotation.MoreClick
import com.darkhorse.baseframe.annotation.SingleClick
import com.darkhorse.baseframe.databinding.ActivityAopBinding
import com.darkhorse.baseframe.databinding.BaseBindingActivity
import com.darkhorse.baseframe.extension.toast

class AOPActivity : BaseBindingActivity<ActivityAopBinding>() {
    override fun initDataBinding() {
    }

    override fun getLayoutId(): Int = R.layout.activity_aop

    override fun initView() {

    }

    override fun initData() {
    }

    @SingleClick(3000)
    fun singleClick(view: View) {
        toast("单击")
    }

    @MoreClick
    fun moreClick(view: View) {
        toast("多击")
    }

}
