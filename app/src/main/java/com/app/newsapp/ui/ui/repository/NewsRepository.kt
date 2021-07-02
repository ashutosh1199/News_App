package com.app.newsapp.ui.ui.repository

import com.app.newsapp.ui.ui.Models.Article
import com.app.newsapp.ui.ui.api.RetrofitInstance
import com.app.newsapp.ui.ui.db.ArticleDatabase

class NewsRepository(
    private val db:ArticleDatabase
) {

    suspend fun getBreakingNews(countryCode:String,pageNumber:Int)=
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchForNews(searchQuery:String,pageNumber: Int)=
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)

    suspend fun upsert(article:Article)=
        db.getArticleDao().upsert(article)

    suspend fun delete(article: Article)=
        db.getArticleDao().delete(article)

    fun getAllArticles()=
        db.getArticleDao().getAllarticles()
}