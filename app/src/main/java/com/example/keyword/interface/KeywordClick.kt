package com.example.keyword.`interface`

import android.view.View

interface KeywordClick {
    fun OnKeywordSelected(keyword: String)
    fun OnKeywordUnselected()
    fun OnLongKeywordClick(keyword: String, itemView: View)
}