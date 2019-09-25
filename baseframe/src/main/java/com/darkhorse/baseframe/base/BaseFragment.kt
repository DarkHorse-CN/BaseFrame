package com.darkhorse.baseframe.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.darkhorse.baseframe.interfaces.IBaseFragment

/**
 * Description:
 * Created by DarkHorse on 2018/6/27.
 */
abstract class BaseFragment : Fragment(), IBaseFragment {

    protected lateinit var mContext: Context
    protected lateinit var mActivity: BaseActivity

    private var isViewCreated: Boolean = false  //判断是否已经创建View
    private var isUIVisible: Boolean = false    //判断View是否已经显示

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mActivity = activity as BaseActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = if (getRootView() == null) {
            layoutInflater.inflate(getLayoutId(), container, false)
        } else {
            getRootView()
        }
        initView(rootView!!)
        initData()
        return rootView
    }

    open fun getRootView(): View? = null

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
            isViewCreated = false
        }
    }
}
