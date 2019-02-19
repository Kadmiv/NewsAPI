package com.kadmiv.newsapi.repo.model

data class NewsList(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)