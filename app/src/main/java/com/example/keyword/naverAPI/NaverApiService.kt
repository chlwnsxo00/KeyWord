package com.example.keyword.naverAPI

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface NaverApiService {
    //Dictionary Service - 검색
    @GET("v1/search/encyc.json")
    fun searchEncyclopedia(
        @Header("X-Naver-Client-Id")id:String,
        @Header("X-Naver-Client-Secret") pw:String,
        @Query("query") query: String,
        @Query("display")display:Int
    ): Call<descriptionItem>
}