package com.app.newsapp.ui.ui.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.newsapp.ui.ui.Models.Article

@Dao
interface articleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("Select * from articles")//Here we used "articles" because it is the table name
    fun getAllarticles():LiveData<List<Article>>//Here we used "Article" because it is the class name

    @Delete
    suspend fun delete(article: Article)

}