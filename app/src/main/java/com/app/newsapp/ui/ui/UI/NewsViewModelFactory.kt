package com.app.newsapp.ui.ui.UI

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.newsapp.ui.ui.repository.NewsRepository

class NewsViewModelFactory(
    val app:Application,
    private val newsRepository: NewsRepository
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(app,newsRepository) as T
    }
}