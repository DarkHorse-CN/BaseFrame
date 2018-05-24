package com.darkhorse.baseframe

import android.content.Intent
import android.provider.MediaStore
import android.view.View
import com.blankj.ALog
import com.darkhorse.baseframe.permission.PermissionActivity
import com.darkhorse.baseframe.permission.PermissionCode
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AfterPermissionGranted


class MainActivity : PermissionActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v){
            btn_access_camera -> requestPermission(PermissionCode.CAMERA)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initView() {
        ALog.d("debug");
        ALog.e("error");
        ALog.w("warning");
        ALog.v("verbose");
        ALog.i("information");
    }

    override fun initListener() {
        btn_access_camera.setOnClickListener(this)
    }

    override fun initData() {
    }

    @AfterPermissionGranted(PermissionCode.CAMERA)
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)// 启动系统相机
        startActivityForResult(intent, 123)
    }
}
