package com.darkhorse.baseframe

import com.darkhorse.baseframe.extension.i

class SecondActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_second

    override fun initView() {
        i("SecondActivity")
    }

    override fun initData() {
    }
}
