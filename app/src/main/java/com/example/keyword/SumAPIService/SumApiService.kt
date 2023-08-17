package com.example.keyword.SumAPIService

import com.example.keyword.keywordAPI.KeywordResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SumApiService {
    @GET("/api/summary")
    fun sumBody(
        @Query("url") url: String
    ): Call<Sum>
}