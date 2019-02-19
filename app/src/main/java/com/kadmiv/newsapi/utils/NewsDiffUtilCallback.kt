package com.kadmiv.newsapi.utils

import android.support.v7.util.DiffUtil
import com.kadmiv.newsapi.repo.model.Article

class NewsDiffUtilCallback(val oldList: List<Article>, val newList: List<Article>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct = oldList[oldItemPosition]
        val newProduct = newList[newItemPosition]
        return oldProduct.url == newProduct.url
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct = oldList[oldItemPosition]
        val newProduct = newList[newItemPosition]
        return oldProduct.url == newProduct.url
    }
}