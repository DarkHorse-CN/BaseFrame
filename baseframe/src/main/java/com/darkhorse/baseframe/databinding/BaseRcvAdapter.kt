package com.darkhorse.baseframe.mvvm

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

abstract class BaseRcvAdapter<T, D : ViewDataBinding, K : BaseViewHolder>(layoutResId: Int) : BaseQuickAdapter<T, K>(layoutResId) {

    override fun getItemView(layoutResId: Int, parent: ViewGroup?): View {
        return if (layoutResId == mLayoutResId) {
            val binding = DataBindingUtil.inflate<D>(mLayoutInflater, mLayoutResId, parent, false)
            binding.root
        } else {
            super.getItemView(layoutResId, parent)
        }
    }

    override fun convert(helper: K, item: T) {
        val binding = DataBindingUtil.getBinding<D>(helper.itemView)!!
        init(helper, item, binding)
    }

    abstract fun init(helper: K, item: T, binding: D)

}
