package com.example.keyword.adapter

import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.keyword.ArticleActivity
import com.example.keyword.MainActivity
import com.example.keyword.R
import com.example.keyword.data.Items
import com.example.keyword.data.News

class NewsAdapter(val itemlist:ArrayList<News>) : RecyclerView.Adapter<NewsAdapter.CustomViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news , parent, false)

        return CustomViewHolder(view)
    }
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = itemlist[position]

        holder.title.text = item.title
        holder.sum.text = item.sum
        holder.press.text = item.press
        Glide.with(holder.itemView).load(item.cover).into(holder.cover)

        holder.touch.setOnClickListener {
            val intent = Intent(holder.itemView.context,ArticleActivity::class.java)
            intent.putExtra("address", item.address);
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }

    class CustomViewHolder(itemview : View): RecyclerView.ViewHolder(itemview) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val cover = itemView.findViewById<ImageView>(R.id.cover)
        val sum = itemView.findViewById<TextView>(R.id.sum)
        val press = itemView.findViewById<TextView>(R.id.press)
        val touch = itemView.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.touch)
    }
}