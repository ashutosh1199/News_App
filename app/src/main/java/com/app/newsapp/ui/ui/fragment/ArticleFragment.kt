package com.app.newsapp.ui.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.app.newsapp.R
import com.app.newsapp.ui.ui.Models.NewsActivity
import com.app.newsapp.ui.ui.UI.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment: Fragment(R.layout.fragment_article) {
    lateinit var viewmodel: NewsViewModel
    val args:ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel=(activity as NewsActivity).viewmodel
        val article=args.article
        webView.apply {
            webViewClient= WebViewClient()
            loadUrl(article.url)
        }

        fab.setOnClickListener{
            viewmodel.saveArticle(article)
            Snackbar.make(view,"Your article has been saved",Snackbar.LENGTH_SHORT).show()
        }

        fab_share.setOnClickListener {
            val intent= Intent(Intent.ACTION_SEND).apply {
                type="text/plain"
                putExtra(Intent.EXTRA_TEXT,"${article?.url}")
            }
            startActivity(intent)
        }

    }
}