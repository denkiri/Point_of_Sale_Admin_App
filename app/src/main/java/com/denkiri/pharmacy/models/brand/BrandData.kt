package com.denkiri.pharmacy.models.brand
import com.denkiri.pharmacy.models.category.Category
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class BrandData {
    @SerializedName("error")
    @Expose
    var error = false

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("brands")
    @Expose
    var brands: List<Brand>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}