package com.ayeshaazeema.newsapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayeshaazeema.newsapp.activity.MainActivity
import com.ayeshaazeema.newsapp.model.ArticlesItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.news_item.view.*

class NewsAdapter(var context: Context, var listNews: List<ArticlesItem?>?) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(news: ArticlesItem) {
            with(itemView) {
                tv_title_item.text = news.title
                tv_author_item.text = news.author
                Glide.with(context).load(news.urlToImage).centerCrop().into(iv_item_news)
            }
        }

        fun bind(news: MainActivity) {
            with(itemView) {
                tv_date_item.text = news.date.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) =
        holder.bind(listNews?.get(position)!!)

    override fun getItemCount(): Int = listNews!!.size
}