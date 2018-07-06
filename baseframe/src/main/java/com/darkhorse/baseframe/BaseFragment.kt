package com.darkhorse.baseframe

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.darkhorse.baseframe.utils.AppManager

/**
 * Description:
 * Created by DarkHorse on 2018/6/27.
 */
abstract class BaseFragment : Fragment() {

    protected lateinit var mContext: Context
    protected lateinit var mActivity: BaseActivity
    private lateinit var mRootView: View

    private var isViewCreated: Boolean = false  //判断是否已经创建View
    private var isUIVisible: Boolean = false    //判断View是否已经显示

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mActivity = activity as BaseActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = layoutInflater.inflate(getLayoutId(), null, false)
        initView(mRootView)
        initData()
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        startLazyLoad()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isUIVisible = isVisibleToUser
        startLazyLoad()
    }

    private fun startLazyLoad() {
        if (isViewCreated && isUIVisible) {
            lazyLoad()
        }
    }

    protected fun startActivity(clz: Class<out BaseActivity>, bundle: Bundle? = null, isFinished: Boolean = false) {
        AppManager.startActivity(clz, bundle, isFinished)
    }

    protected fun startActivityForResult(clz: Class<out BaseActivity>, requestCode: Int, bundle: Bundle? = null) {
        AppManager.startActivityForResult(clz, requestCode, bundle)
    }

    abstract fun getLayoutId(): Int

    abstract fun initView(rootView: View)

    abstract fun initData()

    abstract fun lazyLoad()
}
