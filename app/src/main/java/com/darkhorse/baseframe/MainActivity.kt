package com.darkhorse.baseframe

import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.darkhorse.baseframe.R.id.btn_access_camera
import com.darkhorse.baseframe.permission.PermissionCode
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AfterPermissionGranted


class MainActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v) {
            btn_access_camera -> requestPermission(PermissionCode.LOCATION or PermissionCode.CAMERA)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initView() {
    }

    override fun initListener() {
        btn_access_camera.setOnClickListener(this)
    }

    override fun initData() {
    }

    @AfterPermissionGranted(PermissionCode.LOCATION or PermissionCode.CAMERA)
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)// 启动系统相机
        startActivityForResult(intent, 123)
    }
}
