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
import com.darkhorse.baseframe.extension.d
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

    /**
     * 权限请求返回
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, mActivity)
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