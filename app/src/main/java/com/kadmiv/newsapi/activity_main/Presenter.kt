package com.kadmiv.newsapi.activity_main

import com.kadmiv.newsapi.activity_main.adapter.ItemListener
import com.kadmiv.newsapi.repo.Repo
import com.kadmiv.newsapi.repo.model.Article
import java.util.*

class Presenter(var mView: IView?) : ItemListener<Article>, Repo.RepoListener {

    private var mRepo: Repo? = Repo

    var oldList: ArrayList<Article>? = null

    fun onStart() {
        mRepo?.listener = this
        if (oldList == null) {
            getLastNews()
        } else {
            catchNews(oldList!!)
            if (currentItem != null)
                mView?.showMoreDetails(currentItem!!)
        }
    }

    fun onStop() {
        mRepo?.listener = null
    }

    fun onDestroy() {
        mView = null
        mRepo = null
    }

    //######################################
    //## Adapter functions
    //######################################

    override fun onItemClicked(item: Article) {
        mView?.closeDialog()
        mView?.openSeparateNews(item)
    }

    var currentItem: Article? = null
    override fun onLongItemClicked(item: Article): Boolean {
        mView?.showMoreDetails(item)
        currentItem = item
        return true
    }

    // Use this function stack, in not connection
    // Push last function (getLastNews||getQueryNews) to stack
    // And to-do this function after restore connection
    var toDoStack = Stack<() -> Unit>()

    fun getLastNews() {
        if (mView!!.hasConnection()) {
            mView!!.showLoadingProcess(true)
            mRepo?.getLastNews()
        } else {
            mView?.createInfoDialog()
            toDoStack.push { getLastNews() }
        }
    }

    fun getQueryNews(queryText: String) {
        if (mView!!.hasConnection()) {
            mView!!.showLoadingProcess(true)
            mRepo?.getQueryNews(queryText)
        } else {
            mView?.createInfoDialog()
            toDoStack.push { getQueryNews(queryText) }
        }
    }

    //######################################
    //## RepoListener callback functions
    //######################################
    override fun catchNews(news: List<Article>) {

        oldList = arrayListOf()
        oldList?.addAll(news)
        mView?.showTopNews(news)
        mView!!.showLoadingProcess(false)
    }

    //######################################
    //## DialogError functions
    //######################################
    fun onTryAgainClicked() {
        mView?.closeDialog()

        if (toDoStack.isNotEmpty()) {
            var function = toDoStack.pop()
            function()
        }
    }

}