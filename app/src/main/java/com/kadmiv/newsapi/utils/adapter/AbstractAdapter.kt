package com.kadmiv.newsapi.utils.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class AbstractAdapter<I, L>(
    private var listener: L
) :
    RecyclerView.Adapter<DataBindingViewHolder<ViewDataBinding, L>>() {

    lateinit var mValues: List<I>

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int):
            DataBindingViewHolder<ViewDataBinding, L> {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        var binding: ViewDataBinding?

        binding = DataBindingUtil.inflate(
            layoutInflater, viewType,
            viewGroup, false
        )

        return DataBindingViewHolder(binding, listener)
    }

    override fun onBindViewHolder(viewHolder: DataBindingViewHolder<ViewDataBinding, L>, position: Int) {
        viewHolder.bind(mValues[position])
    }

    override fun getItemCount(): Int {
        if (mValues.isNotEmpty())
            return mValues.size
        return 0
    }
}