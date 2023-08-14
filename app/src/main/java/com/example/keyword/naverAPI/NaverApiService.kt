package com.example.keyword.naverAPI

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NaverApiService {
    //Dictionary Service - 검색
    @Headers("X-Naver-Client-Id: IP3Fb3DpFvR9wKPuRPsc", "X-Naver-Client-Secret: LGQc25YVzU")
    @GET("v1/search/encyc.json")
    fun searchEncyclopedia(
        @Query("query") query: String
    ): Call<descryption>
}