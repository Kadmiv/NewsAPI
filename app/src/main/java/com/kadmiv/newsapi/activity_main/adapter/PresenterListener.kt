package com.kadmiv.newsapi.activity_main.adapter

interface PresenterListener<I> {
    fun addItem(item: I)
    fun changeItem(item: I)
    fun deleteItem(item: I)

}