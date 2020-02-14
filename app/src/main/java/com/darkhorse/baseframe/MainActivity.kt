package com.darkhorse.baseframe

import android.view.View
import com.darkhorse.baseframe.aspectj.annotation.MoreClick
import com.darkhorse.baseframe.aspectj.annotation.Permissions
import com.darkhorse.baseframe.aspectj.annotation.SingleClick
import com.darkhorse.baseframe.databinding.ActivityMainBinding
import com.darkhorse.baseframe.databinding.BaseBindingActivity
import com.darkhorse.baseframe.extension.toast
import com.darkhorse.baseframe.permission.PermissionCode

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {
    override fun initDataBinding() {
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initView() {
    }

    override fun initData() {
    }

    @SingleClick(1000)
    fun singleClick(view: View) {
        toast("单击")
    }

    @MoreClick(5)
    fun moreClick(view: View) {
        toast("多击")
    }

    @SingleClick
    @Permissions(PermissionCode.CAMERA or PermissionCode.STORAGE)
    fun requestPermissions(view: View) {
        toast("申请权限成功")
    }

}
