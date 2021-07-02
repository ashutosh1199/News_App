package com.app.newsapp.ui.ui.api

import com.app.newsapp.ui.ui.Models.NewsResponse
import com.app.newsapp.ui.ui.util.constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Newsapi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode:String="us",
        @Query("page")
        pageNumber:Int=1,
        @Query("apiKey")
        apiKey:String=API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery:String,
        @Query("page")
        pageNumber:Int=1,
        @Query("apiKey")
        apiKey:String=API_KEY
    ): Response<NewsResponse>
}