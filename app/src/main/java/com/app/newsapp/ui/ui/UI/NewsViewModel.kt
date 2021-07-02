package com.app.newsapp.ui.ui.UI

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.newsapp.ui.ui.Models.Article
import com.app.newsapp.ui.ui.Models.NewsResponse
import com.app.newsapp.ui.ui.Models.newsApplication
import com.app.newsapp.ui.ui.repository.NewsRepository
import com.app.newsapp.ui.ui.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    val app: Application,
    private val newsRepository: NewsRepository
): AndroidViewModel(app) {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage=1
    var breakingNewsResponse:NewsResponse?=null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage=1
    var searchNewsResponse:NewsResponse?=null


    init {
        getBreakingNews("in")
    }

    fun getBreakingNews(countryCode:String)=viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    fun searchNews(searchQuery:String)=viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    private fun handleBreakingNewsResponse(response:Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful)
        {
            response.body()?.let {
                breakingNewsPage++
                if(breakingNewsResponse==null)
                {
                    breakingNewsResponse=it
                }
                else
                {
                    val oldArticles=breakingNewsResponse?.articles
                    val newArticles=it.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.success(breakingNewsResponse ?: it)
            }
        }
        return Resource.error(response.message())
    }

    private fun handleSearchNewsResponse(response:Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful)
        {
            response.body()?.let {
                searchNewsPage++
                if(searchNewsResponse==null)
                {
                    searchNewsResponse=it
                }
                else
                {
                    val oldArticles=searchNewsResponse?.articles
                    val newArticles=it.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.success(searchNewsResponse ?: it)
            }
        }
        return Resource.error(response.message())
    }

    fun saveArticle(article: Article)=viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun deleteArticle(article: Article)=viewModelScope.launch {
        newsRepository.delete(article)
    }

    fun getSavedNews()=newsRepository.getAllArticles()

    suspend fun safeSearchNewsCall(searchQuery: String){
       searchNews.postValue(Resource.loading())
        try {
            if(hasInternetConnection())
            {
                val response=newsRepository.searchForNews(searchQuery,searchNewsPage)
               searchNews.postValue(handleBreakingNewsResponse(response))
            }
            else{
                searchNews.postValue(Resource.error("No internet connection"))
            }
        }catch(t:Throwable)
        {
            when(t)
            {
                is IOException->searchNews.postValue(Resource.error("Network call failure"))
                else->searchNews.postValue(Resource.error("conversion error"))
            }

        }
    }

    suspend fun safeBreakingNewsCall(countryCode: String){
        breakingNews.postValue(Resource.loading())
        try {
            if(hasInternetConnection())
            {
                val response=newsRepository.getBreakingNews(countryCode,breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            }
            else{
                breakingNews.postValue(Resource.error("No internet connection"))
            }
        }catch(t:Throwable)
        {
            when(t)
            {
                is IOException->breakingNews.postValue(Resource.error("Network call failure"))
                else->breakingNews.postValue(Resource.error("conversion error"))
            }

        }
    }

    private fun hasInternetConnection():Boolean{
        val connectivityManager=getApplication<newsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            val activeNetwork=connectivityManager.activeNetwork ?: return false
            val capabilities=connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilities.hasTransport(TRANSPORT_WIFI)->true
                capabilities.hasTransport(TRANSPORT_CELLULAR)->true
                capabilities.hasTransport(TRANSPORT_ETHERNET)->true
                else->false
            }
        }
        else
        {
            connectivityManager.activeNetworkInfo?.run{
                return when(type)
                {
                    TYPE_WIFI->true
                    TYPE_MOBILE->true
                    TYPE_ETHERNET->true
                    else->false
                }
            }
        }
        return false

    }
}