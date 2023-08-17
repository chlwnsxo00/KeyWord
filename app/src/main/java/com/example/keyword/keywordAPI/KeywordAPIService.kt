package com.example.keyword.keywordAPI

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface KeywordAPIService {
    @GET("/api/keyword")
    fun getKeywords(
        @Query("mode") mode: Int,
        @Query("sid1") sid1: Int
    ): Call<KeywordResponse>
}