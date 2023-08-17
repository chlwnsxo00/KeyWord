package com.example.keyword.SearchAPIService

import SearchItems
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {
    @GET("/api/search")
    fun searchNews(
        @Query("keyword") keyword: String
    ): Call<SearchItems>
}