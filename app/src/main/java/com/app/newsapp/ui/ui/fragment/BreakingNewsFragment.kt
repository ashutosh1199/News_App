package com.app.newsapp.ui.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.newsapp.R
import com.app.newsapp.ui.ui.Adapters.NewsAdapter
import com.app.newsapp.ui.ui.Models.NewsActivity
import com.app.newsapp.ui.ui.UI.NewsViewModel
import com.app.newsapp.ui.ui.util.Resource
import com.app.newsapp.ui.ui.util.constants.Companion.QUERY_PAGE_SIZE
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class BreakingNewsFragment: Fragment(R.layout.fragment_breaking_news) {

    lateinit var viewmodel:NewsViewModel
    lateinit var newsAdapter:NewsAdapter
    val TAG="breakingNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel=(activity as NewsActivity).viewmodel
        setUpRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewmodel.breakingNews.observe(viewLifecycleOwner, Observer {
            when(it)
            {
                is Resource.success->{
                    hideProgressbar()
                    it.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages=newsResponse.totalResults/ QUERY_PAGE_SIZE +2
                        isLastPage=viewmodel.breakingNewsPage==totalPages
                        if(isLastPage)
                        {
                            rvBreakingNews.setPadding(0,0,0,0)
                        }
                    }
                }

                is Resource.error->{
                    it.message?.let {message->
                       Toast.makeText(activity,"An error has occured ${message}",Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.loading->{
                    showProgressbar()
                }
            }

        })


    }

    private fun hideProgressbar(){
        paginationProgressBar.visibility=View.INVISIBLE
        isLoading=false
    }

    private fun showProgressbar(){
        paginationProgressBar.visibility=View.VISIBLE
        isLoading=true
    }

    var isScrolling=false
    var isLastPage=false
    var isLoading=false

    val scrollListener=object:RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
            {
                isScrolling=true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition=layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount=layoutManager.childCount
            val totalItemCount=layoutManager.itemCount

            val isNotLoadingAndNotAtTheLastPage= !isLoading && !isLastPage
            val isAtLastItem=firstVisibleItemPosition+visibleItemCount>=totalItemCount
            val isNotAtBeginning=firstVisibleItemPosition>=0
            val isTotalMoreThanVisible=totalItemCount>=QUERY_PAGE_SIZE
            val shouldPaginate=isNotLoadingAndNotAtTheLastPage && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling

            if(shouldPaginate)
            {
                viewmodel.getBreakingNews("in")
                isScrolling=false
            }

        }
    }


    fun setUpRecyclerView(){
        newsAdapter= NewsAdapter()

        rvBreakingNews.apply {
            adapter=newsAdapter
            layoutManager=LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }

}