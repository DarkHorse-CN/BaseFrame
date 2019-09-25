package com.darkhorse.baseframe.base

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.darkhorse.baseframe.R
import com.darkhorse.baseframe.interfaces.IWhetherDialogListener

/**
 * Description:
 * Created by DarkHorse on 2018/7/31.
 */
abstract class BaseBindingWhetherDialog<B : ViewDataBinding> : BaseBindingDialog<B> {

    protected val mIWhetherDialogListener: IWhetherDialogListener

    constructor(context: Context, iWhetherDialogListener: IWhetherDialogListener) : this(context, R.style.base_dialog_style, iWhetherDialogListener)

    constructor(context: Context, themeResId: Int, iWhetherDialogListener: IWhetherDialogListener) : super(context, themeResId) {
        mIWhetherDialogListener = iWhetherDialogListener;
    }
}
