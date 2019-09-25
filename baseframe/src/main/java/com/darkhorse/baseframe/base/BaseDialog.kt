package com.darkhorse.baseframe.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.darkhorse.baseframe.R

/**
 * Description:
 * Created by DarkHorse on 2018/7/31.
 */
abstract class BaseDialog : Dialog {

    protected val mContext: Context;

    constructor(context: Context) : this(context, R.style.base_dialog_style)

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(getLayoutId(), null)
        initView(view)
        initListener(view)
        setContentView(view)
    }

    abstract fun getLayoutId(): Int

    abstract fun initView(view: View)

    abstract fun initListener(view: View)
}
