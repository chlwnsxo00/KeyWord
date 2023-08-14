package com.example.keyword.keywordAPI

import com.example.keyword.naverAPI.descryption
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

interface KeywordAPIService {
    @GET("/api/keyword")
    fun getKeywords(
        @Query("mode") mode: Int,
        @Query("sid1") sid1: Int
    ): Call<KeywordResponse>
}