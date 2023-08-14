package com.example.keyword.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.keyword.data.Items
import com.example.keyword.R
import com.example.keyword.`interface`.KeywordClick

class itemAdapter(val itemlist:ArrayList<Items>,val listener : KeywordClick) : RecyclerView.Adapter<itemAdapter.CustomViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item , parent, false)
        return CustomViewHolder(view)
    }
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = itemlist[position]
        holder.keyword.text =item.keyword

        holder.touch.setOnClickListener {
            listener.OnKeywordClick(item.keyword)
        }
        holder.touch.setOnLongClickListener {
            listener.OnLongKeywordClick(item.keyword)
            true
        }
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }

    class CustomViewHolder(itemview : View): RecyclerView.ViewHolder(itemview) {
        val touch =
            itemView.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(com.example.keyword.R.id.touch)

        val keyword = itemView.findViewById<TextView>(R.id.keyword)
    }
}