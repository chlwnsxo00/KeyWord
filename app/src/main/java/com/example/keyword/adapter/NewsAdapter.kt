package com.example.keyword.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.keyword.R
import com.example.keyword.`interface`.NewsClick
import com.example.keyword.data.News
import org.jsoup.Jsoup


class NewsAdapter(val itemlist: ArrayList<News> ,val listener : NewsClick) :
    RecyclerView.Adapter<NewsAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(com.example.keyword.R.layout.news, parent, false)

        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = itemlist[position]

        holder.title.text = item.title
        holder.sum.text = item.sum
        holder.press.text = item.press
        Glide.with(holder.itemView).load(item.cover).into(holder.cover)
        var body : String = ""
        holder.touch.setOnClickListener {
            Thread(Runnable {
                val doc =
                    Jsoup.connect(item.address)
                        .userAgent("Chrome").get()
                body = doc.select(".newsct_article._article_body").text()
                listener.NewsClick(item.address,item.title,item.press,body,item.cover)
            }).start()
        }
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }

    class CustomViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val title = itemView.findViewById<TextView>(com.example.keyword.R.id.title)
        val cover = itemView.findViewById<ImageView>(com.example.keyword.R.id.cover)
        val sum = itemView.findViewById<TextView>(com.example.keyword.R.id.sum)
        val press = itemView.findViewById<TextView>(com.example.keyword.R.id.press)
        val touch =
            itemView.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(com.example.keyword.R.id.touch)
    }
}