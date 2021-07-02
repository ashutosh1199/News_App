package com.app.newsapp.ui.ui.Models

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.app.newsapp.R
import com.app.newsapp.ui.ui.UI.NewsViewModel
import com.app.newsapp.ui.ui.UI.NewsViewModelFactory
import com.app.newsapp.ui.ui.db.ArticleDatabase
import com.app.newsapp.ui.ui.repository.NewsRepository
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    lateinit var viewmodel:NewsViewModel
    lateinit var toggle:ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val articleDb=ArticleDatabase(this)

        val newsRepository=NewsRepository(articleDb)

        val viewModelProviderFactory=NewsViewModelFactory(application,newsRepository)

        viewmodel=ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)

        toggle= ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nav_view.setupWithNavController(newsNavHostFragment.findNavController())

        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
