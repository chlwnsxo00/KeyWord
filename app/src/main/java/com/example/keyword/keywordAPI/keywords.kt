package com.example.keyword.keywordAPI

import com.google.gson.annotations.SerializedName

data class KeywordResponse(
    @SerializedName("response") val response: KeywordsData
)

data class KeywordsData(
    @SerializedName("1") val one : String,
    @SerializedName("2") val two : String,
    @SerializedName("3") val three : String,
    @SerializedName("4") val four : String,
    @SerializedName("5") val five : String,
    @SerializedName("6") val six : String,
    @SerializedName("7") val seven : String,
    @SerializedName("8") val eight : String,
    @SerializedName("9") val nine : String,
    @SerializedName("10") val ten : String,
)
