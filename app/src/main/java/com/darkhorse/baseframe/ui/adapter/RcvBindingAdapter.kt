package com.darkhorse.baseframe.ui.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.darkhorse.baseframe.R
import com.darkhorse.baseframe.bean.RcvBindingBean
import com.darkhorse.baseframe.databinding.RcvItemBindingBinding
import com.darkhorse.baseframe.mvvm.BaseRcvAdapter

class RcvAdapter() : BaseRcvAdapter<RcvBindingBean, RcvItemBindingBinding, BaseViewHolder>(R.layout.rcv_item_binding) {

    override fun init(helper: BaseViewHolder, item: RcvBindingBean, binding: RcvItemBindingBinding) {
        binding.let {
            it.mBean = item
        }
    }
}
