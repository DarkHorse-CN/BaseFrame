package com.darkhorse.baseframe

import android.content.Intent
import android.view.View
import android.provider.MediaStore
import com.blankj.ALog
import com.darkhorse.baseframe.R.id.btn_access_camera
import com.darkhorse.baseframe.permission.PermissionActivity
import com.darkhorse.baseframe.permission.PermissionCode
import pub.devrel.easypermissions.AfterPermissionGranted


class MainActivity : PermissionActivity() {

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initView() {
        ALog.d("debug");
        ALog.e("error");
        ALog.w("warning");
        ALog.v("verbose");
        ALog.i("information");
    }

    override fun initListener() {
    }

    override fun initData() {
    }

    @AfterPermissionGranted(PermissionCode.CAMERA)
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)// 启动系统相机
        startActivityForResult(intent, 123)
    }
}
