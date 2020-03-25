package com.darkhorse.baseframe.base

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.darkhorse.baseframe.interfaces.IBaseActivity
import com.darkhorse.baseframe.utils.AppManager
import com.darkhorse.baseframe.utils.PermissionsUtils
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

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
        preSetContentView()
        setContentView(getContentView())
        initView()
        initData()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        afterFragmentResume()
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
            val list = PermissionsUtils.findPermissionByCode(requestCode)
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
}