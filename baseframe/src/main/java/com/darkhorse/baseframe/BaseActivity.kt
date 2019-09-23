package com.darkhorse.baseframe

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.darkhorse.baseframe.permission.PermissionBean
import com.darkhorse.baseframe.permission.PermissionCode
import com.darkhorse.baseframe.utils.AppManager
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import android.view.View
import com.darkhorse.baseframe.extension.i

/**
 * Description:
 * Created by DarkHorse on 2018/5/8.
 */
abstract class BaseActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    protected val mContext by lazy {
        this
    }
    protected var mBundle: Bundle? = null
        get() {
            if (field == null) {
                field = intent.getBundleExtra("data")
            }
            return field
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(AppManager)
        preSetContentView()
        setContentView(getContentView())
        initView()
        initData()
    }

    protected open fun preSetContentView() {
    }

    protected open fun getContentView(): View = layoutInflater.inflate(getLayoutId(), null)

    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun initData()

    override fun onBackPressed() {
        super.onBackPressed()
        AppManager.finish()
    }

    /**
     * 给予权限回调
     */
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    /**
     * 拒绝权限回调
     */
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

    /**
     * 权限请求返回
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, mContext)
    }

    /**
     * 请求权限
     */
    fun requestPermission(code: Int) {
        val list = findPermissionByCode(code)
        val names = StringBuilder()
        val permissions = ArrayList<String>()
        for (bean in list!!) {
            if (!EasyPermissions.hasPermissions(this, bean.permission)) {
                names.append(bean.name + ",")
                permissions.add(bean.permission)
            }
        }

        if (names.isNotEmpty() && permissions.size >= 1) {
            EasyPermissions.requestPermissions(this, "应用需要使用${names.subSequence(0, names.length - 1)}功能，是否给予权限", code, *permissions.toTypedArray())
        }
    }

    /**
     * 通过权限代码寻找权限
     */
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

    /**
     * 判断是否拥有权限
     */
    fun hasPermission(vararg codes: Int): Boolean {
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