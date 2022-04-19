package com.denkiri.pharmacy.models.product
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class ProductReportData {
    @SerializedName("error")
    @Expose
    var error = false

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("productsTotal")
    @Expose
    var productsTotal = 0

    @SerializedName("items")
    @Expose
    var items: Items? = null

    @SerializedName("stockValue")
    @Expose
    var stockValue: StockValue? = null

    @SerializedName("estimatedSales")
    @Expose
    var estimatedSales: EstimatedSales? = null

    @SerializedName("estimatedProfit")
    @Expose
    var estimatedProfit: EstimatedProfit? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}