package com.example.keyword.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.keyword.data.Items
import com.example.keyword.R
import com.example.keyword.`interface`.KeywordClick

class itemAdapter(val itemlist:ArrayList<Items>,val listener : KeywordClick) : RecyclerView.Adapter<itemAdapter.CustomViewHolder>()
{
    private var selected : String = ""
    private var selected_position : Int = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item , parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = itemlist[position]
        holder.keyword.text =item.keyword

        if (position == selected_position) {
            holder.touch.setBackgroundResource(R.drawable.btn_blue)
            holder.keyword.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        } else {
            holder.touch.setBackgroundResource(R.drawable.news_background)
            holder.keyword.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
        }

        holder.touch.setOnClickListener {
            if (item.keyword!=selected){
                if(selected_position!=-1){
                    notifyItemChanged(selected_position)
                }
                selected_position = position
                holder.touch.setBackgroundResource(R.drawable.btn_blue)
                holder.keyword.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
                selected = item.keyword
                Log.d("search",item.keyword)
                listener.OnKeywordSelected(item.keyword)
            }
            else if (item.keyword==selected){
                selected_position= -1
                holder.touch.setBackgroundResource(R.drawable.news_background)
                holder.keyword.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
                selected = ""
                listener.OnKeywordUnselected()
            }
        }
        holder.touch.setOnLongClickListener {
            listener.OnLongKeywordClick(item.keyword, holder.itemView)
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