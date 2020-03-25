package com.darkhorse.baseframe

import android.view.View
import com.darkhorse.baseframe.aspectj.annotation.MoreClick
import com.darkhorse.baseframe.aspectj.annotation.Permissions
import com.darkhorse.baseframe.aspectj.annotation.SingleClick
import com.darkhorse.baseframe.databinding.ActivityMainBinding
import com.darkhorse.baseframe.databinding.BaseBindingActivity
import com.darkhorse.baseframe.extension.logD
import com.darkhorse.baseframe.extension.logE
import com.darkhorse.baseframe.extension.logI
import com.darkhorse.baseframe.extension.toast
import com.darkhorse.baseframe.retrofit.BaseResponse
import com.darkhorse.baseframe.permission.PermissionCode
import com.darkhorse.baseframe.retrofit.HttpObserver
import com.darkhorse.baseframe.utils.AppManager
import com.darkhorse.httphelper.extension.io_main
import com.example.httphelper.retrofit.API
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

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

}
