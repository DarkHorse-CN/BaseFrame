package com.darkhorse.baseframe.base

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.darkhorse.baseframe.extension.d
import com.darkhorse.baseframe.interfaces.IBaseActivity
import com.darkhorse.baseframe.permission.PermissionBean
import com.darkhorse.baseframe.permission.PermissionCode
import com.darkhorse.baseframe.utils.AppManager
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

/**
 * Description:
 * Created by DarkHorse on 2018/5/8.
 */
abstract class BaseActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks, IBaseActivity {

    protected lateinit var mActivity: Activity

    protected lateinit var mBundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this;
        mBundle = intent.getBundleExtra("data") ?: Bundle()
        lifecycle.addObserver(AppManager)
        preSetContentView()
        setContentView(getContentView())
        initView()
        initData()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        afterFragmentResume()
    }

    override fun onNewIntent(intent: Intent?) {
        d("onNewIntent");
        super.onNewIntent(intent)
    }

    /**
     * 绑定Fragment后的操作
     */
    protected open fun afterFragmentResume() {

    }

    /**
     * 设置布局前的操作
     */
    protected open fun preSetContentView() {
    }

    /**
     * 设置布局
     */
    protected open fun getContentView() = layoutInflater.inflate(getLayoutId(), null)

    /**
     * 给予权限回调
     */
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    /**
     * 拒绝权限回调
     */
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(mActivity, perms)) {
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
                AppSettingsDialog.Builder(mActivity)
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
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, mActivity)
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

        if (code and PermissionCode.CALENDAR != 0) list.add(PermissionBean("日历", Manifest.permission.READ_CALENDAR, PermissionCode.CALENDAR))
        if (code and PermissionCode.CAMERA != 0) list.add(PermissionBean("相机", Manifest.permission.CAMERA, PermissionCode.CAMERA))
        if (code and PermissionCode.CONTACTS != 0) list.add(PermissionBean("联系人", Manifest.permission.READ_CONTACTS, PermissionCode.CONTACTS))
        if (code and PermissionCode.LOCATION != 0) list.add(PermissionBean("定位", Manifest.permission.ACCESS_FINE_LOCATION, PermissionCode.LOCATION))
        if (code and PermissionCode.AUDIO != 0) list.add(PermissionBean("录音", Manifest.permission.RECORD_AUDIO, PermissionCode.AUDIO))
        if (code and PermissionCode.PHONE != 0) list.add(PermissionBean("电话", Manifest.permission.CALL_PHONE, PermissionCode.PHONE))
        if (code and PermissionCode.SMS != 0) list.add(PermissionBean("短信", Manifest.permission.READ_SMS, PermissionCode.SMS))
        if (code and PermissionCode.STORAGE != 0) list.add(PermissionBean("存储", Manifest.permission.READ_EXTERNAL_STORAGE, PermissionCode.STORAGE))
        if (code and PermissionCode.SENSORS != 0) if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            list.add(PermissionBean("传感器", Manifest.permission.BODY_SENSORS, PermissionCode.SENSORS))
        } else {
            return null
        }
        return list
    }

    /**
     * 判断是否拥有权限
     */
    fun hasPermission(code: Int): Boolean {
        val codes = findPermissionByCode(code)
        if (codes != null) {
            for (bean in codes) {
                when (bean.code) {
                    PermissionCode.CALENDAR -> if (!EasyPermissions.hasPermissions(this, Manifest.permission.READ_CALENDAR)) return false
                    PermissionCode.CAMERA -> if (!EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) return false
                    PermissionCode.CONTACTS -> if (!EasyPermissions.hasPermissions(this, Manifest.permission.READ_CONTACTS)) return false
                    PermissionCode.LOCATION -> if (!EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) return false
                    PermissionCode.AUDIO -> if (!EasyPermissions.hasPermissions(this, Manifest.permission.RECORD_AUDIO)) return false
                    PermissionCode.PHONE -> if (!EasyPermissions.hasPermissions(this, Manifest.permission.CALL_PHONE)) return false
                    PermissionCode.SMS -> if (!EasyPermissions.hasPermissions(this, Manifest.permission.READ_SMS)) return false
                    PermissionCode.STORAGE -> if (!EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) return false
                    PermissionCode.SENSORS -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH && !EasyPermissions.hasPermissions(this, Manifest.permission.BODY_SENSORS))
                        return false
                }
            }
        }
        return true
    }

    fun startActivity(clz: Class<out Activity>) {
        this.startActivity(clz, null)
    }

    fun startActivity(clz: Class<out Activity>, bundle: Bundle?) {
        this.startActivity(clz, bundle, false)
    }

    fun startActivity(clz: Class<out Activity>, bundle: Bundle?, isFinished: Boolean) {
        AppManager.startActivity(this, clz, bundle, isFinished)
    }

    fun startActivityForResult(clz: Class<out Activity>, requestCode: Int, bundle: Bundle? = null) {
        AppManager.startActivityForResult(this, clz, requestCode, bundle)
    }

    fun startBrowser(url: String) {
        AppManager.startBrowser(this, url)
    }

    /**
     * 设置沉浸式界面
     */
    fun setImmersion() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    /**
     * 设置全屏
     */
    fun setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

}