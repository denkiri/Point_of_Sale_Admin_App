package com.denkiri.pharmacy.models.category
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class CategoryData {
    @SerializedName("error")
    @Expose
    var error = false

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("categories")
    @Expose
    var categories: List<Category>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

}