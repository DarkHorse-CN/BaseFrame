package com.darkhorse.baseframe

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.blankj.ALog

/**
 * Description:
 * Created by DarkHorse on 2018/5/8.
 */
abstract class BaseActivity : AppCompatActivity() {
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


    protected fun toast(msg: String) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
    }

    protected fun a(msg: String) {
        ALog.a(msg)
    }

    protected fun d(msg: String) {
        ALog.d(msg)
    }

    protected fun e(msg: String) {
        ALog.e(msg)
    }

    protected fun i(msg: String) {
        ALog.i(msg)
    }


    protected fun v(msg: String) {
        ALog.v(msg)
    }

    protected fun w(msg: String) {
        ALog.w(msg)
    }

    protected fun json(msg: String) {
        ALog.json(msg)
    }

    protected fun xml(msg: String) {
        ALog.xml(msg)
    }
}