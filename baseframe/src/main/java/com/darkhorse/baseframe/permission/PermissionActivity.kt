package com.darkhorse.baseframe.permission

import android.Manifest
import com.darkhorse.baseframe.BaseActivity
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


/**
 * Description:
 * Created by DarkHorse on 2018/5/21.
 */
abstract class PermissionActivity : BaseActivity(), EasyPermissions.PermissionCallbacks {

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(mContext, perms)) {
            val details = findPermissionByCode(requestCode)
            if (details != null) {
                AppSettingsDialog.Builder(mContext)
                        .setTitle("应用需要${details[0]}权限")
                        .setRationale("是否前往应用管理进行添加")
                        .setPositiveButton("是")
                        .setNegativeButton("否")
                        .build().show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, mContext)
    }

    protected fun requestPermission(code: Int) {
        val details = findPermissionByCode(code)
        if (details != null) {
            EasyPermissions.requestPermissions(this, "应用需要使用${details[0]}功能，是否给予权限", code, details[1])
        }
    }

    private fun findPermissionByCode(code: Int): Array<String>? {
        when (code) {
            101 -> return arrayOf("日历", Manifest.permission.READ_CALENDAR)
            102 -> return arrayOf("相机", Manifest.permission.CAMERA)
            103 -> return arrayOf("联系人", Manifest.permission.READ_CONTACTS)
            104 -> return arrayOf("定位", Manifest.permission.ACCESS_FINE_LOCATION)
            105 -> return arrayOf("录音", Manifest.permission.RECORD_AUDIO)
            106 -> return arrayOf("电话", Manifest.permission.CALL_PHONE)
            107 -> return arrayOf("传感器", Manifest.permission.BODY_SENSORS)
            108 -> return arrayOf("短信", Manifest.permission.READ_SMS)
            109 -> return arrayOf("存储", Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        return null
    }
}
