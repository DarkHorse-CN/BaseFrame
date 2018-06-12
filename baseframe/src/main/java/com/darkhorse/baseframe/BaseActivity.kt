package com.darkhorse.baseframe

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.blankj.ALog
import com.darkhorse.baseframe.permission.PermissionBean
import com.darkhorse.baseframe.permission.PermissionCode
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

/**
 * Description:
 * Created by DarkHorse on 2018/5/8.
 */
abstract class BaseActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    protected lateinit var mContext: Activity
    protected var mBundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        preSetContentView()
        setContentView(getLayoutId())
        initView()
        initListener()
        initData()
    }

    protected fun preSetContentView() {

    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun initListener()

    abstract fun initData()

    protected fun startActivity(clz: Class<out Activity>) {
        startActivity(Intent(mContext, clz))
    }

    protected fun startActivity(clz: Class<out Activity>, bundle: Bundle) {
        val intent = Intent(mContext, clz)
        intent.putExtra("data", bundle)
        startActivity(intent)
    }

    protected fun getBundle(): Bundle? {
        if (mBundle == null) {
            mBundle = intent.getBundleExtra("data")
        }
        return mBundle
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(mContext, perms)) {
            val list = findPermissionByCode(requestCode)
            val names = StringBuilder()
            val permissions = StringBuilder()
            for (bean in list!!) {
                if (!EasyPermissions.hasPermissions(this, bean.permission)) {
                    names.append(bean.name + ",")
                    permissions.append(bean.permission)
                }
            }
            if (names.length > 1 && permissions.length > 1) {
                AppSettingsDialog.Builder(mContext)
                        .setTitle("应用需要${names.subSequence(0, names.length - 1)}权限")
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
        val list = findPermissionByCode(code)
        val names = StringBuilder()
        val permissions = ArrayList<String>()
        for (bean in list!!) {
            if (!EasyPermissions.hasPermissions(this, bean.permission)) {
                names.append(bean.name + ",")
                permissions.add(bean.permission)
            }
        }

        val array = arrayOfNulls<String>(permissions.size)
        for (i in 0 until array.size) {
            array[i] = permissions[i]
        }
        for (i in permissions)
            if (names.length >= 1 && permissions.size >= 1) {
                EasyPermissions.requestPermissions(this, "应用需要使用${names.subSequence(0, names.length - 1)}功能，是否给予权限", code, *array)
            }
    }

    private fun findPermissionByCode(code: Int): List<PermissionBean>? {
        val list = ArrayList<PermissionBean>()

        if (code and PermissionCode.CALENDAR != 0) list.add(PermissionBean("日历", Manifest.permission.READ_CALENDAR))
        if (code and PermissionCode.CAMERA != 0) list.add(PermissionBean("相机", Manifest.permission.CAMERA))
        if (code and PermissionCode.CONTACTS != 0) list.add(PermissionBean("联系人", Manifest.permission.READ_CONTACTS))
        if (code and PermissionCode.LOCATION != 0) list.add(PermissionBean("定位", Manifest.permission.ACCESS_FINE_LOCATION))
        if (code and PermissionCode.AUDIO != 0) list.add(PermissionBean("录音", Manifest.permission.RECORD_AUDIO))
        if (code and PermissionCode.PHONE != 0) list.add(PermissionBean("电话", Manifest.permission.CALL_PHONE))
        if (code and PermissionCode.SMS != 0) list.add(PermissionBean("短信", Manifest.permission.READ_SMS))
        if (code and PermissionCode.STORAGE != 0) list.add(PermissionBean("存储", Manifest.permission.READ_EXTERNAL_STORAGE))
        if (code and PermissionCode.SENSORS != 0) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            list.add(PermissionBean("传感器", Manifest.permission.BODY_SENSORS))
        } else {
            return null
        }

        return list
    }

    protected fun hasPermission(vararg codes: Int): Boolean {
        for (code in codes) {
            when (code) {
                PermissionCode.CALENDAR -> if (!EasyPermissions.hasPermissions(this, Manifest.permission.READ_CALENDAR)) return false
                PermissionCode.CAMERA -> if (!EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) return false
                PermissionCode.CONTACTS -> if (!EasyPermissions.hasPermissions(this, Manifest.permission.READ_CONTACTS)) return false
                PermissionCode.LOCATION -> if (!EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) return false
                PermissionCode.AUDIO -> if (!EasyPermissions.hasPermissions(this, Manifest.permission.RECORD_AUDIO)) return false
                PermissionCode.PHONE -> if (!EasyPermissions.hasPermissions(this, Manifest.permission.CALL_PHONE)) return false
                PermissionCode.SMS -> if (!EasyPermissions.hasPermissions(this, Manifest.permission.READ_SMS)) return false
                PermissionCode.STORAGE -> if (!EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) return false
                PermissionCode.SENSORS -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH)
                    if (!EasyPermissions.hasPermissions(this, Manifest.permission.BODY_SENSORS))
                        return false
            }
        }
        return true
    }
}