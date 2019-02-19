package com.kadmiv.newsapi.activity_main.adapter

interface ItemListener<I> {

    fun onItemClicked(item: I) {}

    fun onLongItemClicked(item: I): Boolean {
        return false
    }

}