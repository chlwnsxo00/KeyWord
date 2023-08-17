

import com.google.gson.annotations.SerializedName

data class SearchItems(
    @SerializedName("response")val searchItems : List<SearchItem>
)
data class SearchItem(
    @SerializedName("origin")val origin : String,
    @SerializedName("img")val img : String,
    @SerializedName("summary")val summary : String,
    @SerializedName("title")val title : String,
    @SerializedName("press")val press : String,
)
