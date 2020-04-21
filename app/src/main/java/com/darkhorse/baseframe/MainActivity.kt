package com.darkhorse.baseframe

import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.darkhorse.baseframe.aspectj.annotation.MoreClick
import com.darkhorse.baseframe.aspectj.annotation.Permissions
import com.darkhorse.baseframe.aspectj.annotation.SingleClick
import com.darkhorse.baseframe.bean.ArouterTestBean
import com.darkhorse.baseframe.constant.GlobalVal
import com.darkhorse.baseframe.databinding.ActivityMainBinding
import com.darkhorse.baseframe.databinding.BaseBindingActivity
import com.darkhorse.baseframe.extension.logD
import com.darkhorse.baseframe.extension.logE
import com.darkhorse.baseframe.extension.startActivity
import com.darkhorse.baseframe.extension.toast
import com.darkhorse.baseframe.permission.PermissionCode
import com.darkhorse.baseframe.retrofit.HttpObserver
import com.darkhorse.baseframe.ui.activity.TestCoroutinesActivity
import com.darkhorse.baseframe.utils.AppManager
import com.darkhorse.httphelper.extension.io_main
import com.example.httphelper.retrofit.API


class MainActivity : BaseBindingActivity<ActivityMainBinding>() {
    override fun initDataBinding() {
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initView() {
    }

    override fun initData() {
        AppManager.curActivity()
    }

    @SingleClick(3000)
    fun singleClick(view: View) {
        toast("单击")
        logD("单击")
    }

    @MoreClick(5)
    fun moreClick(view: View) {
        toast("多击")
        logD("多击")
    }

    @SingleClick
    @Permissions(PermissionCode.CAMERA or PermissionCode.STORAGE)
    fun requestPermissions(view: View) {
        toast("申请权限成功")
        logD("申请权限成功")
    }

    @SingleClick
    fun testArouter(view: View) {
        ARouter.getInstance().build(GlobalVal.AROUTER_TEST)
                .withLong("key1", 666L)
                .withString("data", "888")
                .withSerializable("bean", ArouterTestBean("Jack", 10))
                .navigation()
    }

    @SingleClick
    fun testHttp(view: View) {
        API.newsService.getNews("top")
                .io_main()
                .subscribe(object : HttpObserver<String>() {
                    override fun onSuccess(bean: String, msg: String) {
                        toast(bean)
                        logE(msg)
                    }

                    override fun onFailure(code: Int, msg: String) {
                        toast(msg)
                        logE(msg)
                    }
                })

    }

    @SingleClick
    fun testCoroutines(view:View){
        startActivity(TestCoroutinesActivity::class.java)
    }

}
