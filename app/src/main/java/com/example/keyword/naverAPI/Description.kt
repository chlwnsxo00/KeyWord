package com.example.keyword.naverAPI

import com.google.gson.annotations.SerializedName

data class descriptionItem(
    @SerializedName("items")val items: List<description>
)

data class description(
    @SerializedName("description")val description : String
)
