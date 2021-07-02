package com.app.newsapp.ui.ui.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.newsapp.R
import com.app.newsapp.ui.ui.Models.Article
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter():RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(item:View):RecyclerView.ViewHolder(item)

    private val differCallBack=object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
           return oldItem.url==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem==newItem
        }
    }

    val differ=AsyncListDiffer(this,differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_article_preview,parent,false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currentarticle=differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(currentarticle.urlToImage).into(ivArticleImage)
            tvSource.text=currentarticle.source?.name
            tvTitle.text=currentarticle.title
            tvDescription.text=currentarticle.description
            tvPublishedAt.text=currentarticle.publishedAt
            setOnClickListener {
                onItemClickListener?.let {
                    it(currentarticle)
                }
            }
        }
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    private var onItemClickListener: ((Article)->Unit)?=null

    fun setOnItemClickListener(listener:(Article)->Unit){
        onItemClickListener=listener
    }
}