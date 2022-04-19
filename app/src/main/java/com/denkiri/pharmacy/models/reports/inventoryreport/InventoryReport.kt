package com.denkiri.pharmacy.models.reports.inventoryreport

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class InventoryReport {
    @SerializedName("transaction_id")
    @Expose
    var transactionId:Int? = 0

    @SerializedName("invoice")
    @Expose
    var invoice: String? = null

    @SerializedName("product")
    @Expose
    var product: String? = null

    @SerializedName("qty")
    @Expose
    var qty: Int? = null

    @SerializedName("amount")
    @Expose
    var amount: Double? = 0.00

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("price")
    @Expose
    var price: Double? =0.00

    @SerializedName("discount")
    @Expose
    var discount: String? = null

    @SerializedName("category")
    @Expose
    var category: String? = null

    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("omonth")
    @Expose
    var omonth: String? = null

    @SerializedName("oyear")
    @Expose
    var oyear: String? = null

    @SerializedName("qtyleft")
    @Expose
    var qtyleft: Int? = null

    @SerializedName("dname")
    @Expose
    var dname: String? = null

    @SerializedName("vat")
    @Expose
    var vat: String? = null

    @SerializedName("expiry")
    @Expose
    var expiry: String? = null

    @SerializedName("total_amount")
    @Expose
    var totalAmount: Double? = 0.00

    @SerializedName("total_cost")
    @Expose
    var totalCost: Double? = 0.00
}