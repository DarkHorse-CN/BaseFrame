package com.darkhorse.baseframe.utils

import android.Manifest
import com.darkhorse.baseframe.permission.PermissionBean
import com.darkhorse.baseframe.permission.PermissionCode
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

object PermissionsUtils {


    /**
     * 判断是否拥有权限
     */
    fun hasPermission(code: Int): Boolean {
        val codes = findPermissionByCode(code)
        if (codes != null) {
            for (bean in codes) {
                when (bean.code) {
                    PermissionCode.CALENDAR -> if (!EasyPermissions.hasPermissions(AppManager.mApplication, Manifest.permission.READ_CALENDAR)) return false
                    PermissionCode.CAMERA -> if (!EasyPermissions.hasPermissions(AppManager.mApplication, Manifest.permission.CAMERA)) return false
                    PermissionCode.CONTACTS -> if (!EasyPermissions.hasPermissions(AppManager.mApplication, Manifest.permission.READ_CONTACTS)) return false
                    PermissionCode.LOCATION -> if (!EasyPermissions.hasPermissions(AppManager.mApplication, Manifest.permission.ACCESS_FINE_LOCATION)) return false
                    PermissionCode.AUDIO -> if (!EasyPermissions.hasPermissions(AppManager.mApplication, Manifest.permission.RECORD_AUDIO)) return false
                    PermissionCode.PHONE -> if (!EasyPermissions.hasPermissions(AppManager.mApplication, Manifest.permission.CALL_PHONE)) return false
                    PermissionCode.SMS -> if (!EasyPermissions.hasPermissions(AppManager.mApplication, Manifest.permission.READ_SMS)) return false
                    PermissionCode.STORAGE -> if (!EasyPermissions.hasPermissions(AppManager.mApplication, Manifest.permission.READ_EXTERNAL_STORAGE)) return false
                    PermissionCode.SENSORS -> if (!EasyPermissions.hasPermissions(AppManager.mApplication, Manifest.permission.BODY_SENSORS)) return false
                }
            }
        }
        return true
    }

    /**
     * 请求权限
     */
    fun requestPermission(code: Int) {
        val list = findPermissionByCode(code)
        val names = StringBuilder()
        val permissions = ArrayList<String>()
        for (bean in list!!) {
            if (!EasyPermissions.hasPermissions(AppManager.mApplication, bean.permission)) {
                names.append(bean.name + ",")
                permissions.add(bean.permission)
            }
        }

        if (names.isNotEmpty() && permissions.size >= 1) {
            EasyPermissions.requestPermissions(AppManager.curActivity(), "应用需要使用${names.subSequence(0, names.length - 1)}功能，是否给予权限", code, *permissions.toTypedArray())
        }
    }

    /**
     * 通过权限代码寻找权限
     */
    fun findPermissionByCode(code: Int): List<PermissionBean>? {
        val list = ArrayList<PermissionBean>()

        if (code and PermissionCode.CALENDAR != 0) list.add(PermissionBean("日历", Manifest.permission.READ_CALENDAR, PermissionCode.CALENDAR))
        if (code and PermissionCode.CAMERA != 0) list.add(PermissionBean("相机", Manifest.permission.CAMERA, PermissionCode.CAMERA))
        if (code and PermissionCode.CONTACTS != 0) list.add(PermissionBean("联系人", Manifest.permission.READ_CONTACTS, PermissionCode.CONTACTS))
        if (code and PermissionCode.LOCATION != 0) list.add(PermissionBean("定位", Manifest.permission.ACCESS_FINE_LOCATION, PermissionCode.LOCATION))
        if (code and PermissionCode.AUDIO != 0) list.add(PermissionBean("录音", Manifest.permission.RECORD_AUDIO, PermissionCode.AUDIO))
        if (code and PermissionCode.PHONE != 0) list.add(PermissionBean("电话", Manifest.permission.CALL_PHONE, PermissionCode.PHONE))
        if (code and PermissionCode.SMS != 0) list.add(PermissionBean("短信", Manifest.permission.READ_SMS, PermissionCode.SMS))
        if (code and PermissionCode.STORAGE != 0) list.add(PermissionBean("存储", Manifest.permission.READ_EXTERNAL_STORAGE, PermissionCode.STORAGE))
        if (code and PermissionCode.SENSORS != 0) list.add(PermissionBean("传感器", Manifest.permission.BODY_SENSORS, PermissionCode.SENSORS))
        return list
    }
}