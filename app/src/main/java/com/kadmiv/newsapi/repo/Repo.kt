package com.kadmiv.newsapi.repo

import android.util.Log
import com.kadmiv.newsapi.locale
import com.kadmiv.newsapi.repo.model.Article
import com.kadmiv.newsapi.repo.model.NewsList
import com.kadmiv.newsapi.repo.rest.NewsAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val API_KEY = "559b81538daa4480b25d5df57dd630ef"

object Repo {

    var listener: RepoListener? = null

    var restApi = NewsAPI.Factory.getInstance()

    interface RepoListener {
        fun catchNews(news: List<Article>)
    }

    fun getLastNews() {
        restApi.getTopNews(locale, 100, API_KEY).enqueue(object : Callback<NewsList> {
            override fun onFailure(call: Call<NewsList>, t: Throwable) {
                Log.d("12", t.stackTrace.toString())
                getLastNews()
            }

            override fun onResponse(call: Call<NewsList>, response: Response<NewsList>) {
                if (response.isSuccessful) {
                    listener?.catchNews(response.body()!!.articles)
                    return
                }
                Log.d("12", "Error ${response.errorBody().toString()}")
            }
        })
    }

    fun getQueryNews(queryText: String) {
        restApi.getQueryNews(queryText, 1, 100, API_KEY).enqueue(object : Callback<NewsList> {
            override fun onFailure(call: Call<NewsList>, t: Throwable) {
                Log.d("12", t.stackTrace.toString())
                getQueryNews(queryText)
            }

            override fun onResponse(call: Call<NewsList>, response: Response<NewsList>) {
                if (response.isSuccessful) {
                    listener?.catchNews(response.body()!!.articles)
                    return
                }
                Log.d("12", "Error ${response.errorBody().toString()}")
            }
        })
    }
}