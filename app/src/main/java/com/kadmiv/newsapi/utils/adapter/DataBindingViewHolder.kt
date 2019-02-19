package com.kadmiv.newsapi.utils.adapter

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import com.kadmiv.newsapi.BR


class DataBindingViewHolder<B : ViewDataBinding, L>(
    val binding: B,
    val listener: L
) :
    RecyclerView.ViewHolder(binding.root) {

    fun <I> bind(item: I) {
        binding.setVariable(BR.model, item)
        binding.setVariable(BR.listener, listener)
        binding.executePendingBindings()
    }
}