package com.darkhorse.baseframe

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Description:
 * Created by DarkHorse on 2018/5/18.
 */
abstract class BaseFragment : Fragment() {

    lateinit var mContext: Context
    lateinit var mActivity: BaseActivity

    //实现懒加载
    private var isViewCreated: Boolean = false
    private var isUIVisible: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mActivity = activity as BaseActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(getLayoutId(), container, false)
        initView(rootView)
        initListener(rootView)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        lazyLoad()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isUIVisible = isVisibleToUser

    }

    private fun lazyLoad() {
        if (isUIVisible && isViewCreated) {
            initData()
            isViewCreated = false
            isUIVisible = false
        }
    }

    abstract fun getLayoutId(): Int

    abstract fun initView(rootView: View)

    abstract fun initListener(rootView: View)

    abstract fun initData()

    protected fun startActivity(clz: Class<out Activity>) {
        startActivity(Intent(mContext, clz))
    }

    protected fun startActivity(clz: Class<out Activity>, bundle: Bundle) {
        val intent = Intent(mContext, clz)
        intent.putExtra("data", bundle)
        startActivity(intent)
    }

}
