package com.kadmiv.newsapi.activity_main.adapter

import com.kadmiv.newsapi.utils.adapter.AbstractAdapter
import com.kadmiv.newsapi.R
import com.kadmiv.newsapi.repo.model.Article


class NewsAdapter(var listener: ItemListener<Article>) :
    AbstractAdapter<Article, ItemListener<Article>>(listener) {

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_news
    }
}