package com.denkiri.pharmacy.models.reports.expiredproducts
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class ExpiryReport {
    @SerializedName("p_id")
    @Expose
    var pId:Int? = 0

    @SerializedName("product_code")
    @Expose
    var productCode: String? = null

    @SerializedName("product_name")
    @Expose
    var productName: String? = null

    @SerializedName("description_name")
    @Expose
    var descriptionName: String? = null

    @SerializedName("amount_lose")
    @Expose
    var amountLose: Double? =0.00

    @SerializedName("qty")
    @Expose
    var qty: String? = null

    @SerializedName("cost")
    @Expose
    var cost: Double? = 0.00

    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("category")
    @Expose
    var category: String? = null

    @SerializedName("exdate")
    @Expose
    var exdate: String? = null
}